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

/**
 * Created by anstue on 12/25/17.
 */

public interface ItemCallback {

    int NOTIFICATION_ID_RUNNING_ITEM = 10001;

    void onClick(AbstractItem item);
    void onBtnClick(AbstractItem item);
    void onTextChanged(AbstractItem item);

    default void stopItem(AbstractItem item, Context context, DataAccessFacade dataAccessFacade) {
        if (item.isRunning()) {
            dataAccessFacade.stopItem(item);
            Logging.logDebug(LogTag.UI, "stopItem: " + item.getName());
        }
        removeNotification(context);
    }

    default void removeNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID_RUNNING_ITEM);
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
        createNotification(item, context);
    }

    default void createNotification(AbstractItem item, Context context) {
        Logging.logDebug(LogTag.UI, "creating notification: " + item.getName());
        //TODO only works for older android versions, consider newer ones too, API 26
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("AppTime is running")
                        .setContentText("Item: " + item.getName());

        Intent notificationIntent = new Intent(context, MainTimeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID_RUNNING_ITEM, builder.build());


    }
}
