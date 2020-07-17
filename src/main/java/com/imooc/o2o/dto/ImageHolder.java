package com.imooc.o2o.dto;

import java.io.InputStream;

public class ImageHolder {

    private String imageName;
    private InputStream inputStream;

    public ImageHolder() {
    }

    public ImageHolder(String imageName, InputStream inputStream) {
        this.imageName = imageName;
        this.inputStream = inputStream;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
