package com.teamgrau.altourism.util.data.database;

import android.provider.BaseColumns;

/**
 * Defines names of tables and columns for the used database
 *
 * User: Simon
 */

public final class DBDefinition{

    public static final String DB_NAME = "AltourismDB";


    // The table that stores GPS-positions
    public static abstract class PositionEntry implements BaseColumns{

        public static final String TABLE_NAME = "Positions";
        public static final String COLUMN_NAME_Lat = "Lat";
        public static final String COLUMN_NAME_Lng = "Lng";

    }


    // No one should never ever instantiate this class as all information here is static
    private DBDefinition(){}

}
