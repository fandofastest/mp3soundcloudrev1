package com.music.free.musicapp;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.music.free.adapter.SongAdapter;
import com.music.free.modalclass.SongModalClass;

import static com.music.free.musicapp.MediaPlayerService.listsongModalSearch;

/**
 * Created by Remmss on 28-08-2017.
 */

public class SearchFragment extends Fragment {

    RecyclerView recycle;
    private SongAdapter songAdapter;

    SearchView searchView;
    Context ctx;
    String q;
    ImageView play,pause;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        ctx=getContext();

        recycle = view.findViewById(R.id.recycle);
        searchView =view.findViewById(R.id.searchview);




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                listsongModalSearch.clear();

                getsongs(query,"search");

                songAdapter.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        songAdapter = new SongAdapter(listsongModalSearch,ctx);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycle.setLayoutManager(mLayoutManager);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(songAdapter);






        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (MainActivity.postab==1){
            getsongs(MainActivity.noworigingenre,"genre");
        }

    }

    public void getsongs(final String q, final String type){
        listsongModalSearch.clear();
        recycle.removeAllViews();
        String url;
        if (type.equals("genre")){
            url="https://api-v2.soundcloud.com/charts?genre=soundcloud:genres:"+q+"&high_tier_only=false&kind=top&limit=100&client_id="+Constants.getKey();
        }
        else{
            url="https://api-v2.soundcloud.com/search/tracks?q="+q+"&client_id="+Constants.getKey()+"&limit=100";

        }
        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                linearLayout.setVisibility(View.GONE);
//            Log.e("url",url);
                if (type.equals("genre")){
                    try {
                        JSONArray jsonArray1=response.getJSONArray("collection");

                        for (int i = 0;i<jsonArray1.length();i++){
                            JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                            JSONObject jsonObject=jsonObject1.getJSONObject("track");
                            SongModalClass listModalClass = new SongModalClass();
                            listModalClass.setId(jsonObject.getString("id"));
                            listModalClass.setSongName(jsonObject.getString("title"));
                            listModalClass.setImgurl(jsonObject.getString("artwork_url"));
                            listModalClass.setDuration(jsonObject.getString("full_duration"));
                            listModalClass.setType("online");
                            listModalClass.setArtistName(q);
                            listsongModalSearch.add(listModalClass);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}

                else if (type.equals("search")){
                    try {
                        JSONArray jsonArray1=response.getJSONArray("collection");

                        for (int i = 0;i<jsonArray1.length();i++){
                            JSONObject jsonObject=jsonArray1.getJSONObject(i);

                            String id= jsonObject.getString("id");
                            String title = jsonObject.getString("title");
                            String imageurl= jsonObject.getString("artwork_url");
                            String duration = jsonObject.getString("full_duration");


                            SongModalClass songModalClass = new SongModalClass();
                            songModalClass.setSongName(title);
                            songModalClass.setDuration(duration);
                            songModalClass.setId(id);
                            songModalClass.setImgurl(imageurl);

                            try {
                                JSONObject jsonArray3=jsonObject.getJSONObject("publisher_metadata");
                                songModalClass.setArtistName(jsonArray3.getString("artist"));

                            }
                            catch (JSONException e){
                                songModalClass.setArtistName("Artist");

                            }
                            listsongModalSearch.add(songModalClass);



                        }





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



                songAdapter.notifyDataSetChanged();
//                songAdapter.notifyDataSetChanged();
                //    System.out.println("update"+listsongModalSearch);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);


    }

//    public void searchSong(String q){
//        listsongModalSearch.clear();
//        recycle.removeAllViews();
//
//
//        String url="https://api-v2.soundcloud.com/search/tracks?q="+q+"&client_id="+Constants.getKey()+"&limit=100";
//
//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//
//
//
//
//            try {
//                JSONArray jsonArray=response.getJSONArray("collection");
//
//
//
//                for (int i = 0;i<jsonArray.length();i++){
//                    JSONObject jsonObject=jsonArray.getJSONObject(i);
//
//                    String id= jsonObject.getString("id");
//                    String title = jsonObject.getString("title");
//                    String imageurl= jsonObject.getString("artwork_url");
//                    String duration = jsonObject.getString("full_duration");
//
//
//                    SongModalClass songModalClass = new SongModalClass();
//                    songModalClass.setSongName(title);
//                    songModalClass.setDuration(duration);
//                    songModalClass.setArtistName(q);
//                    songModalClass.setId(id);
//                    songModalClass.setImgurl(imageurl);
//                    listsongModalSearch.add(songModalClass);
//
//                }
//
//
//
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            songAdapter.notifyDataSetChanged();
//            if (ctx instanceof MainActivity) {
//                ((MainActivity)ctx).hideLoading();
//            }
//
//
//
//        }, error -> Log.e("err", Objects.requireNonNull(error.getMessage())));
//
//        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(jsonObjectRequest);
//
//
//    }


}