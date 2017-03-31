package com.wolff.wshablon.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wolff.wshablon.objects.WItem;
import com.wolff.wshablon.objects.WSeasons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wolff on 07.03.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "WCatalog";
    private static final String DATABASE_TABLE = "WItems";
    private static final int DATABASE_VERSION = 1;

    private static final String ID_COLUMN = "_ID";
    private static final String NAME_COLUMN = "_NAME";
    private static final String IMAGEPATH_COLUMN = "_IMAGEPATH";
    private static final String MINT_COLUMN = "_MINT";
    private static final String MAXT_COLUMN = "_MAXT";
    private static final String SEASON_COLUMN = "_SEASON";


    private static final String CREATE_DATABASE_TABLE = "CREATE TABLE "
            + DATABASE_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + NAME_COLUMN + " TEXT, "+IMAGEPATH_COLUMN+" TEXT, "+MINT_COLUMN+" INTEGER, "+MAXT_COLUMN+" INTEGER, "+SEASON_COLUMN+" TEXT)";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            //sInstance = new DatabaseHelper(context);
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //===============================================================================================
    public long item_add(WItem item){
        long rowId;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        db.beginTransaction();
        try {
            values.put(NAME_COLUMN, item.getName());
            values.put(IMAGEPATH_COLUMN, item.getPictureName());
            values.put(MINT_COLUMN, item.getMinTemperature());
            values.put(MAXT_COLUMN, item.getMaxTemperature());
            values.put(SEASON_COLUMN, item.getSeason().toString());

            rowId = db.insertOrThrow(DATABASE_TABLE, null, values);

            db.setTransactionSuccessful();
        }catch (Exception e){
            rowId=0;
        }finally {
            db.endTransaction();
            db.close();
        }
        Log.e("SQLITE","ADD ITEM "+rowId);
        return rowId;
    }
    public void item_update(WItem item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, item.getName());
        values.put(IMAGEPATH_COLUMN, item.getPictureName());
        values.put(MINT_COLUMN, item.getMinTemperature());
        values.put(MAXT_COLUMN, item.getMaxTemperature());
        values.put(SEASON_COLUMN, item.getSeason().toString());

        String whereClause = ID_COLUMN+" = ?";
        String[] whereArgs =new String[]{String.valueOf(item.getId())};
        db.update(DATABASE_TABLE,values,whereClause,whereArgs);
        Log.e("SQLITE","UPDATE ITEM");
    }
    public void item_delete(WItem item){
        SQLiteDatabase db = getWritableDatabase();
        try {

            String whereClause = ID_COLUMN+" = ?";
            String[] whereArgs =new String[]{String.valueOf(item.getId())};
            int delCount =db.delete(DATABASE_TABLE,whereClause,whereArgs);
          }catch (Exception e){
        }finally {
            db.close();
        }

    }
//--------------------------------------------------------------------------------------------------

    public ArrayList<WItem> items_getAll_list() {
        ArrayList<WItem>items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[]{ID_COLUMN, NAME_COLUMN, IMAGEPATH_COLUMN, MINT_COLUMN, MAXT_COLUMN, SEASON_COLUMN};
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = NAME_COLUMN + " DESC";
        Cursor curr = db.query(DATABASE_TABLE, columns, selection, selectionArgs, groupBy, having, orderBy);
        try {
            if (curr.moveToFirst()) {
                do {
                  WItem newItem = new WItem();
                    newItem.setId(curr.getInt(curr.getColumnIndex(ID_COLUMN)));
                    newItem.setName(curr.getString(curr.getColumnIndex(NAME_COLUMN)));
                    newItem.setPictureName(curr.getString(curr.getColumnIndex(IMAGEPATH_COLUMN)));
                    try {
                        newItem.setSeason(WSeasons.valueOf(curr.getString(curr.getColumnIndex(SEASON_COLUMN))));
                    }catch (Exception ee){
                        newItem.setSeason(WSeasons.valueOf("НЕТ"));
                    }
                    newItem.setMinTemperature(curr.getInt(curr.getColumnIndex(MINT_COLUMN)));
                    newItem.setMaxTemperature(curr.getInt(curr.getColumnIndex(MAXT_COLUMN)));
                    Log.e("ITEMS","NAME - "+newItem.getName()+"; ID - "+newItem.getId());
                    items.add(newItem);
                }while(curr.moveToNext());
            }
        } catch (Exception e) {
            Log.d("ERROR", "Error while trying to get posts from database \n"+e.getLocalizedMessage());
        } finally {
            if (curr != null && !curr.isClosed()) {
                curr.close();
            }
        }
        Log.e("DB HELPER","Reading item list! "+items.size()+"; "+curr.getCount());
        return items;
    }

    public ArrayList<WItem> items_getSelection_list(int minT,int maxT,WSeasons season) {
        ArrayList<WItem>items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[]{ID_COLUMN, NAME_COLUMN, IMAGEPATH_COLUMN, MINT_COLUMN, MAXT_COLUMN, SEASON_COLUMN};
        String selection;
        String[] selectionArgs;
        if(!season.name().equalsIgnoreCase("НЕТ")){
            selection = ""+MINT_COLUMN+">=? AND "+MAXT_COLUMN+"<=? AND "+SEASON_COLUMN+"=?";
            selectionArgs = new String[]{String.valueOf(minT),String.valueOf(maxT),season.name()};
        }else {
            selection = ""+MINT_COLUMN+">=? AND "+MAXT_COLUMN+"<=?";
            selectionArgs = new String[]{String.valueOf(minT),String.valueOf(maxT)};
        }
       // Log.e("SELECTION"," = "+selection);
       // Log.e("SELECTION","MIN > "+minT+" AND MAX<"+maxT);
        String groupBy = null;
        String having = null;
        String orderBy = NAME_COLUMN + " DESC";
        Cursor curr = db.query(DATABASE_TABLE, columns, selection, selectionArgs, groupBy, having, orderBy);
        try {
            if (curr.moveToFirst()) {
                do {
                    WItem newItem = new WItem();
                    newItem.setId(curr.getInt(curr.getColumnIndex(ID_COLUMN)));
                    newItem.setName(curr.getString(curr.getColumnIndex(NAME_COLUMN)));
                    newItem.setPictureName(curr.getString(curr.getColumnIndex(IMAGEPATH_COLUMN)));
                    try {
                        newItem.setSeason(WSeasons.valueOf(curr.getString(curr.getColumnIndex(SEASON_COLUMN))));
                    }catch (Exception ee){
                        newItem.setSeason(WSeasons.valueOf("НЕТ"));
                    }
                    newItem.setMinTemperature(curr.getInt(curr.getColumnIndex(MINT_COLUMN)));
                    newItem.setMaxTemperature(curr.getInt(curr.getColumnIndex(MAXT_COLUMN)));
                    Log.e("ITEMS","NAME - "+newItem.getName()+"; MIN - "+newItem.getMinTemperature()+"; MAX - "+newItem.getMaxTemperature()+"; SEASON - "+newItem.getSeason());
                    items.add(newItem);
                }while(curr.moveToNext());
            }
        } catch (Exception e) {
            Log.d("ERROR", "Error while trying to get posts from database \n"+e.getLocalizedMessage());
        } finally {
            if (curr != null && !curr.isClosed()) {
                curr.close();
            }
        }
        Log.e("DB HELPER","Reading item list! "+items.size()+"; "+curr.getCount());
        return items;
    }

}
