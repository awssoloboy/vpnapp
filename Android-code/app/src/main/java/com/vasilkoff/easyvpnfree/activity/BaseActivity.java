package com.vasilkoff.easyvpnfree.activity;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.vasilkoff.easyvpnfree.App;
import com.vasilkoff.easyvpnfree.BuildConfig;
import com.vasilkoff.easyvpnfree.R;
import com.vasilkoff.easyvpnfree.database.DBHelper;
import com.vasilkoff.easyvpnfree.model.Server;
import com.vasilkoff.easyvpnfree.util.CountriesNames;
import com.vasilkoff.easyvpnfree.util.PropertiesService;
import com.vasilkoff.easyvpnfree.util.TotalTraffic;
import com.vasilkoff.easyvpnfree.util.iap.IabHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.blinkt.openvpn.core.VpnStatus;


public abstract class BaseActivity extends AppCompatActivity {

    private DrawerLayout fullLayout;
    private Toolbar toolbar;

    static final int ADBLOCK_REQUEST = 10001;
    static final int PREMIUM_SERVERS_REQUEST = 10002;
    public static Server connectedServer = null;
    boolean hideCurrentConnection = false;
    IabHelper iapHelper;
    public static final String IAP_TAG = "IAP";
    static final String TEST_ITEM_SKU = "android.test.purchased";
    static final String ADBLOCK_ITEM_SKU = "adblock";
    static final String MORE_SERVERS_ITEM_SKU = "more_servers";
    static String key = "";

    static boolean availableFilterAds = false;
    static boolean premiumServers = false;

    static String adblockSKU;
    static String moreServersSKU;
    static String currentSKU;

    int widthWindow ;
    int heightWindow;

    static DBHelper dbHelper;
    Map<String, String> localeCountries;


    @Override
    public void setContentView(int layoutResID)
    {

        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (useToolbar()) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        if (useHomeButton()) {
            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }

        if (false) {
            moreServersSKU = TEST_ITEM_SKU;
            adblockSKU = TEST_ITEM_SKU;
        } else {
            moreServersSKU = MORE_SERVERS_ITEM_SKU;
            adblockSKU = ADBLOCK_ITEM_SKU;
        }



        dbHelper = new DBHelper(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        widthWindow = dm.widthPixels;
        heightWindow = dm.heightPixels;

        localeCountries = CountriesNames.getCountries();

        App application = (App) getApplication();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TotalTraffic.saveTotal();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iapHelper != null) iapHelper.dispose();
        iapHelper = null;
    }




    protected boolean useToolbar()
    {
        return true;
    }

    protected boolean useHomeButton()
    {
        return true;
    }

    protected boolean useMenu()
    {
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.actionCurrentServer
                    && (connectedServer == null || hideCurrentConnection || !VpnStatus.isVPNActive()))
                menu.getItem(i).setVisible(false);

            if (premiumServers && menu.getItem(i).getItemId() == R.id.actionGetMoreServers)
                menu.getItem(i).setTitle(getString(R.string.current_servers_list));

            if (BuildConfig.FLAVOR == "underground" && menu.getItem(i).getItemId() == R.id.actionShare)
                menu.getItem(i).setVisible(false);
        }

        return useMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.actionRefresh:
                startActivity(new Intent(getApplicationContext(), LoaderActivity.class));
                finish();
                return true;
            case R.id.actionAbout:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            case R.id.actionShare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.actionCurrentServer:
                if (connectedServer != null)
                    startActivity(new Intent(this, ServerActivity.class));
                return true;
            case R.id.actionGetMoreServers:
                if (premiumServers) {
                    startActivity(new Intent(this, ServersInfo.class));
                }
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, MyPreferencesActivity.class));
                return true;
            case R.id.action_bookmarks:
                startActivity(new Intent(this, BookmarkServerListActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PREMIUM_SERVERS_REQUEST:
                    Log.d(IAP_TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

                    if (iapHelper == null) return;

                    if (iapHelper.handleActivityResult(requestCode, resultCode, data)) {
                        Log.d(IAP_TAG, "onActivityResult handled by IABUtil.");
                        Intent intent = new Intent(getApplicationContext(), LoaderActivity.class);
                        intent.putExtra("firstPremiumLoad", true);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
    }

    public Server getRandomServer() {
        Server randomServer;
        if (PropertiesService.getCountryPriority()) {
            randomServer = dbHelper.getGoodRandomServer(PropertiesService.getSelectedCountry());
        } else {
            randomServer = dbHelper.getGoodRandomServer(null);
        }
        return randomServer;
    }

    public void newConnecting(Server server, boolean fastConnection, boolean autoConnection) {
        if (server != null) {
            Intent intent = new Intent(this, ServerActivity.class);
            intent.putExtra(Server.class.getCanonicalName(), server);
            intent.putExtra("fastConnection", fastConnection);
            intent.putExtra("autoConnection", autoConnection);
            startActivity(intent);
        }
    }



    protected void ipInfoResult() {}

    protected void getIpInfo(Server server) {
        List<Server> serverList = new ArrayList<Server>();
        serverList.add(server);
        getIpInfo(serverList);
    }

    protected void getIpInfo(final List<Server> serverList) {
        JSONArray jsonArray = new JSONArray();

        for (Server server : serverList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("query", server.getIp());
                jsonObject.put("lang", Locale.getDefault().getLanguage());

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        AndroidNetworking.post(getString(R.string.url_check_ip_batch))
                .addJSONArrayBody(jsonArray)
                .setTag("getIpInfo")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (dbHelper.setIpInfo(response, serverList))
                            ipInfoResult();
                    }
                    @Override
                    public void onError(ANError error) {

                    }
                });
    }
}
