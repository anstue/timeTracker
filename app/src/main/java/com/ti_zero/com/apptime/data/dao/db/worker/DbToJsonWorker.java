package com.ti_zero.com.apptime.data.dao.db.worker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.dao.db.entities.AccountEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.TimeEntity;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.TimeEntry;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.helper.NotificationHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by anstue on 12/24/17.
 */

public class DbToJsonWorker extends GenericWorkerThread {

    private AppDatabase appDatabase;
    private String fileName;
    private Context context;

    public DbToJsonWorker(AppDatabase appDatabase, String fileName, Context context) {
        this.appDatabase = appDatabase;
        this.fileName = fileName;
        this.context = context;
    }

    @Override
    public void run() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        NotificationHelper.createLoadingNotification(context);
        List<TimeEntity> timeEntities = appDatabase.timeEntityDao().getAllTimeEntities();
        List<AccountEntity> accountEntities = appDatabase.accountEntityDao().getAllAccountEntities();
        List<GroupEntity> groupEntities = appDatabase.groupEntityDao().getAllGroupEntities();
        //close timeEntries
        for(TimeEntity timeEntity: timeEntities) {
            if(timeEntity.getEnd() == -1) {
                timeEntity.setEnd(new Date().getTime());
                Logging.logInfo(LogTag.PERSISTENZ, "DbToJsonWorker closing open TimeEntry for export");
            }
        }
        try {
            Logging.logInfo(LogTag.PERSISTENZ, "DbToJsonWorker writing to file: "+ fileName);
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.print(gson.toJson(timeEntities));
            writer.print(gson.toJson(accountEntities));
            writer.print(gson.toJson(groupEntities));
            writer.flush();
            writer.close();
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
            Logging.logError(LogTag.UI, e.getMessage());
        }
        NotificationHelper.removeLoadingNotification(context);
        Logging.logInfo(LogTag.PERSISTENZ, "DbToJsonWorker finished");
    }
}
