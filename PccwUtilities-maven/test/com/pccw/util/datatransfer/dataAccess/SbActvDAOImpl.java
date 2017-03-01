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
   public class SbActvDAOImpl extends DaoBaseImpl {
 private OraNumber actvId; //SB_ACTV.ACTV_ID
 private String actvType; //SB_ACTV.ACTV_TYPE
 private String lob; //SB_ACTV.LOB
 private String shopCd; //SB_ACTV.SHOP_CD
 private OraNumber channelId; //SB_ACTV.CHANNEL_ID
 private String orderId; //SB_ACTV.ORDER_ID
 private OraNumber profileId; //SB_ACTV.PROFILE_ID
 private String keyA; //SB_ACTV.KEY_A
 private String keyB; //SB_ACTV.KEY_B
 private String keyC; //SB_ACTV.KEY_C
 private String keyD; //SB_ACTV.KEY_D
 private String keyE; //SB_ACTV.KEY_E
 private String keyF; //SB_ACTV.KEY_F
 private String keyG; //SB_ACTV.KEY_G
 private String keyH; //SB_ACTV.KEY_H
 private String keyI; //SB_ACTV.KEY_I
 private String keyJ; //SB_ACTV.KEY_J
 private String createBy; //SB_ACTV.CREATE_BY
 private OraDate createDate= new OraDateCreateDate(); //SB_ACTV.CREATE_DATE
 private String lastUpdBy; //SB_ACTV.LAST_UPD_BY
 private OraDate lastUpdDate = new OraDateLastUpdDate(); //SB_ACTV.LAST_UPD_DATE
 public SbActvDAOImpl() { primaryKeyFields = new String[] {
"actvId"
}; }
public String getTableName() {
return "SB_ACTV";}
 public String getActvType() { return this.actvType; }
 public String getLob() { return this.lob; }
 public String getShopCd() { return this.shopCd; }
 public String getOrderId() { return this.orderId; }
 public String getKeyA() { return this.keyA; }
 public String getKeyB() { return this.keyB; }
 public String getKeyC() { return this.keyC; }
 public String getKeyD() { return this.keyD; }
 public String getKeyE() { return this.keyE; }
 public String getKeyF() { return this.keyF; }
 public String getKeyG() { return this.keyG; }
 public String getKeyH() { return this.keyH; }
 public String getKeyI() { return this.keyI; }
 public String getKeyJ() { return this.keyJ; }
 public String getCreateBy() { return this.createBy; }
 public String getLastUpdBy() { return this.lastUpdBy; }
 public String getActvId() { return this.actvId != null ? this.actvId.toString() : null; }
 public String getChannelId() { return this.channelId != null ? this.channelId.toString() : null; }
 public String getProfileId() { return this.profileId != null ? this.profileId.toString() : null; }
 public String getCreateDate() { return this.createDate != null ? this.createDate.toString() : null; }
 public String getLastUpdDate() { return this.lastUpdDate != null ? this.lastUpdDate.toString() : null; }
 public OraDate getCreateDateORACLE() { return this.createDate; }
 public OraDate getLastUpdDateORACLE() { return this.lastUpdDate; }
 public void setActvType(String pActvType) { this.actvType = pActvType; }
 public void setLob(String pLob) { this.lob = pLob; }
 public void setShopCd(String pShopCd) { this.shopCd = pShopCd; }
 public void setOrderId(String pOrderId) { this.orderId = pOrderId; }
 public void setKeyA(String pKeyA) { this.keyA = pKeyA; }
 public void setKeyB(String pKeyB) { this.keyB = pKeyB; }
 public void setKeyC(String pKeyC) { this.keyC = pKeyC; }
 public void setKeyD(String pKeyD) { this.keyD = pKeyD; }
 public void setKeyE(String pKeyE) { this.keyE = pKeyE; }
 public void setKeyF(String pKeyF) { this.keyF = pKeyF; }
 public void setKeyG(String pKeyG) { this.keyG = pKeyG; }
 public void setKeyH(String pKeyH) { this.keyH = pKeyH; }
 public void setKeyI(String pKeyI) { this.keyI = pKeyI; }
 public void setKeyJ(String pKeyJ) { this.keyJ = pKeyJ; }
 public void setCreateBy(String pCreateBy) { this.createBy = pCreateBy; }
 public void setLastUpdBy(String pLastUpdBy) { this.lastUpdBy = pLastUpdBy; }
 public void setActvId(String pActvId) { this.actvId = new OraNumber(pActvId); }
 public void setChannelId(String pChannelId) { this.channelId = new OraNumber(pChannelId); }
 public void setProfileId(String pProfileId) { this.profileId = new OraNumber(pProfileId); }
 public void setCreateDate(String pCreateDate) { this.createDate = new OraDateCreateDate(pCreateDate); }
 public void setLastUpdDate(String pLastUpdDate) { this.lastUpdDate = new OraDateLastUpdDate (pLastUpdDate); }
}
