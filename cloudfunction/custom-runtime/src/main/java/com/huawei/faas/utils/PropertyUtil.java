package com.huawei.faas.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    public static Properties props = null;

    static {
        props = new Properties();
        try {
            InputStream in = PropertyUtil.class.getResourceAsStream("/application.properties");
            props.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String name) {
        return props.getProperty(name, "");

    }
}