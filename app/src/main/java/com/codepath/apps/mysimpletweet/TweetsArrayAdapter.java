package com.codepath.apps.mysimpletweet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweet.models.Tweet;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

// Taking the tweet objects and turning them into views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // overrride and setup custom template
    // be sure to do viewholder pattern later

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the tweet
        final Tweet tweet = getItem(position);
        // find or inflate the template
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // find the subviews and fill with data
        final ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvHandle = (TextView) convertView.findViewById(R.id.tvHandle);
        TextView tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
        Button btnReply = (Button) convertView.findViewById(R.id.btnReply);
 //       TextView tvFavorites = (TextView) convertView.findViewById(R.id.tvFavorites);
 //       TextView tvRetweets = (TextView) convertView.findViewById(R.id.tvRetweets);
        // populate subviews
        //Log.d("HELP", tweet.getRelativeTimeAgo(tweet.getCreatedAt()));
        tvTimestamp.setText(tweet.getRelativeTimeAgo(tweet.getCreatedAt()));
        tvHandle.setText("@" + tweet.getUser().getScreenName());
        tvUserName.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        //Toast.makeText(getContext(), String.valueOf(tweet.getFavCount()), Toast.LENGTH_LONG).show();
//        if (String.valueOf(tweet.getFavCount()) != "0") {
//            tvFavorites.setText("10");
//        }
//        if (String.valueOf(tweet.getRetweets()) != "0") {
//            tvRetweets.setText("10");
//        }
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        // Click on profile image
        ivProfileImage.setTag(tweet.getUser().getScreenName());

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfile = new Intent(getContext(), ProfileActivity.class);
                toProfile.putExtra("screen_name", (String) ivProfileImage.getTag());
                getContext().startActivity(toProfile);
            }
        });

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long replyTo = tweet.getUid();
                String usernameReply = ("@" + tweet.getUser().getScreenName());
                Intent compose = new Intent(getContext(), ComposeActivity.class);
                compose.putExtra("name", usernameReply);
                compose.putExtra("reply", replyTo);
                getContext().startActivity(compose);
            }
        });

        //return
        return convertView;
    }

}
