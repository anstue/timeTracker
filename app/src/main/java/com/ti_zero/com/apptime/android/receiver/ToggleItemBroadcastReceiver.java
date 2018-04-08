package com.ti_zero.com.apptime.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ti_zero.com.apptime.BaseApp;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.helper.NotificationHelper;

public class ToggleItemBroadcastReceiver extends BroadcastReceiver {

    private DataAccessFacade dataAccessFacade;

    @Override
    public void onReceive(Context context, Intent intent) {
        dataAccessFacade = ((BaseApp)context.getApplicationContext()).getDataAccessFacade();//TODO könnte sein dass es nicht BaseApp ist. https://stackoverflow.com/questions/5018545/getapplication-vs-getapplicationcontext
        long itemId = intent.getExtras().getLong(NotificationHelper.NOTIFICATION_ACTION_BTN_TOGGLE);
        Logging.logInfo(LogTag.NOTIFICATION, "Getting start/stop call from notification " + itemId);
        AbstractItem item = dataAccessFacade.findItem(itemId);
        if (item.isRunning()) {
            dataAccessFacade.stopItem(item);
        } else {
            dataAccessFacade.stopOtherItemsAndStartItem(item);
        }
        NotificationHelper.createNotification(item, context);
    }
}
