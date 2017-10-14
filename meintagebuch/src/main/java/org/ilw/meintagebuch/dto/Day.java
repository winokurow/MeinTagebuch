package org.ilw.meintagebuch.dto;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * Created by Ilja.Winokurow on 17.02.2017.
 */

public class Day {
    Map<Integer, Subject> subjects;
    String comment;
    int color;

    public Day(Map<Integer, Subject> subjects, String comment, int color) {
        this.subjects = subjects;
        this.comment = comment;
        this.color = color;
    }

    public Map<Integer, Subject> getSubjects() {
        return subjects;
    }
    public String getSubjectsString() {
        StringBuilder builder = new StringBuilder();
        Iterator it = subjects.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            builder.append(pair.getKey());
            builder.append("XXXXX");
            builder.append(((Subject)pair.getValue()).getValue());
            builder.append("XXXXX");
            String temp = ((Subject)pair.getValue()).getComments();
            if ((temp == null) ||(temp.isEmpty()))
            {
                temp = " ";
            }
            builder.append(temp);
            builder.append("XXXXX");
            builder.append("YYYYY");
        }
        return builder.toString();
    }
    public void setSubjects(HashMap<Integer, Subject> subjects) {
        this.subjects = subjects;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
