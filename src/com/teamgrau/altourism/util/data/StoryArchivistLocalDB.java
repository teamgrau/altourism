package com.teamgrau.altourism.util.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import com.teamgrau.altourism.util.data.database.AltourismDBHelper;
import com.teamgrau.altourism.util.data.database.DBDefinition;
import com.teamgrau.altourism.util.data.model.Story;

/**
 * User: simon
 */

public class StoryArchivistLocalDB implements StoryArchivist {

    AltourismDBHelper AlDBHelper;

    public StoryArchivistLocalDB(Context context) {
        AlDBHelper = new AltourismDBHelper(context);
    }


    @Override
    public void storeGeschichte(Location position, Story story) {
        // a key-value map for the insert-method
        ContentValues values = new ContentValues();
        values.put(DBDefinition.POI.COLUMN_NAME_Lat, position.getLatitude());
        values.put(DBDefinition.POI.COLUMN_NAME_Lng, position.getLongitude());
        values.put(DBDefinition.POI.COLUMN_NAME_Geschichte, story.getStoryText());

        SQLiteDatabase db = AlDBHelper.getWritableDatabase();
        long newRowId;
        // the second argument null makes the framework insert no row if values is empty
        newRowId = db.insert( DBDefinition.POI.TABLE_NAME, null, values );
        Log.d("Altourism beta", "inserted Story into DB, new rowId is: " + newRowId);
        db.close();
    }
}
