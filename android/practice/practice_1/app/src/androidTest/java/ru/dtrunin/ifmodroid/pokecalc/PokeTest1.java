package ru.dtrunin.ifmodroid.pokecalc;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by dmitry.trunin on 08.09.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PokeTest1 {

    private static final String charmander = "Charmander";

    @Rule
    public ActivityTestRule<PokeCalcActivity> activityRule =
            new ActivityTestRule<>(PokeCalcActivity.class);

    @Test
    public void testPokeSelector1() {
        onView(withId(R.id.pokemon_spinner))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is(charmander)))
                .perform(click());
        onView(withId(R.id.pokemon_spinner))
                .check(matches(withSpinnerText(containsString(charmander))));
    }
}
