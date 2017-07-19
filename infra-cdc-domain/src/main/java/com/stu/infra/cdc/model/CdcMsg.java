package com.stu.infra.cdc.model;

public class CdcMsg<T> implements java.io.Serializable {
	
	private static final long serialVersionUID = -6030638714902405159L;

	String head;
	
	T data;
	
	public CdcMsg() {}

	public CdcMsg(String head, T data) {
		this.head = head;
		this.data = data;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}	
	
}
