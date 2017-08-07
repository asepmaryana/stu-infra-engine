package com.stu.infra.cdc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stu.infra.cdc.model.serializer.CustomLocalDateTimeDeserializer;
import com.stu.infra.cdc.model.serializer.CustomLocalDateTimeSerializer;

@Entity
@Table(name = "inbox")
public class Inbox  implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	@Column(name = "sender", nullable = false, length=16)
	private String sender;
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "message_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime messageDate;
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "receive_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime receiveDate;
	
	@Column(name = "text", nullable = false)
	private String text;
	
	@Column(name = "request_id", length=12)
	private String requestId;
	
	@Column(name = "gateway_id", length=30)
	private String gatewayId;
	
	@Column(name = "message_type")
	private char messageType;
	
	@Column(name = "encoding")
	private char encoding;
	
	@Column(name = "status")
	private char status;
	
	public Inbox() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setOriginator(String sender) {
		this.sender = sender;
	}	

	public char getEncoding() {
		return encoding;
	}

	public void setEncoding(char encoding) {
		this.encoding = encoding;
	}
	
	
	public LocalDateTime getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(LocalDateTime messageDate) {
		this.messageDate = messageDate;
	}
	
	public LocalDateTime getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(LocalDateTime receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public char getMessageType() {
		return messageType;
	}

	public void setMessageType(char messageType) {
		this.messageType = messageType;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

}
