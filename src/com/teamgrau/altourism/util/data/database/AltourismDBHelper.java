package com.teamgrau.altourism.util.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class delivers a DB connection when needed like follows:
 * AltourismDBHelper ADDHelper = new AltourismDBHelper(getContext());
 * <p/>
 * User: simon
 */

public class AltourismDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4; // Should be incremented on schema update

    public AltourismDBHelper(Context context) {
        super(context, DBDefinition.DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA encoding = " + DBDefinition.DB_ENCODING + "; ");
        db.execSQL(DBDefinition.CREATE_TABLE_POSITIONS_STATEMENT);
        db.execSQL(DBDefinition.CREATE_TABLE_POIs_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // At this time we just drop all data and create a new DB out of the definition
        db.execSQL(DBDefinition.DELETE_ENTRIES_TABLE_POSITIONS);
        db.execSQL(DBDefinition.DELETE_ENTRIES_TABLE_POIs);
        onCreate(db);
    }
}