package org.ilw.meintagebuch.dto;

/**
 * Created by Ilja.Winokurow on 17.02.2017.
 */

public class Subject {
    public int value;
    public String comments;

    public Subject(int value, String comments) {
        this.value = value;
        this.comments = comments;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
