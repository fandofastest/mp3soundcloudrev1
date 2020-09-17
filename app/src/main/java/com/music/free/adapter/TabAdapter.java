package com.music.free.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.music.free.musicapp.GenreFragment;
import com.music.free.musicapp.LocalFragment;
import com.music.free.musicapp.MainActivity;
import com.music.free.musicapp.SearchFragment;
import com.music.free.musicapp.SongsFragment;


/**
 * Created by praja on 06-06-17.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

   static final int PAGE_COUNT =4 ;
    private String tabTitles[] = new String[] { "TOP SONGS", "SEARCH","GENRE","LOCAL"};



    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new SongsFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new GenreFragment();
            case 3:
                return new LocalFragment();


            default:



        }

        return null;


    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

}