package com.imooc.o2o.web.shopAdmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/shopadmin")
public class productManagementController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    //最234234大上传6张详情图片
    private static final int IMAGEMAXCOUNT = 6;

    @RequestMapping(value = "/addproduct",method = RequestMethod.POST)
    public Map<String,Object> addProduct(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        //验证码校验

        if(CodUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        //接收前端参数的数量的初始化,包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        //最终入库的商品实体类
        Product product = null;
        //将前台传来的实体类由json转换成string
        String productStr = HttpServletRequestUtil.getString(request,"productStr");
        //处理缩略图
        ImageHolder thumbnail = null;
        //装详细图片
        List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
        //从request的session中获取文件流
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try{
            if(multipartResolver.isMultipart(request)){
                //处理文件流
                thumbnail = handleImage((MultipartHttpServletRequest) request, thumbnail, productImgList);
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","上传图片不能为空");
                return modelMap;
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        try {
            product = mapper.readValue(productStr,Product.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        //若product，缩略图，以及详情列表都不为空，可以开始进行商品添加操作
        if(product!=null && thumbnail!=null && productImgList.size()>0){
            try {
                Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                //执行添加操作
                ProductExecution pe = productService.addProduct(product,thumbnail,productImgList);
                if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (ProductOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入商品信息");
        }
        return modelMap;
    }

    private ImageHolder handleImage(MultipartHttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList) throws IOException {
        MultipartHttpServletRequest multipartRequest = request;
        //取出缩略图并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if (thumbnailFile != null) {
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }
        for (int i = 0; i < IMAGEMAXCOUNT; i++) {
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
            if (productImgFile != null) {
                ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
                productImgList.add(productImg);
            } else {
                //若取出的第i个详情图片文件流为空，则终止循环
                break;
            }
        }
        return thumbnail;
    }

    /**
     *通过productId获取指定的product信息
     * @param productId
     * @return
     */
    @RequestMapping(value = "/getproductbyId",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getProductById(@RequestParam Long productId) {
        Map<String,Object> modelMap = new HashMap<String,Object>();
        if(productId >-1){
            Product product = productService.queryByProductId(productId);
            //获取该店铺下的商品类别列表
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("success",true);
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty productId");
        }
        return modelMap;
    }

    /**
     * 查询商品列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/getproductlistbyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getProductListByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if(pageIndex >-1 && pageSize>-1 && (currentShop!=null) && (currentShop.getShopId() !=null)){
           long productCategoryId =  HttpServletRequestUtil.getLong(request,"productCategoryId");
            String productName = HttpServletRequestUtil.getString(request,"productName");
            Product productCondition =compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
            ProductExecution pe = productService.getProductList(productCondition,pageIndex,pageSize);
            modelMap.put("success",true);
            modelMap.put("count",pe.getCount());
            modelMap.put("productList",pe.getProductList());
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","空的pageSize、pageIndex or shopId");
        }
            return modelMap;
    }

    private Product compactProductCondition(long shopId,long productCategoryId,String productName){
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if(productCategoryId!=-1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if(productName!=null){
            productCondition.setProductName(productName);
        }
        return productCondition;
    }

    /**
     * 修改商品信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyproduct",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> modifyProduct(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        //上下架时不需要进行验证码判断
        //boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        //验证码校验
        //if(!statusChange && CodUtil.checkVerifyCode(request)){
       /* if(CodUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }*/
        //接收前端参数的数量的初始化,包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        //最终入库的商品实体类
        Product product = null;
        //处理缩略图
        ImageHolder thumbnail = null;
        //装详细图片
        List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
        //从request的session中获取文件流
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try{
            if(multipartResolver.isMultipart(request)){
                thumbnail = handleImage((MultipartHttpServletRequest) request, thumbnail, productImgList);
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        try {
          String productStr = HttpServletRequestUtil.getString(request,"productStr");
          product = mapper.readValue(productStr,Product.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        if(product!=null){
            try {
                Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
                Shop s = new Shop();
                s.setShopId(1L);
                product.setShop(s);
                //product.setShop(currentShop);
                ProductExecution pe = productService.updateProduct(product,thumbnail,productImgList);
                if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入商品信息");
        }
        return modelMap;
    }

}
