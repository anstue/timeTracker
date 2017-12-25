package com.ti_zero.com.apptime;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.AccountItemArrayAdapter;

public class MainTimeActivity extends AppCompatActivity {

    public static final String ITEM_UUID = "GroupItemUUID";
    private static DataAccessFacade dataAccessFacade;
    private ObjectFactory objectFactory = new ObjectFactory();
    private AccountItemArrayAdapter adapter;
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

        adapter = new AccountItemArrayAdapter(this,
                android.R.layout.simple_expandable_list_item_1, selectedGroupItem.getChildren(), dataAccessFacade);

        setSupportActionBar(toolbar);
        ListView accountItems = (ListView) findViewById(R.id.items);
        accountItems.setAdapter(adapter);
        registerForContextMenu(accountItems);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataAccessFacade.createNewItem(selectedGroupItem, objectFactory.getNewAccountItem());
                adapter.notifyDataSetChanged();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
            }
        });
        Logging.logInfo(LogTag.UI, "MainTimeActivity created with UUID: " + selectedGroupItem.getUniqueID());
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, contextMenu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.btnMenuItemEdit:
                //deleteNote(info.id);
                return true;
            case R.id.btnMenuItemRemove:
                removeItem(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void removeItem(int position) {
        dataAccessFacade.removeItem(selectedGroupItem, position);
        adapter.notifyDataSetChanged();
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
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            //System.out.println(gson.toJson(dataInMemoryStorage.getRootItem())); not working circular dependencies, wait for entities
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

}
