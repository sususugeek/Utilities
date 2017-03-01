package com.pccw.util.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.google.common.base.Joiner;
import com.pccw.util.db.DaoHelperResolver;
import com.pccw.util.db.DaoHelperResolverImpl;
import com.pccw.util.db.stringOracleType.OraArray;
import com.pccw.util.search.Criteria;
import com.pccw.util.search.SearchResult;

public abstract class DaoBaseCustomQuery extends NamedParameterJdbcDaoSupport {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	// E0002506 - Springboard SLV Enhancement (2015)
	private DaoHelperResolver daoHelperResolver;
	
	public DaoHelperResolver getDaoHelperResolver() {
		if(daoHelperResolver == null){
			daoHelperResolver = DaoHelperResolverImpl.getInstance();
		}
		return daoHelperResolver;
	}

	public void setDaoHelperResolver(DaoHelperResolver daoHelperResolver) {
		this.daoHelperResolver = daoHelperResolver;
	}
	// E0002506 - Springboard SLV Enhancement (2015)
	
    protected void logSql(String pMethod, String pSql) {
        if(logger.isDebugEnabled()) {
            logger.debug("[{}] Sql: {}", pMethod, pSql);
        }
    }
    
    protected void logSql(String pMethod, String pSql, MapSqlParameterSource pParam) {
        if(logger.isDebugEnabled()) {
			logger.debug("[{}] Sql: {}, Param: {}", new Object[] {
					pMethod,
					pSql,
					Joiner.on(",").withKeyValueSeparator("=")
							.useForNull("null").join(pParam.getValues()) });
        }
    }
    
    protected void logSql(String pMethod, String pSql, Object pObject) {
        if(logger.isDebugEnabled()) {
            logger.debug("[{}] Sql: {}, Object: {}", new Object[]{pMethod, pSql, pObject.toString()});
        }
    }
    
    @SuppressWarnings("unchecked")
	protected <T> List<T> queryForList(String pSql, SqlParameterSource pParamSource, RowMapper pRowMapper) throws Exception {
    	return new ArrayList<T>(getNamedParameterJdbcTemplate().query(pSql, pParamSource, pRowMapper));
    }

    @SuppressWarnings("unchecked")
	protected <T> List<T> queryForList(String pSql, SqlParameterSource pParamSource, Class<T> pElementClass) throws Exception {
        return new ArrayList<T>(getNamedParameterJdbcTemplate().queryForList(pSql, pParamSource, pElementClass));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> T queryForObject(String pQueryName, StringBuilder pSqlBuilder, Criteria pCriteria, RowMapper pRowMapper) throws Exception {
        if(!pCriteria.getCriteriaList().isEmpty()) {
            pSqlBuilder.append(" WHERE ").append(Joiner.on(" AND ").useForNull("null").join(pCriteria.getCriteriaList()));
        }

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(pCriteria.getValueMap());

        logSql(pQueryName, pSqlBuilder.toString(), mapSqlParameterSource);

        List list = getNamedParameterJdbcTemplate().query(pSqlBuilder.toString(), mapSqlParameterSource, pRowMapper);
        if(list == null || list.size() == 0) {
            return null;
        }

        return (T) list.get(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> T queryForObject(String sql, SqlParameterSource paramSource, RowMapper rowMapper) throws Exception {
        List list = getNamedParameterJdbcTemplate().query(sql, paramSource, rowMapper);
        if(list == null || list.size() == 0) {
            return null;
        }

        return (T) list.get(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T> T queryForObject(String sql, SqlParameterSource paramSource, Class<T> pElementType) throws Exception {
        List list = getNamedParameterJdbcTemplate().queryForList(sql, paramSource, pElementType);
        if(list == null || list.size() == 0) {
            return null;
        }
        return (T) list.get(0);
    }

	protected <T> SearchResult<T> search(String pName,
			StringBuilder pSqlBuilder, Criteria pCriteria,
			final Class<T> pElementType) throws Exception {
    	return search(pName, pSqlBuilder, pCriteria, new RowMapperByFieldName(pElementType, this.getDaoHelperResolver().resolveDBType(this.getConnection())));
    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> SearchResult<T> search(String pName, StringBuilder pSqlBuilder, Criteria pCriteria, RowMapper pRowMapper) throws Exception {
        
    	SearchResult<T> result = SearchResult.newInstance();

    	StringBuilder totalCntSql = new StringBuilder();
    	StringBuilder resultSql = new StringBuilder();
    	
    	totalCntSql.append("SELECT COUNT(*) FROM ( ");
    	resultSql.append("SELECT * FROM (SELECT ROWNUM as ROW_SEQ, rs.* FROM ( ");
    	
    	if (pSqlBuilder.toString().indexOf("{0}") != -1) {
    		String sql = pSqlBuilder.toString();
    		pSqlBuilder.setLength(0);
    		String whereStr = pCriteria.getCriteriaList().isEmpty() ? "" : " WHERE " + Joiner.on(" AND ").useForNull("null").join(pCriteria.getCriteriaList()); 
    		pSqlBuilder.append(MessageFormat.format(sql, new Object[] {whereStr}));
    	} else if (!pCriteria.getCriteriaList().isEmpty()) {
            pSqlBuilder.append(" WHERE ").append(Joiner.on(" AND ").useForNull("null").join(pCriteria.getCriteriaList()));
        }

		if (!pCriteria.getGroupByList().isEmpty()) {
            pSqlBuilder.append(" GROUP BY ").append(Joiner.on(",").useForNull("null").join(pCriteria.getGroupByList()));
        }

        if(!pCriteria.getOrderByMap().isEmpty()) {
            pSqlBuilder.append(" ORDER BY ").append(Joiner.on(",").withKeyValueSeparator(" ").useForNull("null").join(pCriteria.getOrderByMap()));
        }
        totalCntSql.append(pSqlBuilder.toString());
        totalCntSql.append(" )");
        resultSql.append(pSqlBuilder.toString());
        resultSql.append(" ) rs)");
        
		if (pCriteria.getLimit() > 0) {
			resultSql.append(" WHERE ROW_SEQ BETWEEN ")
					.append(String.valueOf(pCriteria.getOffset() + 1)).append(" AND ")
					.append(String.valueOf(pCriteria.getLimit() + pCriteria.getOffset()));
		}
        
		Map<String, Object> valueMap = pCriteria.getValueMap();
		for (Entry<String, Object> entry : valueMap.entrySet()) {
			if (entry.getValue() instanceof OraArray) {
				entry.setValue(((OraArray) entry.getValue()).getOracleArray(getDataSource()));
			}
		}
		
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(pCriteria.getValueMap());

        logSql(pName, resultSql.toString(), mapSqlParameterSource);

        List list = getNamedParameterJdbcTemplate().query(resultSql.toString(), mapSqlParameterSource, pRowMapper);
        result.setResult(new ArrayList<T>(list));

        int rows = getNamedParameterJdbcTemplate().queryForInt(totalCntSql.toString(), mapSqlParameterSource);
        result.setTotalCount(rows);

        return result;
    }
}