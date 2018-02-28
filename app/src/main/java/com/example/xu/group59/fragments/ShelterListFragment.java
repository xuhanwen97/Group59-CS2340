package com.example.xu.group59.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xu.group59.Adapters.ShelterListRecyclerViewAdapter;
import com.example.xu.group59.R;
import com.example.xu.group59.Utils.FirebaseUtils;
import com.example.xu.group59.models.Shelter;

import java.util.ArrayList;
import java.util.List;

public class ShelterListFragment extends android.support.v4.app.Fragment implements FirebaseUtils.FirebaseUtilsListener,
        ShelterListRecyclerViewAdapter.ShelterListRecyclerViewListener
{

    // TODO: 2/28/18 Add loading circle when first loading data
    // TODO: 2/28/18 Add loading circle when refreshing list 

    public static final String TAG = "shelter_list_fragment";

    private ShelterListFragmentListener mListener;

    private RecyclerView mRecyclerView;

    private List<Shelter> shelterList;

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

        shelterList = new ArrayList<>(10);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.shelter_list_recycler_view);
        ShelterListRecyclerViewAdapter adapter = new ShelterListRecyclerViewAdapter(shelterList, this);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        FirebaseUtils.updateShelterList(this);

        return view;
    }

    //region [ FirebaseUtilsListener ] ================================= //

    @Override
    public void onDataLoadComplete(Object loadedData) {

        //Add error check for null loaded Data
        shelterList = (List<Shelter>) loadedData;
        ShelterListRecyclerViewAdapter adapter = new ShelterListRecyclerViewAdapter(shelterList, this);
        mRecyclerView.swapAdapter(adapter, true);
    }

    @Override
    public void onDataLoadError(String errorMessage) {

    }

    //endregion

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
