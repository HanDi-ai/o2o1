package com.imooc.o2o.util;

import com.imooc.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
   private static String basePath = Thread.currentThread().getContextClassLoader()
            .getResource("").getPath();
   private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
   private static final Random r = new Random();
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 将CommonsMultipartFile转换成File
     * @param cfile
     * @return
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cfile){
        File newFile = new File(cfile.getOriginalFilename());
        try {
            cfile.transferTo(newFile);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     * 处理缩略图，并返回新生成图片的相对值路径
     * @param thumbnail
     * @param targerAddr
     * @return
     */
    public static String generateThumbnail(ImageHolder thumbnail, String targerAddr){
        //给文件生成随机名
        String realFileName = getRandomFileName();
        //文件尾椎扩展名(png,jpg)
        String extension = getFileExtension(thumbnail.getImageName());
        //如果目录不存在，就创建
        makeDirPath(targerAddr);
        String relativeAddr = targerAddr+realFileName+extension;
        logger.debug("current relativeAddr is :" + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath()+relativeAddr);
        logger.debug("current completeAddr is :" + PathUtil.getImgBasePath()+relativeAddr);
        try{
            Thumbnails.of(thumbnail.getInputStream()).size(200,200).watermark(Positions.BOTTOM_CENTER,
                    ImageIO.read(new File(basePath+"/22.jpg")),0.25f) .outputQuality(0.8f).toFile(dest);
        }catch (Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，如需要/A/b/XX.JPG
     * 那么A,b文件夹都需要创建出来
     * @return
     */
    public static void makeDirPath(String targetAddr){
        String realFileParentPath = PathUtil.getImgBasePath()+targetAddr;
        //String realFileParentPath = targetAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件流的扩展名(jpg还是png还是....)
     * @return
     */
    public static String getFileExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     * @return
     */
    public static String getRandomFileName(){
        //获取随机五位数
        int rannum = r.nextInt(89999)+10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }

    /**
     * storePath是文件的路径还是目录的路径
     * 如果是文件路径则删除该文件
     * 如果是目录路径则删除该目录下的所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File files[] = fileOrPath.listFiles();
                for(int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }

    public static void main(String[] args) throws Exception{
        String basePath = Thread.currentThread().getContextClassLoader()
                .getResource("").getPath();
        Thumbnails.of(new File("F:/1/123.jpg")).size(200,200)
                .watermark(Positions.BOTTOM_CENTER,
                        ImageIO.read(new File(basePath+"/22.jpg")),0.25f)
        .outputQuality(0.8f).toFile("F:/1/33new.jpg");
    }
}
