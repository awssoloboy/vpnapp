package com.vasilkoff.easyvpnfree.vpn;

import com.google.gson.annotations.SerializedName;

public class VpnCountry {


    @SerializedName(("id"))
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("short")
    private String shortName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
