package com.ti_zero.com.apptime.data;

import com.ti_zero.com.apptime.data.objects.GroupItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class DataStorage {

    GroupItem rootItem;
    GroupItem selectedGroup;

    public DataStorage(GroupItem groupItem) {
        this.rootItem = groupItem;
        this.selectedGroup = rootItem;
    }

    public DataStorage() {
        rootItem = new GroupItem("Standard","", new Date(), false);
        selectedGroup = rootItem;
    }

    public GroupItem getRootItem() {
        return rootItem;
    }

    public GroupItem getSelectedGroup() {
        return selectedGroup;
    }
    public void changeSelectedGroup(GroupItem groupItem) {
        selectedGroup = groupItem;
    }
}
