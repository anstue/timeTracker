package com.ti_zero.com.apptime.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by anstue on 5/6/18.
 */
public class CustomMatchers {
    public static Matcher<View> withRecyclerViewSize(final int size) {

        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(final View view) {
                return ((RecyclerView) view).getAdapter().getItemCount() == size;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("List should have " + size + " items");
            }
        };
    }
}
