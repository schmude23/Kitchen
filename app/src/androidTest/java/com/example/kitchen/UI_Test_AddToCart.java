package com.example.kitchen;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UI_Test_AddToCart {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void uI_Test_AddToCart() {
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.recipe_list_recycler),
                                childAtPosition(
                                        withId(R.id.linear),
                                        1)),
                        0),
                        isDisplayed()));
        linearLayout.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recipe_toolbar),
                                        1),
                                2)));
        overflowMenuButton.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Add to Cart"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction checkBox = onView(
                allOf(withId(R.id.ingredient_check_box),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.shopping_cart_recycler),
                                        1),
                                0),
                        isDisplayed()));
        checkBox.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.ingredient_quantity_text), withText("4.0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.shopping_cart_recycler),
                                        1),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("4.0")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.ingredient_unit_text), withText("tablespoon(s)"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.shopping_cart_recycler),
                                        1),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("tablespoon(s)")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
