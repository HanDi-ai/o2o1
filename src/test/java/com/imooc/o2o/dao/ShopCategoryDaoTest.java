package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopCategoryDaoTest extends BaseTest {
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory(){
        List<ShopCategory> shopCategoryList = null;
        ShopCategory shopCategory = new ShopCategory();
        ShopCategory parentSh = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        parentSh.setShopCategory(shopCategory);
        shopCategoryList = shopCategoryDao.queryShopCategory(parentSh);
        assertEquals(1,shopCategoryList.size());
        System.out.println(shopCategoryList.get(0).getShopCategoryName());
    }
}
