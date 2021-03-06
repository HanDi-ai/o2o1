package com.imooc.o2o.util;

public class PathUtil {
    private static String seperator = System.getProperty("file.separator");

    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")){
            basePath = "F:/";
        }else{
            basePath = "/1";
        }
        basePath = basePath.replace("/",seperator);
        return basePath;
    }

    public static String getShopImagePath(long shopId){
        String imagePath = "1/item/shop/"+shopId+"/";
        return imagePath.replace("/",seperator);
    }
}
