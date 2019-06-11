package com.servedonline.bellandroidassignment.activities.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.servedonline.bellandroidassignment.R;
import com.servedonline.bellandroidassignment.activities.Entity.Status;

public class ViewTweetFragment extends Fragment {

    public static final String STATUS_KEY = "_statusKey";

    private TextView tvUserName, tvUserScreenName, tvUserDescription, tvUserLocation, tvUserFollowers, tvUserFollowing, tvUserTweets, tvUserLang,
            tvTweetCreated, tvTweetText, tvTweetCoordinates, tvTweetPlace, tvTweetRetweets, tvTweetFavorites;

    private Status status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = getLayoutInflater().inflate(R.layout.fragment_show_tweet, container, false);

        tvUserName = v.findViewById(R.id.tvUserName);
        tvUserScreenName = v.findViewById(R.id.tvUserScreenName);
        tvUserDescription = v.findViewById(R.id.tvUserdescription);
        tvUserLocation = v.findViewById(R.id.tvUserLocation);
        tvUserFollowers = v.findViewById(R.id.tvUserFollowers);
        tvUserFollowing = v.findViewById(R.id.tvUserFollowing);
        tvUserTweets = v.findViewById(R.id.tvUserTweets);
        tvUserLang = v.findViewById(R.id.tvUserLang);
        tvTweetCreated = v.findViewById(R.id.tvTweetCreatedAt);
        tvTweetText = v.findViewById(R.id.tvTweetText);
        tvTweetCoordinates = v.findViewById(R.id.tvTweetCoordinates);
        tvTweetPlace = v.findViewById(R.id.tvTweetPlace);
        tvTweetRetweets = v.findViewById(R.id.tvTweetRetweeted);
        tvTweetFavorites = v.findViewById(R.id.tvTweetFavorite);

        if (getArguments() != null) {
            status = getArguments().getParcelable(STATUS_KEY);

            tvUserName.setText(status.getUser().getName());
            tvUserScreenName.setText(status.getUser().getScreen_name());
            tvUserDescription.setText(status.getUser().getDescription());
            if (status.getUser().getLocation() != null) {
                tvUserLocation.setText(status.getUser().getLocation());
            }
            tvUserFollowers.setText(String.valueOf(status.getUser().getFollowers_count()));
            tvUserFollowing.setText(String.valueOf(status.getUser().getFriends_count()));
            tvUserTweets.setText(String.valueOf(status.getUser().getStatuses_count()));
            tvUserLang.setText(status.getUser().getLang());
            tvTweetCreated.setText(status.getCreated_at());
            tvTweetText.setText(status.getText());
            if (status.getCoordinates().getCoordinates() != null && status.getCoordinates().getCoordinates().length > 0) {
                tvTweetCoordinates.setText(status.getCoordinates().getCoordinates()[1] + ", " + status.getCoordinates().getCoordinates()[0]);
            }
            if (status.getPlace().getFull_name() != null) {
                tvTweetPlace.setText(status.getPlace().getFull_name());
            }
            tvTweetRetweets.setText(String.valueOf(status.getRetweet_count()));
            tvTweetFavorites.setText(String.valueOf(status.getFavorite_count()));

        }




        return v;
    }
}
