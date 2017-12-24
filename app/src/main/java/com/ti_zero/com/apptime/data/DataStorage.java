package com.ti_zero.com.apptime.data;

import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class DataStorage {

    GroupItem rootItem;

    public DataStorage(GroupItem groupItem) {
        this.rootItem = groupItem;
    }

    public DataStorage() {
        rootItem = new GroupItem("Standard","", new Date(), false);
    }

    public GroupItem getRootItem() {
        return rootItem;
    }

    /**
     * find an item with a UUID recursively in the tree
     * @param itemUUID
     * @return
     */
    public AbstractItem findItem(String itemUUID) {
        return rootItem.findByUUID(itemUUID);
    }

}
