package org.ilw.meintagebuch.org.ilw.meintagebuch.data;

/**
 * Created by Ilja.Winokurow on 02.10.2017.
 */

public enum Period {
    ONE_DAY (1, "1 Tag"),
    THREE_DAYS (3, "3 Tage"),
    FIVE_DAYS (5, "5 Tage"),
    SEVEN_DAYS (7, "7 Tage"),
    THIRTY_DAYS (30, "30 Tage"),
    NINETY_DAYS (90, "90 Tage"),
    ONE_HUNDRED_EIGHTY (180, "180 Tage"),
    THREE_HUNDRED_SIXTY (360, "360 Tage");

    int number;
    String text;

    Period(int i, String s) {
        number = i;
        text = s;
    }

    @Override
    public String toString()
    {
        return text;
    }
}
