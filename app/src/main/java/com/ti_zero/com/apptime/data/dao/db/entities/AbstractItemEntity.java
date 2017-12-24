package com.ti_zero.com.apptime.data.dao.db.entities;

import java.util.Date;

/**
 * Created by anstue on 12/24/17.
 */

public abstract class AbstractItemEntity {

    private String name;
    private String description;
    private long lastUsage;
    private boolean favorite;

    public AbstractItemEntity(String name, String description, long lastUsage, boolean favorite) {
        this.name = name;
        this.description = description;
        this.lastUsage = lastUsage;
        this.favorite = favorite;
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

    public long getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(long lastUsage) {
        this.lastUsage = lastUsage;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
