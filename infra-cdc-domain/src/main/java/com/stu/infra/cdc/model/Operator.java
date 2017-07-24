package com.stu.infra.cdc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="operator")
public class Operator implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", nullable = false, length=100)
	private String name;
	
	@Column(name = "phone", nullable = false, length=20)
	private String phone;
	
	@Column(name = "mon")
	private Short mon;
	
	@Column(name = "tue")
	private Short tue;
	
	@Column(name = "wed")
	private Short wed;
	
	@Column(name = "thu")
	private Short thu;
	
	@Column(name = "fri")
	private Short fri;
	
	@Column(name = "sat")
	private Short sat;
	
	@Column(name = "sun")
	private Short sun;
	
	@Column(name = "enabled")
	private Short enabled;
	
	public Operator() {}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Short getMon() {
		return mon;
	}

	public void setMon(Short mon) {
		this.mon = mon;
	}

	public Short getTue() {
		return tue;
	}

	public void setTue(Short tue) {
		this.tue = tue;
	}

	public Short getWed() {
		return wed;
	}

	public void setWed(Short wed) {
		this.wed = wed;
	}

	public Short getThu() {
		return thu;
	}

	public void setThu(Short thu) {
		this.thu = thu;
	}

	public Short getFri() {
		return fri;
	}

	public void setFri(Short fri) {
		this.fri = fri;
	}

	public Short getSat() {
		return sat;
	}

	public void setSat(Short sat) {
		this.sat = sat;
	}

	public Short getSun() {
		return sun;
	}

	public void setSun(Short sun) {
		this.sun = sun;
	}

	public Short getEnabled() {
		return enabled;
	}

	public void setEnabled(Short enabled) {
		this.enabled = enabled;
	}
	
	
}
