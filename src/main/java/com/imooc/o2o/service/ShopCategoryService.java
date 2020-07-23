package com.imooc.o2o.service;

import com.imooc.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    /**
     * 根据查询条件，获取ShopCategory列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> queryShopCategory(ShopCategory shopCategoryCondition);
}
