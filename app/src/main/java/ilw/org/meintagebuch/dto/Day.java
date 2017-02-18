package ilw.org.meintagebuch.dto;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * Created by Ilja.Winokurow on 17.02.2017.
 */

public class Day {
    Map<String, Subject> subjects;
    String comment;

    public Day(Map<String, Subject> subjects, String comment) {
        this.subjects = subjects;
        this.comment = comment;
    }

    public Map<String, Subject> getSubjects() {
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
            if (temp.isEmpty())
            {
                temp = " ";
            }
            builder.append(temp);
            builder.append("XXXXX");
            builder.append("YYYYY");
        }
        return builder.toString();
    }
    public void setSubjects(HashMap<String, Subject> subjects) {
        this.subjects = subjects;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
