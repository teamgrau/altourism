package com.teamgrau.altourism.util.data.database;

import android.provider.BaseColumns;

/**
 * Defines names of tables and columns for the used database
 *
 * User: Simon
 */

public final class DBDefinition implements BaseColumns{

    public static final String DB_NAME = "AltourismDB";

    public static abstract class PositionEntry{

        public static final String TABLE_POSITIONS = "Positions";
        public static final String COLUMN_NAME_Lat = "Lat";
        public static final String COLUMN_NAME_Lng = "Lng";

    }

}
