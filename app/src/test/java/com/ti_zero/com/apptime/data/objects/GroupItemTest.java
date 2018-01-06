package com.ti_zero.com.apptime.data.objects;

import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by anstue on 1/6/18.
 */

public class GroupItemTest {

    private final static long TEST_UUID_1=1;
    private final static long TEST_UUID_2=2;
    private final static long TEST_UUID_3=3;
    private final static String NAME_1="1";
    private final static String NAME_2="2";
    private final static String NAME_3="3";
    private GroupItem groupItem;
    private ObjectFactory objectFactory = new ObjectFactory();

    @Before
    public void setup() {
        groupItem = new GroupItem("1","", new Date(), false, TEST_UUID_1);
    }

    @Test
    public void testAddItem() {
        groupItem.addItem(objectFactory.getNewAccountItem(NAME_1, TEST_UUID_1));
        GroupItem newGroupItem = objectFactory.getNewGroupItem(NAME_2, TEST_UUID_2);
        groupItem.addItem(newGroupItem);
        newGroupItem.addItem(objectFactory.getNewAccountItem(NAME_3, TEST_UUID_3));

        assertThat(groupItem.getChildren().size(), is(2));
        assertThat(groupItem.getChildren().get(0).getUniqueID(), is(TEST_UUID_1));
        assertThat(groupItem.getChildren().get(1).getUniqueID(), is(TEST_UUID_2));

        assertThat(groupItem.getChildren().get(1).getChildren().size(), is(1));
        assertThat(groupItem.getChildren().get(1).getChildren().get(0).getUniqueID(), is(TEST_UUID_3));
    }

    @Test
    public void testIsRunning() {
        assertThat(groupItem.isRunning(), is(false));
        groupItem.addTimeEntry();
        assertThat(groupItem.isRunning(), is(true));
        groupItem.stop();
        assertThat(groupItem.isRunning(), is(false));


    }

    @Test
    public void testAddTimeEntry() {
        groupItem.addTimeEntry();
        assertThat(groupItem.getChildren().size(), is(1));
    }

    @Test
    public void testGetTodayTime() {
        fillGroupItem();
        assertThat(groupItem.getTodayTime(), is(100l));
    }

    @Test
    public void testGetTotalTime() {
        fillGroupItem();
        assertThat(groupItem.getTotalTime(), is(250l));
    }
    private void fillGroupItem() {
        AccountItem item = objectFactory.getNewAccountItem(NAME_1, TEST_UUID_1);
        item.getTimeEntries().add(new TimeEntry(new Date(0), new Date(40)));
        GroupItem item2 = objectFactory.getNewGroupItem(NAME_2, TEST_UUID_2);
        AccountItem item3 = objectFactory.getNewAccountItem(NAME_3, TEST_UUID_3);
        item3.getTimeEntries().add(new TimeEntry(new Date(40), new Date(150)));
        //add timeEntries which are today
        Date today = getToday();
        item.getTimeEntries().add(new TimeEntry(new Date(today.getTime()), new Date(today.getTime()+ 40)));
        item3.getTimeEntries().add(new TimeEntry(new Date(today.getTime()+40), new Date(today.getTime()+100)));

        item2.addItem(item3);
        groupItem.addItem(item);
        groupItem.addItem(item2);
    }

    public Date getToday() {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }
}
