package com.codepath.apps.mysimpletweet;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweet.Fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweet.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;

    @BindView(R.id.tvFullName) TextView tvName; //= (TextView) findViewById(R.id.tvFullName);
    @BindView(R.id.tvTagLine) TextView tvTagline; // = (TextView) findViewById(R.id.tvTagLine);
    @BindView(R.id.tvFollowers) TextView tvFollowers; // = (TextView) findViewById(R.id.tvFollowers);
    @BindView(R.id.tvFollowing) TextView tvFollowing; // = (TextView) findViewById(R.id.tvFollowing);
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage; // = (ImageView) findViewById(R.id.ivProfileImage);
    @BindView(R.id.ivBannerImage) ImageView ivBannerImage; // = (ImageView) findViewById(R.id.ivBannerImage);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        // get the screen name from activity that launches this
        String screenName = getIntent().getStringExtra("screen_name");

        client = TwitterApplication.getRestClient();
        // get account info
        client.getUserInfo(screenName, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
                // my current user account's info
                //getSupportActionBar().setTitle("@" + user.getScreenName());
                //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55acee")));
                getSupportActionBar().hide();
                populateProfileHeader(user);
            }
        });



        if (savedInstanceState == null) {
            // create the user timeline fragment
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            // display user fragment within the activity (dynamically)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
//        TextView tvName = (TextView) findViewById(R.id.tvFullName);
//        TextView tvTagline = (TextView) findViewById(R.id.tvTagLine);
//        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
//        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
//        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
//        ImageView ivBannerImage = (ImageView) findViewById(R.id.ivBannerImage);
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFriendsCount() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).transform(new RoundedCornersTransformation(4, 4)).into(ivProfileImage);
        Picasso.with(this).load(user.getBannerImageUrl()).into(ivBannerImage);
    }
}
