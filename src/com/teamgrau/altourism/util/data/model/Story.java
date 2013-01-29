package com.teamgrau.altourism.util.data.model;

/**
 * The class Geschichte models the data-object Geschichte.
 *
 * @author simon
 */

public final class Story {

    private String text;

    // fields to come: Tags, date of entry, media, user

    public Story(String text) {
        this.text = text;
    }

    public String getStoryText() {
        return text;
    }
}
