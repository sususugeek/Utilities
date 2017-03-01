package com.pccw.util.db;

import java.util.ArrayList;

import javax.sql.DataSource;

public class MapSqlParameterSource extends org.springframework.jdbc.core.namedparam.MapSqlParameterSource {
	private String prasedSql;
	private Object[] prasedBindingValues;

	public String getPrasedSql() {
		return prasedSql;
	}

	public Object[] getPrasedBindingValues() {
		return prasedBindingValues;
	}

    public void parseSql(String pSql) throws Exception {
    	this.parseSql(null, pSql);
    }
	
    /**
     * Parse the query string containing named parameters and result a parsed sql, 
     * (named parameters replaced by standard '?' parameters and an ordered list of the parameters value.
     * 
     * SQL parsing code borrowed from Adam Crume. Thanks Adam.
     * See <a href="http://www.javaworld.com/article/2077706/core-java/named-parameters-for-preparedstatement.html?page=2">http://www.javaworld.com/article/2077706/core-java/named-parameters-for-preparedstatement.html?page=2</a>
     *  
     * @param pDataSource JDBC DataSource
     * @param pSql Query containing named parameters
     * @return String
     */
    public void parseSql(DataSource pDataSource, String pSql) throws Exception {
		ArrayList<Object> bingingValueList  = new ArrayList<Object>();
        int length = pSql.length();
        StringBuffer parsedQuery = new StringBuffer(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inSingleLineComment = false;
        boolean inMultiLineComment = false;
        String name = null;
        
        for (int i = 0; i < length; i++) {
            char c = pSql.charAt(i);
            if (inSingleQuote) {
                if (c == '\'') {
                    inSingleQuote = false;
                }
            } else if (inDoubleQuote) {
                if (c == '"') {
                    inDoubleQuote = false;
                }
            } else if (inMultiLineComment) {
                if (c == '*' && pSql.charAt(i + 1) == '/') {
                    inMultiLineComment = false;
                }
            } else if (inSingleLineComment) {
                if (c == '\n') {
                    inSingleLineComment = false;
                }
            } else {
                if (c == '\'') {
                    inSingleQuote = true;
                } else if (c == '"') {
                    inDoubleQuote = true;
                } else if (c == '/' && pSql.charAt(i + 1) == '*') {
                    inMultiLineComment = true;
                } else if (c == '-' && pSql.charAt(i + 1) == '-') {
                    inSingleLineComment = true;
                } else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(pSql.charAt(i + 1))) {
                    int j = i + 2;
                    while (j < length && Character.isJavaIdentifierPart(pSql.charAt(j))) {
                        j++;
                    }
                    name = pSql.substring(i + 1, j);
                    bingingValueList.add(this.getValue(name));
                    c = '?'; // replace the parameter with a question mark
                    i += name.length(); // skip past the end if the parameter
                }
            }
            parsedQuery.append(c);
        }
        
        if (pDataSource ==  null) {
            this.prasedSql = parsedQuery.toString();
            this.prasedBindingValues = bingingValueList.toArray();
	        return;	
        }
        this.prasedSql = OracleSelectHelper.praseSql(pDataSource, parsedQuery.toString(), bingingValueList);
        this.prasedBindingValues = bingingValueList.toArray();
    }
}
