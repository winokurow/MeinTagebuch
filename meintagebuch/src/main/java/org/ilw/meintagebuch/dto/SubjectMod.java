package org.ilw.meintagebuch.dto;

/**
 * Created by Ilja.Winokurow on 17.02.2017.
 */
public class SubjectMod implements Cloneable{
    private String name;
    private String description;
    private String status;

    public SubjectMod(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SubjectMod(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjectMod that = (SubjectMod) o;

        if (!name.equals(that.name)) return false;
        if (!description.equals(that.description)) return false;
        return status.equals(that.status);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
