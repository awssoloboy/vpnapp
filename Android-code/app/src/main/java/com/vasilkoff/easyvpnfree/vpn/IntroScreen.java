package com.vasilkoff.easyvpnfree.vpn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.vasilkoff.easyvpnfree.R;
import com.vasilkoff.easyvpnfree.activity.HomeActivity;

public class IntroScreen extends AppIntro {


    private  String [] pageTitle = {
            "High Speed",
            "Incognito",
            "Stable & Fastest"
    };
    private  String [] pageDesc = {
            "A range of  VPN servers are available, super faster speed.",
            "Encrypts your internet traffic, protect your privacy, and keep you safe from 3rd party tracking for your information security",
            "You can choose VPN servers easily with high speed, unlimited bandwidth and unlimited server switches, you can connect from anywhere in the world"
    };

    private int [] imageRes = {
            R.drawable.ic_flash,
            R.drawable.ic_incoginito,
            R.drawable.ic_jet
    };

    private int [] backgroundColor={
        R.color.flash,R.color.colorAccent,R.color.telegram};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pagesize = pageTitle.length;

        for(int i = 0; i< pagesize; i++){
            SliderPage sliderPage = new SliderPage();
            sliderPage.setTitle(pageTitle[i]);
            sliderPage.setDescription(pageDesc[i]);
            sliderPage.setImageDrawable(imageRes[i]);
            sliderPage.setBgColor(ContextCompat.getColor(this,backgroundColor[i]));
            addSlide(AppIntroFragment.newInstance(sliderPage));

        }

        setBarColor(Color.parseColor("#003F51B5"));
        setSeparatorColor(Color.parseColor("#002196F3"));

        showSkipButton(true);
        setProgressButtonEnabled(true);


        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        start();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        start();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    private void start(){
        Intent myIntent = new Intent(this, HomeActivity.class);
        startActivity(myIntent);
        finish();

    }
}