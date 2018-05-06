package com.ti_zero.com.apptime.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ti_zero.com.apptime.BaseApp;
import com.ti_zero.com.apptime.MainTimeActivity;
import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;

public class ItemOptionsActivity extends AppCompatActivity {

    private AbstractItem selectedItem;
    private DataAccessFacade dataAccessFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        dataAccessFacade = ((BaseApp) getApplication()).getDataAccessFacade();

        final long selectedGroupUUID = getIntent().getLongExtra(MainTimeActivity.ITEM_UUID, -1);
        if (selectedGroupUUID == -1) {
            selectedItem = dataAccessFacade.getDataInMemoryStorage().getRootItem();
        } else {
            selectedItem = dataAccessFacade.getDataInMemoryStorage().findItem(selectedGroupUUID);
        }
        toolbar.setTitle(selectedItem.getName() + " - Options");
        setSupportActionBar(toolbar);
    }
}
