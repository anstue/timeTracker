package com.ti_zero.com.apptime.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.databinding.AgendaItemBinding;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.builder.AgendaItemBuilder;
import com.ti_zero.com.apptime.ui.objects.AgendaItem;

import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class AgendaAdapter extends RecyclerView.Adapter<AgendaViewHolder> {
    private final Context context;
    private List<AgendaItem> agendaItems;
    private final DataAccessFacade dataAccessFacade;

    public AgendaAdapter(Context context, GroupItem groupItem, DataAccessFacade dataAccessFacade) {
        this.context = context;
        this.dataAccessFacade = dataAccessFacade;
        this.agendaItems = new AgendaItemBuilder(groupItem).build();
    }

    @Override
    public AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Logging.logInfo(LogTag.UI, "Entering AgendaAdapter onCreateViewHolder");
        AgendaItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.agenda_item,
                        parent, false);
        return new AgendaViewHolder(binding, dataAccessFacade, this);
    }


    public void setItems(List<AgendaItem> timeEntries) {
        if (this.agendaItems == null) {
            this.agendaItems = timeEntries;
            notifyItemRangeInserted(0, timeEntries.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return timeEntries.size();
                }

                @Override
                public int getNewListSize() {
                    return timeEntries.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return timeEntries.get(oldItemPosition).getUuid() ==
                            timeEntries.get(newItemPosition).getUuid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;//TODO use equals algorithms to compare
                }
            });
            this.agendaItems = timeEntries;
            result.dispatchUpdatesTo(this);
            Logging.logInfo(LogTag.UI, "Entering AgendaAdapter setItems");
        }
    }

    @Override
    public void onBindViewHolder(AgendaViewHolder holder, int position) {
        Logging.logInfo(LogTag.UI, "Entering AgendaAdapter onBindViewHolder, position: " + position);
        holder.getBinding().setItem(agendaItems.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return agendaItems.size();
    }


}