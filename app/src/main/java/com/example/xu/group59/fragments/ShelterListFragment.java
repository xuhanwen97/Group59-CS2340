package com.example.xu.group59.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xu.group59.Adapters.ShelterListFirebaseRecyclerViewAdapter;
import com.example.xu.group59.Adapters.ShelterListRecyclerViewAdapter;
import com.example.xu.group59.R;
import com.example.xu.group59.models.Shelter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ShelterListFragment extends android.support.v4.app.Fragment implements ShelterListRecyclerViewAdapter.ShelterListRecyclerViewListener
{

    // TODO: 2/28/18 Add loading circle when first loading data
    // TODO: 2/28/18 Add loading circle when refreshing list 

    public static final String TAG = "shelter_list_fragment";

    private ShelterListFragmentListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private Query query;

    private List<Shelter> shelterList;

    public static ShelterListFragment newInstance(ShelterListFragmentListener listener) {

        return newInstanceWithQuery(listener, null);
    }

    public static ShelterListFragment newInstance(ShelterListFragmentListener listener, List<Shelter> shelterList) {

        ShelterListFragment fragment = newInstanceWithQuery(listener, null);
        fragment.shelterList = shelterList;

        return fragment;
    }

    private static ShelterListFragment newInstanceWithQuery(ShelterListFragmentListener listener, Query query) {

        Bundle args = new Bundle();

        ShelterListFragment fragment = new ShelterListFragment();
        fragment.setArguments(args);

        if (listener != null) {
            fragment.mListener = listener;
        }

        if (query != null) {
            fragment.query = query;
        }

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shelter_list, container, false);

        mRecyclerView = view.findViewById(R.id.shelter_list_recycler_view);

        //Query to be created below. It seems it needs to be all one line
        Query query;

        if (this.query != null) {
            query = this.query;

            ArrayList<String> temp = new ArrayList<>(10);

            FirebaseRecyclerOptions<Shelter> options = new FirebaseRecyclerOptions.Builder<Shelter>()
                    .setQuery(query, new SnapshotParser<Shelter>() {
                        @NonNull
                        @Override
                        public Shelter parseSnapshot(@NonNull DataSnapshot snapshot) {
                            return new Shelter(snapshot);
                        }
                    })
                    .build();

            mAdapter = ShelterListFirebaseRecyclerViewAdapter.newInstance(this, options);
        }
        //If no query given, use the given shelterList
        else {
            if (shelterList == null) {
                return view;
            }

            mAdapter = ShelterListRecyclerViewAdapter.newInstance(this, shelterList);

        }

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAdapter instanceof FirebaseRecyclerAdapter) {
            ((FirebaseRecyclerAdapter) mAdapter).startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter instanceof FirebaseRecyclerAdapter) {
            ((FirebaseRecyclerAdapter) mAdapter).stopListening();
        }
    }



    @Override
    public void onShelterClicked(Shelter shelter) {
        if (shelter != null) {
            mListener.onShelterClicked(shelter);
        } else {
            Toast.makeText(this.getActivity(), "Incorrect shelter data", Toast.LENGTH_SHORT).show();
        }
    }

    public interface ShelterListFragmentListener {
        void onShelterClicked(Shelter shelter);
    }
}
