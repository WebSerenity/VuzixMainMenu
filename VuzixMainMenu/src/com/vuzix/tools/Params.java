package com.vuzix.tools;

public interface Params {
	public static final String TAG_GEN = "WS";
	public static final boolean TAG_FG_DEBUG = true;

	public static final String EXPORT_REP = "/download/";
	
	public static final int DB_DATABASE_VERSION = 1;
	public static final String DB_DATABASE_NAME = "dbBook";
	public static final String DB_EXPORT = "dbBook.db";
	
	//Boussole
	public static int BOUSSOLE_VIEW = 20;
	public static int BOUSSOLE_STEP = 20;
	
	//LaucnhAppli
	public static String[] APP_EXCEPTION = {"com.facebook.katana",
											"com.svox.pico",
											"com.tinno.lcd",
											"com.tinno.turntomute",
											"com.tinnomobile.tp",
											"fishnoodle.koipond"};
	
	//Bluetooth
	public static final String BT_REF = "M100";
}
