package com.franklinho.vidtrain_android.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.franklinho.vidtrain_android.activities.ProfileActivity;
import com.franklinho.vidtrain_android.models.VidTrain;
import com.franklinho.vidtrain_android.utilities.EndlessRecyclerViewScrollListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by rahul on 3/13/16.
 */
public class UserCreationsFragment extends VidTrainListFragment {

    String userId;
    ParseUser parseUser;

    public static UserCreationsFragment newInstance(String userId) {
        UserCreationsFragment userCreationsFragment = new UserCreationsFragment();
        Bundle args = new Bundle();
        args.putString(ProfileActivity.USER_ID, userId);
        userCreationsFragment.setArguments(args);
        return userCreationsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        userId = getArguments().getString(ProfileActivity.USER_ID);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvVidTrains.setLayoutManager(linearLayoutManager);
        rvVidTrains.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                requestVidTrains(false);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestVidTrains(true);
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userId);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                parseUser = user;
                requestVidTrains(true);
            }
        });

        requestVidTrains(true);
        return v;
    }


    @Override
    public void requestVidTrains(final boolean newTimeline) {
        super.requestVidTrains(newTimeline);
        final int currentSize;
        if (newTimeline) {
            vidTrains.clear();
            currentSize = 0;
        } else {
            currentSize = vidTrains.size();
        }

        if (parseUser != null) {
            ParseQuery<VidTrain> query = ParseQuery.getQuery("VidTrain");
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
            query.addDescendingOrder("createdAt");
            query.setSkip(currentSize);
            query.whereEqualTo("user", parseUser);
            query.setLimit(5);
            query.findInBackground(new FindCallback<VidTrain>() {
                @Override
                public void done(List<VidTrain> objects, ParseException e) {
                    swipeContainer.setRefreshing(false);
                    if (e == null) {
                        vidTrains.addAll(objects);
                        if (newTimeline) {
                            aVidTrains.notifyDataSetChanged();
                        } else {
                            aVidTrains.notifyItemRangeInserted(currentSize, vidTrains.size() - 1);
                        }
                    }
                }
            });
        }

    }


}
