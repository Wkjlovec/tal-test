package cn.diinj.productservice.mapper;

import cn.diinj.productservice.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductMapper {
    Product selectProductById(@Param("id") Long id);
    List<Product> selectAllProducts();
    int insertProduct(Product product);
    int updateProduct(Product product);
    int deleteProduct(@Param("id") Long id);
    
    /**
     * Select products by ID list
     * @param ids Product ID list
     * @return List of products
     */
    List<Product> selectProductsByIds(@Param("ids") List<Long> ids);
} 