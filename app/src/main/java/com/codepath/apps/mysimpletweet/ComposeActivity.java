package com.codepath.apps.mysimpletweet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweet.Fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweet.models.Tweet;
import com.codepath.apps.mysimpletweet.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    TwitterClient client;
    User user;

    EditText etNewTweet;
    Button btnTweet;
    TextView tvHandle;
    TextView tvUserName;
    ImageView ivProfileImage;

    String newTweet;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient();
        // get account info
        client.getUserInfo(null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
                // my current user account's info
                getSupportActionBar().setTitle("");
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
                populateHeader(user);
            }
        });
    }

    private void populateHeader(User user) {
        etNewTweet = (EditText) findViewById(R.id.etNewTweet);
        btnTweet = (Button) findViewById(R.id.btnTweet);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvUserName.setText(user.getName());
        tvHandle.setText("@" + user.getScreenName());

        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

    public void onTweet(View view) {
        newTweet = etNewTweet.getText().toString();

        client.postTweet(newTweet, new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                tweet = Tweet.fromJSON(json); // Continue
                Intent k = new Intent();
                k.putExtra("new_tweet", Parcels.wrap(tweet));
                setResult(RESULT_OK, k);
                finish();
            }

            // Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });

    }
}