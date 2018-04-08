package com.ti_zero.com.apptime.ui.callbacks;

import android.content.Context;
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
    void onBtnMinus10Click(AbstractItem item);
    void onBtnPlus10Click(AbstractItem item);
    void openChangeNameDialog(AbstractItem item);

    default void stopItem(AbstractItem item, Context context, DataAccessFacade dataAccessFacade) {
        if (item.isRunning()) {
            dataAccessFacade.stopItem(item);
            Logging.logDebug(LogTag.UI, "stopItem: " + item.getName());
        }
        NotificationHelper.removeNotification(context);
    }

    default void startAndChangeItemRunningTimeEntry(AbstractItem item, Context context, DataAccessFacade dataAccessFacade, int minutes) {
        dataAccessFacade.startAndChangeItemRunningTimeEntry(item, minutes);
        if (!item.isRunning()) {
            NotificationHelper.removeNotification(context);
        }
    }

    default void startItem(AbstractItem item, boolean existingTimeEntry, Context context, DataAccessFacade dataAccessFacade) {
        Logging.logDebug(LogTag.UI, "stopOtherItemsAndStartItem: " + item.getName());
        if (!existingTimeEntry) {
            dataAccessFacade.stopOtherItemsAndStartItem(item);
        }
        NotificationHelper.createNotification(item, context);
    }

}
