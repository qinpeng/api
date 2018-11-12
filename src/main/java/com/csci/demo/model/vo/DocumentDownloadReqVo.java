package com.csci.demo.model.vo;

/**
 * Created by ben on 2018/9/5. benkris1@126.com
 */
public class DocumentDownloadReqVo {

  /**
   * 支用申请ID.
   */
  private String loanApplyId;
  /**
   * 信用申请Id.
   */
  private String creditApplyCode;
  private String docId;

  public String getLoanApplyId() {
    return loanApplyId;
  }

  public void setLoanApplyId(String loanApplyId) {
    this.loanApplyId = loanApplyId;
  }

  public String getCreditApplyCode() {
    return creditApplyCode;
  }

  public void setCreditApplyCode(String creditApplyCode) {
    this.creditApplyCode = creditApplyCode;
  }

  public String getDocId() {
    return docId;
  }

  public void setDocId(String docId) {
    this.docId = docId;
  }

}
