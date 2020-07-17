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
                addproductImgList(product,productImgList);
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

    public void addproductImgList(Product product,List<ImageHolder> productImgHolderList){
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
}
