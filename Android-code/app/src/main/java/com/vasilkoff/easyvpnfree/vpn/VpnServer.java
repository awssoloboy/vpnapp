package com.vasilkoff.easyvpnfree.vpn;

import com.google.gson.annotations.SerializedName;
import com.vasilkoff.easyvpnfree.model.Country;

public class VpnServer {



    @SerializedName("id")
    private String id;

    @SerializedName("config_data")
    private String confidData;

    @SerializedName("country")
    private VpnCountry country;

    @SerializedName("hostname")
    private String hostName;

    @SerializedName("ip")
    private String ip;

    @SerializedName("score")
    private String score;

    @SerializedName("ping")
    private String ping;

    @SerializedName("speed")
    private String speed;

    @SerializedName("numVpnSessions")
    private String numVpnSessions;

    @SerializedName("uptime")
    private String uptime;

    @SerializedName("totalUsers")
    private String totalUsers;

    @SerializedName("totalTraffic")
    private String totalTraffic;

    @SerializedName("log_type")
    private String logType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfidData() {
        return confidData;
    }

    public void setConfidData(String confidData) {
        this.confidData = confidData;
    }

    public VpnCountry getCountry() {
        return country;
    }

    public void setCountry(VpnCountry country) {
        this.country = country;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPing() {
        return ping;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getNumVpnSessions() {
        return numVpnSessions;
    }

    public void setNumVpnSessions(String numVpnSessions) {
        this.numVpnSessions = numVpnSessions;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(String totalUsers) {
        this.totalUsers = totalUsers;
    }

    public String getTotalTraffic() {
        return totalTraffic;
    }

    public void setTotalTraffic(String totalTraffic) {
        this.totalTraffic = totalTraffic;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }
}
