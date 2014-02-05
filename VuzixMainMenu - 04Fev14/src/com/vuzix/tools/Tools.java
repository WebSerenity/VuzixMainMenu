package com.vuzix.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.os.Environment;
import android.util.Log;

import com.vuzix.main.menu.BaseActivity;

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
	
	public static void exportDB(){
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "exportDB DEB");};
		File sd = Environment.getExternalStorageDirectory();
      	File data = Environment.getDataDirectory();
      	FileChannel source=null;
      	FileChannel destination=null;
      	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "package : " + BaseActivity.strPackage);};
      	
      	String currentDBPath = "/data/"+ BaseActivity.strPackage +"/databases/"+Params.DB_DATABASE_NAME;
      	String backupDBPath = Params.EXPORT_REP + Params.DB_EXPORT;
      	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "backupDBPath = " + backupDBPath);};
      	File currentDB = new File(data, currentDBPath);
      	File backupDB = new File(sd, backupDBPath);
      	try {
      		source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            
      	} catch(IOException e) {
      		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "exportDB Error : " + e.getMessage());};
        }
      	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "exportDB FIN");};
	}
	
	public static boolean importDB(String strPackage, String pathAppli){
		boolean fgResult = false;
		try {
			File sd = Environment.getExternalStorageDirectory();
            //File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String pathDest = pathAppli + "/databases/" + Params.DB_DATABASE_NAME;
                if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "pathDest = " + pathDest);};
                String pathSrc = sd + "/Download/dbBook.db";
                if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "pathSrc = " + pathSrc);};
                File fileSrc = new File(pathSrc);
                if (fileSrc.exists()){
                	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "pathSrc existe OK");};
                }else{
                	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "pathSrc existe NO");};
                }
                File fileDest = new File(pathDest);

                @SuppressWarnings("resource")
				FileChannel src = new FileInputStream(fileSrc).getChannel();
                @SuppressWarnings("resource")
				FileChannel dst = new FileOutputStream(fileDest).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                fgResult = true;
            }
		} catch (Exception e) {
			if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "importDB Error : " + e.getMessage());};
        
		}
		return fgResult;
	}
}

