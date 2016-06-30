package com.codepath.apps.mysimpletweet.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweet.R;
import com.codepath.apps.mysimpletweet.TwitterApplication;
import com.codepath.apps.mysimpletweet.TwitterClient;
import com.codepath.apps.mysimpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchFragment extends TweetsListFragment {
    private TwitterClient client;
    String query;

    public static SearchFragment newInstance(String query) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("q", query);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query = getArguments().getString("q");
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    // make this populate timeline thing with the first line getting the query from here and passing it in


    private void populateTimeline() {
        client.searchTwitter(query, new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());
                // Json here
                // deserialize json
                // create models and add to adapter
                // load the model data into the list view
                JSONArray tweets = null;
                try {
                    tweets = json.getJSONArray("statuses");
                    addAll(Tweet.fromJSONArray(tweets));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.d("DEBUG", aTweets.toString());
            }

            // Failure

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}

