package com.music.free.musicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.music.free.modalclass.LocalModel;
import com.music.free.modalclass.SongModalClass;




/**
 * Created by Remmss on 29-08-2017.
 */

public class PlayerActivity extends AppCompatActivity implements CommonFragment.onSomeEventListener,View.OnClickListener {

    LinearLayout admoblayout;
    ImageView imgplay;
    ImageView imgpause;
    int pos=0;
    ProgressBar progressBar;
    TextView tvtitle;
    TextView      tvartist;
    TextView      tvsongcurrentduration;
    TextView     tvsongtotalduration;
    ImageView imageView;
    ImageView       next;
    ImageView       prev;
    SeekBar seekBar;
//    List <Lrc> lrcs;
    int currentpost=0;
    int newpost;
    private  String type;

    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
      com.lauzy.freedom.library.LrcView mlirik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        admoblayout=findViewById(R.id.banner_container);
        Ads ads  = new Ads();
        Display display =getWindowManager().getDefaultDisplay();
        ads.ShowBannerAds(PlayerActivity.this,admoblayout,Constants.getBannerfan(),Constants.getBanner(),display);

//        File file = new File("https://musicpedia.xyz/api/lirik.php?id=b1g1UjFtWHBoenY2RHpJZU1Td3Z5UT09");
//        lrcs=LrcHelper.parseLrcFromFile(file);
//
//        mlirik=findViewById(R.id.mlirik);
//
//        mlirik.setLrcData(lrcs);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String status = intent.getStringExtra(Constants.getStatus());

                    if (status.equals("playing")) {

                        tvtitle.setText(MediaPlayerService.currenttitle);
                        tvartist.setText(MediaPlayerService.currentartist);
                        imgpause.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        try {
                            Glide.with(getApplicationContext()).load(MediaPlayerService.currentimageurl).error(R.drawable.icon).into(imageView);
                        }
                        catch (Exception e){
                            imageView.setImageResource(R.drawable.icon);
                        }

                        playMediaPlayer();


                    } else if (status.equals("stoping")) {
                        pauseMediaPlayer();
                    }

                }


            }, new IntentFilter(Constants.getIntentfilter()));


            imgplay = (ImageView) findViewById(R.id.img_play);
            imgpause = (ImageView) findViewById(R.id.img_pause);
            next=findViewById(R.id.nextview);
            prev=findViewById(R.id.prevview);

            progressBar = findViewById(R.id.progressBar);
            seekBar = findViewById(R.id.seekbar);

            // set Progress bar values
            seekBar.setProgress(0);

            seekBar.setMax(Utils.MAX_PROGRESS);


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar1, int progress, boolean b) {

                    if(b){

                        seekBar.setProgress(progress);
                            updateseekbarmp(progress);

                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    //must needed for seekbar
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //must needed for seekbar

                }


            });



            tvsongcurrentduration = (TextView) findViewById(R.id.tvmin);
            tvsongtotalduration = (TextView) findViewById(R.id.tvmax);

            imgplay.setOnClickListener(this);
            imgpause.setOnClickListener(this);
            next.setOnClickListener(this);
            prev.setOnClickListener(this);
            tvartist = findViewById(R.id.artist);
            tvtitle = findViewById(R.id.title);
            imageView = findViewById(R.id.imagefoto);

            pos=getIntent().getIntExtra("pos",0);
            type=getIntent().getStringExtra("type");

            tvtitle.setText("Please Wait Preparing Your Music");
            tvartist.setText("");

        playmusic(pos,type);




            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_play:
               playMediaPlayer();
                break;

            case R.id.img_pause:
                pauseMediaPlayer();
                break;
            case R.id.nextview:
                next();
                break;
            case R.id.prevview:
               prev();
                break;
            default:

        }

    }

    public void playmusic(int pos,String type) {

//        Log.e("tyoe",type);
        currentpost=pos;

        if (type.equals("online")){
            SongModalClass songModalClass = MediaPlayerService.currentplay.get(pos);
            tvartist.setText(songModalClass.getArtistName());
            tvtitle.setText(songModalClass.getSongName());
            Glide.with(getApplicationContext()).load(songModalClass.getImgurl()).error(R.drawable.icon).into(imageView);

        }

        else {
            LocalModel songModalClass = MediaPlayerService.localplaylists.get(pos);
            tvartist.setText(songModalClass.getType());
            tvtitle.setText(songModalClass.getFilename());
            imageView.setImageResource(R.drawable.bg);
        }





        Intent plyerservice = new Intent(PlayerActivity.this, MediaPlayerService.class);

        plyerservice.putExtra("pos", pos);
        plyerservice.putExtra("type", type);


        startService(plyerservice);


    }



    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            updateTimerAndSeekbar();
            // Running this thread after 10 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    private void updateTimerAndSeekbar() {


        Intent intent = new Intent(Constants.getIntentfilter());
        intent.putExtra(Constants.getStatus(), "getduration");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);


        // Displaying Total Duration time
        Utils utils = new Utils();
        tvsongtotalduration.setText(utils.milliSecondsToTimer(Constants.getTotalduration()));
        // Displaying time completed playing
        tvsongcurrentduration.setText(utils.milliSecondsToTimer(Constants.getCurrentduration()));

        // Updating progress bar
        int progress =  (utils.getProgressSeekBar(Constants.getCurrentduration(), Constants.getTotalduration()));
        seekBar.setProgress(progress);
    }

    // stop player when destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    public void next (){
        tvsongcurrentduration.setText("");
        tvsongtotalduration.setText("");
        tvtitle.setText("Please Wait");
        tvartist.setText("");
        imgpause.setVisibility(View.GONE);
        imageView.setImageResource(R.color.white);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent("fando");
        intent.putExtra("status", "next");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        mHandler.post(mUpdateTimeTask);

    }
    public void prev (){
        tvsongcurrentduration.setText("");
        tvsongtotalduration.setText("");
        tvtitle.setText("Please Wait");
        tvartist.setText("");
        imgpause.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.color.white);
        Intent intent = new Intent("fando");
        intent.putExtra("status", "prev");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        mHandler.post(mUpdateTimeTask);

    }





    public void pauseMediaPlayer() {
        imgplay.setVisibility(View.VISIBLE);
        imgpause.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        Intent intent = new Intent(Constants.getIntentfilter());
        intent.putExtra(Constants.getStatus(), "pause");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void playMediaPlayer() {

        Intent intent = new Intent(Constants.getIntentfilter());
        intent.putExtra(Constants.getStatus(), "resume");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        progressBar.setVisibility(View.GONE);
        imgplay.setVisibility(View.GONE);
        imgpause.setVisibility(View.VISIBLE);

        // Changing button image to pause button
        mHandler.post(mUpdateTimeTask);
    }

    public void  updateseekbarmp(int progress){

        double currentseek = ((double) progress/(double)Utils.MAX_PROGRESS);

        int totaldura= Constants.getTotalduration();
        int seek= (int) (totaldura*currentseek);

        Intent intent = new Intent("fando");
        intent.putExtra("status", "seek");
        intent.putExtra("seektime",seek);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);





    }

    @Override
    public void someEvent(int s) {

        //needed for implements class

    }
}