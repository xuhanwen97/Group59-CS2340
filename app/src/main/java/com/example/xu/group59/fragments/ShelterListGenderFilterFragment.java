package com.example.xu.group59.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xu.group59.Adapters.GenderFilterAdapter;
import com.example.xu.group59.R;
import com.example.xu.group59.models.Shelter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShelterListGenderFilterFragment extends android.support.v4.app.Fragment implements GenderFilterAdapter.GenderFilterListener{

    Shelter.Gender[] genders;
    private RecyclerView mRecyclerView;
    public static final String TAG = "shelter_list_gender_filter_dialog_fragment";
    private ShelterListGenderFilterFragmentListener mListener;

    public ShelterListGenderFilterFragment() {
        // Required empty public constructor
    }

    public static ShelterListGenderFilterFragment newInstance(ShelterListGenderFilterFragmentListener listener) {

        Bundle args = new Bundle();

        ShelterListGenderFilterFragment fragment = new ShelterListGenderFilterFragment();
        fragment.setArguments(args);

        fragment.genders = Shelter.Gender.values();

        if (listener != null) {
            fragment.mListener = listener;
        }

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shelter_list_filter, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.filter_list_recycler_view);

        TextView filterTypeTextView = view.findViewById(R.id.filter_type_name_text_view);
        filterTypeTextView.setText(getString(R.string.title_gender_filter) + ": Click to clear filter");

        filterTypeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send it back to activity to clear filter
                mListener.onGenderFilterClicked(null);
            }
        });

        GenderFilterAdapter adapter = GenderFilterAdapter.newInstance(Shelter.Gender.values(), this);

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onGenderFilterClicked(Shelter.Gender gender) {
        mListener.onGenderFilterClicked(gender);
    }

    public interface ShelterListGenderFilterFragmentListener {
        void onGenderFilterClicked(Shelter.Gender gender);
    }
}

