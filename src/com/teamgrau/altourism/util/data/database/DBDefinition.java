package com.teamgrau.altourism.util.data.database;

import android.provider.BaseColumns;

/**
 * Defines names of tables and columns for the used database
 *
 * User: Simon
 */

public final class DBDefinition {

    public static final String DB_NAME = "AltourismDB";


// The table that stores GPS-positions the user has passed
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
            "DROP TABLE IF EXITS " + PositionEntry.TABLE_NAME;



// The table that stores geschichten for certain locations
    // table definition
    public static abstract class Geschichte implements BaseColumns {

        public static final String TABLE_NAME = "Geschichten";
        public static final String COLUMN_NAME_LOCATION = "Location";
        public static final String COLUMN_NAME_GESCHICHTE = "Geschichte";
    }

    // Create statement


    // No one should never ever instantiate this class as all information here is static
    private DBDefinition() {}

}
