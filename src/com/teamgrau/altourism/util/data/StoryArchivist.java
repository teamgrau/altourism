package com.teamgrau.altourism.util.data;

import android.location.Location;
import com.teamgrau.altourism.util.data.model.Story;

/**
 * A StoryArchivist provides functionality to store Storys and/or POI's permanently
 * <p/>
 * User: simon
 */

public interface StoryArchivist {

    void storeGeschichte(Location position, Story story);
}
