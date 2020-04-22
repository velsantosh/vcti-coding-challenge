package com.vcti.ct.CCTServices.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sandeepkumar.yadav
 *
 */
@XmlRootElement
public class TechnologyMap {

	private String technology;
	private List<String> topics;

	public TechnologyMap() {
	}
	
	public TechnologyMap(String id, String technology, List<String> topics) {
		super();
		this.technology = technology;
		this.topics = topics;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public List<String> getTopics() {
		return topics;
	}

	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

}
