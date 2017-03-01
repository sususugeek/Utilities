package com.pccw.util.db;

import org.apache.commons.lang.StringUtils;

public class DaoBasePartitionImpl extends DaoBaseImpl {
	static final long serialVersionUID = 1339792324468830842L;

	private String partition;

	public String getPartition() {
		return partition;
	}

	public void setPartition(String pPartition) {
		if (StringUtils.isBlank(pPartition)) {
			throw new RuntimeException("PARTITION NOT ALLOW NULL");
		}
		this.partition = pPartition;
	}
}