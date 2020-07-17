package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testQueryShopListAndCount(){
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(1L);
        shopCondition.setShopCategory(sc);
        ShopExecution se = shopService.getShopList(shopCondition,2,7);
        System.out.println("列表数:"+se.getShopList().size());
        System.out.println("总数:"+se.getCount());
    }

    @Ignore
    @Test
    public void testModifyShop() throws ShopOperationException,FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopName("小改一下15");
        shop.setShopId(1L);
        File shopImg = new File("E:/tx/123.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder("123.jpg",is);
        ShopExecution shopExecution = shopService.modifyShop(shop,imageHolder);
        System.out.println("新图片地址："+shopExecution.getShop().getShopImg());
    }

    @Ignore
    @Test
    public void testAddShop() throws ShopOperationException,FileNotFoundException {
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
        shop.setShopName("测试的店铺77");
        shop.setShopDesc("test6");
        shop.setShopAddr("test6");
        shop.setPhone("test6");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
        File shopImg = new File("E:/tx/123.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(shopImg.getName(),is);
        ShopExecution shopExecution = shopService.addShop(shop,imageHolder);
        assertEquals(ShopStateEnum.CHECK.getState(),shopExecution.getState());
    }
}
