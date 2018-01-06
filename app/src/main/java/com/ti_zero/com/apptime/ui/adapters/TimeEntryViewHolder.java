package com.ti_zero.com.apptime.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
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

    TimeEntryViewHolder(TimeEntryBinding binding, DataAccessFacade dataAccessFacade, TimeEntryAdapter timeEntryAdapter) {
        super(binding.getRoot());
        this.binding = binding;
        this.dataAccessFacade = dataAccessFacade;
        this.timeEntryAdapter = timeEntryAdapter;
        itemView.setOnCreateContextMenuListener(this);
    }

    public TimeEntryBinding getBinding() {
        return binding;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        Logging.logInfo(LogTag.UI, "TimeEntryViewHolder onCreateContextMenu ");
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.menu_item, popup.getMenu());
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
        dataAccessFacade.removeTimeEntry(binding.getItem(), binding.getTimeEntry());
        timeEntryAdapter.notifyDataSetChanged();
    }
}
