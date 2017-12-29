package com.ti_zero.com.apptime.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.SystemClock;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.PopupMenu;

import com.ti_zero.com.apptime.BR;
import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.databinding.ItemAccountBinding;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.callbacks.ItemCallback;
import com.ti_zero.com.apptime.ui.dto.ItemRowPair;

import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private final Context context;
    private List<AbstractItem> items;
    private final DataAccessFacade dataAccessFacade;
    private ItemCallback itemCallback;

    public ItemAdapter(Context context, List<AbstractItem> objects, DataAccessFacade dataAccessFacade,
                       ItemCallback itemClickCallback) {
        this.context = context;
        this.dataAccessFacade = dataAccessFacade;
        this.itemCallback = itemClickCallback;
        this.items = objects;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Logging.logInfo(LogTag.UI, "Entering ItemAdapter onCreateViewHolder");
        ItemAccountBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_account,
                        parent, false);
        binding.setCallback(itemCallback);
        return new ItemViewHolder(binding, dataAccessFacade, this);
    }

    public void setItems(List<AbstractItem> items) {
        if (this.items == null) {
            this.items = items;
            notifyItemRangeInserted(0, items.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return items.size();
                }

                @Override
                public int getNewListSize() {
                    return items.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return items.get(oldItemPosition).getUniqueID() ==
                            items.get(newItemPosition).getUniqueID();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;//TODO use equals algorithms to compare
                }
            });
            this.items = items;
            result.dispatchUpdatesTo(this);
            Logging.logInfo(LogTag.UI, "Entering ItemAdapter setItems");
        }
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Logging.logInfo(LogTag.UI, "Entering ItemAdapter onBindViewHolder, position: " + position);

            holder.getBinding().setItem(items.get(position));
            holder.getBinding().executePendingBindings();
            Chronometer chronoTime = holder.itemView.findViewById(R.id.chronoTime);
            AbstractItem item = items.get(position);
            chronoTime.setBase(SystemClock.elapsedRealtime() - item.getTotalTime());
            if (item.isRunning()) {
                chronoTime.start();
            } else {
                chronoTime.stop();
            }
            item.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    Logging.logInfo(LogTag.UI, "Entering item onPropertyChanged, property: " + i);
                    if (i == BR.btnToggleText) {
                        AbstractItem item = (AbstractItem) observable;
                        chronoTime.setBase(SystemClock.elapsedRealtime() - item.getTotalTime());
                        if (item.isRunning()) {
                            chronoTime.start();
                            Logging.logDebug(LogTag.UI, "item onPropertyChanged: start " + item.getName());
                        } else {
                            chronoTime.stop();
                            Logging.logDebug(LogTag.UI, "item onPropertyChanged: stop");
                        }
                        Logging.logDebug(LogTag.UI, "After item onPropertyChanged, itemName: " + item.getName());
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}