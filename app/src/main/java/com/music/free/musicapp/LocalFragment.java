package com.music.free.musicapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.music.free.adapter.LocalAdapter;
import com.music.free.adapter.SongAdapter;
import com.music.free.modalclass.LocalModel;

import static com.music.free.musicapp.MediaPlayerService.listsong;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recycle;
    private LocalAdapter songAdapter;
    LinearLayout admoblayout;
    Context ctx;
    public LocalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalFragment newInstance(String param1, String param2) {
        LocalFragment fragment = new LocalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);


        ctx=getContext();

        recycle = view.findViewById(R.id.recycle);


        if (ctx instanceof MainActivity) {
            ((MainActivity)ctx).showLoading();
        }
        songAdapter = new LocalAdapter(MediaPlayerService.localplaylists,ctx);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycle.setLayoutManager(mLayoutManager);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(songAdapter);

        admoblayout=view.findViewById(R.id.banner_container);
        Ads ads  = new Ads();
        Display display =getActivity().getWindowManager().getDefaultDisplay();
        ads.ShowBannerAds(ctx,admoblayout,Constants.getBannerfan(),Constants.getBanner(),display);





        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMusic();
    }

    public void getMusic(){

        MediaPlayerService.localplaylists.clear();
        recycle.removeAllViews();


        Uri allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor =  getContext().getContentResolver().query(allSongsUri, null, null, null, selection);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    LocalModel modalClass = new LocalModel();
                    modalClass.setFilename(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    modalClass.setFilepath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    modalClass.setSize(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                    modalClass.setDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    modalClass.setType("offline");
                    MediaPlayerService.localplaylists.add(modalClass);




                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        if (ctx instanceof MainActivity) {
            ((MainActivity)ctx).hideLoading();
        }
        songAdapter.notifyDataSetChanged();
    }

}