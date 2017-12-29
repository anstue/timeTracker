package com.ti_zero.com.apptime.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.ti_zero.com.apptime.MainTimeActivity;
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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.AbstractItemViewHolder> {
    public static final String STOP = "Stop";
    public static final String START = "Start";
    public static final int NOTIFICATION_ID_RUNNING_ITEM = 10001;
    private final Context context;
    private List<AbstractItem> items;
    private ItemRowPair runningItemRowPair;
    private DataAccessFacade dataAccessFacade;
    private ItemCallback itemCallback;

    public ItemAdapter(Context context, List<AbstractItem> objects, DataAccessFacade dataAccessFacade,
                       ItemCallback itemClickCallback) {
        this.context = context;
        this.dataAccessFacade = dataAccessFacade;
        this.itemCallback = itemClickCallback;
        this.items = objects;
    }

    @Override
    public AbstractItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAccountBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_account,
                        parent, false);
        binding.setCallback(itemCallback);
        return new AbstractItemViewHolder(binding);
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
        }
    }

    @Override
    public void onBindViewHolder(AbstractItemViewHolder holder, int position) {
        Logging.logInfo(LogTag.UI, "Entering ItemAdapter onBindViewHolder");
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView = inflater.inflate(R.layout.item_account, parent, false);
//        EditText txtAccountName = (EditText) rowView.findViewById(R.id.txtName);
//        final Chronometer chronoTime = (Chronometer) rowView.findViewById(R.id.chronoTime);
//        final Button btnToggle = (Button) rowView.findViewById(R.id.btnToggle);
//        final AbstractItem item = items.get(position);
//        txtAccountName.setText(item.getName());
//        chronoTime.setBase(SystemClock.elapsedRealtime() - item.getTotalTime());
//        if (item.isRunning()) {
//            startItem(item, chronoTime, btnToggle, true);
//        } else {
//            stopItem(new ItemRowPair(item, btnToggle, chronoTime));
//        }
//        btnToggle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (item.isRunning()) {
//                    stopItem(new ItemRowPair(item, btnToggle, chronoTime));
//                } else {
//                    startItem(item, chronoTime, btnToggle, false);
//                }
//
//            }
//        });
//        txtAccountName.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            public void afterTextChanged(Editable s) {
//
//                if (!s.toString().equals("")) {
//                    item.setName(s.toString());
//                    dataAccessFacade.changeItem(item);
//                }
//
//            }
//        });
        holder.binding.setItem(items.get(position));
        holder.binding.executePendingBindings();
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
                AbstractItem item = (AbstractItem)observable;
                if (item.isRunning()) {
                    chronoTime.start();
                } else {
                    chronoTime.stop();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
//
//    private void stopItem(ItemRowPair itemRowPair) {
//        if (itemRowPair.getItem().isRunning()) {
//            dataAccessFacade.stopItem(itemRowPair.getItem());
//            Logging.logDebug(LogTag.UI, "stopItem: " + itemRowPair.getItem().getName());
//        }
//        itemRowPair.getBtnToggle().setText(START);
//        itemRowPair.getChronoTime().stop();
//        removeNotification();
//        runningItemRowPair = null;
//    }
//
//    private void removeNotification() {
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.cancel(NOTIFICATION_ID_RUNNING_ITEM);
//    }
//
//    private void startItem(AbstractItem item, Chronometer chronoTime, Button btnToggle, boolean existingTimeEntry) {
//        Logging.logDebug(LogTag.UI, "startItem: " + item.getName());
//        if (runningItemRowPair != null && runningItemRowPair.getItem() != item) {
//            stopItem(runningItemRowPair);
//        } else if (dataAccessFacade.getDataInMemoryStorage().getRootItem().isRunning()) {
//            //check rootItem maybe sth is running in another group
//            //check if item itself is running(child of group)
//            if (!item.isRunning()) {
//                dataAccessFacade.stopItem(dataAccessFacade.getDataInMemoryStorage().getRootItem());
//                Logging.logDebug(LogTag.UI, "Stopped item over root item: " + item.getName());
//            }
//        }
//        if (!existingTimeEntry) {
//            dataAccessFacade.startItem(item);
//        }
//        chronoTime.start();
//        chronoTime.setBase(SystemClock.elapsedRealtime() - item.getTotalTime());
//        btnToggle.setText(STOP);
//        createNotification(item);
//        runningItemRowPair = new ItemRowPair(item, btnToggle, chronoTime);
//    }
//
//    private void createNotification(AbstractItem item) {
//        Logging.logDebug(LogTag.UI, "creating notification: " + item.getName());
//        //TODO only works for older android versions, consider newer ones too, API 26
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(context)
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentTitle("AppTime is running")
//                        .setContentText("Item: " + item.getName());
//
//        Intent notificationIntent = new Intent(context, MainTimeActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);
//
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(NOTIFICATION_ID_RUNNING_ITEM, builder.build());
//
//
//    }

    static class AbstractItemViewHolder extends RecyclerView.ViewHolder {

        final ItemAccountBinding binding;

        public AbstractItemViewHolder(ItemAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}