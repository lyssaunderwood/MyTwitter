package com.codepath.apps.mysimpletweet;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweet.Fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweet.Fragments.SearchFragment;
import com.codepath.apps.mysimpletweet.Fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweet.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55acee")));

        //searchFragment = new SearchFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.miClickToSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
            public boolean onQueryTextSubmit(String query) {
             FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
             SearchFragment searchFragment = SearchFragment.newInstance(query);
             ft.replace(R.id.frame_layout, searchFragment);
             ft.commit();

             searchView.clearFocus();

             return true;
            }

         @Override
            public boolean onQueryTextChange(String newText) {
                return false;
             }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public void onReturn(MenuItem item) {
        finish();
    }
}
