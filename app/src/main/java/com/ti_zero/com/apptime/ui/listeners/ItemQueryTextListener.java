package com.ti_zero.com.apptime.ui.listeners;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.widget.SearchView;

import com.ti_zero.com.apptime.MainTimeActivity;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

/**
 * Created by anstue on 5/6/18.
 */
public class ItemQueryTextListener implements SearchView.OnQueryTextListener {

    private Context context;
    private long uuid;

    public ItemQueryTextListener(Context context, long uuid) {
        this.context = context;
        this.uuid = uuid;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Intent intent = new Intent(context, MainTimeActivity.class);
        intent.putExtra(MainTimeActivity.ITEM_UUID, uuid);
        propagateActiveSearch(intent, s);
        context.startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private void propagateActiveSearch(Intent intent, String searchString) {
        Logging.logInfo(LogTag.UI, "propagateActiveSearch called");
        intent.putExtra(SearchManager.QUERY, searchString);
        intent.setAction(Intent.ACTION_SEARCH);
    }
}
