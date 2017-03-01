package com.pccw.util.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.pccw.util.db.stringOracleType.OraArray;

public class Criteria {
	 /**
     * Order By Map [key: property, value: asc / desc
     */
    private Map<String, String> orderByMap;
    private List<String> groupByList;
    private Map<String, Object> valueMap;
    private List<String> criteriaList;

    private int limit;
    private int offset;

    public enum Order {
        ASC, DESC
    }

    public Map<String, String> getOrderByMap() {
        return orderByMap;
    }
    public List<String> getGroupByList() {
        return groupByList;
    }
    public Map<String, Object> getValueMap() {
        return valueMap;
    }
    public List<String> getCriteriaList() {
        return criteriaList;
    }
    public int getLimit() {
        return limit;
    }
    public int getOffset() {
        return offset;
    }

    public Criteria() {
        orderByMap = new HashMap<String, String>();
        groupByList = new ArrayList<String>();
        valueMap = new HashMap<String, Object>();
        criteriaList = new ArrayList<String>();
    }

    protected Criteria addCriteria(String condition) {
        if(StringUtils.isNotBlank(condition)) {
            criteriaList.add(condition);
        }
        return this;
    }

    protected Criteria addCriteria(String pProperty, String pFormat) {
        return addCriteria(genCriteria(pProperty, pFormat));
    }

    protected String genCriteria(String pProperty, String pFormat) {
        return String.format(pFormat, pProperty);
    }
    
    protected String processProperty(String property, Object value) {
        return processProperty(property, null, null, value);
    }

    protected String processProperty(String property, String prefix, String suffix, Object value) {
        StringBuilder paramNameForMap = new StringBuilder();
        StringBuilder paramName = new StringBuilder(":");

        if(StringUtils.isNotBlank(prefix)) {
            paramNameForMap.append(prefix);
            paramName.append(prefix);
        }

        if(property.contains(".")) {
            paramNameForMap.append(property.substring(property.indexOf(".")+1));
            paramName.append(property.substring(property.indexOf(".")+1));
        } else {
            paramNameForMap.append(property);
            paramName.append(property);
        }

        if(StringUtils.isNotBlank(suffix)) {
            paramNameForMap.append(suffix);
            paramName.append(suffix);
        }

        putPropertyIntoMap(paramNameForMap.toString(), value);
        return paramName.toString();
    }

    protected void putPropertyIntoMap(String paramNameForMap, Object value) {
        if(value instanceof Enum) {
            if(PropertyUtils.isReadable(value, "value")) {
                Object enumValue = null;
                try {
                    enumValue = PropertyUtils.getProperty(value, "value");
                } catch (Exception e) {
                    // Not Reachable
                }

                if(enumValue != null) {
                    valueMap.put(paramNameForMap.toString(), enumValue);
                } else {
                    valueMap.put(paramNameForMap.toString(), value);
                }
            }
        } else {
            valueMap.put(paramNameForMap.toString(), value);
        }
    }

    protected Criteria addCriteria(String pProperty, String pFormat, Object pValue) {
        return addCriteria(genCriteria(pProperty, pFormat, pValue));
    }
    
    protected String genCriteria(String pProperty, String pFormat, Object pValue) {
        String paramName = processProperty(pProperty, pValue);
        return String.format(pFormat, pProperty, paramName);
    }

    protected Criteria addCriteria(String pProperty, String pParamName, String pFormat, Object pValue) {
        return addCriteria(genCriteria(pProperty, pParamName, pFormat, pValue));
    }
    
    protected String genCriteria(String pProperty, String pParamName, String pFormat, Object pValue) {
        valueMap.put(pParamName, pValue);
        pParamName = ":" + pParamName;
        if(pValue instanceof Enum) {
            pParamName += ".value";
        }
        return String.format(pFormat, pProperty, pParamName);
    }

    protected Criteria addCriteria(String pProperty, String pFormat, Object pMinValue, Object pMaxValue) {
        return addCriteria(genCriteria(pProperty, pFormat, pMinValue, pMaxValue));
    }
    
