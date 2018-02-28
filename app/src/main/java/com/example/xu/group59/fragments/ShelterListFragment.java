package com.example.xu.group59.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xu.group59.Adapters.ShelterListRecyclerViewAdapter;
import com.example.xu.group59.R;
import com.example.xu.group59.models.Shelter;

import java.util.ArrayList;

public class ShelterListFragment extends android.support.v4.app.Fragment {

    public static final String TAG = "shelter_list_fragment";

    private ShelterListFragmentListener mListener;

    private RecyclerView mRecyclerView;

    public static ShelterListFragment newInstance(ShelterListFragmentListener listener) {

        Bundle args = new Bundle();

        ShelterListFragment fragment = new ShelterListFragment();
        fragment.setArguments(args);

        if (listener != null) {
            fragment.mListener = listener;
        }

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shelter_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.shelter_list_recycler_view);
        ArrayList<Shelter> shelterList = new ArrayList<>(3);
        shelterList.add(new Shelter("TempShelter"));
        shelterList.add(new Shelter("TempShelter2"));
        ShelterListRecyclerViewAdapter adapter = new ShelterListRecyclerViewAdapter(shelterList);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public interface ShelterListFragmentListener {
    }
}
