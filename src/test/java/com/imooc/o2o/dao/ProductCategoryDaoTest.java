package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    @Ignore
    public void testBQueryByShopId() throws Exception{
        long shopId = 1l;
        List<ProductCategory> list = productCategoryDao.queryProductCategoryList(shopId);
        System.out.println("店铺数量"+list.size());
    }

    @Test
    public void testA(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryName("测试商品类别1");
        productCategory.setPriority(1);
        productCategory.setCreateTime(new Date());
        productCategory.setShopId(1L);
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setProductCategoryName("测试商品类别2");
        productCategory1.setPriority(2);
        productCategory1.setCreateTime(new Date());
        productCategory1.setShopId(1L);
        List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
        productCategoryList.add(productCategory);
        productCategoryList.add(productCategory1);
        int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        assertEquals(2,effectedNum);
    }

    @Test
    public void testC(){
        long shopId =1;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        for(ProductCategory productCategory:productCategoryList){
            if("测试商品类别1".equals(productCategory.getProductCategoryName()) || "测试商品类别2".equals(productCategory.getProductCategoryName())){
                int effectedNum = productCategoryDao.deleteProductCategory(productCategory.getProductCategoryId(),shopId);
                assertEquals(1,effectedNum);
            }
        }
    }
}
