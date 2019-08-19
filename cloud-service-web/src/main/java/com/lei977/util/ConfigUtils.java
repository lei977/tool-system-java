package com.lei977.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigUtils {
    public static PropertiesConfiguration getTdpServer() {
        PropertiesConfiguration pc = null;
        try {
            pc = new PropertiesConfiguration("config.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return pc;
    }

}
