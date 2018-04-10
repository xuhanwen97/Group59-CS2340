package com.example.xu.group59;

import android.content.Intent;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import com.example.xu.group59.R;
import com.example.xu.group59.activities.HomelessHomeActivity;
import com.example.xu.group59.fragments.ShelterInformationFragment;
import com.example.xu.group59.models.HomelessPerson;
import com.example.xu.group59.models.Shelter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by xu on 4/10/18
 */
@RunWith(AndroidJUnit4.class)
public class ShelterInformationFragmentTest {

    private ShelterInformationFragment mShelterInfoFrag;

    /** this line is preferred way to hook up to activity */
    @Rule
    public ActivityTestRule<HomelessHomeActivity> mActivityRule =
            new ActivityTestRule<>(HomelessHomeActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        Intent loggedInUserIntent = new Intent();
        HomelessPerson loggedInUser = new HomelessPerson("name", "pass", "temp name");
        loggedInUserIntent.putExtra(HomelessHomeActivity.LOGGED_IN_USER_TAG, loggedInUser);

        mActivityRule.launchActivity(loggedInUserIntent);
        mActivityRule.getActivity().setupShowShelterList(null);

        List<Shelter> shelterList = mActivityRule.getActivity().getShelterList();

        if (shelterList != null) {
            mActivityRule.getActivity().onShelterClicked(shelterList.get(0));
        } else {
            fail("Shelter List was not properly initialized in ShelterListFragment contained in HomelessHomeActivity");
        }

        int count = 0;

        while (mShelterInfoFrag == null) {
            mShelterInfoFrag = (ShelterInformationFragment) mActivityRule.getActivity().getSupportFragmentManager().findFragmentByTag(ShelterInformationFragment.TAG);
        }
    }

    @Test
    @UiThreadTest
    public void checkGetNumberReserved() {
        if (mShelterInfoFrag == null) {
            fail("Shelter Information Fragment was not created successfully");
        }

        mActivityRule.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeless_home_fragment_container, mShelterInfoFrag, ShelterInformationFragment.TAG)
                .addToBackStack("temp")
                .commit();

        EditText shelterUserReservedAmountEditText = mShelterInfoFrag.getView().findViewById(R.id.shelter_user_reserved_amount_text_view);

        // 0 gets parsed into zero - returns 0
        shelterUserReservedAmountEditText.setText("0");
        int zeroReserved = mShelterInfoFrag.getNumberReserved();
        assertEquals(zeroReserved, 0);

        // Empty String gets parsed into zero - returns 0
        shelterUserReservedAmountEditText.setText("");
        int nothingReserved = mShelterInfoFrag.getNumberReserved();
        assertEquals(nothingReserved, 0);

        // Text can't be parsed into integer - returns numberUserReserved
        shelterUserReservedAmountEditText.setText("f");
        int userReserved = mShelterInfoFrag.getNumberReserved();
        assertEquals(userReserved, mShelterInfoFrag.numberUserReseved);

        // Less than 0 can't be parsed - returns numberUserReserved
        shelterUserReservedAmountEditText.setText("-1");
        int negativeReserved = mShelterInfoFrag.getNumberReserved();
        assertEquals(negativeReserved, mShelterInfoFrag.numberUserReseved);

        // Entered number is greater than maximum per person - returns numberUserReserved
        int overMax = Shelter.singleUserMaxReservation + 1;
        shelterUserReservedAmountEditText.setText(String.format("%d", overMax));
        int overMaxReserved = mShelterInfoFrag.getNumberReserved();
        assertEquals(overMaxReserved, mShelterInfoFrag.numberUserReseved);

        // Entered number is greater than number of vacancies - returns numberUserReserved
        int tooMany = 9999;
        shelterUserReservedAmountEditText.setText(String.format("%d", tooMany));
        int tooManyReserved = mShelterInfoFrag.getNumberReserved();
        assertEquals(tooManyReserved, mShelterInfoFrag.numberUserReseved);

    }

    @After
    public void tearDown() throws Exception {
    }

}