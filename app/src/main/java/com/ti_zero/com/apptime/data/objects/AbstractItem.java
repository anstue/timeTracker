package com.ti_zero.com.apptime.data.objects;

import android.databinding.Bindable;

import com.ti_zero.com.apptime.BR;
import com.ti_zero.com.apptime.data.dto.StartItemDTO;
import com.ti_zero.com.apptime.helper.DurationPrinter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public abstract class AbstractItem extends ObservableWithUUID implements Serializable, Comparable {

    private String name;
    private String description;
    private Date lastUsage;
    private boolean favorite;


    public AbstractItem(String name, String description, Date lastUsage, boolean favorite) {
        this.name = name;
        this.description = description;
        this.lastUsage = lastUsage;
        this.favorite = favorite;
    }

    public AbstractItem(String name, String description, Date lastUsage, boolean favorite, long uuid) {
        super(uuid);
        this.name = name;
        this.description = description;
        this.lastUsage = lastUsage;
        this.favorite = favorite;
    }

    public AbstractItem() {
        //for serializer
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.notifyPropertyChanged(BR.name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(Date lastUsage) {
        this.lastUsage = lastUsage;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return name;
    }

    @Bindable
    public String getTotalTimePrettyPrint() {
        return DurationPrinter.printDuration(getTotalTime());
    }

    @Bindable
    public String getTodayTimePrettyPrint() {
        return DurationPrinter.printDuration(getTodayTime());
    }

    @Bindable
    public String getBtnToggleText() {
        if (isRunning()) {
            return "Stop";
        } else {
            return "Start";
        }
    }

    @Override
    public int compareTo(Object another) {
        if (((AbstractItem) another).getLastUsage().getTime() > lastUsage.getTime()) {
            return 1;
        } else if (((AbstractItem) another).getLastUsage().getTime() == lastUsage.getTime()) {
            return 0;
        }
        return -1;

    }

    public abstract long getTotalTime();

    @Bindable
    public abstract boolean isRunning();

    public abstract AccountItem stop();

    /**
     * @return item where time_entry is added
     */
    public abstract StartItemDTO addTimeEntry();

    @Bindable
    public abstract List<AbstractItem> getChildren();

    @Bindable
    public abstract GroupItem getParent();

    public abstract void setParent(GroupItem item);

    public AbstractItem findByUUID(long itemUUID) {
        return itemUUID == getUniqueID() ? this : null;
    }

    public abstract long getTodayTime();
}
