package com.ti_zero.com.apptime.data.dao.db.worker;

import android.support.annotation.NonNull;

import com.ti_zero.com.apptime.data.dao.db.entities.AccountEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;
import com.ti_zero.com.apptime.data.objects.AbstractItem;

/**
 * Created by anstue on 12/24/17.
 */

abstract class GenericWorkerThread implements Runnable {

    @NonNull
    protected AccountEntity convertAccountItemToAccountEntity(long parentUUID, AbstractItem item) {
        return new AccountEntity(item.getName(), item.getDescription(), item.getLastUsage().getTime(),
                item.isFavorite(), item.getUniqueID(), parentUUID);
    }

    @NonNull
    protected GroupEntity convertGroupItemToGroupEntity(Long parentUUID, AbstractItem item) {
        return new GroupEntity(item.getName(), item.getDescription(), item.getLastUsage().getTime(),
                item.isFavorite(), item.getUniqueID(), parentUUID);
    }
}
