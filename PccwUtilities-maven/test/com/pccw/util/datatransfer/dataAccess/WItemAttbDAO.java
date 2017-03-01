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
   public class WItemAttbDAO extends DaoBaseImpl {
 private OraNumber itemId; //W_ITEM_ATTB.ITEM_ID
 private String attbCd; //W_ITEM_ATTB.ATTB_CD
 private String attbDesc; //W_ITEM_ATTB.ATTB_DESC
 private String attbValue; //W_ITEM_ATTB.ATTB_VALUE
 private String createBy; //W_ITEM_ATTB.CREATE_BY
 private OraDate createDate= new OraDateCreateDate(); //W_ITEM_ATTB.CREATE_DATE
 private String lastUpdBy; //W_ITEM_ATTB.LAST_UPD_BY
 private OraDate lastUpdDate = new OraDateLastUpdDate(); //W_ITEM_ATTB.LAST_UPD_DATE
 public WItemAttbDAO() { primaryKeyFields = new String[] {"itemId","attbCd","attbDesc"
}; }
public String getTableName() { if(super.getTableName()==null){ super.setTableName("W_ITEM_ATTB");}
return super.getTableName();}
 public String getAttbCd() { return this.attbCd; }
 public String getAttbDesc() { return this.attbDesc; }
 public String getAttbValue() { return this.attbValue; }
 public String getCreateBy() { return this.createBy; }
 public String getLastUpdBy() { return this.lastUpdBy; }
 public String getItemId() { return this.itemId != null ? this.itemId.toString() : null; }
 public String getCreateDate() { return this.createDate != null ? this.createDate.toString() : null; }
 public String getLastUpdDate() { return this.lastUpdDate != null ? this.lastUpdDate.toString() : null; }
 public OraDate getCreateDateORACLE() { return this.createDate; }
 public OraDate getLastUpdDateORACLE() { return this.lastUpdDate; }
 public void setAttbCd(String pAttbCd) { this.attbCd = pAttbCd; }
 public void setAttbDesc(String pAttbDesc) { this.attbDesc = pAttbDesc; }
 public void setAttbValue(String pAttbValue) { this.attbValue = pAttbValue; }
 public void setCreateBy(String pCreateBy) { this.createBy = pCreateBy; }
 public void setLastUpdBy(String pLastUpdBy) { this.lastUpdBy = pLastUpdBy; }
 public void setItemId(String pItemId) { this.itemId = new OraNumber(pItemId); }
 public void setCreateDate(String pCreateDate) { this.createDate = new OraDateCreateDate(pCreateDate); }
 public void setLastUpdDate(String pLastUpdDate) { this.lastUpdDate = new OraDateLastUpdDate (pLastUpdDate); }
}
