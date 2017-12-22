package com.ti_zero.com.apptime;

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

import com.ti_zero.com.apptime.data.DataStorage;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;
import com.ti_zero.com.apptime.ui.AccountItemArrayAdapter;

public class MainTimeActivity extends AppCompatActivity {

    private static DataStorage dataStorage = new DataStorage();
    private ObjectFactory objectFactory = new ObjectFactory();
    private AccountItemArrayAdapter adapter;


    public MainTimeActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AccountItemArrayAdapter(this,
                android.R.layout.simple_expandable_list_item_1, dataStorage.getSelectedGroup().getChildren());
        setContentView(R.layout.activity_main_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView accountItems = (ListView) findViewById(R.id.items);
        accountItems.setAdapter(adapter);
        registerForContextMenu(accountItems);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataStorage.getSelectedGroup().addItem(objectFactory.getNewAccountItem());
                adapter.notifyDataSetChanged();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
            }
        });

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
            case R.id.btnMenuItemOpen:
                //editNote(info.position);
                return true;
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
        dataStorage.getSelectedGroup().removeItem(position);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.btnMenuMainNewGroup) {
            dataStorage.getSelectedGroup().addItem(objectFactory.getNewGroupItem());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
