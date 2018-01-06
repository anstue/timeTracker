package com.ti_zero.com.apptime.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.TimeEntry;
import com.ti_zero.com.apptime.databinding.TimeEntryBinding;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class TimeEntryAdapter extends RecyclerView.Adapter<TimeEntryViewHolder> {
    private final Context context;
    private List<TimeEntry> timeEntries;
    private final DataAccessFacade dataAccessFacade;
    private final AccountItem accountItem;

    public TimeEntryAdapter(Context context, AccountItem accountItem, DataAccessFacade dataAccessFacade) {
        this.context = context;
        this.dataAccessFacade = dataAccessFacade;
        this.accountItem = accountItem;
        this.timeEntries = accountItem.getTimeEntries();
    }

    @Override
    public TimeEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Logging.logInfo(LogTag.UI, "Entering TimeEntryAdapter onCreateViewHolder");
        TimeEntryBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.time_entry,
                        parent, false);
        return new TimeEntryViewHolder(binding, dataAccessFacade, this);
    }


    public void setItems(List<TimeEntry> timeEntries) {
        if (this.timeEntries == null) {
            this.timeEntries = timeEntries;
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
                    return timeEntries.get(oldItemPosition).getUniqueID() ==
                            timeEntries.get(newItemPosition).getUniqueID();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;//TODO use equals algorithms to compare
                }
            });
            this.timeEntries = timeEntries;
            result.dispatchUpdatesTo(this);
            Logging.logInfo(LogTag.UI, "Entering TimeEntryAdapter setItems");
        }
    }

    @Override
    public void onBindViewHolder(TimeEntryViewHolder holder, int position) {
        Logging.logInfo(LogTag.UI, "Entering TimeEntryAdapter onBindViewHolder, position: " + position);
        holder.getBinding().setItem(accountItem);
        holder.getBinding().setTimeEntry(timeEntries.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return timeEntries.size();
    }


}