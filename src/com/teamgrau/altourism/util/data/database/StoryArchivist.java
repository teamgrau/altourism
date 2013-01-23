package com.teamgrau.altourism.util.data.database;

import android.location.Location;
import com.teamgrau.altourism.util.data.model.Story;

/**
 * A StoryArchivist provides functionality to store Storys and/or POI's permanently
 *
 * User: simon
 */

public interface StoryArchivist {

    void storeGeschichte( Location position, Story story );
}
