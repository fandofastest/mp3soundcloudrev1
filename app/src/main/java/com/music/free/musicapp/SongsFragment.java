package com.music.free.musicapp;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.music.free.adapter.SongAdapter;
import com.music.free.modalclass.SongModalClass;

import static com.music.free.musicapp.MediaPlayerService.listsong;

/**
 * Created by Remmss on 28-08-2017.
 */

public class SongsFragment extends Fragment {

    RecyclerView recycle;
    private SongAdapter songAdapter;
    LinearLayout admoblayout;
    Context ctx;
    String q;
    ImageView play,pause;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        q=Constants.q;
        ctx=getContext();

        recycle = view.findViewById(R.id.recycle);


        if (ctx instanceof MainActivity) {
            ((MainActivity)ctx).showLoading();
        }
        songAdapter = new SongAdapter(listsong,ctx);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycle.setLayoutManager(mLayoutManager);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(songAdapter);

        admoblayout=view.findViewById(R.id.banner_container);
        Ads ads  = new Ads();
        Display display =getActivity().getWindowManager().getDefaultDisplay();
        ads.ShowBannerAds(ctx,admoblayout,Constants.getBannerfan(),Constants.getBanner(),display);



        prepareMovieData();

        ((MainActivity)getActivity()).setFragmentRefreshListener(() -> {
            listsong.clear();
            recycle.removeAllViews();
            searchQuery(Constants.getQuerysearch());
            songAdapter.notifyDataSetChanged();


        });

        return view;
    }

    private void prepareMovieData() {
        gettopchart();
        songAdapter.notifyDataSetChanged();

    }



    public void searchQuery(String q){
        listsong.clear();
        recycle.removeAllViews();


        String url="https://api-v2.soundcloud.com/search/tracks?q="+q+"&client_id="+Constants.getKey()+"&limit=100";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, response -> {




            try {
                JSONArray jsonArray=response.getJSONArray("collection");



                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                    String id= jsonObject.getString("id");
                    String title = jsonObject.getString("title");
                    String imageurl= jsonObject.getString("artwork_url");
                    String duration = jsonObject.getString("full_duration");


                    SongModalClass songModalClass = new SongModalClass();
                    songModalClass.setSongName(title);
                    songModalClass.setDuration(duration);
                    songModalClass.setArtistName(q);
                    songModalClass.setId(id);
                    songModalClass.setImgurl(imageurl);
                    listsong.add(songModalClass);
                }





            } catch (JSONException e) {
                e.printStackTrace();
            }

            songAdapter.notifyDataSetChanged();
            if (ctx instanceof MainActivity) {
                ((MainActivity)ctx).hideLoading();
            }



        }, error -> Log.e("err",error.getMessage()));

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);


    }


    public void gettopchart(){
//        String url="https://api-v2.soundcloud.com/charts?charts-top:all-music&&high_tier_only=false&kind=top&limit=100&client_id=z7xDdzwjM6kB7fmXCd06c8kU6lFNtBCT";
        String url="https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Areggae&client_id="+Constants.getKey()+"&limit=100&offset=0";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                linearLayout.setVisibility(View.GONE);
//                System.out.println(response);


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


                        try {
                            JSONObject jsonArray3=jsonObject.getJSONObject("publisher_metadata");
                            listModalClass.setArtistName(jsonArray3.getString("artist"));

                        }
                        catch (JSONException e){
                            listModalClass.setArtistName("Artist");

                        }


                        listsong.add(listModalClass);
//



//                        Toast.makeText(getActivity(),id,Toast.LENGTH_LONG).show();


                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }
                songAdapter.notifyDataSetChanged();
                if (ctx instanceof MainActivity) {
                    ((MainActivity)ctx).hideLoading();
                }
//                songAdapter.notifyDataSetChanged();
                //    System.out.println("update"+listsongModalSearch);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(ctx).add(jsonObjectRequest);


    }


}