    protected String genCriteria(String pProperty, String pFormat, Object pMinValue, Object pMaxValue) {
        String minParamName = processProperty(pProperty, null, "_min", pMinValue);
        String maxParamName = processProperty(pProperty, null, "_max", pMaxValue);
        return String.format(pFormat, pProperty, minParamName, maxParamName);
    }

    protected Criteria addCriteria(String pProperty, String pFormat, List<?> pValueList) {
        return addCriteria(genCriteria(pProperty, pFormat, pValueList));
    }
    
    protected String genCriteria(String pProperty, String pFormat, List<?> pValueList) {
        StringBuilder list = new StringBuilder();
        int index = 1;
        for(Object value : pValueList) {
            if(list.length() > 0) {
                list.append(",");
            }
            String paramName = processProperty(pProperty, null, String.valueOf(index), value);
            list.append(paramName);
            index++;
        }
        return String.format(pFormat, pProperty, list.toString());
    }
    
    public Criteria andIsNull(String property) {
        return addCriteria(property, "%s IS NULL");
    }

    public Criteria andIsNotNull(String property) {
        return addCriteria(property, "%s IS NOT NULL");
    }

    public Criteria andEqual(String property, Object value) {
        return addCriteria(property, "%s = %s", value);
    }
    
    private void appendCriteria(StringBuilder pCriteria, String pProperty, Object pValue) {
        if (pValue == null) {
        	pCriteria.append(genCriteria(pProperty, "%s IS NULL"));
        } else if (pValue instanceof List<?>) {
        	pCriteria.append(genCriteria(pProperty, "%s IN (%s)", (List<?>) pValue));
        } else if (pValue instanceof String[]) {
        	pCriteria.append(genCriteria(pProperty, "%s IN (%s)", Arrays.asList((String[]) pValue)));
        } else if (pValue instanceof OraArray) {
        	pCriteria.append(genCriteria(pProperty, "%s MEMBER OF %s", pValue));
        } else {
        	pCriteria.append(genCriteria(pProperty, "%s = %s", pValue));
        }
    }
    
    public Criteria andEqual(String[] pProperties, Object pValue) {
    	if (ArrayUtils.isEmpty(pProperties)) {
    		return this;
    	}
        StringBuilder sbCriteria = new StringBuilder("( ");
        appendCriteria(sbCriteria, pProperties[0], pValue);

        for (int i = 1; i < pProperties.length; i++) {
        	sbCriteria.append(" OR ");
            appendCriteria(sbCriteria, pProperties[i], pValue);
		}
        sbCriteria.append(" ) ");
        return addCriteria(sbCriteria.toString());
    }
    
    public Criteria andEqualOr(String[] pProperties, Object[] pValues) {
    	if (ArrayUtils.isEmpty(pProperties)
    			|| ArrayUtils.isEmpty(pValues)
    			|| (pProperties.length != pValues.length)) {
    		return this;
    	}
        StringBuilder sbCriteria = new StringBuilder("( ");
        appendCriteria(sbCriteria, pProperties[0], pValues[0]);

        for (int i = 1; i < pProperties.length; i++) {
        	sbCriteria.append(" OR ");
            appendCriteria(sbCriteria, pProperties[i], pValues[i]);
		}
        sbCriteria.append(" ) ");
        return addCriteria(sbCriteria.toString());
    }
    
    public Criteria andNotEqual(String property, Object value) {
        return addCriteria(property, "%s <> %s", value);
    }

    public Criteria andGreaterThan(String property, Object value) {
        return addCriteria(property, "%s > %s", value);
    }

    public Criteria andGreaterEqual(String property, Object value) {
        return addCriteria(property, "%s >= %s", value);
    }

    public Criteria andGreaterEqual(String property, String paramName, Object value) {
        return addCriteria(property, paramName, "%s >= %s", value);
    }

    public Criteria andLessThan(String property, Object value) {
        return addCriteria(property, "%s < %s", value);
    }

