package com.example.kitchen;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.kitchen.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UI_Test_ConvertUnits2 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void uI_Test_ConvertUnits2() {
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
 Thread.sleep(5000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction overflowMenuButton = onView(
allOf(withContentDescription("More options"),
childAtPosition(
childAtPosition(
withId(R.id.recipe_toolbar),
1),
1),
isDisplayed()));
        overflowMenuButton.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(250);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatTextView = onView(
allOf(withId(R.id.title), withText("Convert Units"),
childAtPosition(
childAtPosition(
withId(R.id.content),
0),
0),
isDisplayed()));
        appCompatTextView.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(300);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatSpinner = onView(
allOf(withId(R.id.unit_conversion_popup_new_unit_spinner),
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
.atPosition(2);
        appCompatCheckedTextView.perform(click());
        
        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.unit_conversion_popup_okay_button), withText("Okay"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
5),
isDisplayed()));
        appCompatButton.perform(click());
        
        ViewInteraction textView = onView(
allOf(withId(R.id.list_item_string), withText("Chili powder [ 0.02 cup(s) ]"),
childAtPosition(
allOf(withId(R.id.ingredient_list),
childAtPosition(
withId(R.id.constraint),
3)),
6),
isDisplayed()));
        textView.check(matches(withText("Chili powder [ 0.02 cup(s) ]")));
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
