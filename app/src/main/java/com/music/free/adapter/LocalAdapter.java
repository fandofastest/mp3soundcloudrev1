package com.music.free.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.music.free.modalclass.LocalModel;
import com.music.free.modalclass.SongModalClass;
import com.music.free.musicapp.Ads;
import com.music.free.musicapp.Constants;
import com.music.free.musicapp.MediaPlayerService;
import com.music.free.musicapp.PlayerActivity;
import com.music.free.musicapp.R;
import com.music.free.musicapp.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;
import static android.content.Context.DOWNLOAD_SERVICE;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.MyViewHolder> {

    private List<LocalModel> songModalClassList;

    private Context ctx;
    private Boolean online;



    public class MyViewHolder extends RecyclerView.ViewHolder  {


        private TextView    txtsongname;
        private TextView    txtartistname;
        private TextView    txtmoviename;
        private TextView    txttime;
        private ImageView img;
        private LinearLayout lytlayout;



        public MyViewHolder(View view) {
            super(view);

            txtsongname = (TextView) view.findViewById(R.id.txt_song_name);
            img = (ImageView) view.findViewById(R.id.img);
            txtartistname = (TextView) view.findViewById(R.id.txt_artist_name);
            txtmoviename = (TextView) view.findViewById(R.id.txt_movie_name);
            txttime = (TextView) view.findViewById(R.id.txt_time);
            lytlayout=view.findViewById(R.id.lyt_item);


        }


    }

    public LocalAdapter(List<LocalModel> songModalClassList, Context context) {

        this.songModalClassList = songModalClassList;
        this.ctx=context;
        this.online=online;


    }






    @Override
    public LocalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_songs, parent, false);

        return new LocalAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocalAdapter.MyViewHolder holder, final int position) {


        final LocalModel modalClass = songModalClassList.get(position);
        holder.txtsongname.setText(modalClass.getFilename());
//        Utils utils = new Utils();
//        String duratiom= utils.milliSecondsToTimer(Long.parseLong(modalClass.getDuration()));
        holder.txttime.setVisibility(View.GONE);

        holder.txtmoviename.setVisibility(View.GONE);

        holder.txtartistname.setText("Local");
        holder.img.setImageResource(R.drawable.icon);



        holder.lytlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ads ads = new Ads();
                if (Constants.getAds().equals("admob")){
                    ads.showinter(ctx,Constants.getInter());
                }
                else {
                    ads.showinterfb(ctx,Constants.getInterfan());

                }
                ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
                    @Override
                    public void onAdsfinish() {

                        Intent intent = new Intent(ctx, PlayerActivity.class);
                        intent.putExtra("pos",position);
                        intent.putExtra("type","offline");
                        ctx.startActivities(new Intent[]{intent});



                    }

                    @Override
                    public void onRewardOk() {

                    }
                });
            }
        });





    }




    @Override
    public int getItemCount() {
        return songModalClassList.size();
    }



}
