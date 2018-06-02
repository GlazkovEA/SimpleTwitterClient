package com.homounikumus1.simpletwitterclient;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SomeUserTwitsFragment extends Fragment {
    private long userID;


    public SomeUserTwitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        TwitterActivity.fragment = 3;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_twits, container, false);

        final SwipeRefreshLayout swipeLayout = rootView.findViewById(R.id.swipe_layout);

        Bundle args = getArguments();
        if (args!=null) {
            userID = args.getLong("userID");
        }

        ListView listView = rootView.findViewById(R.id.recycler_view);

        UserTimeline userTimeline = new UserTimeline.Builder()
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

        ProgressBar bar = rootView.findViewById(R.id.progress_bar);
        bar.setVisibility(View.GONE);

        return rootView;
    }

    public static Fragment newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong("userID", id);
        Fragment fragment = new SomeUserTwitsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}