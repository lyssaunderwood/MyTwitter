package com.codepath.apps.mysimpletweet.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//Parse the json + store the data, encapsulate any state logic or display logic
@Parcel
public class Tweet {
    // list the attributes
    public String getBody() {
        return body;
    }
    public long getUid() {
        return uid;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public User getUser() {
        return user;
    }

    public String body;
    public long uid; // unique id for the tweet
    public User user; // store imbedded user object
    public String createdAt;

    public Tweet() {
    }

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        // Extract values from Json, store them, and return
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return tweet;
    }

    // pass in array of items and it will output a list of tweets
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String returnDate;

        if (relativeDate.contains("minute")) {
            returnDate = relativeDate.substring(0, relativeDate.indexOf(" "));
            returnDate += "m";
        } else if (relativeDate.contains("second")) {
            returnDate = relativeDate.substring(0, relativeDate.indexOf(" "));
            returnDate += "s";
        } else if (relativeDate.contains("hour")) {
            returnDate = relativeDate.substring(0, relativeDate.indexOf(" "));
            returnDate += "h";
        } else if (relativeDate.contains("day")) {
            returnDate = relativeDate.substring(0, relativeDate.indexOf(" "));
            returnDate += "d";
        } else {
            returnDate = relativeDate;
        }

        return returnDate;
    }
}
