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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
} 