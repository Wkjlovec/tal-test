package cn.diinj.productservice.repository;

import cn.diinj.productservice.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 把数据存储到es中，有两种方式一种是 ElasticsearchRepository 接口，另一种是ElasticsearchTemplate接口
 * findBy、readBy、getBy：根据字段精确查询
 * findFirstBy、findTopBy：查询第一个/前几个
 * findByXxxAndYyy：多条件查询
 * findByXxxOrYyy：或条件查询
 * findByXxxBetween：区间查询
 * findByXxxLike / Containing / StartingWith / EndingWith：模糊、包含、前缀、后缀查询
 * OrderByXxxDesc/Asc：排序
 */


@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, Long> {
// 名称模糊查询 ,containing = like ("%xxx%)
List<Product> findByNameContaining(String name);

//自定义通配符,需要自己加通配符
List<Product> findByNameLike(String name);

// 按品牌和价格区间查询
List<Product> findByBrandAndPriceBetween(String brand, BigDecimal price, BigDecimal price2);

// 名称前缀查询并按价格降序排序
List<Product> findByNameStartingWithOrderByPriceDesc(String prefix);
    
}
    