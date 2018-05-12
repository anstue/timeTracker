package com.ti_zero.com.apptime.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.TimeEntry;
import com.ti_zero.com.apptime.databinding.TimeEntryBinding;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

/**
 * Created by anstue on 1/6/18.
 */

class TimeEntryViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
        PopupMenu.OnMenuItemClickListener {


    private final TimeEntryBinding binding;
    private final DataAccessFacade dataAccessFacade;
    private final TimeEntryAdapter timeEntryAdapter;
    private final Context context;

    TimeEntryViewHolder(TimeEntryBinding binding, DataAccessFacade dataAccessFacade, TimeEntryAdapter timeEntryAdapter, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.dataAccessFacade = dataAccessFacade;
        this.timeEntryAdapter = timeEntryAdapter;
        itemView.setOnCreateContextMenuListener(this);
        this.context = context;
    }

    public TimeEntryBinding getBinding() {
        return binding;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        Logging.logInfo(LogTag.UI, "TimeEntryViewHolder onCreateContextMenu ");
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.menu_time_entry, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();

    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Logging.logInfo(LogTag.UI, "TimeEntryViewHolder onMenuItemClick, getAdapterPosition:" + getAdapterPosition());
        switch (item.getItemId()) {
            case R.id.btnMenuItemEdit:
                return true;
            case R.id.btnMenuItemRemove:
                removeItem();
                return true;
            default:
                return true;
        }
    }

    private void removeItem() {
        TimeEntry timeEntryToBeRemoved =  binding.getTimeEntry();
        AccountItem parent = binding.getItem();
        dataAccessFacade.removeTimeEntry(parent, timeEntryToBeRemoved);
        timeEntryAdapter.notifyDataSetChanged();
        Snackbar snackbar = Snackbar.make(
                ((Activity) context).getLayoutInflater().inflate(R.layout.snackbar_time_entry_removed,
                        ((Activity) context).findViewById(android.R.id.content)), R.string.time_entry_removed,
                Snackbar.LENGTH_LONG)
                .setAction("UNDO", (View view) -> {
                    dataAccessFacade.undoRemoveTimeEntry(parent, timeEntryToBeRemoved);
                    timeEntryAdapter.notifyDataSetChanged();
                });
        snackbar.show();
    }
}
