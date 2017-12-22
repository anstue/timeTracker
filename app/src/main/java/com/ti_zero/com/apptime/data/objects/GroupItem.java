package com.ti_zero.com.apptime.data.objects;

import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;

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
    private AbstractItem parent;

    public GroupItem(String name, String description, Date lastUsage, boolean favorite) {
        super(name, description, lastUsage, favorite);
        items = new ArrayList<>();
    }


    public GroupItem(String name, String description, Date lastUsage, boolean favorite, List<AbstractItem> items, AbstractItem parent) {
        super(name, description, lastUsage, favorite);
        this.items = items;
        this.parent = parent;
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
    public void stop() {
        for (AbstractItem i : items) {
            if (i.isRunning()) {
                i.stop();
            }
        }
    }

    @Override
    public void addTimeEntry() {
        if (getStandardAccountItem() == null) {
            //TODO show toast or snackbar, that accountitem got created
            addItem(new ObjectFactory().getNewAccountItem());
        }
        getStandardAccountItem().addTimeEntry();
    }

    @Override
    public List<AbstractItem> getChildren() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public AbstractItem getParent() {
        return parent;
    }

    @Override
    public void setParent(AbstractItem item) {
        this.parent = item;
    }

    public void removeItem(int position) {
        items.remove(position);
    }


}
