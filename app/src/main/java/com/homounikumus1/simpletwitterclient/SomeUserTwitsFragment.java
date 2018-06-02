package com.homounikumus1.simpletwitterclient;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
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

        Bundle args = getArguments();
        if (args!=null) {
            userID = args.getLong("userID");
        }

        ListView listView = rootView.findViewById(R.id.recycler_view);

        UserTimeline userTimeline = new UserTimeline.Builder()
                .userId(userID)
                .build();
        TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(rootView.getContext())
                .setTimeline(userTimeline)
                .build();

        listView.setAdapter(adapter);

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