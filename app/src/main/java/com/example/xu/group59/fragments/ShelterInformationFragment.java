package com.example.xu.group59.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xu.group59.R;
import com.example.xu.group59.Utils.StringUtils;
import com.example.xu.group59.Utils.ToastUtils;
import com.example.xu.group59.models.HomelessPerson;
import com.example.xu.group59.models.Shelter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class for shelter information fragment with attributes for the shelter visualization.
 */
public class ShelterInformationFragment extends android.support.v4.app.Fragment {

    public static final String TAG = "shelter_information_fragment";

    private Shelter shelter;
    private HomelessPerson loggedInPerson;
    private int numberUserReseved;
    private int numberVacancies = 0;

    private TextView shelterNameTextView;
    private TextView addressTextView;
    private TextView phoneNumberTextView;
    private TextView capacityTextView;
    private TextView restrictionsTextView;
    private TextView specialNotesTextView;
    private EditText shelterUserReservedAmountEditText;
    private Button reserveSpotsButton;

    /**
     * Creates an instance of shelter information fragment. Sets attributes for shelter, and loggged
     * in person. Sets arguments.
     * @param shelter the shelter for the new instance
     * @param loggedInPerson the logged in person viewing the instance
     * @return the new instance of shelter information fragment
     */
    public static ShelterInformationFragment newInstance(Shelter shelter, HomelessPerson loggedInPerson) {
        Bundle args = new Bundle();
        
        ShelterInformationFragment fragment = new ShelterInformationFragment();
        fragment.setArguments(args);

        fragment.shelter = shelter;
        fragment.loggedInPerson = loggedInPerson;
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

        shelterUserReservedAmountEditText = view.findViewById(R.id.shelter_user_reserved_amount_text_view);
        reserveSpotsButton = view.findViewById(R.id.reserve_spots_button);
        reserveSpotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateShelterVacancies();
            }
        });

        getShelterVacancies();
        populateShelterFields(shelter);
        populateUserFields();

        return view;
    }

    private void getShelterVacancies() {
        DatabaseReference shelterOccupancyRef = FirebaseDatabase.getInstance()
                .getReference(Shelter.shelterOccupancyKey)
                .child(shelter.getShelterKey());

        shelterOccupancyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap occupantData = (HashMap) dataSnapshot.getValue();
                if (occupantData != null) {
                    numberVacancies = shelter.getCapacity() - ((Long)occupantData.get(Shelter.shelterOccupancyTotalReservedKey)).intValue();
                } else {
                    throw new IllegalArgumentException("Data snapshot does not contain data for occupants");
                }
                capacityTextView.setText(String.format(Locale.ENGLISH,"%d/%d", numberVacancies, shelter.getCapacity()));


//                int total_reserved_count = 0;
//
//                for (Object key: occupantData.keySet()) {
//                    total_reserved_count += ((Long)occupantData.get(key)).intValue();
//                }
//
//                numberVacancies = shelter.getCapacity() - total_reserved_count;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void updateShelterVacancies() {
        DatabaseReference shelterOccupancyRef = FirebaseDatabase.getInstance()
                .getReference(Shelter.shelterOccupancyKey)
                .child(shelter.getShelterKey());

        Map<String, Object> occupancyUpdates = new HashMap<>();
        int userVacancies = getNumberReserved();
        if (userVacancies == numberUserReseved) {
            shelterUserReservedAmountEditText.setText(String.format(Locale.ENGLISH, "%d", userVacancies));
        } else {
            numberVacancies += numberUserReseved - userVacancies;

            numberUserReseved = userVacancies;

            occupancyUpdates.put(loggedInPerson.getUserLogin(), userVacancies);
            occupancyUpdates.put(Shelter.shelterOccupancyTotalReservedKey, shelter.getCapacity() - numberVacancies);

            shelterOccupancyRef.updateChildren(occupancyUpdates);
        }
    }

    private int getNumberReserved() {
        String textToParse = shelterUserReservedAmountEditText.getText().toString();

        if (StringUtils.isNullOrEmpty(textToParse)) {
            return 0;
        } else {
            int numberParsed;
            try {
                numberParsed = Integer.parseInt(textToParse);
            } catch (Exception e) {
                ToastUtils.shortToastCenter(getContext(), "Please enter a whole number").show();
                return numberUserReseved;
            }

            if ((numberParsed < 0) || (numberParsed > Shelter.singleUserMaxReservation)) {
                ToastUtils.shortToastCenter(getContext(), "Do not enter a number " +
                        "smaller than 0 or larger than " + Shelter.singleUserMaxReservation).show();

                return numberUserReseved;
            }

            if (numberParsed > numberVacancies) {
                ToastUtils.shortToastCenter(getContext(), "There are not enough vacancies").show();

                return numberUserReseved;
            }

            return numberParsed;
        }
    }

    private void populateUserFields() {
        DatabaseReference shelterOccupancyRef = FirebaseDatabase.getInstance()
                .getReference(Shelter.shelterOccupancyKey)
                .child(shelter.getShelterKey())
                .child(loggedInPerson.getUserLogin());

        shelterOccupancyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // user has nothing reserved
                if (dataSnapshot.getValue() == null) {
                    numberUserReseved = 0;
                    shelterUserReservedAmountEditText.setText("0");
                } else {
                    numberUserReseved = ((Long) dataSnapshot.getValue()).intValue();
                    shelterUserReservedAmountEditText.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                numberUserReseved = 0;
                shelterUserReservedAmountEditText.setText("0");
            }
        });
    }

    private void populateShelterFields(Shelter shelter) {
        shelterNameTextView.setText(shelter.getShelterName());
        addressTextView.setText(shelter.getAddress());
        phoneNumberTextView.setText(shelter.getPhoneNumber());
        capacityTextView.setText(String.format(Locale.ENGLISH, "%d/%d", numberVacancies, shelter.getCapacity()));
        restrictionsTextView.setText(shelter.getRestrictionsString());
        specialNotesTextView.setText(shelter.getSpecialNotes());
    }

}
