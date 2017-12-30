package com.ti_zero.com.apptime.data;

import android.arch.lifecycle.MutableLiveData;

import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;

import java.io.File;

/**
 * Created by anstue on 12/24/17.
 */

public interface IDataAccessFacade {

    MutableLiveData<Boolean> isInitialized();

    void createNewItem(GroupItem parent, AbstractItem item);
    void changeItem(AbstractItem item);
    void removeItem(long itemId);

    void startItem(AbstractItem item);
    void stopItem(AbstractItem item);

    void removeItem(GroupItem selectedGroupItem, int position);
    void generateJson(String fileName);
    void loadFromJson(File file);
}
