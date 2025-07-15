package cn.diinj.productservice.service.impl;

import cn.diinj.productservice.model.Product;
import cn.diinj.productservice.repository.ProductRepository;
import cn.diinj.productservice.service.ProductSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {
private static final org.slf4j.Logger log = LoggerFactory.getLogger(ProductSearchServiceImpl.class);
@Autowired
private ProductRepository productRepository;
@Autowired
ElasticsearchRestTemplate esRestTemplate;
@Autowired
RestHighLevelClient client;
@Autowired
private ObjectMapper objectMapper;

@Override
public List<Product> searchByName(String name) {
    return productRepository.findByNameContaining(name);
}

@Override
public List<Product> searchMathesName(String name) {
    SearchRequest searchRequest = new SearchRequest("product"); // 只指定索引名
    
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(
            QueryBuilders.matchQuery("name", name).analyzer("ik_smart")
    );
    
    searchRequest.source(searchSourceBuilder);
    
    SearchResponse searchResponse;
    try {
        searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 转换结果
        List<Product> products = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Product product = objectMapper.readValue(hit.getSourceAsString(), Product.class);
            products.add(product);
        }
        return products;
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    //return productRepository.findByNameMatches(name);
}

@Override
public List<Product> searchByNameLike(String name) {
    return productRepository.findByNameLike(name);
}

@Override
public List<Product> searchByBrandAndPriceBetween(String brand, double min, double max) {
    return productRepository.findByBrandAndPriceBetween(brand, BigDecimal.valueOf(min),
            BigDecimal.valueOf(max));
}

@Override
public List<Product> searchByNamePrefixOrderByPriceDesc(String prefix) {
    return productRepository.findByNameStartingWithOrderByPriceDesc(prefix);
}

@Override
public Map<String, Map<String, Object>> aggregateByBrand() {
    SearchRequest searchRequest = new SearchRequest("product"); // 指定索引名
    
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    // 设置size为0，不需要文档
    searchSourceBuilder.size(0);
    
    // 创建按品牌分组的聚合
    TermsAggregationBuilder brandAgg = AggregationBuilders
            .terms("by_brand")
            .field("brand");
    
    // 为每个品牌添加子聚合：平均价格
    brandAgg.subAggregation(
            AggregationBuilders.avg("avg_price").field("price")
    );
    
    // 为每个品牌添加子聚合：销售总量
    brandAgg.subAggregation(
            AggregationBuilders.sum("total_sold").field("soldQuantity")
    );
    
    // 将聚合添加到查询中
    searchSourceBuilder.aggregation(brandAgg);
    searchRequest.source(searchSourceBuilder);
    
    try {
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 处理聚合结果
        Map<String, Map<String, Object>> result = new HashMap<>();
        // 获取品牌聚合结果
        Terms brandTerms = searchResponse.getAggregations().get("by_brand");
        for (Terms.Bucket bucket : brandTerms.getBuckets()) {
            String brand = bucket.getKeyAsString();
            long docCount = bucket.getDocCount(); 
            
            Aggregation avgPrice = bucket.getAggregations().get("avg_price");
            double avgPriceValue = ((NumericMetricsAggregation.SingleValue) avgPrice).value();
            
            Aggregation totalSold = bucket.getAggregations().get("total_sold");
            double totalSoldValue = ((NumericMetricsAggregation.SingleValue) totalSold).value();
            
            Map<String, Object> brandStats = new HashMap<>();
            brandStats.put("count", docCount);
            brandStats.put("avgPrice", avgPriceValue);
            brandStats.put("totalSold", totalSoldValue);
            
            result.put(brand, brandStats);
        }
        return result;
    } catch (IOException e) {
        log.error("Error executing brand aggregation query", e);
        throw new RuntimeException("Failed to execute aggregation query", e);
    }
}
}