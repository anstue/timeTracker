package com.ti_zero.com.apptime.data;

import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;

/**
 * Created by anstue on 12/24/17.
 */

public interface IDataAccessFacade {

    void initialize();
    boolean isInitialized();

    void createNewItem(GroupItem parent, AbstractItem item);
    void changeItem(AbstractItem item);
    void removeItem(long itemId);

    void startItem(AbstractItem item);
    void stopItem(AbstractItem item);

    void removeItem(GroupItem selectedGroupItem, int position);
}
