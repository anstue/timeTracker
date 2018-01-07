package com.ti_zero.com.apptime.ui.callbacks;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ti_zero.com.apptime.MainTimeActivity;
import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.helper.NotificationHelper;

/**
 * Created by anstue on 12/25/17.
 */

public interface ItemCallback {

    void onClick(AbstractItem item);
    void onBtnClick(AbstractItem item);
    void openChangeNameDialog(AbstractItem item);

    default void stopItem(AbstractItem item, Context context, DataAccessFacade dataAccessFacade) {
        if (item.isRunning()) {
            dataAccessFacade.stopItem(item);
            Logging.logDebug(LogTag.UI, "stopItem: " + item.getName());
        }
        NotificationHelper.removeNotification(context);
    }

    default void startItem(AbstractItem item, boolean existingTimeEntry, Context context, DataAccessFacade dataAccessFacade) {
        Logging.logDebug(LogTag.UI, "startItem: " + item.getName());
        if (dataAccessFacade.getDataInMemoryStorage().getRootItem().isRunning()) {
            //check rootItem maybe sth is running in another group
            //check if item itself is running(child of group)
            if (!item.isRunning()) {
                dataAccessFacade.stopItem(dataAccessFacade.getDataInMemoryStorage().getRootItem());
                Logging.logDebug(LogTag.UI, "Stopped item over root item: " + item.getName());
            }
        }
        if (!existingTimeEntry) {
            dataAccessFacade.startItem(item);
        }
        NotificationHelper.createNotification(item, context);
    }

}
