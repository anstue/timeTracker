package com.ti_zero.com.apptime.data;

import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;

/**
 * Created by anstue on 12/24/17.
 */

public interface IDataAccessFacade {

    void initialize();

    void createNewItem(long parentItemId, AbstractItem item);
    void changeItem(AbstractItem item);
    void removeItem(long itemId);

    void startItem(long itemId);
    void stopItem(long itemId);

}
