package com.quarklabs.volo;

import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.WindowManager;

import com.quarklabs.volo.core.animations.ZoomOutPageTransformer;
import com.quarklabs.volo.core.fragments.HowToPlayFragment;
import com.quarklabs.volo.core.fragments.ModesFragment;
import com.quarklabs.volo.core.fragments.UtilitiesFragment;

public class HowToPlay extends FragmentActivity {

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_how_to_play);

        // Instantiate a ViewPager and a PagerAdapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private class PagerAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_PAGES = 3;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return new HowToPlayFragment();
            }else if (position == 1){
                return new ModesFragment();
            }else{
                return new UtilitiesFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
