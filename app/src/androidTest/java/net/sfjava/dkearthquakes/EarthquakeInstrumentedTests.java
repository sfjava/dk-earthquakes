package net.sfjava.dkearthquakes;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import net.sfjava.dkearthquakes.ui.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkArgument;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 */
@RunWith(AndroidJUnit4.class)
public class EarthquakeInstrumentedTests {

    /**
     * {@link ActivityTestRule} to launch the activity under test.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    /**
     * A custom {@link Matcher} which matches an item in a {@link RecyclerView} by its text.
     */
    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RecyclerView with text " + itemText);
            }
        };
    }

    /**
     * A custom matcher for a progress bar value.
     */
    public static Matcher<View> withProgress(final int expectedProgress) {
        return new BoundedMatcher<View, ProgressBar>(ProgressBar.class) {
            @Override
            public boolean matchesSafely(ProgressBar progressBar) {
                return progressBar.getProgress() == expectedProgress;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("expected progress value: " + expectedProgress);
            }
        };
    }

    @Test
    public void useAppContext() throws Exception {
        // example use of the 'context' of the app under test...
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("net.sfjava.dkearthquakes", appContext.getPackageName());
    }

    @Test
    public void verifyEarthquakeItem() {

        // wait until the earthquakes list has finished being populated
        onView(withId(R.id.earthquakes_list_rv)).check(matches(isDisplayed()));

        // now verify that earthquake list shows at least one item we know is in our JSON feed
        onView(withItemText("c0001xgp")).check(matches(isDisplayed()));
        // and that it's "magnitude progress-bar" indicator reflects the correct magnitude value (times 10)
        Matcher<View> magnitudePB = withTagValue(is((Object) "c0001xgp"));
        onView(magnitudePB).check(matches(withProgress(88)));

        // and verify another one... for good measure :)
        onView(withItemText("us10007uph")).check(matches(isDisplayed()));
    }
}
