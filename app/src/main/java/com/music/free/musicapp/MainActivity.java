package com.music.free.musicapp;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;


import com.music.free.adapter.TabAdapter;

import guy4444.smartrate.SmartRate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    SearchView searchView;
    TabLayout tabs;
    ViewPager viewPager;
    TabAdapter tabAdapter;
    LinearLayout linear;
    ImageView play;
    ImageView  pause;
    ImageView  imgsearch;
     Typeface mTypeface;
    LinearLayout progresly;
    public static String nowgenre="",noworigingenre="";




    FragmentRefreshListener fragmentRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        viewPager = findViewById(R.id.viewpager);
        linear = findViewById(R.id.linear);
        searchView=findViewById(R.id.searchview);
        imgsearch=findViewById(R.id.ic_search);
        progresly=findViewById(R.id.llProgressBar);



        GDPRChecker gdprChecker = new GDPRChecker();
        gdprChecker.check(getApplicationContext(),MainActivity.this,Constants.getAppid());



        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        play= findViewById(R.id.play);
        pause= findViewById(R.id.pause);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)&& (!Settings.System.canWrite(this))) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);

        }

        imgsearch.setOnClickListener(v -> viewPager.setCurrentItem(1));





        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue Medium.ttf");
        ViewGroup vg = (ViewGroup) tabs.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }

    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.play:

                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);


                break;

            case R.id.pause:

                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                break;

            default:


        }


    }


    public  void showLoading(){
        progresly.setVisibility(View.VISIBLE);
    }

    public  void hideLoading(){
        progresly.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        exitdialog();

    }

    public void gototab(int tab){
        Log.e("kilked", MainActivity.noworigingenre);

        viewPager.setCurrentItem(tab,true);
//        Fragment activeFragment = tabAdapter.getItem(tab);
//        ((SearchFragment)activeFragment).getsongs(MainActivity.noworigingenre,"genre");

    }

    public void  exitdialog(){
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle(R.string.quittitle)
                .setMessage(R.string.quitmessage)

                .addButton(getString(R.string.quitbutton), -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                        finishAffinity();
                        finish();

                    }
                })
                .addButton(getString(R.string.rate), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showRate();

                    }
                });

// Show the alert
        builder.show();
    }

    public void showRate(){

        SmartRate.Rate(MainActivity.this
                , "Rate Us"
                , "Tell others what you think about this app"
                , "Continue"
                , "Please take a moment and rate us on Google Play"
                , "click here"
                , "Cancel"
                , "Thanks for the feedback"
                , Color.parseColor("#2196F3")
                , 4
        );

    }

}
