package com.teamgrau.altourism.util.data.database;

import android.provider.BaseColumns;

/**
 * Defines names of tables and columns for the used database
 * <p/>
 * User: Simon
 */

public final class DBDefinition {

    public static final String DB_NAME = "AltourismDB";
    public static final String DB_ENCODING = "\"UTF-8\"";

    /**
     * ***********************************************************
     * The table that stores GPS-positions the user has passed
     */
    // table definition
    public static abstract class PositionEntry implements BaseColumns {

        public static final String TABLE_NAME = "Positions";
        public static final String COLUMN_NAME_Lat = "Lat";
        public static final String COLUMN_NAME_Lng = "Lng";

    }

    // Create statement
    static final String CREATE_TABLE_POSITIONS_STATEMENT =
            "CREATE TABLE " + PositionEntry.TABLE_NAME + " (" +
                    PositionEntry._ID + " INTEGER PRIMARY KEY," +    // Autoincrementing rowid
                    PositionEntry.COLUMN_NAME_Lat + " REAL," +
                    PositionEntry.COLUMN_NAME_Lng + " REAL" +
                    ")";

    // Drop statement
    static final String DELETE_ENTRIES_TABLE_POSITIONS =
            "DROP TABLE IF EXISTS " + PositionEntry.TABLE_NAME;

/*
    The table that stores GPS-positions the user has passed
 **************************************************************/

    /**
     * *********************************************************************
     * The table that stores POI's, respective geschichten + their location
     * Begin
     */
    // table definition
    public static abstract class POI implements BaseColumns {

        public static final String TABLE_NAME = "POIs";
        public static final String COLUMN_NAME_Lat = "Lat";    // for location
        public static final String COLUMN_NAME_Lng = "Lng";    // for location
        public static final String COLUMN_NAME_Geschichte = "Geschichte";
        public static final String COLUMN_NAME_Title = "Title";
    }

    // Create statement
    static final String CREATE_TABLE_POIs_STATEMENT =
            "CREATE TABLE " + POI.TABLE_NAME + " (" +
                    POI._ID + " INTEGER PRIMARY KEY," +    // Autoincrementing rowid; Will be used to identify stories later
                    POI.COLUMN_NAME_Lat + " INTEGER," +
                    POI.COLUMN_NAME_Lng + " INTEGER," +
                    POI.COLUMN_NAME_Geschichte + " TEXT," +
                    POI.COLUMN_NAME_Title + " TEXT" +
                    ")";

    // Drop statement
    static final String DELETE_ENTRIES_TABLE_POIs =
            "DROP TABLE IF EXISTS " + POI.TABLE_NAME;

/*
    The table that stores POI's, respective geschichten + their location
    End
**************************************************************************/


    /**
     * *********************************************************************
     * The table that stores POI's media
     * Begin
     */
    // table definition
    public static abstract class Media implements BaseColumns {

        public static final String TABLE_NAME = "Media";
        public static final String COLUMN_NAME_URI = "URI";

    }

    // Create statement
    static final String CREATE_TABLE_MEDIA_STATEMENT =
            "CREATE TABLE " + Media.TABLE_NAME + " (" +
                    POI._ID + " INTEGER," +                    // identifies the story which the media belongs to
                    Media.COLUMN_NAME_URI + " Text" +
                    " FOREIGN KEY(" + POI._ID + ") REFERENCES " + POI.TABLE_NAME + "(" + POI._ID + ")" +
                    ")";

    // Drop statement
    static final String DELETE_ENTRIES_TABLE_MEDIA =
            "DROP TABLE IF EXISTS " + Media.TABLE_NAME;

/*
    The table that stores POI's media
    End
**************************************************************************/

    // No one should never ever instantiate this class as all information here is static
    private DBDefinition() {
    }

}