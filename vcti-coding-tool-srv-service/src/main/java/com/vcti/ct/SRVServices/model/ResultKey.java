package com.vcti.ct.SRVServices.model;

import static org.springframework.data.cassandra.core.cql.Ordering.DESCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

import java.io.Serializable;

import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class ResultKey implements Serializable {
	@PrimaryKeyColumn(name = "userid", type = PARTITIONED)
	private String userId;

	@PrimaryKeyColumn(name = "qid", ordinal = 0, ordering = DESCENDING)
	private String qid;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	@Override
	public String toString() {
		return "ResultKey{" + "userId='" + userId + '\'' + ", Question Id=" + qid + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ResultKey resultKey = (ResultKey) o;

		if (userId != null ? !userId.equals(resultKey.userId) : resultKey.userId != null)
			return false;
		return qid != null ? qid.equals(resultKey.qid) : resultKey.qid == null;
	}

	@Override
	public int hashCode() {
		int result = userId != null ? userId.hashCode() : 0;
		result = 31 * result + (qid != null ? qid.hashCode() : 0);
		return result;
	}
}
