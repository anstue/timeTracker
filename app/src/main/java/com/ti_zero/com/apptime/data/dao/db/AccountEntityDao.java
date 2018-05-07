package com.ti_zero.com.apptime.data.dao.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.ti_zero.com.apptime.data.dao.db.entities.AccountEntity;

import java.util.List;

/**
 * Created by anstue on 12/24/17.
 */


@Dao
public interface AccountEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAccountEntity(AccountEntity accountEntity);

    @Query("select * from accountentity order by lastUsage desc")
    List<AccountEntity> getAllAccountEntities();

    @Query("select * from accountentity where accountEntityId = :accountEntityId")
    List<AccountEntity> getAccountEntity(long accountEntityId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAccountEntity(AccountEntity accountEntity);

    @Query("delete from accountentity where accountEntityId=:accountEntityId")
    void deleteAccountEntity(long accountEntityId);

    @Query("delete from accountentity")
    void removeAll();

    @Query("select * from accountentity where groupEntityId = :groupEntityId order by lastUsage desc")
    List<AccountEntity> getAccountEntities(long groupEntityId);
}
