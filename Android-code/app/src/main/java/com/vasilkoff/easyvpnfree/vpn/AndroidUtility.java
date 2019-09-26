package com.vasilkoff.easyvpnfree.vpn;

import com.vasilkoff.easyvpnfree.model.Server;

public class AndroidUtility {


    public static Server fromServerToVpnServer(VpnServer vpnServer){
        Server server = new Server(
                vpnServer.getHostName(),
                vpnServer.getIp(),
                vpnServer.getScore(),
                vpnServer.getPing(),
                vpnServer.getSpeed(),
                vpnServer.getCountry().getName(),
                vpnServer.getCountry().getShortName(),
                vpnServer.getNumVpnSessions(),
                vpnServer.getUptime(),
                vpnServer.getTotalUsers(),
                vpnServer.getTotalTraffic(),
                vpnServer.getLogType(),
                "opretor",
                "message",
                vpnServer.getConfidData(),
                0,"city",1,vpnServer.getLogType(),1.11,12.1
        );
        return server;
    }

}
