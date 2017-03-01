package com.pccw.util.datatransfer.dataAccess;
import com.pccw.util.db.DaoBaseImpl;
import com.pccw.util.db.stringOracleType.OraDate;
import com.pccw.util.db.stringOracleType.OraDateCreateDate;
				
import com.pccw.util.db.stringOracleType.OraDateLastUpdDate;
				
import
					com.pccw.util.db.stringOracleType.OraDateYYYYMMDDHH24MISS;
				
import com.pccw.util.db.stringOracleType.OraNumber;
import com.pccw.util.db.stringOracleType.OraCLOB;
import com.pccw.util.db.stringOracleType.OraBLOB;
   public class BomwebAcctDAOImpl extends DaoBaseImpl {
 private String orderId; //BOMWEB_ACCT.ORDER_ID
 private String custNo; //BOMWEB_ACCT.CUST_NO
 private String acctName; //BOMWEB_ACCT.ACCT_NAME
 private String billFreq; //BOMWEB_ACCT.BILL_FREQ
 private String billLang; //BOMWEB_ACCT.BILL_LANG
 private String smsNo; //BOMWEB_ACCT.SMS_NO
 private String emailAddr; //BOMWEB_ACCT.EMAIL_ADDR
 private String acctNo; //BOMWEB_ACCT.ACCT_NO
 private OraDate createDate= new OraDateCreateDate(); //BOMWEB_ACCT.CREATE_DATE
 private OraNumber billPeriod; //BOMWEB_ACCT.BILL_PERIOD
 private String createBy; //BOMWEB_ACCT.CREATE_BY
 private String lastUpdBy; //BOMWEB_ACCT.LAST_UPD_BY
 private OraDate lastUpdDate = new OraDateLastUpdDate(); //BOMWEB_ACCT.LAST_UPD_DATE
 private OraNumber dtlId; //BOMWEB_ACCT.DTL_ID
 private String billMedia; //BOMWEB_ACCT.BILL_MEDIA
 private String existBillMedia; //BOMWEB_ACCT.EXIST_BILL_MEDIA
 private String autopayStatementInd; //BOMWEB_ACCT.AUTOPAY_STATEMENT_IND
 private String billFmt; //BOMWEB_ACCT.BILL_FMT
 private String existEmailAddr; //BOMWEB_ACCT.EXIST_EMAIL_ADDR
 private String redemptionMedia; //BOMWEB_ACCT.REDEMPTION_MEDIA
 private String redemptionEmailAddr; //BOMWEB_ACCT.REDEMPTION_EMAIL_ADDR
 private String redemptionSmsNo; //BOMWEB_ACCT.REDEMPTION_SMS_NO
 private String chrgType; //BOMWEB_ACCT.CHRG_TYPE
 private String isNew; //BOMWEB_ACCT.IS_NEW
 private String brand; //BOMWEB_ACCT.BRAND
 private String acctWaivePaperInd; //BOMWEB_ACCT.ACCT_WAIVE_PAPER_IND
 private String waivePaperReaCd; //BOMWEB_ACCT.WAIVE_PAPER_REA_CD
 private String sameAsCustInd; //BOMWEB_ACCT.SAME_AS_CUST_IND
 private String action; //BOMWEB_ACCT.ACTION
 private String waivePaperRemarks; //BOMWEB_ACCT.WAIVE_PAPER_REMARKS
 public BomwebAcctDAOImpl() { primaryKeyFields = new String[] {"acctName","custNo"
}; }
public String getTableName() { if(super.getTableName()==null){ super.setTableName("BOMWEB_ACCT");}
return super.getTableName();}
 public String getOrderId() { return this.orderId; }
 public String getCustNo() { return this.custNo; }
 public String getAcctName() { return this.acctName; }
 public String getBillFreq() { return this.billFreq; }
 public String getBillLang() { return this.billLang; }
 public String getSmsNo() { return this.smsNo; }
 public String getEmailAddr() { return this.emailAddr; }
 public String getAcctNo() { return this.acctNo; }
 public String getCreateBy() { return this.createBy; }
 public String getLastUpdBy() { return this.lastUpdBy; }
 public String getBillMedia() { return this.billMedia; }
 public String getExistBillMedia() { return this.existBillMedia; }
 public String getAutopayStatementInd() { return this.autopayStatementInd; }
 public String getBillFmt() { return this.billFmt; }
 public String getExistEmailAddr() { return this.existEmailAddr; }
 public String getRedemptionMedia() { return this.redemptionMedia; }
 public String getRedemptionEmailAddr() { return this.redemptionEmailAddr; }
 public String getRedemptionSmsNo() { return this.redemptionSmsNo; }
 public String getChrgType() { return this.chrgType; }
 public String getIsNew() { return this.isNew; }
 public String getBrand() { return this.brand; }
 public String getAcctWaivePaperInd() { return this.acctWaivePaperInd; }
 public String getWaivePaperReaCd() { return this.waivePaperReaCd; }
 public String getSameAsCustInd() { return this.sameAsCustInd; }
 public String getAction() { return this.action; }
 public String getWaivePaperRemarks() { return this.waivePaperRemarks; }
 public String getBillPeriod() { return this.billPeriod != null ? this.billPeriod.toString() : null; }
 public String getDtlId() { return this.dtlId != null ? this.dtlId.toString() : null; }
 public String getCreateDate() { return this.createDate != null ? this.createDate.toString() : null; }
 public String getLastUpdDate() { return this.lastUpdDate != null ? this.lastUpdDate.toString() : null; }
 public OraDate getCreateDateORACLE() { return this.createDate; }
 public OraDate getLastUpdDateORACLE() { return this.lastUpdDate; }
 public void setOrderId(String pOrderId) { this.orderId = pOrderId; }
 public void setCustNo(String pCustNo) { this.custNo = pCustNo; }
 public void setAcctName(String pAcctName) { this.acctName = pAcctName; }
 public void setBillFreq(String pBillFreq) { this.billFreq = pBillFreq; }
 public void setBillLang(String pBillLang) { this.billLang = pBillLang; }
 public void setSmsNo(String pSmsNo) { this.smsNo = pSmsNo; }
 public void setEmailAddr(String pEmailAddr) { this.emailAddr = pEmailAddr; }
 public void setAcctNo(String pAcctNo) { this.acctNo = pAcctNo; }
 public void setCreateBy(String pCreateBy) { this.createBy = pCreateBy; }
 public void setLastUpdBy(String pLastUpdBy) { this.lastUpdBy = pLastUpdBy; }
 public void setBillMedia(String pBillMedia) { this.billMedia = pBillMedia; }
 public void setExistBillMedia(String pExistBillMedia) { this.existBillMedia = pExistBillMedia; }
 public void setAutopayStatementInd(String pAutopayStatementInd) { this.autopayStatementInd = pAutopayStatementInd; }
 public void setBillFmt(String pBillFmt) { this.billFmt = pBillFmt; }
 public void setExistEmailAddr(String pExistEmailAddr) { this.existEmailAddr = pExistEmailAddr; }
 public void setRedemptionMedia(String pRedemptionMedia) { this.redemptionMedia = pRedemptionMedia; }
 public void setRedemptionEmailAddr(String pRedemptionEmailAddr) { this.redemptionEmailAddr = pRedemptionEmailAddr; }
 public void setRedemptionSmsNo(String pRedemptionSmsNo) { this.redemptionSmsNo = pRedemptionSmsNo; }
 public void setChrgType(String pChrgType) { this.chrgType = pChrgType; }
 public void setIsNew(String pIsNew) { this.isNew = pIsNew; }
 public void setBrand(String pBrand) { this.brand = pBrand; }
 public void setAcctWaivePaperInd(String pAcctWaivePaperInd) { this.acctWaivePaperInd = pAcctWaivePaperInd; }
 public void setWaivePaperReaCd(String pWaivePaperReaCd) { this.waivePaperReaCd = pWaivePaperReaCd; }
 public void setSameAsCustInd(String pSameAsCustInd) { this.sameAsCustInd = pSameAsCustInd; }
 public void setAction(String pAction) { this.action = pAction; }
 public void setWaivePaperRemarks(String pWaivePaperRemarks) { this.waivePaperRemarks = pWaivePaperRemarks; }
 public void setBillPeriod(String pBillPeriod) { this.billPeriod = new OraNumber(pBillPeriod); }
 public void setDtlId(String pDtlId) { this.dtlId = new OraNumber(pDtlId); }
 public void setCreateDate(String pCreateDate) { this.createDate = new OraDateCreateDate(pCreateDate); }
 public void setLastUpdDate(String pLastUpdDate) { this.lastUpdDate = new OraDateLastUpdDate (pLastUpdDate); }
}
