package com.sidorii.scheduler.util;


import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Properties;

public class HeadersUtil {

    private static final String GLOBAL_PREFIX = "request.default.headers.";
    private static final String CONFIG = "configuration.";
    private static final Properties RESOURCE = new Properties();

    static {
        try {
           RESOURCE.load(
                    HeadersUtil.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static MediaType defaultContentType() {
        String contentType = RESOURCE.getProperty(defaultConfigPath().concat("contentType"));

        if (contentType != null) {
            return MediaType.valueOf(contentType);
        }

        return MediaType.ALL;
    }

    public static MediaType defaultAccept() {
        String accept = RESOURCE.getProperty(defaultConfigPath().concat("accept"));

        if (accept != null) {
            return MediaType.valueOf(accept);
        }
        return MediaType.ALL;
    }

    private static String defaultConfigPath() {
        return GLOBAL_PREFIX.concat(CONFIG);
    }

}
