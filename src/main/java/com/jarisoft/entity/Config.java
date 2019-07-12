package com.jarisoft.entity;

/**
 * 配置项
 * Created by robot on 2018/7/24.
 */

public class Config {
    private String webService;
    private String nameSpcae;
    private String ftpAddress;
    private int ftpPort;
    private String user;
    private String password;
    private boolean logOpen;
    private int logNum;

    public int getLogNum() {
        return logNum;
    }

    public void setLogNum(int logNum) {
        this.logNum = logNum;
    }

    public String getWebService() {
        return webService;
    }

    public void setWebService(String webService) {
        this.webService = webService;
    }

    public String getNameSpcae() {
        return nameSpcae;
    }

    public void setNameSpcae(String nameSpcae) {
        this.nameSpcae = nameSpcae;
    }

    public String getFtpAddress() {
        return ftpAddress;
    }

    public void setFtpAddress(String ftpAddress) {
        this.ftpAddress = ftpAddress;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogOpen() {
        return logOpen;
    }

    public void setLogOpen(boolean logOpen) {
        this.logOpen = logOpen;
    }
}
