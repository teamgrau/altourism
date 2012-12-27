package com.teamgrau.altourism.util.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * This class delivers a DB connection when needed like follows:
 * AltourismDBHelper ADDHelper = new AltourismDBHelper(getContext());
 *
 * User: simon
 */

public class AltourismDBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1; // Should be incremented on schema update

    private static final String CREATE_TABLE_POSITIONS_STATEMENT =
            "CREATE TABLE " + DBDefinition.PositionEntry.TABLE_NAME + " (" +
            DBDefinition.PositionEntry._ID + " INTEGER PRIMARY KEY," +         // Autoincrementing rowid
            DBDefinition.PositionEntry.COLUMN_NAME_Lat + " REAL," +
            DBDefinition.PositionEntry.COLUMN_NAME_Lng + " REAL" +
            ")";

    private static final String DELETE_ENTRIES_TABLE_POSITIONS =
            "DROP TABLE IF EXITS " + DBDefinition.PositionEntry.TABLE_NAME;




    public AltourismDBHelper(Context context) {
        super(context, DBDefinition.DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_POSITIONS_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // At this time we just drop all data and create a new DB out of the definition
        db.execSQL(DELETE_ENTRIES_TABLE_POSITIONS);
        onCreate(db);
    }
}
