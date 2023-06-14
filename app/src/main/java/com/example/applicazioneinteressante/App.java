package com.example.applicazioneinteressante;

public class App {
    private String appName;
    private String packageName;
    private String sourceDir;

    public App(String appName, String packageName, String sourceDir) {
        this.appName = appName;
        this.packageName = packageName;
        this.sourceDir = sourceDir;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }
}
