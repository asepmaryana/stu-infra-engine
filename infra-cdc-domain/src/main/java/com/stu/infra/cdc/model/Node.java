package com.stu.infra.cdc.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stu.infra.cdc.model.serializer.CustomLocalDateTimeDeserializer;
import com.stu.infra.cdc.model.serializer.CustomLocalDateTimeSerializer;

@Entity
@Table(name = "node")
public class Node  implements java.io.Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "phone", nullable = false, length=20)
	private String phone;
	
	@Column(name = "name", nullable = false, length=50)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subnet_id")
	private Subnet subnet;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opr_status_id")
	private OprStatus oprStatus;
	
	@Column(name = "latitude")
	private BigDecimal latitude;
	
	@Column(name = "longitude")
	private BigDecimal longitude;
	
	@Column(name = "genset_vr")
	private BigDecimal gensetVr;
	
	@Column(name = "genset_vs")
	private BigDecimal gensetVs;
	
	@Column(name = "genset_vt")
	private BigDecimal gensetVt;
	
	@Column(name = "batt_volt")
	private BigDecimal battVolt;
	
	@Column(name = "batt_volt_minor")
	private BigDecimal battVoltMinor;
	
	@Column(name = "batt_volt_major")
	private BigDecimal battVoltMajor;
	
	@Column(name = "batt_volt_critical")
	private BigDecimal battVoltCritical;
	
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
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "created_at")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime createdAt;
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "updated_at")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime updatedAt;
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "trap_updated")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime trapUpdated;
	
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
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "next_on")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime nextOn;
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "next_off")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime nextOff;
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "last_on")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime lastOn;
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "last_off")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime lastOff;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "node", cascade = CascadeType.ALL)
	private Set<Datalog> datalogs = new HashSet<Datalog>(0);
	
	public Set<Datalog> getDatalogs() {
		return datalogs;
	}

	public void setDatalogs(Set<Datalog> datalogs) {
		this.datalogs = datalogs;
	}

	public Node() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Subnet getSubnet() {
		return subnet;
	}

	public void setSubnet(Subnet subnet) {
		this.subnet = subnet;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
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

	public LocalDateTime getNextOn() {
		return nextOn;
	}

	public void setNextOn(LocalDateTime nextOn) {
		this.nextOn = nextOn;
	}

	public LocalDateTime getNextOff() {
		return nextOff;
	}

	public void setNextOff(LocalDateTime nextOff) {
		this.nextOff = nextOff;
	}

	public LocalDateTime getLastOn() {
		return lastOn;
	}

	public void setLastOn(LocalDateTime lastOn) {
		this.lastOn = lastOn;
	}

	public LocalDateTime getLastOff() {
		return lastOff;
	}

	public void setLastOff(LocalDateTime lastOff) {
		this.lastOff = lastOff;
	}

	public BigDecimal getBattVoltMinor() {
		return battVoltMinor;
	}

	public void setBattVoltMinor(BigDecimal battVoltMinor) {
		this.battVoltMinor = battVoltMinor;
	}

	public BigDecimal getBattVoltMajor() {
		return battVoltMajor;
	}

	public void setBattVoltMajor(BigDecimal battVoltMajor) {
		this.battVoltMajor = battVoltMajor;
	}

	public BigDecimal getBattVoltCritical() {
		return battVoltCritical;
	}

	public void setBattVoltCritical(BigDecimal battVoltCritical) {
		this.battVoltCritical = battVoltCritical;
	}

	public LocalDateTime getTrapUpdated() {
		return trapUpdated;
	}

	public void setTrapUpdated(LocalDateTime trapUpdated) {
		this.trapUpdated = trapUpdated;
	}

	public OprStatus getOprStatus() {
		return oprStatus;
	}

	public void setOprStatus(OprStatus oprStatus) {
		this.oprStatus = oprStatus;
	}
	
}
