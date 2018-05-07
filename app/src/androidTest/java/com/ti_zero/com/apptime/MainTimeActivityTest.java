package com.ti_zero.com.apptime;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ti_zero.com.apptime.data.dao.db.worker.InitializeMemoryDbWorker;
import com.ti_zero.com.apptime.helper.CustomMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ti_zero.com.apptime.helper.CustomActions.clickLongXY;

/**
 * Created by anstue on 5/6/18.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainTimeActivityTest {

    @Rule
    public ActivityTestRule<MainTimeActivity> mActivityRule =
            new ActivityTestRule<>(MainTimeActivity.class);

    @Before
    public void setUp() {
        BaseApp baseApp = (BaseApp)mActivityRule.getActivity().getApplication();
        baseApp.getDatabase().removeAll();
        new InitializeMemoryDbWorker(baseApp.getDatabase(),baseApp.getDataAccessFacade().getDataInMemoryStorage()).run();
    }

    @Test
    public void addItem() {
        int itemCount = mActivityRule.getActivity().getAdapter().getItemCount();
        onView(withId(R.id.addItem))
                .perform(click());
        onView(withId(R.id.items))
                .check(matches(CustomMatchers.withRecyclerViewSize(itemCount + 1)));
    }

    @Test
    public void removeItem() {
        onView(withId(R.id.addItem))
                .perform(click());

        int itemCount = mActivityRule.getActivity().getAdapter().getItemCount();
        onView(withId(R.id.items))
                .perform(actionOnItemAtPosition(0, clickLongXY(0,0)));
        onView(withText(R.string.remove)).inRoot(isPlatformPopup())
                .perform(click());

        onView(withId(R.id.items))
                .check(matches(CustomMatchers.withRecyclerViewSize(itemCount - 1)));

    }
}
