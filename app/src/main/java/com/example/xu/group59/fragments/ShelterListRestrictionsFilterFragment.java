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

import com.example.xu.group59.Adapters.RestrictionsFilterAdapter;
import com.example.xu.group59.R;
import com.example.xu.group59.models.Shelter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShelterListRestrictionsFilterFragment extends android.support.v4.app.Fragment
        implements RestrictionsFilterAdapter.RestrictionsFilterListener{

    private Shelter.Restrictions[] restrictions;
    private RecyclerView mRecyclerView;
    public static final String TAG = "shelter_list_Restrictions_filter_dialog_fragment";
    private ShelterListRestrictionsFilterFragmentListener mListener;

    /**
     * A required constructor for the Shelter List Restrictions Filter Fragment.
     */
    public ShelterListRestrictionsFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of the Shelter List Restrictions Filter Fragment. Sets the args,
     * restrictions, and listener attributes.
     * @param listener tracks changes for the fragment
     * @return a new instance of the Shelter List Restrictions Filter Fragment
     */
    public static ShelterListRestrictionsFilterFragment newInstance(ShelterListRestrictionsFilterFragmentListener listener) {

        Bundle args = new Bundle();

        ShelterListRestrictionsFilterFragment fragment = new ShelterListRestrictionsFilterFragment();
        fragment.setArguments(args);

        fragment.restrictions = Shelter.Restrictions.values();

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

        mRecyclerView = view.findViewById(R.id.filter_list_recycler_view);

        TextView filterTypeTextView = view.findViewById(R.id.filter_type_name_text_view);
        filterTypeTextView.setText(getString(R.string.title_restrictions_filter) + ": Click to clear filter");

        filterTypeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send it back to activity to clear filter
                mListener.onRestrictionsFilterClicked(null);
            }
        });

        RestrictionsFilterAdapter adapter = RestrictionsFilterAdapter.newInstance(Shelter.Restrictions.values(), this);

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onRestrictionsFilterClicked(Shelter.Restrictions restriction) {
        mListener.onRestrictionsFilterClicked(restriction);
    }

    /**
     * Interface for the Shelter List Restrictions Filter Fragment Listener. Contains a method to
     * let the listener know when a restrictions filter in the list has been clicked on.
     */
    public interface ShelterListRestrictionsFilterFragmentListener {
        void onRestrictionsFilterClicked(Shelter.Restrictions restriction);
    }
}
