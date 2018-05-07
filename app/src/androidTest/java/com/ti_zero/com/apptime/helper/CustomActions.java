package com.ti_zero.com.apptime.helper;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by anstue on 5/7/18.
 */
public class CustomActions {

    public static ViewAction clickLongXY(final int x, final int y) {
        return new GeneralClickAction(
                Tap.LONG,
                (View view) -> {

                    final int[] screenPos = new int[2];
                    view.getLocationOnScreen(screenPos);

                    final float screenX = screenPos[0] + x;
                    final float screenY = screenPos[1] + y;
                    return new float[]{screenX, screenY};
                },
                Press.FINGER,
                InputDevice.SOURCE_UNKNOWN,
                MotionEvent.BUTTON_PRIMARY);
    }

}
