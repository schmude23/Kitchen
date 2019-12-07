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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UI_Test_PreloadedRecipes {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void uI_Test_PreloadedRecipes() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.text_recipe_name), withText("Mac n Cheese"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Mac n Cheese")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.text_recipe_name), withText("Chicken and Rice"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Chicken and Rice")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.text_recipe_name), withText("Beef and Bean Chili"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Beef and Bean Chili")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.text_recipe_name), withText("Restaurant-style Salsa"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Restaurant-style Salsa")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.text_recipe_name), withText("Breakfast Sandwich"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView5.check(matches(withText("Breakfast Sandwich")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.text_recipe_name), withText("Quick Lunch Panini"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView6.check(matches(withText("Quick Lunch Panini")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.text_recipe_name), withText("Ground Beef Tacos"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView7.check(matches(withText("Ground Beef Tacos")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.text_recipe_name), withText("Spaghetti with Meat Sauce"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView8.check(matches(withText("Spaghetti with Meat Sauce")));
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
