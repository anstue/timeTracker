package com.ti_zero.com.apptime.data.objects;

import com.ti_zero.com.apptime.helper.DurationPrinter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by uni on 12/22/17.
 */

public abstract class AbstractItem implements Serializable {

    private String name;
    private String description;
    private Date lastUsage;
    private boolean favorite;
    private long uniqueID = UUID.randomUUID().getMostSignificantBits();//TODO change to a more secure method

    public AbstractItem(String name, String description, Date lastUsage, boolean favorite) {
        this.name = name;
        this.description = description;
        this.lastUsage = lastUsage;
        this.favorite = favorite;
    }

    public AbstractItem() {
        //for serializer
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getUniqueID() {
        return uniqueID;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getTotalTimePrettyPrint() {
        return DurationPrinter.printDuration(getTotalTime());
    }

    public abstract long getTotalTime();
    public abstract boolean isRunning();
    public abstract void stop();
    public abstract void addTimeEntry();
    public abstract List<AbstractItem> getChildren();
    public abstract AbstractItem getParent();
    public abstract void setParent(AbstractItem item);

    public AbstractItem findByUUID(long itemUUID) {
        return null;
    }
}
