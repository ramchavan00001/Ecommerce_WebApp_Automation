package ramchavantestautomation.Utils;

//package com.ramchavantestautomation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties prop = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found in resources");
            }
            prop.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }

    public static int getInt(String key, int defaultValue) {
        String val = prop.getProperty(key);
        return (val == null) ? defaultValue : Integer.parseInt(val);
    }
}
