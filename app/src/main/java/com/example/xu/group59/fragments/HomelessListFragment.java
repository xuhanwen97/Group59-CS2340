package com.example.xu.group59.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xu.group59.Adapters.HomelessListRecyclerViewAdapter;
import com.example.xu.group59.R;
import com.example.xu.group59.Utils.ToastUtils;
import com.example.xu.group59.models.HomelessPerson;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomelessListFragment extends android.support.v4.app.Fragment implements HomelessListRecyclerViewAdapter.HomelessListRecyclerViewListener {

    public static final String TAG = "homeless_list_fragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<HomelessPerson> homelessPersonList;

    public HomelessListFragment() {
        // Required empty public constructor
    }

    public static HomelessListFragment newInstance(List<HomelessPerson> homelessList) {

        HomelessListFragment fragment = new HomelessListFragment();

        fragment.homelessPersonList = homelessList;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homeless_list, container, false);

        mRecyclerView = view.findViewById(R.id.homeless_list_recycler_view);

        if (homelessPersonList == null) {
            return view;
        }

        mAdapter = HomelessListRecyclerViewAdapter.newInstance(this, homelessPersonList);

        mRecyclerView.setAdapter(mAdapter);

        if (getActivity() != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL));
        }

        return view;
    }

    //Listener

    @Override
    public void onHomelessBanClicked(HomelessPerson hp) {
        DatabaseReference homelessPersonReference = FirebaseDatabase.getInstance()
                .getReference(HomelessPerson.homelessPersonKey)
                .child(hp.getUserLogin())
                .child("status");

        Map<String, Object> homelessPersonUpdates = new HashMap<>();

        homelessPersonUpdates.put("Blocked", true);
        homelessPersonUpdates.put("Active", null);

        homelessPersonReference.updateChildren(homelessPersonUpdates);


        int indexOfHp = homelessPersonList.indexOf(hp);

        // Homeless Person exists in list
        if (indexOfHp != -1) {
            List<HomelessPerson.UserStatus> userStatuses = new ArrayList<>();
            userStatuses.add(HomelessPerson.UserStatus.Blocked);

            homelessPersonList.get(indexOfHp).setUserStatus(userStatuses);

            mAdapter.notifyItemChanged(indexOfHp);
        }


    }

    @Override
    public void onHomelessUnbanClicked(HomelessPerson hp) {
        DatabaseReference homelessPersonReference = FirebaseDatabase.getInstance()
                .getReference(HomelessPerson.homelessPersonKey)
                .child(hp.getUserLogin())
                .child("status");

        Map<String, Object> homelessPersonUpdates = new HashMap<>();

        homelessPersonUpdates.put("Blocked", null);
        homelessPersonUpdates.put("Active", true);

        homelessPersonReference.updateChildren(homelessPersonUpdates);


        int indexOfHp = homelessPersonList.indexOf(hp);

        // Homeless Person exists in list
        if (indexOfHp != -1) {
            List<HomelessPerson.UserStatus> userStatuses = new ArrayList<>();
            userStatuses.add(HomelessPerson.UserStatus.Active);

            homelessPersonList.get(indexOfHp).setUserStatus(userStatuses);

            mAdapter.notifyItemChanged(indexOfHp);
        }
    }
}
