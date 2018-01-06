package com.ti_zero.com.apptime.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.databinding.ItemAccountBinding;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

/**
 * Created by anstue on 12/28/17.
 */

class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
        PopupMenu.OnMenuItemClickListener {

    private final ItemAccountBinding binding;
    private final DataAccessFacade dataAccessFacade;
    private final ItemAdapter itemAdapter;

    ItemViewHolder(ItemAccountBinding binding, DataAccessFacade dataAccessFacade, ItemAdapter itemAdapter) {
        super(binding.getRoot());
        this.binding = binding;
        this.dataAccessFacade = dataAccessFacade;
        this.itemAdapter = itemAdapter;
        itemView.setOnCreateContextMenuListener(this);
    }


    ItemAccountBinding getBinding() {
        return binding;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        Logging.logInfo(LogTag.UI, "ItemViewHolder onCreateContextMenu ");
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.menu_item, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Logging.logInfo(LogTag.UI, "ItemViewHolder onMenuItemClick, getAdapterPosition:" + getAdapterPosition());
        switch (item.getItemId()) {
            case R.id.btnMenuItemEdit:
                //deleteNote(info.id);
                return true;
            case R.id.btnMenuItemRemove:
                removeItem(getAdapterPosition());
                return true;
            default:
                return true;
        }
    }

    private void removeItem(int position) {
        dataAccessFacade.removeItem(binding.getItem().getParent(), position);
        itemAdapter.notifyDataSetChanged();
    }

}