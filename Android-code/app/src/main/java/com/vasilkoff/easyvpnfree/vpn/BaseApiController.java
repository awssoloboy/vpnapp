package com.vasilkoff.easyvpnfree.vpn;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vasilkoff.easyvpnfree.App;
import com.vasilkoff.easyvpnfree.model.Country;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BaseApiController{

    private static int totalEvents = 1;

    public static String base_url="http://159.65.94.189:80/";
    private static String proxyLink = "https://telegramnews-a456a.firebaseio.com/proxy.json";
    public final static String socks_5_proxy_api ="https://www.proxy-list.download/api/v1/get?type=socks5&anon=elite";//https://www.proxy-list.download/api/v1
    private final static String locationApi = "http://ip-api.com/json/";


    private static final String countiriesUrl = "http://157.245.14.8:70/countries/";
    private static final String serversUrl = "http://157.245.14.8:70/country/";
    private static final String randomServersUrl = "http://157.245.14.8:70/random/";

    public static final int didReceiveCountries = totalEvents++;
    public static  int didReceiveRandomServers = totalEvents++;
    public static int didReciveServersForcountry = totalEvents++;
    public static final int didReceiveProxy = totalEvents++;
    public static  int didReceiveSocks5proxy = totalEvents++;
    public static int diReceiveLocationData = totalEvents++;



    public interface ApiCallBack{
        void didReceiveData(int type, Object... object);
        void onError(String error_message);
    }

    private static BaseApiController instance;
    private RequestQueue requestQueue;
    private Gson gson;

    private BaseApiController() {
        requestQueue = getRequestQueue();
        gson = new GsonBuilder().create();
    }


    public static synchronized BaseApiController getInstance() {
        if (instance == null) {
            instance = new BaseApiController();
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(App.getInstance());
        }
        return requestQueue;
    }


    private <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public void getCountires(final  ApiCallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, countiriesUrl, response -> {
            if(callBack!=null){
                List<VpnCountry> countryList = (new Gson()).fromJson(response, new TypeToken<List<VpnCountry>>() {}.getType());
                callBack.didReceiveData(didReceiveCountries,countryList);
            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }

    public void getRandomServers(final ApiCallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, randomServersUrl, response -> {
            if(callBack!=null){
                Log.i("databack",response +  " is the rspise");
                List<VpnServer> vpnServerList = (new Gson()).fromJson(response, new TypeToken<List<VpnServer>>() {}.getType());
                for(VpnServer server: vpnServerList){
                    Log.i("servername",server.getHostName());
                }

                callBack.didReceiveData(didReceiveRandomServers,vpnServerList);
            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        Log.i("servername",error.getMessage());

                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);

    }

    public void getServerListForCoutry(String countryId,final  ApiCallBack callBack){
        String url = serversUrl + countryId + "/servers/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            if(callBack!=null){
                List<VpnServer> vpnServerList = (new Gson()).fromJson(response, new TypeToken<List<VpnServer>>() {}.getType());
                callBack.didReceiveData(didReciveServersForcountry,vpnServerList);
            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);

    }

    public void getProxysFromFirebase(final  ApiCallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, proxyLink,
               response -> {
                   if(callBack!=null){

                     List<String> proxyLinkList = (new Gson()).fromJson(response, new TypeToken<List<String>>(){}.getType());
                     callBack.didReceiveData(didReceiveProxy, proxyLinkList);

                   }
               },
               error -> {
                   if(callBack!=null && error!=null){
                       callBack.onError(error.getMessage());
                   }
               });
       addToRequestQueue(stringRequest);
    }

    public void getSocks5ProxyList(final  ApiCallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, socks_5_proxy_api, response -> {
                    if(callBack!=null){
                       String [] data= response.split("\n");
                       if(data.length >=1){
                           List<String> proxyLinkList = Arrays.asList(data);

                           callBack.didReceiveData(didReceiveSocks5proxy,proxyLinkList);
                       }


                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }

    public void getLocationDataByIp(String ip,final ApiCallBack callBack){
        String url = locationApi + ip;
        Log.i("response",url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.i("response",response);
            if(callBack!=null){
                 callBack.didReceiveData(diReceiveLocationData,response);
            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);

    }





}