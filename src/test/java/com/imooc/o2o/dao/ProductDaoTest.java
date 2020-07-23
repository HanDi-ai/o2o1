package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public  void TestA(){
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        Product product1 = new Product();
        product1.setProductName("测试111");
        product1.setProductDesc("测试DESC11");
        product1.setImgAddr("est1");
        product1.setPriority(1);
        product1.setEnableStatus(1);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setShop(shop);
        product1.setProductCategory(productCategory);
        Product product2 = new Product();
        product2.setProductName("测试222");
        product2.setProductDesc("测试DESC22");
        product2.setImgAddr("est2");
        product2.setPriority(1);
        product2.setEnableStatus(1);
        product2.setCreateTime(new Date());
        product2.setLastEditTime(new Date());
        product2.setShop(shop);
        product2.setProductCategory(productCategory);
        Product product3 = new Product();
        product3.setProductName("测试333");
        product3.setProductDesc("测试DESC33");
        product3.setImgAddr("est3");
        product3.setPriority(1);
        product3.setEnableStatus(1);
        product3.setCreateTime(new Date());
        product3.setLastEditTime(new Date());
        product3.setShop(shop);
        product3.setProductCategory(productCategory);
        int fv = productDao.insertProduct(product1);
        assertEquals(1,fv);
        fv = productDao.insertProduct(product2);
        assertEquals(1,fv);
        fv = productDao.insertProduct(product3);
        assertEquals(1,fv);
    }

    @Test
    public void TestB(){
        ProductImg p1 = new ProductImg();
        p1.setImgAddr("15");
        p1.setPriority(1);
        p1.setCreateTime(new Date());
        p1.setProductId(4L);
        ProductImg p2 = new ProductImg();
        p2.setImgAddr("25");
        p2.setPriority(2);
        p2.setCreateTime(new Date());
        p2.setProductId(4L);
        List<ProductImg> list = new ArrayList<ProductImg>();
        list.add(p1);
        list.add(p2);
        /*int ff = productImgDao.batchInsertProductImg(list);
        assertEquals(2,ff);*/

        Product pr = productDao.queryByProductId(4l);
        assertEquals(1,pr.getProductImgList().size());

        /*int ff = productImgDao.deleteProductImgByProductId(4l);
        assertEquals(4,ff);*/
    }

    @Test
    public void TestC(){
    Product f = new Product();
        List<Product> g = productDao.queryProductList(f,0,5);
        int gf = productDao.queryProductCount(f);
        assertEquals(5,g.size());
        assertEquals(5,gf);
    }

    @Test
    public void TestD(){
        int gg = productDao.updataProductCategoryToNull(2L);
        assertEquals(1,gg);
    }
}
