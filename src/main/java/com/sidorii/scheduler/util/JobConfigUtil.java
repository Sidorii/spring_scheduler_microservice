package com.sidorii.scheduler.util;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

public class JobConfigUtil {

    private static final String GLOBAL_PREFIX = "request.default.job.";
    private static final String CONFIG = "configuration.";
    private static final Properties RESOURCE = new Properties();


    public enum Times {
        NOW, FOREVER
    }

    static {
        try {
            RESOURCE.load(
                    HeadersUtil.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Date defaultStartDate() {
        String startTime = RESOURCE.getProperty(defaultConfigPath().concat("startDate"));

        if (startTime == null) {
            return null;
        }

        switch (Times.valueOf(startTime)) {
            case NOW:
                return new Date();
            default:
                return null;
        }
    }

    public static Date defaultEndDate() {
        String endTime = RESOURCE.getProperty(defaultConfigPath().concat("endDate"));


        if (endTime == null) {
            return null;
        }

        switch (Times.valueOf(endTime)) {
            case FOREVER:
                return null;
            default:
                return new Date();
        }
    }

    public static TimeZone defaultTimeZone() {
        String timeZone = RESOURCE.getProperty(defaultConfigPath().concat("time.zone"));

        if (timeZone != null) {
            return TimeZone.getTimeZone(timeZone);
        }

        return TimeZone.getDefault();
    }

    private static String defaultConfigPath() {
        return GLOBAL_PREFIX.concat(CONFIG);
    }

}
