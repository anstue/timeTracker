package com.ti_zero.com.apptime.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ti_zero.com.apptime.BaseApp;
import com.ti_zero.com.apptime.MainTimeActivity;
import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.TimeEntry;
import com.ti_zero.com.apptime.ui.adapters.TimeEntryAdapter;
import com.ti_zero.com.apptime.ui.helper.DateHelper;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeEntryActivity extends AppCompatActivity {

    private TimeEntryAdapter adapter;
    private AccountItem selectedItem;
    private Dialog timeEntryDialog;
    private DataAccessFacade dataAccessFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_entry);
        dataAccessFacade = ((BaseApp)getApplication()).getDataAccessFacade();

        final long selectedItemUUID = getIntent().getLongExtra(MainTimeActivity.ITEM_UUID, -1);

        if (selectedItemUUID == -1) {
            throw new RuntimeException("TimeEntryActivity opened without ITEM_UUID");
        }
        selectedItem = (AccountItem) dataAccessFacade.getDataInMemoryStorage().findItem(selectedItemUUID);

        adapter = new TimeEntryAdapter(this, selectedItem, dataAccessFacade);
        RecyclerView recyclerViewItems = findViewById(R.id.timeEntries);
        recyclerViewItems.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.timeEntryToolbar);
        toolbar.setTitle(selectedItem.getName());
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_time_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_new_time_entry) {
            timeEntryDialog = new Dialog(this);
            timeEntryDialog.setCancelable(true);
            timeEntryDialog.setContentView(R.layout.create_time_entry);
            ((TextView)timeEntryDialog.findViewById(R.id.txtStartDate)).setText(DateHelper.getStringFromDate(new Date()));
            ((TextView)timeEntryDialog.findViewById(R.id.txtEndDate)).setText(DateHelper.getStringFromDate(new Date()));
            Calendar calendarStartTime = Calendar.getInstance();
            calendarStartTime.add(Calendar.HOUR,-1);
            ((TextView)timeEntryDialog.findViewById(R.id.txtStartTime)).setText(DateHelper.getStringFromTime(calendarStartTime.getTime()));
            ((TextView)timeEntryDialog.findViewById(R.id.txtEndTime)).setText(DateHelper.getStringFromTime(new Date()));
            timeEntryDialog.show();
        }
        return true;
    }

    public void openTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (TimePicker timePicker, int hours, int minutes) -> {
            ((TextView) view).setText(String.format(Locale.US, "%02d:%02d", hours, minutes));
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, 9, 0, true);
        timePickerDialog.show();
    }

    public void openDatePicker(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = (DatePicker datePicker, int year, int month, int day) -> {
            ((TextView) view).setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day));
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void createTimeEntry(View view) {
        String startDate = ((TextView) ((View)view.getParent()).findViewById(R.id.txtStartDate)).getText().toString();
        String startTime = ((TextView) ((View)view.getParent()).findViewById(R.id.txtStartTime)).getText().toString();
        String endDate = ((TextView) ((View)view.getParent()).findViewById(R.id.txtEndDate)).getText().toString();
        String endTime = ((TextView) ((View)view.getParent()).findViewById(R.id.txtEndTime)).getText().toString();
        try {
            Date start = DateHelper.getDateFromString(startDate + " " + startTime);
            Date end = DateHelper.getDateFromString(endDate + " " + endTime);
            if(start.getTime()<end.getTime()) {
                TimeEntry timeEntry = new TimeEntry(start, end);
                dataAccessFacade.addTimeEntry(selectedItem, timeEntry);
                adapter.notifyDataSetChanged();
                timeEntryDialog.dismiss();
            } else {
                Toast.makeText(this, "Start has to be before end", Toast.LENGTH_LONG).show();
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Couldn't parse date", Toast.LENGTH_LONG).show();
        }

    }
}
