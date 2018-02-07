package com.murey.poster.postermaster.model;

public class DataStore {

    /**
     * The instance of the DataStore
     */
    private static DataStore instance;

    /**
     * Getter of the instance DataStore
     *
     * @return the DataStore instance
     */
    public static DataStore getInstance() {
        if (instance == null) {
            synchronized (DataStore.class) {
                if (instance == null) {
                    instance = new DataStore();
                }
            }
        }
        return instance;
    }

    /**
     * Object ==> app config
     **/
    private AppConfiguration appConfiguration;

    public AppConfiguration getAppConfiguration() {
        return appConfiguration;
    }

    public void setAppConfiguration(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

}
