package com.ti_zero.com.apptime.data.dao.db.worker;

import android.support.annotation.NonNull;

import com.ti_zero.com.apptime.data.dao.db.entities.AccountEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by anstue on 12/24/17.
 */

abstract class GenericWorkerThread implements Runnable {

    @NonNull
    AccountEntity convertAccountItemToAccountEntity(long parentUUID, AbstractItem item) {
        return new AccountEntity(item.getName(), item.getDescription(), item.getLastUsage().getTime(),
                item.isFavorite(), item.getUniqueID(), parentUUID);
    }

    @NonNull
    GroupEntity convertGroupItemToGroupEntity(Long parentUUID, AbstractItem item) {
        return new GroupEntity(item.getName(), item.getDescription(), item.getLastUsage().getTime(),
                item.isFavorite(), item.getUniqueID(), parentUUID);
    }

    GroupItem convertGroupEntityToGroupItem(@NonNull GroupEntity entity, GroupItem parent) {
        Date lastUsage = getJavaDate(entity.getLastUsage());
        GroupItem newGroupItem = new GroupItem(entity.getName(), entity.getDescription(), lastUsage, entity.isFavorite(), entity.getGroupEntityId());
        if (parent != null) {
            parent.getChildren().add(newGroupItem);
            newGroupItem.setParent(parent);
        }
        return newGroupItem;
    }

    @NonNull
    AccountItem convertAccountEntityToAccountItem(AccountEntity entity, GroupItem parent) {
        Date lastUsage = getJavaDate(entity.getLastUsage());
        AccountItem newAccountItem = new AccountItem(entity.getName(), entity.getDescription(), lastUsage, entity.isFavorite(),entity.getAccountEntityId());
        parent.getChildren().add(newAccountItem);
        newAccountItem.setParent(parent);
        return newAccountItem;

    }

    Date getJavaDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.getTime();
    }
}
