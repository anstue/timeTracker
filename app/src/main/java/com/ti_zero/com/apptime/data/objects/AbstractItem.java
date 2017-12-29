package com.ti_zero.com.apptime.data.objects;

import android.databinding.Bindable;

import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.dto.StartItemDTO;
import com.ti_zero.com.apptime.helper.DurationPrinter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by uni on 12/22/17.
 */

public abstract class AbstractItem extends ObjWithUUID implements Serializable {

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

    public String getTotalTimePrettyPrint() {
        return DurationPrinter.printDuration(getTotalTime());
    }

    @Bindable
    public String getBtnToggleText() {
        if(isRunning()) {
            return "Stop";
        } else {
            return "Start";
        }
    }

    public abstract long getTotalTime();
    public abstract boolean isRunning();
    public abstract AccountItem stop();

    /**
     *
     * @return item where timeEntry is added
     */
    public abstract StartItemDTO addTimeEntry();
    public abstract List<AbstractItem> getChildren();
    public abstract GroupItem getParent();
    public abstract void setParent(GroupItem item);

    public AbstractItem findByUUID(long itemUUID) {
        return null;
    }
}
