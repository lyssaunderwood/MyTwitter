package com.codepath.apps.mysimpletweet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    TextView tvCount;

    String newTweet;
    Tweet tweet;
    int tweetCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55acee")));

        client = TwitterApplication.getRestClient();
        // get account info
        client.getUserInfo(null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
                // my current user account's info
                populateHeader(user);
            }
        });

        tweetCount = 140;
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvCount.setText(String.valueOf(tweetCount));
        etNewTweet = (EditText) findViewById(R.id.etNewTweet);
        etNewTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
                //Toast.makeText(getApplicationContext(), String.valueOf(count), Toast.LENGTH_SHORT).show();
                tweetCount = 140 - (s.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                //tvCount.setText();
                tvCount.setText(String.valueOf(tweetCount));
                if (tweetCount < 0) {
                    tvCount.setTextColor(Color.parseColor("#FF0000"));
                    btnTweet.setEnabled(false);
                } else {
                    tvCount.setTextColor(Color.parseColor("#A9A9A9"));
                    btnTweet.setEnabled(true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    public void onClickExit(MenuItem mi) {
        finish();
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