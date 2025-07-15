package cn.diinj.productservice.service;

import cn.diinj.productservice.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductSearchService {
List<Product> searchByName(String name);

List<Product> searchMathesName(String name);

List<Product> searchByNameLike(String name);

List<Product> searchByBrandAndPriceBetween(String brand, double min, double max);

List<Product> searchByNamePrefixOrderByPriceDesc(String prefix);

Map<String, Map<String, Object>> aggregateByBrand();
} 