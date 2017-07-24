package com.stu.infra.cdc.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="severity")
public class Severity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", nullable = false, length=10)
	private String name;
	
	@Column(name = "color", length=7)
	private String color;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "alarmList", cascade = CascadeType.ALL)
	private Set<AlarmList> alarmLists = new HashSet<AlarmList>(0);
	
	public Severity() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Set<AlarmList> getAlarmLists() {
		return alarmLists;
	}

	public void setAlarmLists(Set<AlarmList> alarmLists) {
		this.alarmLists = alarmLists;
	}
	
}