    public Criteria andLessEqual(String property, Object value) {
        return addCriteria(property, "%s <= %s", value);
    }

    public Criteria andLessEqual(String property, String paramName, Object value) {
        return addCriteria(property, paramName, "%s <= %s", value);
    }

    public Criteria andLike(String property, Object value) {
        String strValue = String.format("%s%s%s", "%", value, "%");
        return addCriteria(property, "%s LIKE %s", strValue);
    }

    public Criteria andNotLike(String property, Object value) {
        String strValue = String.format("%s%s%s", "%", value, "%");
        return addCriteria(property, "%s NOT LIKE %s", strValue);
    }

    public Criteria andBetween(String property, Object minValue, Object maxValue) {
        return addCriteria(property, "%s BETWEEN %s AND %s", minValue, maxValue);
    }

    public Criteria andTruncDateBetween(String property, Object minValue, Object maxValue) {
        return addCriteria(property, "TRUNC(%s) BETWEEN %s AND %s", minValue, maxValue);
    }

    public Criteria andNotBetween(String property, Object minValue, Object maxValue) {
        return addCriteria(property, "%s NOT BETWEEN %s AND %s", minValue, maxValue);
    }

    public Criteria andMemberOf(String pProperty, OraArray pValue) {
        return addCriteria(pProperty, "%s MEMBER OF %s", pValue);
    }
    
    public Criteria andIn(String pProperty, List<?> pValueList) {
        return addCriteria(pProperty, "%s IN (%s)", pValueList);
    }

    public Criteria andIn(String pProperty, Object... pValues) {
        return addCriteria(pProperty, "%s IN (%s)", Arrays.asList(pValues));
    }
    public Criteria andInNull(String property, List<?> values) {
        return addCriteria(property, "(" + property + " IS NULL OR %s IN (%s))", values);
    }

    public Criteria andNotMemberOf(String pProperty, OraArray pValue) {
        return addCriteria(pProperty, "%s NOT MEMBER OF %s", pValue);
    }
    
    public Criteria andNotIn(String property, List<?> values) {
        return addCriteria(property, "%s NOT IN (%s)", values);
    }

    public Criteria andNotIn(String property, Object... values) {
        return addCriteria(property, "%s NOT IN (%s)", Arrays.asList(values));
    }

    public Criteria andQuery(String condition) {
        return addCriteria(condition);
    }

    public Criteria andQuery(String condition, Map<String, Object> values) {
        valueMap.putAll(values);
        return addCriteria(condition);
    }

    public Criteria orderBy(String property) {
        return orderBy(property, Order.ASC);
    }

    public Criteria orderBy(String property, Order order) {
        orderByMap.put(property, order.name());
        return this;
    }

    public Criteria groupBy(String property) {
        return groupBy(property, false);
    }

    public Criteria groupBy(String property, Boolean withRollUp) {
        if(withRollUp) {
            groupByList.add(String.format("%s WITH ROLLUP", property));
        } else {
            groupByList.add(property);
        }
        return this;
    }

    public Criteria limit(int limit) {
        if(limit > 0) {
            this.limit = limit;
        }
        return this;
    }

    public Criteria offset(int offset) {
        if(offset > 0) {
            this.offset = offset;
        }
        return this;
    }

    public Criteria remove(String paramName) {
        for(int i=0; i<criteriaList.size(); i++) {
            if(criteriaList.get(i).contains(paramName)) {
                criteriaList.remove(i);
            }
        }
        return this;
    }

    public void clear() {
        criteriaList.clear();
        orderByMap.clear();
        groupByList.clear();
        valueMap.clear();
        this.limit = 0;
        this.offset = 0;
    }
    
	@Override
	public String toString() {
		return "Criteria [orderByMap=" + orderByMap + ", groupByList="
				+ groupByList + ", valueMap=" + valueMap + ", criteriaList="
				+ criteriaList + ", limit=" + limit + ", offset=" + offset
				+ "]";
	}


}
