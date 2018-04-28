package com.ti_zero.com.apptime.data.dao.db;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ti_zero.com.apptime.AppExecutors;
import com.ti_zero.com.apptime.BaseApp;
import com.ti_zero.com.apptime.data.dao.db.entities.AccountEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.TimeEntity;

/**
 * Created by anstue on 12/24/17.
 */


@Database(entities = {AccountEntity.class, TimeEntity.class, GroupEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "itemdatabase";
    private static AppDatabase INSTANCE;

    public abstract AccountEntityDao accountEntityDao();

    public abstract GroupEntityDao groupEntityDao();

    public abstract TimeEntityDao timeEntityDao();

    private MediatorLiveData<Boolean> initialized = new MediatorLiveData<>();

    private final MediatorLiveData<Boolean> databaseCreated = new MediatorLiveData<>();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context.getApplicationContext(), executors);
                    INSTANCE.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(new DbCreator(appContext, executors));
                    }
                }).build();
    }

    private void setDatabaseCreated() {
        databaseCreated.postValue(true);
    }

    public MediatorLiveData<Boolean> getDatabaseCreated() {
        return databaseCreated;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public MutableLiveData<Boolean> isInitialized() {
        return initialized;
    }

    /**
     * you can only set it to true
     */
    public void setInitialized() {
        this.initialized.postValue(true);
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }


    private static class DbCreator implements Runnable {

        private Context appContext;
        private AppExecutors appExecutors;

        public DbCreator(Context appContext, AppExecutors appExecutors) {
            this.appContext = appContext;
            this.appExecutors = appExecutors;
        }

        @Override
        public void run() {
            AppDatabase database = AppDatabase.getInstance(appContext, appExecutors);
            database.setDatabaseCreated();
        }
    }
}
