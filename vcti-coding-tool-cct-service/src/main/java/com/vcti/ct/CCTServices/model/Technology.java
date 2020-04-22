package com.vcti.ct.CCTServices.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author sandeepkumar.yadav
 *
 */
@AllArgsConstructor
@Getter
@Setter
@Table("technologies")
public class Technology {

	@PrimaryKey
	private @NonNull String id;
	private @NonNull String technology;
	private @NonNull String topic;
	
	public Technology() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
