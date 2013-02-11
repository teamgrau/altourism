package com.teamgrau.altourism.util.data.model;

/**
 * The class Geschichte models the data-object Geschichte.
 *
 * @author simon
 */

public final class Story {

    private String text;
    private String title;

    // fields to come: Tags, date of entry, media, user

    public Story( String text, String title ) {
        this.text = text;
        this.title = title;
    }

    public String getStoryText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
}
