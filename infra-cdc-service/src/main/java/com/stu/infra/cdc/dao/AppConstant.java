package com.stu.infra.cdc.dao;

public interface AppConstant {
	
	// alarm list
	public static final int GENSET_ON_FAIL		= 1;
	public static final int GENSET_OFF_FAIL		= 2;
	public static final int LOW_FUEL			= 3;
	public static final int RECTIFIER_FAIL		= 4;
	public static final int LOW_BATTERY			= 5;
	public static final int SINEGEN_HIGH_TEMP	= 6;
	public static final int ENGINE_HIGH_TEMP	= 7;
	public static final int OIL_PRESSURE		= 8;
	public static final int MAINTENANCE			= 9;
	public static final int COMM_LOST			= 10;
	
	// status of node
	public static final int OPERATIONAL_STATUS	= 1;
	public static final int MAINTENANCE_STATUS	= 2;
	public static final int COMMLOST_STATUS		= 3;	
	
}
