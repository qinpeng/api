package com.csci.demo.model.vo;


/**
 * Created by ben on 2018/9/5. benkris1@126.com
 */
public class DocumentDownloadRespVo {


  /**
   *   文件内容的base64.
   */
  private String fileContent;

  /**
   * 文档Id.
   */
  private String docId;

  /**
   * 文件名称.
   */
  private String fileName;

  public String getFileContent() {
    return fileContent;
  }

  public void setFileContent(String fileContent) {
    this.fileContent = fileContent;
  }

  public String getDocId() {
    return docId;
  }

  public void setDocId(String docId) {
    this.docId = docId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
