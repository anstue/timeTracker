package com.ti_zero.com.apptime.data.objects;

import com.ti_zero.com.apptime.data.dto.StartItemDTO;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class GroupItem extends AbstractItem {

    private int standardItemIdx = 0;
    private List<AbstractItem> items;
    private GroupItem parent;


    public GroupItem(String name, String description, Date lastUsage, boolean favorite) {
        super(name, description, lastUsage, favorite);
        items = new ArrayList<>();
    }

    public GroupItem(String name, String description, Date lastUsage, boolean favorite, long uuid) {
        super(name, description, lastUsage, favorite, uuid);
        items = new ArrayList<>();
    }


    public GroupItem(String name, String description, Date lastUsage, boolean favorite, List<AbstractItem> items, GroupItem parent) {
        super(name, description, lastUsage, favorite);
        this.items = items;
        this.parent = parent;
    }

    public GroupItem() {
        //for serializer
    }

    public int getStandardItemIdx() {
        return standardItemIdx;
    }

    public void setStandardItemIdx(int standardItemIdx) {
        this.standardItemIdx = standardItemIdx;
    }

    public List<AbstractItem> getItems() {
        return items;
    }

    public void setItems(List<AbstractItem> items) {
        this.items = items;
    }

    public void addItem(AbstractItem item) {
        item.setParent(this);
        items.add(item);
    }

    public AbstractItem getStandardAccountItem() {
        if (items.size() > standardItemIdx) {
            return items.get(standardItemIdx);
        }
        return null;
    }

    @Override
    public long getTotalTime() {
        long sum = 0;
        for (AbstractItem t : items) {
            sum += t.getTotalTime();
        }
        return sum;
    }

    @Override
    public boolean isRunning() {
        for (AbstractItem i : items) {
            if (i.isRunning()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AccountItem stop() {
        for (AbstractItem i : items) {
            if (i.isRunning()) {
                return i.stop();
            }
        }
        throw new RuntimeException("Stop called, but no item to stop");
    }

    @Override
    public StartItemDTO addTimeEntry() {
        boolean newItem=false;
        if (getStandardAccountItem() == null) {
            //TODO show toast or snackbar, that accountitem got created
            addItem(new ObjectFactory().getNewAccountItem());
            newItem = true;
            Logging.logInfo(LogTag.DATA_OBJECTS, "Automatically creating new AccountItem");
        }
        StartItemDTO dto = getStandardAccountItem().addTimeEntry();
        if(newItem) {
            dto.setNewItem(true);
            return dto;
        }
        return dto;
    }

    @Override
    public List<AbstractItem> getChildren() {
        return items;
    }

    @Override
    public GroupItem getParent() {
        return parent;
    }

    @Override
    public void setParent(GroupItem item) {
        this.parent = item;
    }

    public void removeItem(int position) {
        items.remove(position);
    }

    @Override
    public AbstractItem findByUUID(long itemUUID) {
        if (getUniqueID() == itemUUID) {
            return this;
        }
        for (AbstractItem item : items) {
            AbstractItem temp = item.findByUUID(itemUUID);
            if (temp != null) {
                return temp;
            }
        }
        throw new RuntimeException("Item not found by UUID");
    }

    public void findAndRemoveItem(long itemUUID) {
        for(AbstractItem item : items) {
            if(item.getUniqueID()==itemUUID) {
                items.remove(item);
                return;
            } else if(item instanceof GroupItem) {
                ((GroupItem) item).findAndRemoveItem(itemUUID);
            }
        }
    }
}
