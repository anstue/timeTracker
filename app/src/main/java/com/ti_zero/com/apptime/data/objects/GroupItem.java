package com.ti_zero.com.apptime.data.objects;

import com.ti_zero.com.apptime.BR;
import com.ti_zero.com.apptime.data.dto.StartItemDTO;
import com.ti_zero.com.apptime.data.dto.TimeEntryDTO;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        items.add(0, item);
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
                AccountItem retValue = i.stop();
                setLastUsage(new Date());
                notifyPropertyChanged(BR.btnToggleText);
                notifyPropertyChanged(BR.running);
                return retValue;
            }
        }
        throw new RuntimeException("Stop called, but no item to stop");
    }

    @Override
    public StartItemDTO addTimeEntry() {
        boolean newItem = false;
        setLastUsage(new Date());
        if (getStandardAccountItem() == null) {
            //TODO show toast or snackbar, that accountitem got created
            addItem(new ObjectFactory().getNewAccountItem());
            newItem = true;
            Logging.logInfo(LogTag.DATA_OBJECTS, "Automatically creating new AccountItem");
        }
        StartItemDTO dto = getStandardAccountItem().addTimeEntry();
        if (newItem) {
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
        return null;
    }

    @Override
    public long getTodayTime() {
        long sum = 0;
        for (AbstractItem t : items) {
            sum += t.getTodayTime();
        }
        return sum;
    }

    @Override
    public String getShortStartTime() {
        if (getStandardAccountItem() != null) {
            return getStandardAccountItem().getShortStartTime();
        } else {
            return "";
        }
    }

    @Override
    public TimeEntryDTO findCurrentTimeEntry() {
        for (AbstractItem item : items) {
            TimeEntryDTO timeEntryDTO = item.findCurrentTimeEntry();
            if (timeEntryDTO != null) {
                return timeEntryDTO;
            }
        }
        return null;
    }

    public void findAndRemoveItem(long itemUUID) {
        for (AbstractItem item : items) {
            if (item.getUniqueID() == itemUUID) {
                items.remove(item);
                return;
            } else if (item instanceof GroupItem) {
                ((GroupItem) item).findAndRemoveItem(itemUUID);
            }
        }
    }

    public List<AbstractItem> getChildrenFiltered(String searchKeyword) {
        if ("".equals(searchKeyword)) {
            return getChildren();
        } else {
            return getChildren().stream().filter(item -> {
                if (item instanceof GroupItem) {
                    //also include groups which have children with the searchKeyword
                    return itemNameMatches(searchKeyword, item) || ((GroupItem) item).getChildrenFiltered(searchKeyword).size() > 0;
                } else {
                    return itemNameMatches(searchKeyword, item);
                }
            }).collect(Collectors.toList());
        }
    }

    private boolean itemNameMatches(String searchKeyword, AbstractItem item) {
        return item.getName().toLowerCase().contains(searchKeyword.toLowerCase());
    }
}
