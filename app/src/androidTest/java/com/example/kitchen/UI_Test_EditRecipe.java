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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UI_Test_EditRecipe {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void uI_Test_EditRecipe() {
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.recipe_list_recycler),
                                childAtPosition(
                                        withId(R.id.linear),
                                        1)),
                        1),
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
                allOf(withId(R.id.title), withText("Edit Recipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_recipe_servings_edit_text), withText("4.0"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("2"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edit_recipe_servings_edit_text), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.edit_recipe_next_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                3),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.edit_ingredient_next_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                1),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.edit_direction_next_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                1),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.edit_category_next_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                1),
                        isDisplayed()));
        floatingActionButton4.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.recipe_servings), withText("2.0"),
                        childAtPosition(
                                allOf(withId(R.id.constraint),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                                0)),
                                7),
                        isDisplayed()));
        textView.check(matches(withText("2.0")));
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
