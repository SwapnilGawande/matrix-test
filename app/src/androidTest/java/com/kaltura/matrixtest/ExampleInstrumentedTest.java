package com.kaltura.matrixtest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kaltura.matrixtest.view.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Transpose matrix instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final int TEST_MATRIX_SIZE = 2;

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule(HomeActivity.class);

    @Test
    public void transposeMatrix() throws Exception {
        onView(withId(R.id.recyclerViewMatrix)).check(matches(not(isDisplayed())));

        onView(withId(R.id.etMatrixSize)).perform(typeText(String.valueOf(TEST_MATRIX_SIZE)));

        onView(withId(R.id.btnGo)).perform(click());

        onView(withId(R.id.recyclerViewMatrix)).check(matches(isDisplayed()));

        onView(withId(R.id.btnTranspose)).perform(click());
    }
}
