package com.pccw.util.db;

import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class StreamingResultSetEnabledJdbcTemplate extends JdbcTemplate {
	public StreamingResultSetEnabledJdbcTemplate(final DataSource dataSource) {
		super(dataSource);
		
	}
	  public StreamingResultSetEnabledJdbcTemplate(final DataSource dataSource, final boolean lazyInit)
	    {
	        super(dataSource, lazyInit);
	    }
@Override
protected void applyStatementSettings(final Statement stmt) throws SQLException{
	int fetchSize = getFetchSize();
	stmt.setFetchSize(fetchSize);
	
	int maxRows = getMaxRows();
	if(maxRows>0) {
		stmt.setMaxRows(maxRows);
	}
	DataSourceUtils.applyTimeout(stmt, getDataSource(), getQueryTimeout());
}
}
