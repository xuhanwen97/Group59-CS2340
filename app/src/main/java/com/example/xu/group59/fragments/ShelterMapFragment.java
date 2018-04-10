package com.example.xu.group59.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xu.group59.R;
import com.example.xu.group59.models.Shelter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Class for a shelter map fragment. Contains methods and sttributes for the fragment.
 */
public class ShelterMapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback{

    public static final String TAG = "shelter_map_fragment";
    private GoogleMap mMap;
    private MapView mMapView;
    private View mView;

    private List<Shelter> shelterList;

    /**
     * A required constructor for the Shelter Map Fragment.
     */
    public ShelterMapFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of the Shelter Map Fragment. Sets the restrictions, and shelter list
     * attribute.
     * @param shelterList the list of shelters displayed in the fragment
     * @return an instance of the Shelter Map Fragment.
     */
    public static ShelterMapFragment newInstance(List<Shelter> shelterList) {
        ShelterMapFragment fragment = new ShelterMapFragment();

        fragment.shelterList = shelterList;

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_shelter_map, container, false);
        MapsInitializer.initialize(this.getActivity());
        mMapView = mView.findViewById(R.id.shelter_map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return mView;
    }

    //The methods called below are all part of the MapView's lifecycle!
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Makes the zoom buttons appear
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if ((shelterList != null) && !shelterList.isEmpty()) {
            //Adds a marker at each shelter location
            for (Shelter s : shelterList) {
                LatLng loc = new LatLng(s.getLatitude(), s.getLongitude());
                //TODO: Add a custom info window adapter that allows multi line, for now just do phone number
                //https://stackoverflow.com/questions/13904651/android-google-maps-v2-how-to-add-marker-with-multiline-snippet
//                String description = String.format("Address: %s\n, Phone Number: %s\n", s.getAddress(), s.getPhoneNumber()) ;
                String description = String.format("Phone Number: %s\n", s.getPhoneNumber()) ;
                mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(s.getShelterName())
                        .snippet(description));
            }

            //Moves the camera to the first shelter, and sets the zoom to a decent level
            Shelter firstShelter = shelterList.get(0);
            LatLng firstShelterLoc = new LatLng(firstShelter.getLatitude(), firstShelter.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstShelterLoc, 11.0f));
        }

    }
}
