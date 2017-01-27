package com.patloew.countries;

import android.content.res.Resources;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import junit.framework.AssertionFailedError;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;

/* Copyright 2017 Tailored Media GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
public class EspressoUtils {

    public static Matcher<View> matchFirstDisplayedWithId(final int id) {
        return new TypeSafeMatcher<View>() {

            private boolean alreadyMatched = false;
            private Resources resources = null;

            @Override
            public void describeTo(Description description) {
                String idDescription = Integer.toString(id);
                if (resources != null) {
                    try {
                        idDescription = resources.getResourceName(id);
                    } catch (Resources.NotFoundException e) {
                        // No big deal, will just use the int value.
                        idDescription = String.format("%s (resource name not found)", id);
                    }
                }
                description.appendText("with id: " + idDescription);
            }

            @Override
            public boolean matchesSafely(View view) {
                if(alreadyMatched) {
                    return false;
                } else {
                    resources = view.getResources();
                    alreadyMatched = isDisplayed().matches(view) && id == view.getId();
                    return alreadyMatched;
                }
            }
        };
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayingAtLeast(90);
            }

            @Override
            public String getDescription() {
                return "Click on a child view with id " + id + ".";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                if (v != null) { v.performClick(); }
            }
        };
    }

    public static ViewAssertion recyclerViewItemCount(int count) {
        return new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if(view instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) view;
                    if(recyclerView.getAdapter().getItemCount() != count) {
                        throw new AssertionFailedError("RecyclerView with id=" + recyclerView.getId() + " has " + recyclerView.getAdapter().getItemCount() + " items, expected " + count + " items");
                    }
                } else {
                    throw new AssertionFailedError("View is not a RecyclerView");
                }
            }
        };
    }

}
