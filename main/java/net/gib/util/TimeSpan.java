package net.gib.util;

public class TimeSpan {

    public static int secs(int secs) {
        return 1000*secs;
    }
    public static int minutes(int minutes) {
        return secs(minutes * 60);
    }

}
