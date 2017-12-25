package com.ti_zero.com.apptime.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import com.ti_zero.com.apptime.MainTimeActivity;
import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.DataInMemoryStorage;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.dto.ItemRowPair;

import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class AccountItemArrayAdapter extends ArrayAdapter<AbstractItem> {
    public static final String STOP = "Stop";
    public static final String START = "Start";
    public static final int NOTIFICATION_ID_RUNNING_ITEM = 10001;
    private final Context context;
    private final List<AbstractItem> items;
    private ItemRowPair runningItemRowPair;
    private DataAccessFacade dataAccessFacade;

    public AccountItemArrayAdapter(Context context, int textViewResourceId,
                                   List<AbstractItem> objects, DataAccessFacade dataAccessFacade) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.items = objects;
        this.dataAccessFacade = dataAccessFacade;

    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Logging.logInfo(LogTag.UI, "Entering AccountItemArrayAdapter getView");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        boolean firstShow = false;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.item_account, parent, false);
            firstShow = true;
        }
        EditText txtAccountName = (EditText) rowView.findViewById(R.id.txtName);
        final Chronometer chronoTime = (Chronometer) rowView.findViewById(R.id.chronoTime);
        final Button btnToggle = (Button) rowView.findViewById(R.id.btnToggle);
        final Button btnUp = (Button) rowView.findViewById(R.id.btnUp);
        final AbstractItem item = items.get(position);
        txtAccountName.setText(item.getName());
        chronoTime.setBase(SystemClock.elapsedRealtime() - item.getTotalTime());
        if (item.isRunning()) {
            startItem(item, chronoTime, btnToggle, true);
        } else {
            stopItem(new ItemRowPair(item, btnToggle, chronoTime));
        }
        if (firstShow) {
            if (item.getChildren() == null) {
                btnUp.setVisibility(View.INVISIBLE);
            }
            btnToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.isRunning()) {
                        stopItem(new ItemRowPair(item, btnToggle, chronoTime));
                    } else {
                        startItem(item, chronoTime, btnToggle, false);
                    }

                }
            });
            btnUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MainTimeActivity.class);
                    intent.putExtra(MainTimeActivity.ITEM_UUID, item.getUniqueID());
                    context.startActivity(intent);
                }
            });
            txtAccountName.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                public void afterTextChanged(Editable s) {

                    if (!s.toString().equals("")) {
                        item.setName(s.toString());
                    }

                }
            });
        }

        return rowView;
    }

    private void stopItem(ItemRowPair itemRowPair) {
        if (itemRowPair.getItem().isRunning()) {
            dataAccessFacade.stopItem(itemRowPair.getItem());
            Logging.logDebug(LogTag.UI, "stopItem: " + itemRowPair.getItem().getName());
        }
        itemRowPair.getBtnToggle().setText(START);
        itemRowPair.getChronoTime().stop();
        removeNotification();
        runningItemRowPair = null;
    }

    private void removeNotification() {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID_RUNNING_ITEM);
    }

    private void startItem(AbstractItem item, Chronometer chronoTime, Button btnToggle, boolean existingTimeEntry) {
        Logging.logDebug(LogTag.UI, "startItem: " + item.getName());
        if (runningItemRowPair != null && runningItemRowPair.getItem() != item) {
            stopItem(runningItemRowPair);
        } else if (dataAccessFacade.getDataInMemoryStorage().getRootItem().isRunning()) {
            //check rootItem maybe sth is running in another group
            //check if item itself is running(child of group)
            if (!item.isRunning()) {
                dataAccessFacade.stopItem(dataAccessFacade.getDataInMemoryStorage().getRootItem());
                Logging.logDebug(LogTag.UI, "Stopped item over root item: " + item.getName());
            }
        }
        if (!existingTimeEntry) {
            dataAccessFacade.startItem(item);
        }
        chronoTime.start();
        chronoTime.setBase(SystemClock.elapsedRealtime() - item.getTotalTime());
        btnToggle.setText(STOP);
        createNotification(item);
        runningItemRowPair = new ItemRowPair(item, btnToggle, chronoTime);
    }

    private void createNotification(AbstractItem item) {
        Logging.logDebug(LogTag.UI, "creating notification: " + item.getName());
        //TODO only works for older android versions, consider newer ones too, API 26
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("AppTime is running")
                        .setContentText("Item: " + item.getName());

        Intent notificationIntent = new Intent(context, MainTimeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID_RUNNING_ITEM, builder.build());


    }


}