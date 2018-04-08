package com.ti_zero.com.apptime.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.ti_zero.com.apptime.BaseApp;
import com.ti_zero.com.apptime.MainTimeActivity;
import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.ui.adapters.AgendaAdapter;
import com.ti_zero.com.apptime.ui.adapters.TimeEntryAdapter;

public class AgendaActivity extends AppCompatActivity {

    private GroupItem selectedGroupItem;
    private AgendaAdapter adapter;
    private final DataAccessFacade dataAccessFacade;

    public AgendaActivity() {
        dataAccessFacade = ((BaseApp)getApplication()).getDataAccessFacade();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        final long selectedGroupUUID = getIntent().getLongExtra(MainTimeActivity.ITEM_UUID, -1);
        if (selectedGroupUUID == -1) {
            selectedGroupItem = dataAccessFacade.getDataInMemoryStorage().getRootItem();
        } else {
            selectedGroupItem = (GroupItem) dataAccessFacade.getDataInMemoryStorage().findItem(selectedGroupUUID);
        }
        toolbar.setTitle(selectedGroupItem.getName() + " - Agenda");
        setSupportActionBar(toolbar);

        adapter = new AgendaAdapter(this, selectedGroupItem, dataAccessFacade);
        RecyclerView recyclerViewItems = findViewById(R.id.agendaItems);
        recyclerViewItems.setAdapter(adapter);
    }

}
