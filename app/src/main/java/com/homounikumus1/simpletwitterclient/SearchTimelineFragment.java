package com.homounikumus1.simpletwitterclient;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.twitter.sdk.android.tweetui.SearchTimeline;
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

        TweetTimelineListAdapter adapter =
                new TweetTimelineListAdapter.Builder(rootView.getContext())
                        .setTimeline(searchTimeline)
                        .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                        .build();

        listView.setAdapter(adapter);
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
