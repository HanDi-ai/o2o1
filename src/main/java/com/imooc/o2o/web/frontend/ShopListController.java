package com.imooc.o2o.web.frontend;


import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class ShopListController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;

    /**
     * 获取商品类别列表和地区列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshopspageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        long parentId = HttpServletRequestUtil.getLong(request,"parentId");
        List<ShopCategory> shopCategoryList = null;
        if(parentId !=-1){
            //如果parentId存在，则取出该一级类别下的二级类别列表
            try{
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setShopCategory(parent);
                shopCategoryList = shopCategoryService.queryShopCategory(shopCategoryCondition);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }else {
            try{
                shopCategoryList = shopCategoryService.queryShopCategory(null);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }
        try {
            modelMap.put("shopCategoryList",shopCategoryList);
            List<Area> areaList = null;
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    /**
     * 获取指定查询条件下的店铺列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshops",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShops(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        if(pageIndex >-1 && pageSize>-1){
            //一级类别id
           long parentId = HttpServletRequestUtil.getLong(request,"parentId");
           //二级类别id
           long shopCategoryId = HttpServletRequestUtil.getLong(request,"shopCategoryId");
           //区域id
            int areaId = HttpServletRequestUtil.getInt(request,"areaId");
            //模糊查询的名字
            String shopName = HttpServletRequestUtil.getString(request,"shopName");
            Shop shopCondition = compactShopCondition4Search(parentId,shopCategoryId,areaId,shopName);
            ShopExecution se = shopService.getShopList(shopCondition,pageIndex,pageSize);
            modelMap.put("success",true);
            modelMap.put("count",se.getCount());
            modelMap.put("shopList",se.getShopList());
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","空的pageSize、pageIndex or shopId");
        }
        return modelMap;
    }

    /**
     *模糊查询的条件整合
     * @param parentId
     * @param shopCategoryId
     * @param areaId
     * @param shopName
     * @return
     */
    private Shop compactShopCondition4Search(long parentId,long shopCategoryId,int areaId,String shopName){
        Shop shopCondition = new Shop();
        if(parentId!=-1){
            ShopCategory parent = new ShopCategory();
            ShopCategory child = new ShopCategory();
            parent.setShopCategoryId(parentId);
            child.setShopCategory(parent);
            shopCondition.setShopCategory(child);
        }
        if(shopCategoryId!=-1){
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if(areaId!=-1){
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if(shopName!=null){
            shopCondition.setShopName(shopName);
        }
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
