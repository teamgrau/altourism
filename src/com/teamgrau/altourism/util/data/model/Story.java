package com.teamgrau.altourism.util.data.model;

import android.net.Uri;

import java.util.LinkedList;
import java.util.List;

/**
 * The class Geschichte models the data-object Geschichte.
 *
 * @author simon
 */

public final class Story {

    private String text;
    private String title;
    private List<Uri> media;


    // fields to come: Tags, date of entry, user

    public Story( String text, String title, List<Uri> media ) {
        this.text = text;
        this.title = title;
        this.media = media;
    }

    public String getStoryText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public List<Uri> getMedia() {
        return media;
    }
}
