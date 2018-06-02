package com.homounikumus1.simpletwitterclient;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchTimelineFragment extends Fragment {


    public SearchTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        TwitterActivity.fragment = 4;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_twits, container, false);

        final SwipeRefreshLayout swipeLayout = rootView.findViewById(R.id.swipe_layout);

        Bundle arg = getArguments();
        String searchable = getString(R.string.hashtag);
        if (arg!=null) {
            searchable = arg.getString("searchable");
        }

        ListView listView = rootView.findViewById(R.id.recycler_view);


        SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(searchable)
                .maxItemsPerRequest(50)
                .build();

        final TweetTimelineListAdapter adapter =
                new TweetTimelineListAdapter.Builder(rootView.getContext())
                        .setTimeline(searchTimeline)
                        .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
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

    public static Fragment newInstance(String s) {
        Bundle args = new Bundle();
        args.putString("searchable", s);
        Fragment fragment = new SearchTimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
