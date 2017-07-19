package com.stu.infra.cdc.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stu.infra.cdc.model.serializer.CustomLocalDateTimeDeserializer;
import com.stu.infra.cdc.model.serializer.CustomLocalDateTimeSerializer;

@Entity
@Table(name = "data_log")
public class Datalog  implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "node_id")
	private Node node;
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "dtime")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime dTime;
	
	@Column(name = "genset_vr")
	private BigDecimal gensetVr;
	
	@Column(name = "genset_vs")
	private BigDecimal gensetVs;
	
	@Column(name = "genset_vt")
	private BigDecimal gensetVt;
	
	@Column(name = "batt_volt")
	private BigDecimal battVolt;
	
	@Column(name = "genset_batt_volt")
	private BigDecimal gensetBattVolt;
	
	@Column(name = "genset_status")
	private Short gensetStatus;
	
	@Column(name = "recti_status")
	private Short rectiStatus;
	
	@Column(name = "low_fuel")
	private Short lowFuel;
	
	@Column(name = "recti_fail")
	private Short rectiFail;
	
	@Column(name = "batt_low")
	private Short battLow;	
	
	@Column(name = "timer_genset_on")
	private Integer timerGensetOn;
	
	@Column(name = "timer_genset_off")
	private Integer timerGensetOff;
	
	@Column(name = "run_hour")
	private BigDecimal runHour;
	
	@Column(name = "run_hour_tresh")
	private BigDecimal runHourTresh;
	
	@Column(name = "genset_on_fail")
	private Short gensetOnFail;
	
	@Column(name = "genset_off_fail")
	private Short gensetOffFail;
	
	@Column(name = "sin_high_temp")
	private Short sinHighTemp;
	
	@Column(name = "eng_high_temp")
	private Short engHighTemp;
	
	@Column(name = "oil_pressure")
	private Short oilPressure;
	
	@Column(name = "maintain_status")
	private Short maintainStatus;
	
	public Datalog() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public LocalDateTime getdTime() {
		return dTime;
	}

	public void setdTime(LocalDateTime dTime) {
		this.dTime = dTime;
	}

	public BigDecimal getGensetVr() {
		return gensetVr;
	}

	public void setGensetVr(BigDecimal gensetVr) {
		this.gensetVr = gensetVr;
	}

	public BigDecimal getGensetVs() {
		return gensetVs;
	}

	public void setGensetVs(BigDecimal gensetVs) {
		this.gensetVs = gensetVs;
	}

	public BigDecimal getGensetVt() {
		return gensetVt;
	}

	public void setGensetVt(BigDecimal gensetVt) {
		this.gensetVt = gensetVt;
	}

	public BigDecimal getBattVolt() {
		return battVolt;
	}

	public void setBattVolt(BigDecimal battVolt) {
		this.battVolt = battVolt;
	}

	public BigDecimal getGensetBattVolt() {
		return gensetBattVolt;
	}

	public void setGensetBattVolt(BigDecimal gensetBattVolt) {
		this.gensetBattVolt = gensetBattVolt;
	}

	public Short getGensetStatus() {
		return gensetStatus;
	}

	public void setGensetStatus(Short gensetStatus) {
		this.gensetStatus = gensetStatus;
	}

	public Short getRectiStatus() {
		return rectiStatus;
	}

	public void setRectiStatus(Short rectiStatus) {
		this.rectiStatus = rectiStatus;
	}

	public Short getLowFuel() {
		return lowFuel;
	}

	public void setLowFuel(Short lowFuel) {
		this.lowFuel = lowFuel;
	}

	public Short getRectiFail() {
		return rectiFail;
	}

	public void setRectiFail(Short rectiFail) {
		this.rectiFail = rectiFail;
	}

	public Short getBattLow() {
		return battLow;
	}

	public void setBattLow(Short battLow) {
		this.battLow = battLow;
	}

	public Integer getTimerGensetOn() {
		return timerGensetOn;
	}

	public void setTimerGensetOn(Integer timerGensetOn) {
		this.timerGensetOn = timerGensetOn;
	}

	public Integer getTimerGensetOff() {
		return timerGensetOff;
	}

	public void setTimerGensetOff(Integer timerGensetOff) {
		this.timerGensetOff = timerGensetOff;
	}

	public BigDecimal getRunHour() {
		return runHour;
	}

	public void setRunHour(BigDecimal runHour) {
		this.runHour = runHour;
	}

	public BigDecimal getRunHourTresh() {
		return runHourTresh;
	}

	public void setRunHourTresh(BigDecimal runHourTresh) {
		this.runHourTresh = runHourTresh;
	}

	public Short getGensetOnFail() {
		return gensetOnFail;
	}

	public void setGensetOnFail(Short gensetOnFail) {
		this.gensetOnFail = gensetOnFail;
	}

	public Short getGensetOffFail() {
		return gensetOffFail;
	}

	public void setGensetOffFail(Short gensetOffFail) {
		this.gensetOffFail = gensetOffFail;
	}

	public Short getSinHighTemp() {
		return sinHighTemp;
	}

	public void setSinHighTemp(Short sinHighTemp) {
		this.sinHighTemp = sinHighTemp;
	}

	public Short getEngHighTemp() {
		return engHighTemp;
	}

	public void setEngHighTemp(Short engHighTemp) {
		this.engHighTemp = engHighTemp;
	}

	public Short getOilPressure() {
		return oilPressure;
	}

	public void setOilPressure(Short oilPressure) {
		this.oilPressure = oilPressure;
	}

	public Short getMaintainStatus() {
		return maintainStatus;
	}

	public void setMaintainStatus(Short maintainStatus) {
		this.maintainStatus = maintainStatus;
	}
	
}
