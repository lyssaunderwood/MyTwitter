package com.codepath.apps.mysimpletweet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweet.Fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweet.Fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweet.Fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweet.models.Tweet;
import com.codepath.apps.mysimpletweet.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 20;

    HomeTimelineFragment homeTimeline;
    MentionsTimelineFragment mentions;
    Tweet tweetToRefresh;
    //String addQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55acee")));

        homeTimeline = new HomeTimelineFragment();
        mentions = new MentionsTimelineFragment();

        // get the viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        // set the viewpager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // find the sliding tabStrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // attach the tabStrip to the viewpager
        tabStrip.setViewPager(vpPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem mi) {
        // launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void onCompose(MenuItem item) {
        Intent j = new Intent(this, ComposeActivity.class);
        long reply = 0;
        String name = "";
        j.putExtra("name", name);
        j.putExtra("reply", reply);
        startActivityForResult(j, REQUEST_CODE);
    }

    public void onSearch(MenuItem item) {
        //Toast.makeText(getApplicationContext(), "Yay", Toast.LENGTH_SHORT).show();
        Intent search = new Intent(this, SearchActivity.class);
        startActivity(search);
    }

    // return the order of the fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // order and creation of fragments
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return homeTimeline;
            } else if (position == 1) {
                return mentions;
            } else {
                return null;
            }
        }


        //returns tab title at top
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        // how many titles to tab between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            tweetToRefresh = (Tweet) Parcels.unwrap(data.getParcelableExtra("new_tweet"));
            homeTimeline.addTweet(tweetToRefresh);
        }
    }
}
