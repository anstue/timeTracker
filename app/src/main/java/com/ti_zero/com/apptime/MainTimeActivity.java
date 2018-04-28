package com.ti_zero.com.apptime;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.AgendaActivity;
import com.ti_zero.com.apptime.ui.adapters.ItemAdapter;
import com.ti_zero.com.apptime.ui.TimeEntryActivity;
import com.ti_zero.com.apptime.ui.callbacks.ItemCallback;
import com.ti_zero.com.apptime.ui.helper.PermissionHelper;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainTimeActivity extends AppCompatActivity {

    public static final String ITEM_UUID = "GroupItemUUID";
    private static final int FILE_OPEN_CODE = 1;
    public static final String NOTIFICATION_CHANNEL_ITEM_ID_INFO = "ITEM_CHANNEL";
    public static final String NOTIFICATION_CHANNEL_PROCESSING_ID_INFO = "PROCESSING_CHANNEL";
    private ObjectFactory objectFactory = new ObjectFactory();

    private DataAccessFacade dataAccessFacade;
    private ItemAdapter adapter;
    private GroupItem selectedGroupItem;
    private Dialog changeNameDialog;


    public MainTimeActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (dataAccessFacade == null) {
            dataAccessFacade= ((BaseApp)getApplication()).getDataAccessFacade();
            setContentView(R.layout.activity_loading);
            Logging.logInfo(LogTag.UI, "MainTimeActivity waits for DataAccessFacade: ");
            dataAccessFacade.isInitialized().observe(this, (Boolean initialized) -> {
                if (initialized != null && initialized) {
                    initializeView();
                }
            });
// Register NotificationChannels needed for API 26+ to display notification messages
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager = (NotificationManager)getSystemService(
                        Context.NOTIFICATION_SERVICE);
                NotificationChannel infoChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ITEM_ID_INFO,
                        getString(R.string.notification_channel_item_name_info), NotificationManager.IMPORTANCE_DEFAULT);
                infoChannel.setDescription(getString(R.string.notification_channel_item_description_info));
                infoChannel.enableLights(false);
                infoChannel.enableVibration(false);
                mNotificationManager.createNotificationChannel(infoChannel);

                NotificationChannel infoChannel_Processing = new NotificationChannel(NOTIFICATION_CHANNEL_PROCESSING_ID_INFO,
                        getString(R.string.notification_channel_processing_name_info), NotificationManager.IMPORTANCE_DEFAULT);
                infoChannel_Processing.setDescription(getString(R.string.notification_channel_processing_description_info));
                infoChannel_Processing.enableLights(false);
                infoChannel_Processing.enableVibration(false);
                mNotificationManager.createNotificationChannel(infoChannel_Processing);
            }
        } else {
            initializeView();
        }
    }


    private void initializeView() {
        setContentView(R.layout.activity_main_time);
        //we cannot pass an object here, because it gets serialized and we get a new instance her. That's bad in a tree which works with references
        final long selectedGroupUUID = getIntent().getLongExtra(ITEM_UUID, -1);

        if (selectedGroupUUID == -1) {
            selectedGroupItem = dataAccessFacade.getDataInMemoryStorage().getRootItem();
        } else {
            selectedGroupItem = (GroupItem) dataAccessFacade.getDataInMemoryStorage().findItem(selectedGroupUUID);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(selectedGroupItem.getName());
        List<AbstractItem> children = selectedGroupItem.getChildren();
        Collections.sort(children);
        adapter = new ItemAdapter(this, children, dataAccessFacade, new ItemClickCallback(this));
        setSupportActionBar(toolbar);
        RecyclerView recyclerViewItems = (RecyclerView) findViewById(R.id.items);
        recyclerViewItems.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addItem);
        fab.setOnClickListener((View view) -> {
            dataAccessFacade.createNewItem(selectedGroupItem, objectFactory.getNewAccountItem());
            adapter.notifyDataSetChanged();
            recyclerViewItems.scrollToPosition(0);
        });
        Logging.logInfo(LogTag.UI, "MainTimeActivity created with UUID: " + selectedGroupItem.getUniqueID());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
            return true;
        } else if (id == R.id.btnMenuMainNewGroup) {
            dataAccessFacade.createNewItem(selectedGroupItem, objectFactory.getNewGroupItem());
            adapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.action_exportToJson) {
            PermissionHelper.verifyStoragePermissions(this);
            generateJson();

        } else if (id == R.id.action_importFromJson) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivityForResult(intent, FILE_OPEN_CODE);
        } else if (id == R.id.action_agenda) {
            Intent intent = new Intent(this, AgendaActivity.class);
            intent.putExtra(MainTimeActivity.ITEM_UUID, selectedGroupItem.getUniqueID());
            this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void generateJson() {
        dataAccessFacade.generateJson(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/appTime" + new Date().getTime() + ".json");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionHelper.REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generateJson();
                } else {

                    //TODO handle permission denied
                }
                return;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void onChangeName(View view) {
        TextView txtName = ((View) view.getParent()).findViewById(R.id.txtChangedName);
        long uuid = (long) txtName.getTag();
        if (!txtName.getText().toString().equals("")) {
            AbstractItem item = dataAccessFacade.findItem(uuid);
            item.setName(txtName.getText().toString());
            dataAccessFacade.changeItem(item);
            changeNameDialog.dismiss();
        }
    }

    class ItemClickCallback implements ItemCallback {

        private Context context;

        public ItemClickCallback(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(AbstractItem item) {
            Logging.logInfo(LogTag.UI, "ItemCallback called");
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                if (item.getChildren() != null) {
                    Intent intent = new Intent(getApplicationContext(), MainTimeActivity.class);
                    intent.putExtra(MainTimeActivity.ITEM_UUID, item.getUniqueID());
                    context.startActivity(intent);
                } else { //AccountItem
                    Intent intent = new Intent(getApplicationContext(), TimeEntryActivity.class);
                    intent.putExtra(MainTimeActivity.ITEM_UUID, item.getUniqueID());
                    context.startActivity(intent);
                }
            }
        }

        @Override
        public void onBtnClick(AbstractItem item) {
            Logging.logInfo(LogTag.UI, "StartStopCallback called");
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                if (item.isRunning()) {
                    stopItem(item, getApplicationContext(), dataAccessFacade);
                } else {
                    startItem(item, false, getApplicationContext(), dataAccessFacade);
                }
            }
        }

        @Override
        public void onBtnMinus10Click(AbstractItem item) {
            Logging.logInfo(LogTag.UI, "onBtnMinus10Click called");
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                startAndChangeItemRunningTimeEntry(item, getApplicationContext(), dataAccessFacade, -10);
            }
        }

        @Override
        public void onBtnPlus10Click(AbstractItem item) {
            Logging.logInfo(LogTag.UI, "onBtnPlus10Click called");
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                startAndChangeItemRunningTimeEntry(item, getApplicationContext(), dataAccessFacade, 10);
            }
        }

        @Override
        public void openChangeNameDialog(AbstractItem item) {
            changeNameDialog = new Dialog(context);
            changeNameDialog.setCancelable(true);
            changeNameDialog.setContentView(R.layout.change_name_dialog);
            ((TextView) changeNameDialog.findViewById(R.id.txtChangedName)).setText(item.getName());
            ((TextView) changeNameDialog.findViewById(R.id.txtChangedName)).setTag(item.getUniqueID());
            changeNameDialog.show();
        }
    }

}
