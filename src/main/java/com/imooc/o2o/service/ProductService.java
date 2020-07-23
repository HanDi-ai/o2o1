package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ProductOperationException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 添加商品信息以及图片处理
 */
public interface ProductService {

    /**
     * 查询商品列表信息
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);

    /**
     * 根据productId查询单个商品信息
     * @param productId
     * @return
     */
    Product queryByProductId(long productId);

    /**
     * 新增商品信息
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgList) throws ProductOperationException;


    /**
     * 修改商品信息
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     */
    ProductExecution updateProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgList) throws ProductOperationException;


}
