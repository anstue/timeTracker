package com.ti_zero.com.apptime.ui;

import android.content.Context;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.ui.dto.ItemRowPair;

import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class AccountItemArrayAdapter extends ArrayAdapter<AbstractItem> {
    public static final String STOP = "Stop";
    public static final String START = "Start";
    private final Context context;
    private final List<AbstractItem> items;
    private ItemRowPair runningItemRowPair;

    public AccountItemArrayAdapter(Context context, int textViewResourceId,
                                   List<AbstractItem> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        if (firstShow) {
            if(item.getChildren()==null) {
                btnUp.setVisibility(View.INVISIBLE);
            }
            if (item.isRunning()) {
                btnToggle.setText(STOP);
                chronoTime.setBase(SystemClock.elapsedRealtime() - item.getTotalTime());
                chronoTime.start();
                runningItemRowPair = new ItemRowPair(item, btnToggle, chronoTime);
            } else {
                btnToggle.setText(START);
            }

            btnToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.isRunning()) {
                        stopItem(item, chronoTime, btnToggle);
                    } else {
                        startItem(item, chronoTime, btnToggle);
                    }

                    //TODO refactor adapter and activity
                    //TODO store data persistently use DataStorage and sqlite db to add stuff
                    //TODO logger
                    //TODO handler for toast or snackbar info
                    //TODO introduce i18n
                    //TODO move up and back in groups
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

                    if (!s.equals("")) {
                        item.setName(s.toString());
                    }

                }
            });
        }

        return rowView;
    }

    private void stopItem(AbstractItem item, Chronometer chronoTime, Button btnToggle) {
        item.stop();
        chronoTime.stop();
        btnToggle.setText(START);
    }

    private void startItem(AbstractItem item, Chronometer chronoTime, Button btnToggle) {
        item.addTimeEntry();
        chronoTime.start();
        chronoTime.setBase(SystemClock.elapsedRealtime() - item.getTotalTime());
        btnToggle.setText(STOP);
        if(runningItemRowPair!=null) {
            runningItemRowPair.getItem().stop();
            runningItemRowPair.getBtnToggle().setText(START);
            runningItemRowPair.getChronoTime().stop();
        }
        runningItemRowPair = new ItemRowPair(item, btnToggle, chronoTime);
    }


}