package com.ti_zero.com.apptime.data;

import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;

/**
 * Created by uni on 12/22/17.
 */

public class DataInMemoryStorage {

    private GroupItem rootItem;

    public DataInMemoryStorage(GroupItem groupItem) {
        this.rootItem = groupItem;
    }

    public DataInMemoryStorage() {
        rootItem = (GroupItem) new ObjectFactory().getRootItem();
    }

    public GroupItem getRootItem() {
        return rootItem;
    }

    public void setRootItem(GroupItem rootItem) {
        this.rootItem = rootItem;
    }

    /**
     * find an item with a UUID recursively in the tree
     *
     * @param itemUUID
     * @return
     */
    public AbstractItem findItem(long itemUUID) {
        AbstractItem item = rootItem.findByUUID(itemUUID);
        if (item != null) {
            return item;
        }
        throw new RuntimeException("Item not found by UUID");
    }

    public void removeItem(long itemUUID) {
        rootItem.findAndRemoveItem(itemUUID);
    }
}
