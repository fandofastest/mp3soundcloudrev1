package com.music.free.musicapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.music.free.adapter.GenreAdapter;
import com.music.free.modalclass.GenreModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GenreAdapter mAdapter;
    Context context;
    List<GenreModel> listgenre = new ArrayList<>();
    public GenreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenreFragment newInstance(String param1, String param2) {
        GenreFragment fragment = new GenreFragment();
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
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_genre, container, false);
        context=getContext();

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(true);


        //set data and list adapter
        mAdapter = new GenreAdapter(getActivity(), listgenre);
        recyclerView.setAdapter(mAdapter);
        getallgenre();
        // on item list clicked
        mAdapter.setOnItemClickListener(new GenreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, GenreModel obj, int position) {

                GenreModel genreModel = listgenre.get(position);
                ((MainActivity)context).gototab(1);

                MainActivity.nowgenre=genreModel.getGenrename();
                MainActivity.noworigingenre=genreModel.getOrigenrename();




            }
        });

        return root;
    }

    public void  getallgenre (){
        listgenre.clear();
        String [] genrelist ={"Alternative Rock","Ambient","Audiobooks","Business","Classical","Comedy","Country","Dance & EDM","Dancehall","Deep House","Disco","Drum & Bass","Dubstep","Electronic","Entertainment","Folk & Singer-Songwriter","Hip Hop & Rap","House","Indie","Jazz & Blues","Latin","Learning","Metal","News & Politics","Piano","Pop","R&B & Soul","Reggae","Reggaeton","Religion & Spirituality","Rock","Science","Soundtrack","Sports","Storytelling","Techno","Technology","Trance","Trap","Trending Audio","Trending Music","Trip Hop","World"};

        for (int i = 0; i < genrelist.length; i++) {
            listgenre.add(new GenreModel(genrelist[i]));

        }


        if (getContext() instanceof MainActivity) {
            ((MainActivity)getContext()).hideLoading();
        }

        mAdapter.notifyDataSetChanged();


    }
}