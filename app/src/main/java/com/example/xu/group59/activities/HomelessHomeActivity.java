package com.example.xu.group59.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
// import android.graphics.PorterDuff;
// import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
// import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.xu.group59.fragments.ShelterListRestrictionsFilterFragment;
import com.example.xu.group59.fragments.ShelterListGenderFilterFragment;
import com.example.xu.group59.R;
import com.example.xu.group59.Utils.ToastUtils;
import com.example.xu.group59.fragments.ShelterInformationFragment;
import com.example.xu.group59.fragments.ShelterListFragment;
import com.example.xu.group59.fragments.ShelterMapFragment;
import com.example.xu.group59.models.HomelessPerson;
import com.example.xu.group59.models.Shelter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
// import java.util.IdentityHashMap;
import java.util.List;

public class HomelessHomeActivity extends AppCompatActivity implements
        ShelterListFragment.ShelterListFragmentListener,
        SearchView.OnQueryTextListener,
        ShelterListGenderFilterFragment.ShelterListGenderFilterFragmentListener,
        ShelterListRestrictionsFilterFragment.ShelterListRestrictionsFilterFragmentListener{

    private static final String TAG = "homeless_home_activity";
    public static final String LOGGED_IN_USER_TAG = "logged_in_user";
    private SearchView mSearchView;

    private List<Shelter> currentShelterList;

    private HomelessPerson loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeless_home);

        //Grab the signed in user
        if (getIntent().getExtras().get(LOGGED_IN_USER_TAG) != null) {
            loggedInUser = (HomelessPerson) getIntent().getExtras().get(LOGGED_IN_USER_TAG);
        } else {
            loggedInUser = null;
        }

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_homeless_home);
        setupShowShelterList(null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            setupShowShelterList(query);
        }
    }

    //region [ Option Menu Methods ] ================================= //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_filter_search_map_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_shelter_option).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        mSearchView = searchView;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shelter_map_option:
                showShelterMap();
                return true;
            case R.id.filter_gender_option:
                showGenderFilterFragment();
                return true;
            case R.id.filter_restrictions_option:
                showRestrictionsFilterDialogFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //endregion


    private void attemptLogout() {
        loggedInUser = null;
        Toast successToast = Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT);
        successToast.setGravity(Gravity.CENTER, 0, 0);
        successToast.show();

        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onShelterClicked(Shelter shelter) {
        if (shelter == null) {
            Log.d(TAG, "onShelterClicked: given shelter is null");
            return;
        }

        ShelterInformationFragment shelterInfoFragment = ShelterInformationFragment.newInstance(shelter, loggedInUser);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeless_home_fragment_container, shelterInfoFragment)
                .addToBackStack("temp")
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        Fragment shelterListFragment = getSupportFragmentManager().findFragmentByTag(ShelterListFragment.TAG);

        // isIconified is false when it's shown fully
        if ((mSearchView != null) && !mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            mSearchView.onActionViewCollapsed();
            setupShowShelterList(null);
            return true;
        }

        if ((shelterListFragment != null) && shelterListFragment.isVisible()) {
            ToastUtils.shortToastCenter(this, "Logout successful").show();
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
        return true;
    }

    //region [ fragment helpers ] ================================= //

    private void setupShowShelterList(String queryString) {
        Query query;

        if (queryString == null) {
            query = FirebaseDatabase.getInstance().getReference().child(Shelter.shelterListKey).orderByChild(Shelter.shelterNameKey);
        } else {
            query = FirebaseDatabase.getInstance().getReference().child(Shelter.shelterListKey).orderByChild(Shelter.shelterNameKey).equalTo(queryString);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Shelter> shelterList = new ArrayList<>(10);
                for (DataSnapshot shelterSnapshot : dataSnapshot.getChildren()) {
                    Shelter tempShelter = new Shelter(shelterSnapshot);
                    shelterList.add(tempShelter);
                }
                showShelterList(shelterList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //TODO: Figure out if the if statement is needed :/
    private void showShelterList(List<Shelter> sheltersList) {
        ShelterListFragment shelterListFragment
                = (ShelterListFragment) getSupportFragmentManager().findFragmentByTag(ShelterListFragment.TAG);

        if (shelterListFragment != null) {
            //replace shelter list fragment, don't make new
            shelterListFragment = ShelterListFragment.newInstance(this, sheltersList);

            currentShelterList = sheltersList;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.homeless_home_fragment_container, shelterListFragment, ShelterListFragment.TAG)
                    .commit();
        } else {
            shelterListFragment = ShelterListFragment.newInstance(this, sheltersList);

            currentShelterList = sheltersList;
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.homeless_home_fragment_container, shelterListFragment, ShelterListFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void showGenderFilterFragment() {
        ShelterListGenderFilterFragment genderFilterFragment = ShelterListGenderFilterFragment.newInstance(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeless_home_fragment_container, genderFilterFragment, ShelterListGenderFilterFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    private void showRestrictionsFilterDialogFragment() {
        ShelterListRestrictionsFilterFragment restrictionFilterFragment = ShelterListRestrictionsFilterFragment.newInstance(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeless_home_fragment_container, restrictionFilterFragment, ShelterListGenderFilterFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
    //endregion


    //region [ SearchView query listener ] ================================= //

    @Override
    public boolean onQueryTextSubmit(String query) {
        setupShowShelterList(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    //endregion

    @Override
    public void onGenderFilterClicked(Shelter.Gender gender) {

        onSupportNavigateUp();

        if (gender == null) {
            setupShowShelterList(null);
        } else {
            setupShowGenderFilteredShelterList(gender);
        }
    }

    //Currently gender is a subset of restrictions
    private void setupShowGenderFilteredShelterList(final Shelter.Gender gender) {
        DatabaseReference shelterListRef = FirebaseDatabase.getInstance().getReference(Shelter.shelterListKey);

        shelterListRef.orderByChild(Shelter.shelterNameKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Collection<Shelter> shelterList = new ArrayList<>(10);
                for (DataSnapshot shelterSnapshot : dataSnapshot.getChildren()) {
                    Shelter tempShelter = new Shelter(shelterSnapshot);
                    shelterList.add(tempShelter);
                }
                showGenderFilteredShelterList(shelterList, gender);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showGenderFilteredShelterList(Collection<Shelter> shelterList, Shelter.Gender gender) {
        if ((shelterList == null) || (gender == null)) {
            return;
        }

        List<Shelter> filteredList = new ArrayList<>(shelterList.size());

        for (Shelter s : shelterList) {
            //Since gender is a subset of restriction, convert gender to restriction
            Shelter.Restrictions restriction = convertGenderToRestrictions(gender);

            if (restriction == null) {
                return;
            }

            if (s.getRestrictions().contains(restriction)) {
                filteredList.add(s);
            }
        }

        currentShelterList = filteredList;
        ShelterListFragment shelterListFragment = ShelterListFragment.newInstance(this, filteredList);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeless_home_fragment_container, shelterListFragment, ShelterListFragment.TAG)
                .commit();
    }
    @Override
    public void onRestrictionsFilterClicked(Shelter.Restrictions restriction) {
        onSupportNavigateUp();

        if (restriction == null) {
            setupShowShelterList(null);
        } else {
            setupShowRestrictionsFilteredShelterList(restriction);
        }
    }

    private void setupShowRestrictionsFilteredShelterList(final Shelter.Restrictions restriction) {
        DatabaseReference shelterListRef = FirebaseDatabase.getInstance().getReference(Shelter.shelterListKey);

        shelterListRef.orderByChild(Shelter.shelterNameKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Collection<Shelter> shelterList = new ArrayList<>(10);
                for (DataSnapshot shelterSnapshot : dataSnapshot.getChildren()) {
                    Shelter tempShelter = new Shelter(shelterSnapshot);
                    shelterList.add(tempShelter);
                }
                showRestrictionsFilteredShelterList(shelterList, restriction);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRestrictionsFilteredShelterList(Collection<Shelter> shelterList, Shelter.Restrictions restriction) {
        if ((shelterList == null) || (restriction == null)) {
            return;
        }

        List<Shelter> filteredList = new ArrayList<>(shelterList.size());

        for (Shelter s : shelterList) {
            if (s.getRestrictions().contains(restriction)) {
                filteredList.add(s);
            }
        }

        currentShelterList = filteredList;
        ShelterListFragment shelterListFragment = ShelterListFragment.newInstance(this, filteredList);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeless_home_fragment_container, shelterListFragment, ShelterListFragment.TAG)
                .commit();
    }

    private Shelter.Restrictions convertGenderToRestrictions(Shelter.Gender gender) {

        for (Shelter.Restrictions r : Shelter.Restrictions.values()) {
            if (r.getName().equals(gender.getName())) {
                return r;
            }
        }

        return null;
    }

    /**
     * Getter method for the currently logged in user
     * @return return the current HomelessPerson that is logged in
     * */
    public HomelessPerson getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Setter method for the current logged in user
     * @param loggedInUser current logged in user
     * */
    public void setLoggedInUser(HomelessPerson loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    private void showShelterMap() {
        if (currentShelterList == null) {
            return;
        }

        ShelterMapFragment shelterMapFragment = ShelterMapFragment.newInstance(currentShelterList);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeless_home_fragment_container, shelterMapFragment, ShelterMapFragment.TAG)
                .addToBackStack("temp")
                .commit();
    }

}
