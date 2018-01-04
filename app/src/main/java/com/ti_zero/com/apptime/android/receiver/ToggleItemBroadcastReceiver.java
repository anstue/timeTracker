package com.ti_zero.com.apptime.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ti_zero.com.apptime.MainTimeActivity;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.helper.NotificationHelper;

public class ToggleItemBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long itemId = intent.getExtras().getLong(NotificationHelper.NOTIFICATION_ACTION_BTN_TOGGLE);
        Logging.logInfo(LogTag.NOTIFICATION, "Getting start/stop call from notification " + itemId);
        AbstractItem item = MainTimeActivity.getDataAccessFacade().findItem(itemId);
        if (item.isRunning()) {
            MainTimeActivity.getDataAccessFacade().stopItem(item);
        } else {
            MainTimeActivity.getDataAccessFacade().startItem(item);
        }
        NotificationHelper.createNotification(item, context);
    }
}
