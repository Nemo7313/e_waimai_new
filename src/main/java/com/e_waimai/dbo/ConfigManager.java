package com.e_waimai.dbo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    protected static Logger logger = LogManager.getLogger("APP");
    private static Properties properties;
    private static ConfigManager configManager;

    private ConfigManager(){
        String configFile = "database.properties";
        properties = new Properties();
        InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream(configFile);
        try {
            properties.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigManager getInstance(){
        if(null == configManager){
            configManager = new ConfigManager();
        }
        return configManager;
    }

    public String getKey(String key){
        return properties.getProperty(key);
    }


}
