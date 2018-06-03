package com.homounikumus1.simpletwitterclient;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SomeUserTwitsFragment extends Fragment {
    private long userID;
    private String userName;


    public SomeUserTwitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        TwitterActivity.fragment = 2;
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(userName);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_twits, container, false);

        Bundle args = getArguments();
        if (args!=null) {
            userID = args.getLong("userID");
            userName = args.getString("userName");
        }

        final SwipeRefreshLayout swipeLayout = rootView.findViewById(R.id.swipe_layout);

        final ListView listView = rootView.findViewById(R.id.recycler_view);

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .userId(userID)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(rootView.getContext())
                .setTimeline(userTimeline)
                .build();

        listView.setAdapter(adapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Toast or some other action
                    }
                });
            }
        });

        return rootView;
    }

    public static Fragment newInstance(long id, String name) {
        Bundle args = new Bundle();
        args.putLong("userID", id);
        args.putString("userName", name);
        Fragment fragment = new SomeUserTwitsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}