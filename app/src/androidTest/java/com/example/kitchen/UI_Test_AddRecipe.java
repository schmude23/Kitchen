package com.example.kitchen;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
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

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UI_Test_AddRecipe {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void uI_Test_AddRecipe() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_add_recipe), withContentDescription("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_recipe_recipe_name_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Meatloaf"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edit_recipe_servings_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("4"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edit_recipe_prep_time_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("20"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edit_recipe_total_time_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("90"), closeSoftKeyboard());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.edit_recipe_next_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                3),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.edit_ingredient_ingredient_edit_text),
                        childAtPosition(
                                allOf(withId(R.id.layout1),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatAutoCompleteTextView.perform(click());

        ViewInteraction appCompatAutoCompleteTextView2 = onView(
                allOf(withId(R.id.edit_ingredient_ingredient_edit_text),
                        childAtPosition(
                                allOf(withId(R.id.layout1),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatAutoCompleteTextView2.perform(replaceText("Hamburger"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.edit_ingredient_add_ingredient_button), withText("Add Ingredient"),
                        childAtPosition(
                                allOf(withId(R.id.layout1),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.edit_ingredient_popup_ingredient_quantity_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("1"), closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.edit_ingredient_popup_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(4);
        appCompatCheckedTextView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.edit_ingredient_popup_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatSpinner2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(7);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.edit_ingredient_popup_okay_button), withText("Okay"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.edit_ingredient_next_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                1),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.edit_direction_direction_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("Brown meat"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.edit_direction_add_direction_button), withText("Add Direction"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.edit_direction_next_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                1),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.edit_category_category_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.edit_category_category_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("Dinner"), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.edit_category_add_category_button), withText("Add Category"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton4.perform(click());

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
                allOf(withId(R.id.text_recipe_name), withText("Meatloaf"),
                        childAtPosition(
                                allOf(withId(R.id.constraint),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                                0)),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("Meatloaf")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.recipe_servings), withText("4.0"),
                        childAtPosition(
                                allOf(withId(R.id.constraint),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                                0)),
                                7),
                        isDisplayed()));
        textView2.check(matches(withText("4.0")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.recipe_prep_time), withText("20 min"),
                        childAtPosition(
                                allOf(withId(R.id.constraint),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                                0)),
                                8),
                        isDisplayed()));
        textView3.check(matches(withText("20 min")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.recipe_total_time), withText("90 min"),
                        childAtPosition(
                                allOf(withId(R.id.constraint),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                                0)),
                                9),
                        isDisplayed()));
        textView4.check(matches(withText("90 min")));

        ViewInteraction textView5 = onView(
                allOf(withId(android.R.id.text1), withText("Hamburger [ 1.0 pound(s) ]"),
                        childAtPosition(
                                allOf(withId(R.id.ingredient_list),
                                        childAtPosition(
                                                withId(R.id.constraint),
                                                11)),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Hamburger [ 1.0 pound(s) ]")));

        ViewInteraction textView6 = onView(
                allOf(withId(android.R.id.text1), withText("1) Brown meat"),
                        childAtPosition(
                                allOf(withId(R.id.direction_list),
                                        childAtPosition(
                                                withId(R.id.constraint),
                                                13)),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("1) Brown meat")));

        ViewInteraction textView7 = onView(
                allOf(withId(android.R.id.text1), withText("Dinner"),
                        childAtPosition(
                                allOf(withId(R.id.category_list),
                                        childAtPosition(
                                                withId(R.id.constraint),
                                                15)),
                                0),
                        isDisplayed()));
        textView7.check(matches(withText("Dinner")));
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
