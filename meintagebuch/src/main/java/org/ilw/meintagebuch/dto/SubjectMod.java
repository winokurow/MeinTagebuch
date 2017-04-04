package org.ilw.meintagebuch.dto;

/**
 * Created by Ilja.Winokurow on 17.02.2017.
 */

public class SubjectMod {
    public String name;
    public String description;

    public SubjectMod(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
