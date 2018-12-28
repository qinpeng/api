package com.csci.demo.model.vo;


import java.util.List;

public class DocumentUploadReqVo {

    public String getLoanApplyId() {
        return loanApplyId;
    }

    public void setLoanApplyId(String loanApplyId) {
        this.loanApplyId = loanApplyId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public List<FileListVo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileListVo> fileList) {
        this.fileList = fileList;
    }

    /**
     * 贷款申请编号
     * */
    private String loanApplyId;


    /**
     * 文档类型
     * */
    private String docType;


    /**
     * 文件列表
     * */
    private List<FileListVo> fileList;
}
