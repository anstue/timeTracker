package com.ti_zero.com.apptime.ui.dto;

import android.widget.Button;
import android.widget.Chronometer;

import com.ti_zero.com.apptime.data.objects.AbstractItem;

/**
 * Created by uni on 12/22/17.
 */

public class ItemRowPair {

    private AbstractItem item;
    private Button btnToggle;
    private Chronometer chronoTime;

    public ItemRowPair(AbstractItem item, Button btnToggle, Chronometer chronoTime) {
        this.item = item;
        this.btnToggle = btnToggle;
        this.chronoTime = chronoTime;
    }

    public AbstractItem getItem() {
        return item;
    }

    public Button getBtnToggle() {
        return btnToggle;
    }

    public Chronometer getChronoTime() {
        return chronoTime;
    }

}
