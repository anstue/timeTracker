package com.ti_zero.com.apptime;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.ItemAdapter;
import com.ti_zero.com.apptime.ui.callbacks.ItemCallback;
import com.ti_zero.com.apptime.ui.helper.PermissionHelper;

import java.io.File;
import java.util.Date;

public class MainTimeActivity extends AppCompatActivity {

    public static final String ITEM_UUID = "GroupItemUUID";
    private static final int FILE_OPEN_CODE = 1;
    private static DataAccessFacade dataAccessFacade;
    private ObjectFactory objectFactory = new ObjectFactory();
    private ItemAdapter adapter;
    private GroupItem selectedGroupItem;


    public MainTimeActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (dataAccessFacade == null) {
            setContentView(R.layout.activity_loading);
            dataAccessFacade = new DataAccessFacade(getApplicationContext());
            Logging.logInfo(LogTag.UI, "MainTimeActivity waits for DataAccessFacade: ");
            dataAccessFacade.isInitialized().observe(this, (Boolean initialized) -> {
                if (initialized != null && initialized) {
                    initializeView();
                }
            });

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

        adapter = new ItemAdapter(this, selectedGroupItem.getChildren(), dataAccessFacade, itemClickCallback);
        setSupportActionBar(toolbar);
        RecyclerView recyclerViewItems = (RecyclerView) findViewById(R.id.items);
        recyclerViewItems.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addItem);
        fab.setOnClickListener((View view) -> {
            dataAccessFacade.createNewItem(selectedGroupItem, objectFactory.getNewAccountItem());
            adapter.notifyDataSetChanged();
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void generateJson() {
        dataAccessFacade.generateJson(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/appTime" + new Date().getTime() + ".json");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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

    private final ItemCallback itemClickCallback = new ItemCallback() {
        @Override
        public void onClick(AbstractItem item) {
            Logging.logInfo(LogTag.UI, "ItemCallback called");
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED) && item.getChildren() != null) {
                Intent intent = new Intent(getApplicationContext(), MainTimeActivity.class);
                intent.putExtra(MainTimeActivity.ITEM_UUID, item.getUniqueID());
                getApplicationContext().startActivity(intent);
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
        public void onTextChanged(AbstractItem item) {
            if (!item.getName().equals("")) {
                dataAccessFacade.changeItem(item);
            }
        }
    };

}
