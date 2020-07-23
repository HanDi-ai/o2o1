package com.imooc.o2o.service.Impl;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.imooc.o2o.util.PathUtil.getShopImagePath;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;
    @Override
    @Transactional
    //1.处理缩略图，获取缩略图相对路径并赋值给Product
    //2.往tb_product写入商品信息，获取productID
    //3.结合productId批量处理商品详情图
    //4.将商品详情图列表批量插入tb_product_img中
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException {
       //缩略图相对路径
        if(product!=null && product.getShop()!=null && product.getShop().getShopId()!=null){
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);
            if(thumbnail!=null){
                addThumbnail(product,thumbnail);
            }
            try {
                int effectedNum = productDao.insertProduct(product);
                if(effectedNum <= 0){
                    throw new ProductOperationException("创建商品失败");
                }
            } catch (ProductOperationException e) {
                throw new ProductCategoryOperationException("error Msg:"+e.getMessage());
            }
            if(productImgList!=null && productImgList.size() > 0){
                addProductImgList(product,productImgList);
            }
                return new ProductExecution(ProductStateEnum.SUCCESS,product);
        }else{
            //传参为空则返回空值错误信息
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    public void addThumbnail(Product product,ImageHolder thumbnail){
            String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
            String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail,dest);
            product.setImgAddr(thumbnailAddr);
    }

    public void addProductImgList(Product product,List<ImageHolder> productImgHolderList){
                String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
                List<ProductImg> productImgList = new ArrayList<ProductImg>();
                for(ImageHolder imageHolder:productImgHolderList){
                    String imgAddr = ImageUtil.generateThumbnail(imageHolder,dest);
                    ProductImg productImg = new ProductImg();
                    productImg.setImgAddr(imgAddr);
                    productImg.setProductId(product.getProductId());
                    productImg.setCreateTime(new Date());
                    productImgList.add(productImg);
                }
                if(productImgList.size() > 0){
                    try {
                        int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                        if(effectedNum <= 0){
                            throw new ProductOperationException("创建商品详情图片失败");
                        }
                    } catch (ProductOperationException e) {
                        throw new ProductOperationException("创建商品详情图片失败:"+e.getMessage());
                    }
                }

    }

    /**
     * 查询商品列表信息
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Product> productList = productDao.queryProductList(productCondition,rowIndex,pageSize);
        int count = productDao.queryProductCount(productCondition);
        ProductExecution productExecution = new ProductExecution();
        if(productList!=null){
            productExecution.setProductList(productList);
            productExecution.setCount(count);
        }else {
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
        }
        return productExecution;
    }

    /**
     * 查询单条商品信息
     * @param productId
     * @return
     */
    @Override
    public Product queryByProductId(long productId) {
        return productDao.queryByProductId(productId);
    }

    /**
     * 修改商品信息
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     */
    @Override
    public ProductExecution updateProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException{
        if(product != null || product.getShop() != null && product.getShop().getShopId() !=null){
            product.setLastEditTime(new Date());
            //如果修改的商品缩略图不为空，且原有的缩略图不为空，则删除原有缩略图，并将新的缩略图添加
            if(thumbnail!=null){
                //先获取原有的缩略图信息信息
                Product tempProduct = productDao.queryByProductId(product.getProductId());
                if(tempProduct.getImgAddr()!=null){
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addProductImg(product,thumbnail);
            }
            //如果有新修改的详情图，就将原有的详情图删除，并把新修改的详情图添加
            if(productImgList!=null && productImgList.size()>0){
                deleteProductImgList(product.getProductId());
                addProductImgList(product,productImgList);
            }
            try{
                int effectedNum = productDao.updateProduct(product);
                if(effectedNum <=0){
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS,product);
            }catch (Exception e){
                throw new ProductOperationException("更新商品信息失败:"+e.getMessage());
            }
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 删除某个商品下的所有详情图
     * @param productId
     */
    public void deleteProductImgList(long productId){
       List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
       //删除原来的详情图
        for(ProductImg productImg:productImgList){
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        productImgDao.deleteProductImgByProductId(productId);
    }

    /**
     * 更新product中图片的imgAddr字段值
     * @param product
     * @param thumbnail
     */
    public void addProductImg(Product product, ImageHolder thumbnail) {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(product.getProductId());
        //在对应的文件夹生成对应名字的图片,将相对路径返回
        String productImgAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        product.setImgAddr(productImgAddr);
    }


}
