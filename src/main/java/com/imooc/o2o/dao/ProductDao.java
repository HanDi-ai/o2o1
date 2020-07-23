package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {

    /**
     * 查询商品列表信息
     * @param productCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Product> queryProductList(@Param("productCondition") Product productCondition,
                                   @Param("rowIndex") int rowIndex,
                                   @Param("pageSize") int pageSize);

    /**
     * 返回总条数
     * @param productCondition
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);

    /**
     * 根据productId查询单个商品信息
     * @param productId
     * @return
     */
    Product queryByProductId(long productId);

    /**
     * 插入商品
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 修改商品信息
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     *
     * @param productCategoryId
     * @return
     */
    int updataProductCategoryToNull(long productCategoryId);
}
