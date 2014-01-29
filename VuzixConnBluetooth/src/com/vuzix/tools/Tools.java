package com.vuzix.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.vuzix.conn.bluetooth.BaseActivity;

import android.os.Environment;
import android.util.Log;


public class Tools {
	private static String TAG_LOCAL = "Tools - ";
	private static boolean fgDebugLocal = true;
	
	public static void killAppli(){
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public static String convertStringToHtml(String str){
		if (str.contains("é")){
			str = str.replace("é", "�");
		}
		if (str.contains("è")){
			str = str.replace("è", "�");
		}
		return str;
	}
	
	public static boolean isInteger(String str){
		boolean fgIsInteger = false;
		char[] all = str.toCharArray();
		String numbers = "";
        for(int i = 0; i < all.length;i++) {
            if(Character.isDigit(all[i])) {
                numbers = numbers + all[i];
            }
        }
		if (numbers.length() > 0){
			fgIsInteger = true;
		}
		return fgIsInteger;
	}

	public static String convertDateEnToFr(String strGB){
		String[] strInfo = strGB.split("-");
		String str = "";
		if (strInfo.length == 3){
			String strFR = strInfo[2] + "/" +  strInfo[1] + "/" + strInfo[0];
			str = strFR.replace(" ", "");
		}else{
			str = strGB;
		}
		return str;
	}
	
	
	
	
}

