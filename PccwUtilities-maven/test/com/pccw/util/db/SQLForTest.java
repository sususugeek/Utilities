package com.pccw.util.db;

import java.util.HashMap;
import java.util.Map;

public class SQLForTest {

	public static String[][] getSqlForTest(){
		String[][] ret = new String[][]{
			//0
			new String[]{
					"select SYSTIMESTAMP, SYSDATE, TO_DATE(a.update_date, 'YYYYMMDDHH24MISS') from dual ",
					"select NOW(), SYSDATE(), STR_TO_DATE(a.update_date, '%Y%m%d%H%i%s') from dual "
			},//1
			new String[]{
					"select SYSDATE, TO_DATE(a.update_date, 'YYYYMMDDHH24MISS'), TO_DATE((select update_date from test_t4 where create_date=TO_DATE('20160830', 'YYYYMMDD')), 'YYYYMMDD' ) "
							+ "from test_t1 a inner join test_t2 b "
							+ "on a.id=b.id and TO_TIMESTAMP(a.update_date,'YYYYMMDDHH24MISS')='20160831230000' "
							+ "inner join test_t3 c "
							+ "on TO_TIMESTAMP(b.update_date,'YYYYMMDDHH24MISS')= TO_TIMESTAMP(c.update_date,'YYYYMMDDHH24MISS') "
							+ "where a.create_date <= SYSDATE",
					"select SYSDATE(), STR_TO_DATE(a.update_date, '%Y%m%d%H%i%s'), STR_TO_DATE((select update_date from test_t4 where create_date=STR_TO_DATE('20160830', '%Y%m%d')), '%Y%m%d') "
							+ "from test_t1 a inner join test_t2 b "
							+ "on a.id=b.id and STR_TO_DATE(a.update_date, '%Y%m%d%H%i%s')='20160831230000' "
							+ "inner join test_t3 c "
							+ "on STR_TO_DATE(b.update_date, '%Y%m%d%H%i%s')= STR_TO_DATE(c.update_date, '%Y%m%d%H%i%s') "
							+ "where a.create_date <= SYSDATE()"
			},
			//2
			new String[]{
					"select SYSTIMESTAMP, SYSDATE, TO_DATE(a.update_date, 'YYYYMMDDHH24MISS'), "
							+ "TO_TIMESTAMP(a.update_date,'YYYYMMDDHH24MISS'), TO_CHAR(456), TO_NUMBER('123'), "
							+ "TO_CHAR(a.create_date, 'YYYYMMDDHH24MISS'), nvl(a.description, 'This value is null'), chr(65) "
							+ "from test_t1 a where a.create_date <= SYSDATE",
					"select NOW(), SYSDATE(), STR_TO_DATE(a.update_date, '%Y%m%d%H%i%s'), "
							+ "STR_TO_DATE(a.update_date, '%Y%m%d%H%i%s'), CAST(456 as char), CONVERT('123', decimal(22, 6)), "
							+ "DATE_FORMAT(a.create_date, '%Y%m%d%H%i%s'), IFNULL(a.description, 'This value is null'), ASCII(65) "
							+ "from test_t1 a where a.create_date <= SYSDATE()"
			},
			//3
			new String[]{
					"select SYSTIMESTAMP, "
							+ "TO_DATE((select nvl(c.description, '20150803') from test_t3 c where c.id=a.id), 'YYYYMMDD') "
							+ "from test_t1 a inner join test_t2 b "
							+ "on a.id=b.id and TO_TIMESTAMP(a.update_date,'YYYYMMDDHH24MISS')='20160831230000' where a.create_date <= SYSDATE",
					"select NOW(), "
							+ "STR_TO_DATE((select IFNULL(c.description, '20150803') from test_t3 c where c.id=a.id), '%Y%m%d') "
							+ "from test_t1 a inner join test_t2 b "
							+ "on a.id=b.id and STR_TO_DATE(a.update_date, '%Y%m%d%H%i%s')='20160831230000' where a.create_date <= SYSDATE()"
			},
			//4
			new String[]{
					" where a.update_date = TO_timestamp(a.update_date,'YYYYMMDDHH24MISS')",
					" where a.update_date = STR_TO_DATE(a.update_date, '%Y%m%d%H%i%s')"
			},
			//5
			new String[]{
					" update test_t1 set create_date = TO_TIMESTAMP('20160831223030','YYYYMMDDHH24MISS')",
					" update test_t1 set create_date = STR_TO_DATE('20160831223030', '%Y%m%d%H%i%s')"
			}
			,
			//6
			new String[]{
					" update test_t1 set create_date = (select TO_TIMESTAMP('20160831223030','YYYYMMDDHH24MISS') from dual)",
					" update test_t1 set create_date = (select STR_TO_DATE('20160831223030', '%Y%m%d%H%i%s') from dual)"
			}
			,
			//7
			new String[]{
					" INSERT INTO TEST_T1 ( ID, UPDATE_DATE, CREATE_DATE, DESCRIPTION ) VALUES (1,'20160831223949',    TO_DATE('20160831223949','YYYYMMDDHH24MISS'),'description')",
					" INSERT INTO TEST_T1 ( ID, UPDATE_DATE, CREATE_DATE, DESCRIPTION ) VALUES (1,'20160831223949',    STR_TO_DATE('20160831223949', '%Y%m%d%H%i%s'),'description')"
			},
			//8
			new String[]{
					" INSERT INTO TEST_T3 (ID,UPDATE_DATE,CREATE_DATE,DESCRIPTION) SELECT ID,   TO_CHAR(CREATE_DATE,''), SYSDATE, DESCRIPTION FROM TEST_T1 ",
					" INSERT INTO TEST_T3 (ID,UPDATE_DATE,CREATE_DATE,DESCRIPTION) SELECT ID,   DATE_FORMAT(CREATE_DATE, ''), SYSDATE(), DESCRIPTION FROM TEST_T1 ",
			},
			//9
			new String[]{
					"select TO_DATE((select nvl((select TO_CHAR(d.create_date, 'YYYYMMDD') from test_t4 d where d.id=c.id) , '20150803') from test_t3 c where c.id=a.id), 'YYYYMMDD'), chr(65) "
							+ "from test_t1 a inner join test_t2 b "
							+ "on a.id=b.id and TO_TIMESTAMP(a.update_date,'YYYYMMDDHH24MISS')='20160831230000' where a.create_date <= SYSDATE",
					"select STR_TO_DATE((select IFNULL((select DATE_FORMAT(d.create_date, '%Y%m%d') from test_t4 d where d.id=c.id), '20150803') from test_t3 c where c.id=a.id), '%Y%m%d'), ASCII(65) "
							+ "from test_t1 a inner join test_t2 b "
							+ "on a.id=b.id and STR_TO_DATE(a.update_date, '%Y%m%d%H%i%s')='20160831230000' where a.create_date <= SYSDATE()"
			},
			//10
			new String[]{
					"    select MTCE_AMT " +
		                       "  from (SELECT SLV_SERVICE_MTCE_PRICING.* " +
		                       "          " +   
		                       "  FROM SLV_SERVICE_MTCE_PRICING" +   
		                       "  WHERE create_date <NVL(to_date(?, 'YYYYMMDD'), TRUNC(sysdate)) " + 
		                       "  and PROFILE_ID = TO_NUMBER(?) " +  
		                       "  AND ITEM_ID = TO_NUMBER(?) " +
		                       "  order by CREATE_DATE DESC)temp_a, " + 
		                       "  (select nvl(to_date(?, 'YYYYMMDD'), trunc(sysdate)) APP_DATE from dual) ad " + 
		                       "  WHERE PROFILE_ID = TO_NUMBER(?) AND ITEM_ID = TO_NUMBER(?) " + 
		                       "    and ad.APP_DATE >= EFF_START_DATE " + 
		                       "    and EFF_END_DATE >= ad.APP_DATE " +
		                       "    AND (     (ALLOW_FROM_WARR_END_MONTH IS NULL) " +
		                       "           OR (ad.APP_DATE >= TRUNC(timestampadd(month, ALLOW_FROM_WARR_END_MONTH, WARR_END_DATE)))) " +
		                       "    AND (     (ALLOW_UNTIL_WARR_END_MONTH IS NULL) " +
		                       "           OR (ad.APP_DATE < TRUNC(timestampadd(month, ALLOW_UNTIL_WARR_END_MONTH, WARR_END_DATE+ INTERVAL 1 DAY)))) LIMIT 1"
,"    select  wis.ITEM_SET_ID \"itemId\", 'ITEM_SET' \"itemType\", wis.ITEM_SET_TYPE \"type\", '' \"mdoInd\", wis.MAX_QTY \"maxQty\", wis.MIN_QTY \"minQty\", CONVERT('', decimal(22, 6)) \"unitPrice\", wis.DEFAULT_QTY \"defaultQty\", '0^' || wis.ITEM_SET_TYPE || '^' || wis.ITEM_SET_CD \"DISPLAY_ORDER\"    from W_ITEM_SET wis   WHERE wis.ITEM_SET_ID = CONVERT(?, decimal(22, 6))"
			},
			//11
			new String[]{
					"SELECT supplier_name,\n\rDECODE(supplier_id, 10000, 'IBM',                    10001, 'Microsoft',                    10002, 'Hewlett Packard',                    'Gateway') result \n\r FROM suppliers",
					"SELECT supplier_name,\n\rCASE WHEN supplier_id, 10000, 'IBM',                    10001, 'Microsoft',                    10002, 'Hewlett Packard',                    'Gateway') result \n\r FROM suppliers",
			},
			//12
			new String[]{
					" select " 
			                + " wis.ITEM_SET_ID \"itemId\", 'ITEM_SET' \"itemType\", wis.ITEM_SET_TYPE \"type\", '' \"mdoInd\", wis.MAX_QTY \"maxQty\", wis.MIN_QTY \"minQty\", TO_NUMBER('') \"unitPrice\", wis.DEFAULT_QTY \"defaultQty\", '0^' || wis.ITEM_SET_TYPE || '^' || wis.ITEM_SET_CD \"DISPLAY_ORDER\" "
			                +  "   from W_ITEM_SET wis "
			                + "  WHERE wis.ITEM_SET_ID = TO_NUMBER(?) "
," select  wis.ITEM_SET_ID \"itemId\", 'ITEM_SET' \"itemType\", wis.ITEM_SET_TYPE \"type\", '' \"mdoInd\", wis.MAX_QTY \"maxQty\", wis.MIN_QTY \"minQty\", CONVERT('', decimal(22, 6)) \"unitPrice\", wis.DEFAULT_QTY \"defaultQty\", '0^' || wis.ITEM_SET_TYPE || '^' || wis.ITEM_SET_CD \"DISPLAY_ORDER\"    from W_ITEM_SET wis   WHERE wis.ITEM_SET_ID = CONVERT(?, decimal(22, 6)) "
			},
			//13
			new String[]{
					"SELECT /*com.smartliving.dao.SlvItemDAO.GET_BASKET_PAYMENT_ITEMS_SQL*/  bisa.DISPLAY_SEQ, bisa.ITEM_SET_ID \"itemId\", 'ITEM_SET' \"itemType\", wis.ITEM_SET_TYPE \"type\", bisa.MDO_IND \"mdoInd\", wis.MAX_QTY \"maxQty\", wis.MIN_QTY \"minQty\", CONVERT('', decimal(22,6)) \"unitPrice\", '' \"suppressInd\", bisa.DEFAULT_QTY \"defaultQty\"  from W_BASKET_ITEM_SET_ASSGN_V bisa, W_ITEM_SET wis, (select IFNULL(STR_TO_DATE(?,'%Y%M%D'), TRUNC(SYSDATE())) APP_DATE from dual) ad   where bisa.BASKET_ID = TO_NUMBER(?)         and bisa.ITEM_SET_ID = wis.ITEM_SET_ID    and wis.ITEM_SET_TYPE IN ('PAYMENT') order by DISPLAY_SEQ"
,"SELECT /*com.smartliving.dao.SlvItemDAO.GET_BASKET_PAYMENT_ITEMS_SQL*/  bisa.DISPLAY_SEQ, bisa.ITEM_SET_ID \"itemId\", 'ITEM_SET' \"itemType\", wis.ITEM_SET_TYPE \"type\", bisa.MDO_IND \"mdoInd\", wis.MAX_QTY \"maxQty\", wis.MIN_QTY \"minQty\", CONVERT('', decimal(22,6)) \"unitPrice\", '' \"suppressInd\", bisa.DEFAULT_QTY \"defaultQty\"  from W_BASKET_ITEM_SET_ASSGN_V bisa, W_ITEM_SET wis, (select IFNULL(STR_TO_DATE(?,'%Y%M%D'), DATE(SYSDATE())) APP_DATE from dual) ad   where bisa.BASKET_ID = CONVERT(?, decimal(22, 6))         and bisa.ITEM_SET_ID = wis.ITEM_SET_ID    and wis.ITEM_SET_TYPE IN ('PAYMENT') order by DISPLAY_SEQ"
			},
			//14
			new String[]{
					" SELECT /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_FIELDS*/  wbdlt.html \"title\", wbdlc.html \"category\", wbdls.html \"summary\", null \"detailDescription\",           wba_tab.attb_value \"displayTabId\", wb.description, wdl.description \"displayTabDesc\",           wb.id \"basketId\", wb.type \"type\", null \"description\", wbp.onetime_amt \"unitPrice\",                     wb.PROJ_PLAN_CHRG_IND \"chrgProjPlanInd\", wb.PROJ_PLAN_RELATED_BASKET \"projRelatedBasketId\",                        dropdown.attb_value \"displayDropdown\",                            CASE                                   WHEN INSTR((WBA_ALLOW_DUPLICATE.ATTB_VALUE                                          || ( SELECT (CASE                                                                WHEN INSTR(DISPLAY_TYPE, 'ALLOW_DUPLICATE') > 0                                                                THEN 'Y'                                                         END )                                                 FROM W_DEVICE_DISPLAY_LKUP WHERE LOB = 'SCS' AND type = wb.type)                                                                ), 'Y')>0                                   THEN 'Y'                                 ELSE 'N'                                END \"allowDuplicateInd\"      from w_basket_attb wba_tab,           w_code_lkup wba_tab_seq,           w_basket_attb wba_seq,           w_basket_attb wba_chrg,           w_basket_attb wba_related_basket,           w_basket_attb dropdown,           w_display_lkup wdl,           w_basket_pricing wbp,           (select ifnull(str_to_date(?, '%Y%m%d'), DATE(sysdate())) APP_DATE from dual) ad,           (SELECT w_basket.*, ? locale FROM w_basket WHERE sys_id = 'SCS'  and type = ?  /* pDbType:mysql - com.smartliving.dao.SlvBasketDAO.GET_BASKET_SQL_ID_NOT_EQUALS*/  and id != IFNULL(?, '-1')  /* pDbType:oracle - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_WHERE*/                          ) wb   LEFT OUTER JOIN w_basket_attb wba_allow_duplicate   ON WBA_ALLOW_DUPLICATE.BASKET_ID = WB.id and wba_allow_duplicate.attb_cd = 'ALLOW_DUPLICATE' and wba_allow_duplicate.attb_cd IS NULL   LEFT OUTER JOIN w_basket_display_lkup wbdlt   ON wb.id = wbdlt.basket_id and wb.locale = wbdlt.locale and (wbdlt.DISPLAY_TYPE = 'TITLE' or wbdlt.DISPLAY_TYPE IS NULL)   LEFT OUTER JOIN w_basket_display_lkup wbdlc   ON wb.id = wbdlc.basket_id and wb.locale = wbdlc.locale and (wbdlc.DISPLAY_TYPE = 'CATEGORY' or wbdlc.DISPLAY_TYPE IS NULL)   LEFT OUTER JOIN w_basket_display_lkup wbdls   ON wb.id = wbdls.basket_id and wb.locale = wbdls.locale and (wbdls.DISPLAY_TYPE = 'RP_PROMOT' or wbdls.DISPLAY_TYPE IS NULL)           WHERE (wba_tab.basket_id = wb.id  or (wb.id = nvl(? , -1) and wba_tab.basket_id = -1))                   and wba_tab.attb_cd = 'DISPLAY_TAB'    and wba_tab_seq.GRP_ID = 'SLV_DISPLAY_TAB_SEQ'    and wba_tab_seq.code = wba_tab.attb_value                         and wba_seq.basket_id = wb.id                 and wba_seq.attb_cd = 'DISPLAY_SEQ'                     and wba_chrg.basket_id = wb.id                and wba_chrg.attb_cd = 'PROJ_PLAN_CHRG_IND'              and wba_related_basket.basket_id = wb.id                     and wba_related_basket.attb_cd = 'PROJ_PLAN_BASKET'                and dropdown.basket_id = wb.id               and dropdown.attb_cd = 'DROPDOWN'     and wbp.id = wb.id    and wbp.eff_start_date <= ad.APP_DATE          and nvl(wbp.eff_end_date, TO_DATE('99991231', 'YYYYMMDD')) >= ad.APP_DATE              and wdl.type = 'SLV_DISPLAY_TAB'        and wdl.id = wba_tab.attb_value     and wdl.locale = wb.locale   and exists (SELECT 1 FROM w_code_lkup WHERE grp_id = 'SLV_DISPLAY_TAB_CA' AND code = wba_tab.attb_value AND (description = '*' OR (description like '%' || ? || '%' AND description not like '%' || ? || '^%')  OR description like '%' || ? || '%'))  and not exists (SELECT 1 FROM w_code_lkup WHERE grp_id = 'SLV_DISPLAY_TAB_CNA' AND code = wba_tab.attb_value AND ((description like '%' || ? || '%' AND description not like '%' || ? || '^%') OR description like '%' || ? || '%'))  /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.SQL_WHERE_NOT_CONVERT_PROFILE_TAB*/  AND wba_tab.attb_value != ?  /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_ORDER*/   ORDER BY wba_tab_seq.description, wba_seq.attb_value, wb.description"
					,""
			},
			//15
			new String[]{
					"SELECT /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_FIELDS*/  wbdlt.html \"title\", wbdlc.html \"category\", wbdls.html \"summary\", null \"detailDescription\",           wba_tab.attb_value \"displayTabId\", wb.description, wdl.description \"displayTabDesc\",           wb.id \"basketId\", wb.type \"type\", null \"description\", wbp.onetime_amt \"unitPrice\",                     wb.PROJ_PLAN_CHRG_IND \"chrgProjPlanInd\", wb.PROJ_PLAN_RELATED_BASKET \"projRelatedBasketId\",                        dropdown.attb_value \"displayDropdown\",                            CASE                                   WHEN INSTR((WBA_ALLOW_DUPLICATE.ATTB_VALUE                                          || ( SELECT (CASE                                                                WHEN INSTR(DISPLAY_TYPE, 'ALLOW_DUPLICATE') > 0                                                                THEN 'Y'                                                         END )                                                 FROM W_DEVICE_DISPLAY_LKUP WHERE LOB = 'SCS' AND type = wb.type)                                                                ), 'Y')>0                                   THEN 'Y'                                 ELSE 'N'                                END \"allowDuplicateInd\"      from w_basket_attb wba_tab,           w_code_lkup wba_tab_seq,           w_basket_attb wba_seq,           w_basket_attb wba_chrg,           w_basket_attb wba_related_basket,           w_basket_attb dropdown,           w_display_lkup wdl,           w_basket_pricing wbp,           (select ifnull(str_to_date(?, '%Y%m%d'), DATE(sysdate())) APP_DATE from dual) ad,           (SELECT w_basket.*, ? locale FROM w_basket WHERE sys_id = 'SCS'  and type = ?  /* pDbType:mysql - com.smartliving.dao.SlvBasketDAO.GET_BASKET_SQL_ID_NOT_EQUALS*/  and id != IFNULL(?, '-1')  /* pDbType:oracle - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_WHERE*/                          ) wb   LEFT OUTER JOIN w_basket_attb wba_allow_duplicate   ON WBA_ALLOW_DUPLICATE.BASKET_ID = WB.id and wba_allow_duplicate.attb_cd = 'ALLOW_DUPLICATE' and wba_allow_duplicate.attb_cd IS NULL   LEFT OUTER JOIN w_basket_display_lkup wbdlt   ON wb.id = wbdlt.basket_id and wb.locale = wbdlt.locale and (wbdlt.DISPLAY_TYPE = 'TITLE' or wbdlt.DISPLAY_TYPE IS NULL)   LEFT OUTER JOIN w_basket_display_lkup wbdlc   ON wb.id = wbdlc.basket_id and wb.locale = wbdlc.locale and (wbdlc.DISPLAY_TYPE = 'CATEGORY' or wbdlc.DISPLAY_TYPE IS NULL)   LEFT OUTER JOIN w_basket_display_lkup wbdls   ON wb.id = wbdls.basket_id and wb.locale = wbdls.locale and (wbdls.DISPLAY_TYPE = 'RP_PROMOT' or wbdls.DISPLAY_TYPE IS NULL)"
					,""
			},
			//16 without comment
			new String[]{
					"SELECT   wbdlt.html \"title\", wbdlc.html \"category\", wbdls.html \"summary\", null \"detailDescription\",           wba_tab.attb_value \"displayTabId\", wb.description, wdl.description \"displayTabDesc\",           wb.id \"basketId\", wb.type \"type\", null \"description\", wbp.onetime_amt \"unitPrice\",                     wb.PROJ_PLAN_CHRG_IND \"chrgProjPlanInd\", wb.PROJ_PLAN_RELATED_BASKET \"projRelatedBasketId\",                        dropdown.attb_value \"displayDropdown\",                            CASE                                   WHEN INSTR((WBA_ALLOW_DUPLICATE.ATTB_VALUE                                          || ( SELECT (CASE                                                                WHEN INSTR(DISPLAY_TYPE, 'ALLOW_DUPLICATE') > 0                                                                THEN 'Y'                                                         END )                                                 FROM W_DEVICE_DISPLAY_LKUP WHERE LOB = 'SCS' AND type = wb.type)                                                                ), 'Y')>0                                   THEN 'Y'                                 ELSE 'N'                                END \"allowDuplicateInd\"      from w_basket_attb wba_tab,           w_code_lkup wba_tab_seq,           w_basket_attb wba_seq,           w_basket_attb wba_chrg,           w_basket_attb wba_related_basket,           w_basket_attb dropdown,           w_display_lkup wdl,           w_basket_pricing wbp,           (select ifnull(str_to_date(?, '%Y%m%d'), DATE(sysdate())) APP_DATE from dual) ad,           (SELECT w_basket.*, ? locale FROM w_basket WHERE sys_id = 'SCS'  and type = ?    and id != IFNULL(?, '-1')                            ) wb   LEFT OUTER JOIN w_basket_attb wba_allow_duplicate   ON WBA_ALLOW_DUPLICATE.BASKET_ID = WB.id and wba_allow_duplicate.attb_cd = 'ALLOW_DUPLICATE' and wba_allow_duplicate.attb_cd IS NULL   LEFT OUTER JOIN w_basket_display_lkup wbdlt   ON wb.id = wbdlt.basket_id and wb.locale = wbdlt.locale and (wbdlt.DISPLAY_TYPE = 'TITLE' or wbdlt.DISPLAY_TYPE IS NULL)   LEFT OUTER JOIN w_basket_display_lkup wbdlc   ON wb.id = wbdlc.basket_id and wb.locale = wbdlc.locale and (wbdlc.DISPLAY_TYPE = 'CATEGORY' or wbdlc.DISPLAY_TYPE IS NULL)   LEFT OUTER JOIN w_basket_display_lkup wbdls   ON wb.id = wbdls.basket_id and wb.locale = wbdls.locale and (wbdls.DISPLAY_TYPE = 'RP_PROMOT' or wbdls.DISPLAY_TYPE IS NULL)"
					,""
			},
			//17 without case when
			new String[]{
					" SELECT /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_FIELDS*/  wbdlt.html \"title\", wbdlc.html \"category\", wbdls.html \"summary\", null \"detailDescription\",           wba_tab.attb_value \"displayTabId\", wb.description, wdl.description \"displayTabDesc\",           wb.id \"basketId\", wb.type \"type\", null \"description\", wbp.onetime_amt \"unitPrice\",                     wb.PROJ_PLAN_CHRG_IND \"chrgProjPlanInd\", wb.PROJ_PLAN_RELATED_BASKET \"projRelatedBasketId\",                        dropdown.attb_value \"displayDropdown\",                            from w_basket_attb wba_tab,           w_code_lkup wba_tab_seq,           w_basket_attb wba_seq,           w_basket_attb wba_chrg,           w_basket_attb wba_related_basket,           w_basket_attb dropdown,           w_display_lkup wdl,           w_basket_pricing wbp,           (select ifnull(str_to_date(?, '%Y%m%d'), DATE(sysdate())) APP_DATE from dual) ad,           (SELECT w_basket.*, ? locale FROM w_basket WHERE sys_id = 'SCS'  and type = ?  /* pDbType:mysql - com.smartliving.dao.SlvBasketDAO.GET_BASKET_SQL_ID_NOT_EQUALS*/  and id != IFNULL(?, '-1')  /* pDbType:oracle - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_WHERE*/                          ) wb   LEFT OUTER JOIN w_basket_attb wba_allow_duplicate   ON WBA_ALLOW_DUPLICATE.BASKET_ID = WB.id and wba_allow_duplicate.attb_cd = 'ALLOW_DUPLICATE' and wba_allow_duplicate.attb_cd IS NULL   LEFT OUTER JOIN w_basket_display_lkup wbdlt   ON wb.id = wbdlt.basket_id and wb.locale = wbdlt.locale and (wbdlt.DISPLAY_TYPE = 'TITLE' or wbdlt.DISPLAY_TYPE IS NULL)   LEFT OUTER JOIN w_basket_display_lkup wbdlc   ON wb.id = wbdlc.basket_id and wb.locale = wbdlc.locale and (wbdlc.DISPLAY_TYPE = 'CATEGORY' or wbdlc.DISPLAY_TYPE IS NULL)   LEFT OUTER JOIN w_basket_display_lkup wbdls   ON wb.id = wbdls.basket_id and wb.locale = wbdls.locale and (wbdls.DISPLAY_TYPE = 'RP_PROMOT' or wbdls.DISPLAY_TYPE IS NULL)           WHERE (wba_tab.basket_id = wb.id  or (wb.id = nvl(? , -1) and wba_tab.basket_id = -1))                   and wba_tab.attb_cd = 'DISPLAY_TAB'    and wba_tab_seq.GRP_ID = 'SLV_DISPLAY_TAB_SEQ'    and wba_tab_seq.code = wba_tab.attb_value                         and wba_seq.basket_id = wb.id                 and wba_seq.attb_cd = 'DISPLAY_SEQ'                     and wba_chrg.basket_id = wb.id                and wba_chrg.attb_cd = 'PROJ_PLAN_CHRG_IND'              and wba_related_basket.basket_id = wb.id                     and wba_related_basket.attb_cd = 'PROJ_PLAN_BASKET'                and dropdown.basket_id = wb.id               and dropdown.attb_cd = 'DROPDOWN'     and wbp.id = wb.id    and wbp.eff_start_date <= ad.APP_DATE          and nvl(wbp.eff_end_date, TO_DATE('99991231', 'YYYYMMDD')) >= ad.APP_DATE              and wdl.type = 'SLV_DISPLAY_TAB'        and wdl.id = wba_tab.attb_value     and wdl.locale = wb.locale   and exists (SELECT 1 FROM w_code_lkup WHERE grp_id = 'SLV_DISPLAY_TAB_CA' AND code = wba_tab.attb_value AND (description = '*' OR (description like '%' || ? || '%' AND description not like '%' || ? || '^%')  OR description like '%' || ? || '%'))  and not exists (SELECT 1 FROM w_code_lkup WHERE grp_id = 'SLV_DISPLAY_TAB_CNA' AND code = wba_tab.attb_value AND ((description like '%' || ? || '%' AND description not like '%' || ? || '^%') OR description like '%' || ? || '%'))  /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.SQL_WHERE_NOT_CONVERT_PROFILE_TAB*/  AND wba_tab.attb_value != ?  /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_ORDER*/   ORDER BY wba_tab_seq.description, wba_seq.attb_value, wb.description"
					,""
			},
			//18 without case when and subquery
			new String[]{
					"SELECT /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_FIELDS*/ wbdlt.html \"title\"" +
							"	,wbdlc.html \"category\"" +
							"	,wbdls.html \"summary\"" +
							"	,NULL \"detailDescription\"" +
							"	,wba_tab.attb_value \"displayTabId\"" +
							"	,wb.description" +
							"	,wdl.description \"displayTabDesc\"" +
							"	,wb.id \"basketId\"" +
							"	,wb.type \"type\"" +
							"	,NULL \"description\"" +
							"	,wbp.onetime_amt \"unitPrice\"" +
							"	,wb.PROJ_PLAN_CHRG_IND \"chrgProjPlanInd\"" +
							"	,wb.PROJ_PLAN_RELATED_BASKET \"projRelatedBasketId\"" +
							"	,dropdown.attb_value \"displayDropdown\"" +
							"" +
							" FROM w_basket_attb wba_tab" +
							"	,w_code_lkup wba_tab_seq" +
							"	,w_basket_attb wba_seq" +
							"	,w_basket_attb wba_chrg" +
							"	,w_basket_attb wba_related_basket" +
							"	,w_basket_attb dropdown" +
							"	,w_display_lkup wdl" +
							"	,w_basket_pricing wbp" +
							"" +
							" LEFT OUTER JOIN w_basket_attb wba_allow_duplicate ON WBA_ALLOW_DUPLICATE.BASKET_ID = WB.id" +
							"	AND wba_allow_duplicate.attb_cd = 'ALLOW_DUPLICATE'" +
							"	AND wba_allow_duplicate.attb_cd IS NULL" +
							" LEFT OUTER JOIN w_basket_display_lkup wbdlt ON wb.id = wbdlt.basket_id" +
							"	AND wb.locale = wbdlt.locale" +
							"	AND (" +
							"		wbdlt.DISPLAY_TYPE = 'TITLE'" +
							"		OR wbdlt.DISPLAY_TYPE IS NULL" +
							"		)" +
							" LEFT OUTER JOIN w_basket_display_lkup wbdlc ON wb.id = wbdlc.basket_id" +
							"	AND wb.locale = wbdlc.locale" +
							"	AND (" +
							"		wbdlc.DISPLAY_TYPE = 'CATEGORY'" +
							"		OR wbdlc.DISPLAY_TYPE IS NULL" +
							"		)" +
							" LEFT OUTER JOIN w_basket_display_lkup wbdls ON wb.id = wbdls.basket_id" +
							"	AND wb.locale = wbdls.locale" +
							"	AND (" +
							"		wbdls.DISPLAY_TYPE = 'RP_PROMOT'" +
							"		OR wbdls.DISPLAY_TYPE IS NULL" +
							"		)" ,
							""
			},
			//19 final test... adding phrase here to find out which phrase has error
			new String[]{
					"SELECT /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_FIELDS*/ wbdlt.html \"title\"" +
							"	,wbdlc.html \"category\"" +
							"	,wbdls.html \"summary\"" +
							"	,NULL \"detailDescription\"" +
							"	,wba_tab.attb_value \"displayTabId\"" +
							"	,wb.description" +
							"	,wdl.description \"displayTabDesc\"" +
							"	,wb.id \"basketId\"" +
							"	,wb.type \"type\"" +
							"	,NULL \"description\"" +
							"	,wbp.onetime_amt \"unitPrice\"" +
							"	,wb.PROJ_PLAN_CHRG_IND \"chrgProjPlanInd\"" +
							"	,wb.PROJ_PLAN_RELATED_BASKET \"projRelatedBasketId\"" +
							"	,dropdown.attb_value \"displayDropdown\"" +
							"	,CASE " +
							"		WHEN INSTR((" +
							"					WBA_ALLOW_DUPLICATE.ATTB_VALUE || (" +
							"						SELECT (" +
							"								CASE " +
							"									WHEN INSTR(DISPLAY_TYPE, 'ALLOW_DUPLICATE') > 0" +
							"										THEN 'Y'" +
							"									END" +
							"								)" +
							"						FROM W_DEVICE_DISPLAY_LKUP" +
							"						WHERE LOB = 'SCS'" +
							"							AND type = wb.type" +
							"						)" +
							"					), 'Y') > 0" +
							"			THEN 'Y'" +
							"		ELSE 'N'" +
							"		END \"allowDuplicateInd\"" +
							" FROM w_basket_attb wba_tab" +
							"	,w_code_lkup wba_tab_seq" +
							"	,w_basket_attb wba_seq" +
							"	,w_basket_attb wba_chrg" +
							"	,w_basket_attb wba_related_basket" +
							"	,w_basket_attb dropdown" +
							"	,w_display_lkup wdl" +
							"	,w_basket_pricing wbp" +
//							"	,(" +
//							"		SELECT ifnull(str_to_date(?, '%Y%m%d'), DATE (sysdate())) APP_DATE" +
//							"		FROM dual" +
//							"		) ad" +
							"	,(" +
							"		SELECT w_basket.*" +
							"			,? locale" +
							"		FROM w_basket" +
							"		WHERE sys_id = 'SCS'" +
							"			AND type = ? /* pDbType:mysql - com.smartliving.dao.SlvBasketDAO.GET_BASKET_SQL_ID_NOT_EQUALS*/" +
							"			AND id != IFNULL(?, '-1') /* pDbType:oracle - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_WHERE*/" +
							"		) wb" +
							""+
							" LEFT OUTER JOIN w_basket_attb wba_allow_duplicate ON WBA_ALLOW_DUPLICATE.BASKET_ID = WB.id" +
							"	AND wba_allow_duplicate.attb_cd = 'ALLOW_DUPLICATE'" +
							"	AND wba_allow_duplicate.attb_cd IS NULL" +
							" LEFT OUTER JOIN w_basket_display_lkup wbdlt ON wb.id = wbdlt.basket_id" +
							"	AND wb.locale = wbdlt.locale" +
							"	AND (" +
							"		wbdlt.DISPLAY_TYPE = 'TITLE'" +
							"		OR wbdlt.DISPLAY_TYPE IS NULL" +
							"		)"+
							" LEFT OUTER JOIN w_basket_display_lkup wbdlc ON wb.id = wbdlc.basket_id" +
							"	AND wb.locale = wbdlc.locale" +
							"	AND (" +
							"		wbdlc.DISPLAY_TYPE = 'CATEGORY'" +
							"		OR wbdlc.DISPLAY_TYPE IS NULL" +
							"		)"+
							" LEFT OUTER JOIN w_basket_display_lkup wbdls ON wb.id = wbdls.basket_id" +
							"	AND wb.locale = wbdls.locale" +
							"	AND (" +
							"		wbdls.DISPLAY_TYPE = 'RP_PROMOT'" +
							"		OR wbdls.DISPLAY_TYPE IS NULL" +
							"		)" +
							" WHERE (" +
							"		wba_tab.basket_id = wb.id" +
							"		OR (" +
							"			wb.id = nvl(?, - 1)" +
							"			AND wba_tab.basket_id = - 1" +
							"			)" +
							"		)"+
							"	AND wba_tab.attb_cd = 'DISPLAY_TAB'" +
							"	AND wba_tab_seq.GRP_ID = 'SLV_DISPLAY_TAB_SEQ'" +
							"	AND wba_tab_seq.code = wba_tab.attb_value" +
							"	AND wba_seq.basket_id = wb.id" +
							"	AND wba_seq.attb_cd = 'DISPLAY_SEQ'" +
							"	AND wba_chrg.basket_id = wb.id" +
							"	AND wba_chrg.attb_cd = 'PROJ_PLAN_CHRG_IND'" +
							"	AND wba_related_basket.basket_id = wb.id" +
							"	AND wba_related_basket.attb_cd = 'PROJ_PLAN_BASKET'" +
							"	AND dropdown.basket_id = wb.id" +
							"	AND dropdown.attb_cd = 'DROPDOWN'" +
							"	AND wbp.id = wb.id" +
							"	AND wbp.eff_start_date <= ad.APP_DATE" +
							"	AND nvl(wbp.eff_end_date, TO_DATE('99991231', 'YYYYMMDD')) >= ad.APP_DATE" +
							"	AND wdl.type = 'SLV_DISPLAY_TAB'" +
							"	AND wdl.id = wba_tab.attb_value" +
							"	AND wdl.locale = wb.locale"+
							"	AND EXISTS (" +
							"		SELECT 1" +
							"		FROM w_code_lkup" +
							"		WHERE grp_id = 'SLV_DISPLAY_TAB_CA'" +
							"			AND code = wba_tab.attb_value" +
							"			AND (" +
							"				description = '*'" +
							"				OR (" +
							"					description LIKE '%' || ? || '%'" +
							"					AND description NOT LIKE '%' || ? || '^%'" +
							"					)" +
							"				OR description LIKE '%' || ? || '%'" +
							"				)" +
							"		)" +
							"	AND NOT EXISTS (" +
							"		SELECT 1" +
							"		FROM w_code_lkup" +
							"		WHERE grp_id = 'SLV_DISPLAY_TAB_CNA'" +
							"			AND code = wba_tab.attb_value" +
							"			AND (" +
							"				(" +
							"					description LIKE '%' || ? || '%'" +
							"					AND description NOT LIKE '%' || ? || '^%'" +
							"					)" +
							"				OR description LIKE '%' || ? || '%'" +
							"				)" +
							"		) /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.SQL_WHERE_NOT_CONVERT_PROFILE_TAB*/" +
							"	AND wba_tab.attb_value != ? /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_ORDER*/"+
							" ORDER BY wba_tab_seq.description" +
							"	,wba_seq.attb_value" +
							"	,wb.description" 
							,""
			},//20 formated sql, equal to 14
			new String[]{
					" SELECT /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_FIELDS*/ wbdlt.html \"title\"" +
							"	,wbdlc.html \"category\"" +
							"	,wbdls.html \"summary\"" +
							"	,NULL \"detailDescription\"" +
							"	,wba_tab.attb_value \"displayTabId\"" +
							"	,wb.description" +
							"	,wdl.description \"displayTabDesc\"" +
							"	,wb.id \"basketId\"" +
							"	,wb.type \"type\"" +
							"	,NULL \"description\"" +
							"	,wbp.onetime_amt \"unitPrice\"" +
							"	,wb.PROJ_PLAN_CHRG_IND \"chrgProjPlanInd\"" +
							"	,wb.PROJ_PLAN_RELATED_BASKET \"projRelatedBasketId\"" +
							"	,dropdown.attb_value \"displayDropdown\"" +
							"	,CASE " +
							"		WHEN INSTR((" +
							"					WBA_ALLOW_DUPLICATE.ATTB_VALUE || (" +
							"						SELECT (" +
							"								CASE " +
							"									WHEN INSTR(DISPLAY_TYPE, 'ALLOW_DUPLICATE') > 0" +
							"										THEN 'Y'" +
							"									END" +
							"								)" +
							"						FROM W_DEVICE_DISPLAY_LKUP" +
							"						WHERE LOB = 'SCS'" +
							"							AND type = wb.type" +
							"						)" +
							"					), 'Y') > 0" +
							"			THEN 'Y'" +
							"		ELSE 'N'" +
							"		END \"allowDuplicateInd\"" +
							" FROM w_basket_attb wba_tab" +
							"	,w_code_lkup wba_tab_seq" +
							"	,w_basket_attb wba_seq" +
							"	,w_basket_attb wba_chrg" +
							"	,w_basket_attb wba_related_basket" +
							"	,w_basket_attb dropdown" +
							"	,w_display_lkup wdl" +
							"	,w_basket_pricing wbp" +
							"	,(" +
//							"		SELECT ifnull(str_to_date(?, '%Y%m%d'), DATE (sysdate())) APP_DATE" +
							"		select nvl(to_date(?, 'YYYYMMDD'), trunc(sysdate)) APP_DATE" +
							"		FROM dual" +
							"		) ad" +
							"	,(" +
							"		SELECT w_basket.*" +
							"			,? locale" +
							"		FROM w_basket" +
							"		WHERE sys_id = 'SCS'" +
							"			AND type = ? /* pDbType:mysql - com.smartliving.dao.SlvBasketDAO.GET_BASKET_SQL_ID_NOT_EQUALS*/" +
							"			AND id != IFNULL(?, '-1') /* pDbType:oracle - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_WHERE*/" +
							"		) wb" +
							" LEFT OUTER JOIN w_basket_attb wba_allow_duplicate ON WBA_ALLOW_DUPLICATE.BASKET_ID = WB.id" +
							"	AND wba_allow_duplicate.attb_cd = 'ALLOW_DUPLICATE'" +
							"	AND wba_allow_duplicate.attb_cd IS NULL" +
							" LEFT OUTER JOIN w_basket_display_lkup wbdlt ON wb.id = wbdlt.basket_id" +
							"	AND wb.locale = wbdlt.locale" +
							"	AND (" +
							"		wbdlt.DISPLAY_TYPE = 'TITLE'" +
							"		OR wbdlt.DISPLAY_TYPE IS NULL" +
							"		)" +
							" LEFT OUTER JOIN w_basket_display_lkup wbdlc ON wb.id = wbdlc.basket_id" +
							"	AND wb.locale = wbdlc.locale" +
							"	AND (" +
							"		wbdlc.DISPLAY_TYPE = 'CATEGORY'" +
							"		OR wbdlc.DISPLAY_TYPE IS NULL" +
							"		)" +
							" LEFT OUTER JOIN w_basket_display_lkup wbdls ON wb.id = wbdls.basket_id" +
							"	AND wb.locale = wbdls.locale" +
							"	AND (" +
							"		wbdls.DISPLAY_TYPE = 'RP_PROMOT'" +
							"		OR wbdls.DISPLAY_TYPE IS NULL" +
							"		)" +
							" WHERE (" +
							"		wba_tab.basket_id = wb.id" +
							"		OR (" +
							"			wb.id = nvl(?, - 1)" +
							"			AND wba_tab.basket_id = - 1" +
							"			)" +
							"		)" +
							"	AND wba_tab.attb_cd = 'DISPLAY_TAB'" +
							"	AND wba_tab_seq.GRP_ID = 'SLV_DISPLAY_TAB_SEQ'" +
							"	AND wba_tab_seq.code = wba_tab.attb_value" +
							"	AND wba_seq.basket_id = wb.id" +
							"	AND wba_seq.attb_cd = 'DISPLAY_SEQ'" +
							"	AND wba_chrg.basket_id = wb.id" +
							"	AND wba_chrg.attb_cd = 'PROJ_PLAN_CHRG_IND'" +
							"	AND wba_related_basket.basket_id = wb.id" +
							"	AND wba_related_basket.attb_cd = 'PROJ_PLAN_BASKET'" +
							"	AND dropdown.basket_id = wb.id" +
							"	AND dropdown.attb_cd = 'DROPDOWN'" +
							"	AND wbp.id = wb.id" +
							"	AND wbp.eff_start_date <= ad.APP_DATE" +
							"	AND nvl(wbp.eff_end_date, TO_DATE('99991231', 'YYYYMMDD')) >= ad.APP_DATE" +
							"	AND wdl.type = 'SLV_DISPLAY_TAB'" +
							"	AND wdl.id = wba_tab.attb_value" +
							"	AND wdl.locale = wb.locale" +
							"	AND EXISTS (" +
							"		SELECT 1" +
							"		FROM w_code_lkup" +
							"		WHERE grp_id = 'SLV_DISPLAY_TAB_CA'" +
							"			AND code = wba_tab.attb_value" +
							"			AND (" +
							"				description = '*'" +
							"				OR (" +
							"					description LIKE '%' || ? || '%'" +
							"					AND description NOT LIKE '%' || ? || '^%'" +
							"					)" +
							"				OR description LIKE '%' || ? || '%'" +
							"				)" +
							"		)" +
							"	AND NOT EXISTS (" +
							"		SELECT 1" +
							"		FROM w_code_lkup" +
							"		WHERE grp_id = 'SLV_DISPLAY_TAB_CNA'" +
							"			AND code = wba_tab.attb_value" +
							"			AND (" +
							"				(" +
							"					description LIKE '%' || ? || '%'" +
							"					AND description NOT LIKE '%' || ? || '^%'" +
							"					)" +
							"				OR description LIKE '%' || ? || '%'" +
							"				)" +
							"		) /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.SQL_WHERE_NOT_CONVERT_PROFILE_TAB*/" +
							"	AND wba_tab.attb_value != ? /* pDbType:mysql - com.smartliving.dao.SlvItemDAO.GET_BASKET_SQL_ORDER*/" +
							" ORDER BY wba_tab_seq.description" +
							"	,wba_seq.attb_value" +
							"	,wb.description" +
							"",
							""
			},//21
			new String[]{
					" select CA.order_id ," +
							" CASE WHEN TRIM(CA.UNIT_NO) IS NULL THEN NULL ELSE 'FLAT ' || CA.UNIT_NO || ', ' END  || " +
							" CASE WHEN TRIM(ca.floor_no) IS NULL THEN NULL ELSE ca.floor_no || '/FLOOR, ' END || " +
							" CASE WHEN TRIM(ca.build_no) IS NULL THEN NULL ELSE ca.build_no || ', ' END || " +
							" CASE WHEN TRIM(ca.hi_lot_no) IS NULL THEN NULL ELSE 'LAND LOT NO ' || ca.hi_lot_no || ', ' END || " +
				            "      CASE WHEN TRIM(ca.unit_no) IS NOT NULL " +
				            "           THEN chr(13) " +
				            "          ELSE " +
				            "              CASE WHEN TRIM(ca.floor_no) IS NOT NULL " +
				            "                   THEN chr(13) " + 
				            "                   ELSE " +
				            "                       CASE WHEN TRIM(ca.build_no) IS NOT NULL " +
				            "                            THEN chr(13) " + 
				            "                            else " + 
				            "                                CASE WHEN TRIM(ca.hi_lot_no) IS NOT NULL " +
				            "                                     THEN chr(13) " +
				            "                                     ELSE '' " +
				            "                                END " + 
				            "                       END " +
				            "                   END " +
				            "     END || " +
				            " CASE WHEN TRIM(ca.sect_desc) IS NULL THEN NULL ELSE ca.sect_desc || ', ' END || " +
				            " CASE WHEN TRIM(ca.str_no) IS NULL THEN NULL ELSE ca.str_no || ' ' END || " +
				            " CASE WHEN TRIM(ca.str_name) IS NULL THEN NULL ELSE ca.str_name || ' ' END || " + 
				            " CASE WHEN TRIM(ca.STR_CAT_DESC) IS NULL THEN NULL ELSE ca.STR_CAT_DESC || ', ' END || " +
				            "     CASE WHEN TRIM(ca.sect_desc) IS NOT NULL " +
				            "          THEN chr(13) " +
				            "          ELSE " +
				            "              CASE WHEN TRIM(ca.str_no) IS NOT NULL " +
				            "                   THEN chr(13) " +
				            "                   ELSE " +
				            "                       CASE WHEN TRIM(ca.str_name) IS NOT NULL " +
				            "                            THEN chr(13) " +
				            "                            ELSE " +
				            "                                CASE WHEN TRIM(ca.STR_CAT_DESC) IS NOT NULL " +
				            "                                     THEN chr(13) " +
				            "                                     ELSE '' " +
				            "                                END " +
				            "                       END " +
				            "                   END " +
				            "     END || " +
				            " CASE WHEN TRIM(Ca.Dist_Desc) IS NULL THEN NULL ELSE Ca.Dist_Desc || ', ' END || Ca.Area_Desc AS DELIVERY_ADDR" +
				            "  From Bomweb_Cust_Addr Ca ,Bomweb_Order_Service_Slv Boss, Bomweb_Order Bo " +
				            "  where CA.ORDER_ID = BO.ORDER_ID and ADDR_USAGE = 'DA' AND CA.ORDER_ID=:orderId AND ROWNUM=1"
				            ,""
			},//22
			new String[]{
					" select WSVD.ITEM_ID \"visitId\", " 
							+	" WSVD.AVAIL_WORKING_PARTY \"availWorkingParty\",  WSVD.MAX_WORKING_PARTY \"maxWorkingParty\", " 
							+	" WSVD.MANDATORY_WORKING_PARTY \"mandatoryWorkingParty\", WSVD.DEFAULT_WORKING_PARTY \"defaultWorkingParty\", " 
							+	" fj.FIELD_JOB_ID \"fieldJobId\", fj.DESCRIPTION \"description\" , " 
							+	" fj.MAX_QTY \"maxQty\", fj.MIN_QTY \"minQty\", fj.DEFAULT_QTY \"defaultQty\", fj.MDO_IND \"mdoInd\" "  
							+	" from  "
							+	" (select TRUNC(sysdate) APP_DATE from DUAL) AD, "
							+	" W_SLV_VISIT_DTL WSVD " 
							+	" LEFT OUTER JOIN  "
							+	" (select WVFJA.VISIT_ID, WVFJA.FIELD_JOB_ID, " 
							+	"		 WVFJA.MAX_QTY , WVFJA.MIN_QTY , WVFJA.DEFAULT_QTY , WVFJA.MDO_IND, " 
							+	"		 WVFJA.EFF_START_DATE, EFF_END_DATE, " 
							+	"		 wi.description  " 
							+	"		 from W_VISIT_FIELD_JOB_ASSGN WVFJA, W_ITEM WI " 
							+	"		 where wvfja.field_job_id = wi.id) fj " 
							+	" ON WSVD.ITEM_ID = fj.VISIT_ID	 "  
							+	" where WSVD.item_id = ?  "
							+	" and AD.APP_DATE >= NVL(FJ.EFF_START_DATE, TRUNC(sysdate)) " 
							+	" and AD.APP_DATE <= NVL(fj.EFF_END_DATE, TO_DATE('99991231', 'YYYYMMDD')) "
							,
							" select WSVD.ITEM_ID \"visitId\", "
							+   " WSVD.AVAIL_WORKING_PARTY \"availWorkingParty\",  WSVD.MAX_WORKING_PARTY \"maxWorkingParty\", "
							+   " WSVD.MANDATORY_WORKING_PARTY \"mandatoryWorkingParty\", WSVD.DEFAULT_WORKING_PARTY \"defaultWorkingParty\", "
							+   " fj.FIELD_JOB_ID \"fieldJobId\", fj.DESCRIPTION \"description\" , "
							+   " fj.MAX_QTY \"maxQty\", fj.MIN_QTY \"minQty\", fj.DEFAULT_QTY \"defaultQty\", fj.MDO_IND \"mdoInd\" "
							+   " from  "
							+   " (select DATE(SYSDATE()) APP_DATE from DUAL) AD, "
							+   " W_SLV_VISIT_DTL WSVD "
							+   " LEFT OUTER JOIN  "
							+   " (select WVFJA.VISIT_ID, WVFJA.FIELD_JOB_ID, "
							+   "		 WVFJA.MAX_QTY , WVFJA.MIN_QTY , WVFJA.DEFAULT_QTY , WVFJA.MDO_IND, "
							+   "		 WVFJA.EFF_START_DATE, EFF_END_DATE, "
							+   "		 wi.description  "
							+   "		 from W_VISIT_FIELD_JOB_ASSGN WVFJA, W_ITEM WI "
							+   "		 where wvfja.field_job_id = wi.id) fj "
							+   " ON WSVD.ITEM_ID = fj.VISIT_ID	 "
							+   " where WSVD.item_id = ?  "
							+   " and AD.APP_DATE >= IFNULL(FJ.EFF_START_DATE, DATE(SYSDATE())) "
							+   " and AD.APP_DATE <= IFNULL(fj.EFF_END_DATE, STR_TO_DATE('99991231', '%Y%m%d')) "
			},//23
			new String[]{
					"SELECT sdsaa.attb_id \"attbId\", " + 
							"  sda.name \"name\", " + 
							"  sdsaa.ord_mdo \"ordMdo\", " + 
							"  wdl.description \"desc\", " + 
							"  sda.field_type \"fieldType\", " + 
							"  sda.length \"length\", " + 
							"  CASE sdsaa.ord_mdo WHEN 'M' THEN sda.css_class||' M' ELSE sda.css_class END \"cssClass\", " +
							"  sda.html_input_type \"htmlInputType\", " + 
							"  sdsaa.row_num \"row\", " + 
							"  sdsaa.column_num \"column\", " + 
							"  (SELECT listagg(sdao.option_value||','||wdll.description,',') within group(order by sdao.display_seq)  " +
							"  FROM SB_DIC_ATTB_OPTION sdao, " + 
							"  w_display_lkup wdll " + 
							"  WHERE sdao.attb_id =sda.id " + 
							"  AND wdll.locale = ? " + 
							"  AND wdll.id = sdao.option_id " + 
							"  and wdll.type = 'ATTB_DISPLAY_OPTION' " + 
							"  ) \"optionStr\", " + 
							"    sdsaa.max \"max\", " + 
							"    sdsaa.min \"min\" " + 
							"FROM sb_dic_attb sda, " + 
							"  sb_dic_srv_attb_assgn sdsaa, " + 
							"  w_display_lkup wdl " + 
							"WHERE sdsaa.attb_id = sda.id " + 
							"AND wdl.type       = 'ATTB_DISPLAY_NAME' " + 
							"AND wdl.locale     = ? " + 
							"AND wdl.id         = sdsaa.attb_id " + 
							"AND sdsaa.srv_type = ? " +
							"ORDER BY sdsaa.row_num, sdsaa.column_num",
					"SELECT sdsaa.attb_id \"attbId\", " + 
							"  sda.name \"name\", " + 
							"  sdsaa.ord_mdo \"ordMdo\", " + 
							"  wdl.description \"desc\", " + 
							"  sda.field_type \"fieldType\", " + 
							"  sda.length \"length\", " + 
							"  CASE sdsaa.ord_mdo WHEN 'M' THEN sda.css_class||' M' ELSE sda.css_class END \"cssClass\", " +
							"  sda.html_input_type \"htmlInputType\", " + 
							"  sdsaa.row_num \"row\", " + 
							"  sdsaa.column_num \"column\", " + 
							"  (SELECT listagg(sdao.option_value||','||wdll.description,',') within group(order by sdao.display_seq)  " +
							"  FROM SB_DIC_ATTB_OPTION sdao, " + 
							"  w_display_lkup wdll " + 
							"  WHERE sdao.attb_id =sda.id " + 
							"  AND wdll.locale = ? " + 
							"  AND wdll.id = sdao.option_id " + 
							"  and wdll.type = 'ATTB_DISPLAY_OPTION' " + 
							"  ) \"optionStr\", " + 
							"    sdsaa.max \"max\", " + 
							"    sdsaa.min \"min\" " + 
							"FROM sb_dic_attb sda, " + 
							"  sb_dic_srv_attb_assgn sdsaa, " + 
							"  w_display_lkup wdl " + 
							"WHERE sdsaa.attb_id = sda.id " + 
							"AND wdl.type       = 'ATTB_DISPLAY_NAME' " + 
							"AND wdl.locale     = ? " + 
							"AND wdl.id         = sdsaa.attb_id " + 
							"AND sdsaa.srv_type = ? " +
							"ORDER BY sdsaa.row_num, sdsaa.column_num"
			}
		};
		
		return ret;
	}
}
