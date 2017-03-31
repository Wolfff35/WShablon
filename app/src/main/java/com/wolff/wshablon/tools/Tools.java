package com.wolff.wshablon.tools;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by wolff on 16.03.2017.
 */

public class Tools {
    private String getPicturesDirectory() {
        File directory;
        if(isExternalStorageWritable()) {
            directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WCatalog");
        }else {
            directory = new File(Environment.getDataDirectory(), "WCatalog");
        }
        if (!directory.exists())
            directory.mkdirs();
        return directory.getPath();
    }
    public File getPictureFullPath(){
        //+"/item_"+ System.currentTimeMillis() + ".jpg"
        String ppath = getPicturesDirectory()+"/item_"+ System.currentTimeMillis() + ".jpg";
        Log.e("getPictureFullPath",""+ppath);
        return new File(ppath);
    }
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
