package com.example.xu.group59.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xu.group59.R;
import com.example.xu.group59.models.Shelter;

public class ShelterInformationFragment extends android.support.v4.app.Fragment {

    private Shelter shelter;

    private TextView shelterNameTextView;
    private TextView addressTextView;
    private TextView phoneNumberTextView;
    private TextView capacityTextView;
    private TextView restrictionsTextView;
    private TextView specialNotesTextView;

    public static ShelterInformationFragment newInstance(Shelter shelter) {
        Bundle args = new Bundle();
        
        ShelterInformationFragment fragment = new ShelterInformationFragment();
        fragment.setArguments(args);

        fragment.shelter = shelter;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shelter_information, container, false);

        shelterNameTextView = view.findViewById(R.id.shelter_name_text_view);
        addressTextView = view.findViewById(R.id.shelter_address_text_view);
        phoneNumberTextView = view.findViewById(R.id.shelter_phone_number_text_view);
        capacityTextView = view.findViewById(R.id.shelter_capacity_text_view);
        restrictionsTextView = view.findViewById(R.id.shelter_restrictions_text_view);
        specialNotesTextView = view.findViewById(R.id.shelter_special_notes_text_view);

        shelterNameTextView.setText(shelter.getShelterName());
        addressTextView.setText(shelter.getAddress());
        phoneNumberTextView.setText(shelter.getPhoneNumber());
        capacityTextView.setText(shelter.getCapacity());
        restrictionsTextView.setText(shelter.getRestrictions());
        specialNotesTextView.setText(shelter.getSpecialNotes());

        return view;
    }

}
