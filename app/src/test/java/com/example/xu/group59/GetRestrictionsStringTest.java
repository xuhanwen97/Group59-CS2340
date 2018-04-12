package com.example.xu.group59;

import com.example.xu.group59.models.Shelter;
import com.example.xu.group59.models.Shelter.Restrictions;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by reneebotyrius on 4/11/18.
 */

public class GetRestrictionsStringTest {
    private List<Shelter.Restrictions> restrictions = new ArrayList<>();
    private Shelter shelter = new Shelter();

    @Test
    public void nullCheck() {
        restrictions = null;
        String restriction = shelter.getRestrictionsString();
        assertEquals("No Restrictions", restriction);
    }

    @Test
    public void emptyCheck() {
        restrictions = new ArrayList<>();
        String restriction = shelter.getRestrictionsString();
        assertEquals("No Restrictions", restriction);
    }

    @Test
    public void oneRestriction() {
        restrictions.add(Restrictions.women);
        shelter.setRestrictions(restrictions);
        String restriction = shelter.getRestrictionsString();
        assertEquals("Women", restriction);
    }

    @Test
    public void twoRestrictions() {
        restrictions.add(Restrictions.men);
        restrictions.add(Restrictions.veterans);
        shelter.setRestrictions(restrictions);
        String restriction = shelter.getRestrictionsString();
        assertEquals("Men, Veterans", restriction);
    }

    @Test
    public void threeRestrictions() {
        restrictions.add(Restrictions.men);
        restrictions.add(Restrictions.familiesNewborns);
        restrictions.add(Restrictions.veterans);
        shelter.setRestrictions(restrictions);
        String restriction = shelter.getRestrictionsString();
        assertEquals("Men, Families With Newborns, Veterans", restriction);
    }

    @Test
    public void allPossibleRestrictions() {
        restrictions.add(Restrictions.men);
        restrictions.add(Restrictions.women);
        restrictions.add(Restrictions.children);
        restrictions.add(Restrictions.youngAdults);
        restrictions.add(Restrictions.families);
        restrictions.add(Restrictions.familiesNewborns);
        restrictions.add(Restrictions.familiesChildrenUnder7);
        restrictions.add(Restrictions.veterans);
        shelter.setRestrictions(restrictions);
        String restriction = shelter.getRestrictionsString();
        assertEquals("Men, Women, Children, Young Adults, Families, " +
                "Families With Newborns, Families With Children Under 7, Veterans", restriction);
    }
}
