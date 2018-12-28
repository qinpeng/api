package com.csci.demo.model.vo;


public class FileListVo {

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEncryptContent() {
        return encryptContent;
    }

    public void setEncryptContent(String encryptContent) {
        this.encryptContent = encryptContent;
    }

    /**
     * 文件名称
     * */
    private String fileName;


    /**
     * base64文件内容
     * */
    private String encryptContent;
}
