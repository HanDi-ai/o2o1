package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductImg;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductImgDaoTest extends BaseTest {
@Autowired
private ProductImgDao productImgDao;

    @Test
    public void TestA(){
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("4");
        productImg1.setImgDesc("4");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(4L);
        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("5");
        productImg2.setImgDesc("5");
        productImg2.setPriority(1);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(4L);
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        productImgList.add(productImg1);
        productImgList.add(productImg2);
        int effectedNum = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2,effectedNum);
    }

    @Test
    public void TestB() throws Exception{
        long productId = 4;
        int t = productImgDao.deleteProductImgByProductId(productId);
        assertEquals(2,t);
    }
}
