package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    public void testQueryShopList(){
        Shop shop = new Shop();
        ShopCategory shopCategory = new ShopCategory();
        ShopCategory shopCategory1 = new ShopCategory();
        shopCategory1.setShopCategoryId(5L);
        shopCategory.setShopCategory(shopCategory1);
        shop.setShopCategory(shopCategory);
        List<Shop> list = shopDao.queryShopList(shop,0,7);
        int f =shopDao.queryShopCount(shop);
        System.out.println("店铺大小："+list.size());
        System.out.println("数量："+f);
    }

    @Ignore
    @Test
    public void queryShop(){
        long shopId = 1;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println("areaId:"+shop.getArea().getAreaId());
        System.out.println("areaName:"+shop.getArea().getAreaName());
    }

    @Test
    @Ignore
    public void testinsertShop(){
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        personInfo.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setPersonInfo(personInfo);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试的店铺1");
        shop.setShopDesc("test1");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        int t = shopDao.insertShop(shop);
        assertEquals(1,t);
    }
    @Ignore
    @Test
    public void testupdateShop(){
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("测试的店铺2");
        shop.setShopDesc("测试描述");
        shop.setShopAddr("测试地址");
        shop.setPhone("test2");
        shop.setShopImg("test2");
        shop.setAdvice("审核中2");
        shop.setLastEditTime(new Date());
        int t = shopDao.updateShop(shop);
        assertEquals(1,t);
    }
}
