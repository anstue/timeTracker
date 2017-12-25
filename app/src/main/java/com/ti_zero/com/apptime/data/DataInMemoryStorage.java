package com.ti_zero.com.apptime.data;

import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.ObjWithUUID;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class DataInMemoryStorage {

    private GroupItem rootItem;

    public DataInMemoryStorage(GroupItem groupItem) {
        this.rootItem = groupItem;
    }

    public DataInMemoryStorage() {
        rootItem = (GroupItem)new ObjectFactory().getRootItem();
    }

    public GroupItem getRootItem() {
        return rootItem;
    }

    /**
     * find an item with a UUID recursively in the tree
     * @param itemUUID
     * @return
     */
    public AbstractItem findItem(long itemUUID) {
        return rootItem.findByUUID(itemUUID);
    }

    public void removeItem(long itemUUID) {
        rootItem.findAndRemoveItem(itemUUID);
    }
}
