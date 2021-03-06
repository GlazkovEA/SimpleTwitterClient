package com.homounikumus1.simpletwitterclient;

import android.app.FragmentTransaction;
import android.content.Context;
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
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTimelineFragment extends Fragment {

    public HomeTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        TwitterActivity.fragment = 1;
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(getString(R.string.home));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_twits, container, false);
        final ProgressBar bar = rootView.findViewById(R.id.progress_bar);

        final SwipeRefreshLayout swipeLayout = rootView.findViewById(R.id.swipe_layout);

        final ListView listView = rootView.findViewById(R.id.recycler_view);

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        statusesService.homeTimeline(200,
                null,null,
                null,null,null,
                null).enqueue(new Callback<List<Tweet>>() {

            @Override
            public void success(Result<List<Tweet>> listResult) {

                final FixedTweetTimeline userTimeline = new FixedTweetTimeline.Builder()
                        .setTweets(listResult.data)
                        .build();

                final CustomTweetTimelineListAdapter adapter = new CustomTweetTimelineListAdapter(rootView.getContext(), userTimeline);
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

                bar.setVisibility(View.GONE);
            }

            @Override
            public void failure(TwitterException e) {
                // Log.e("Error","Error");
            }
        });




        return rootView;
    }

    class CustomTweetTimelineListAdapter extends TweetTimelineListAdapter {

        CustomTweetTimelineListAdapter(Context context, Timeline<Tweet> timeline) {
            super(context, timeline);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = super.getView(position, convertView, parent);
            view.setEnabled(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Tweet tweet = getItem(position);
                    User user = tweet.user;
                    Fragment fragment = SomeUserTwitsFragment.newInstance(user.getId()
                    , user.name);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fragment, "visible_fragment");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            });
            return view;
        }
    }
}


