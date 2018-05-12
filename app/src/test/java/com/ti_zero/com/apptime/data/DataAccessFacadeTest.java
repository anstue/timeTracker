package com.ti_zero.com.apptime.data;

import com.ti_zero.com.apptime.AppExecutors;
import com.ti_zero.com.apptime.BaseApp;
import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.TimeEntry;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by anstue on 5/12/18.
 */
public class DataAccessFacadeTest {

    private DataAccessFacade dataAccessFacade;

    @Before
    public void setUp() {
        BaseApp baseApp = mock(BaseApp.class);
        when(baseApp.getDatabase()).thenReturn(mock(AppDatabase.class));
        AppExecutors appExecutors = mock(AppExecutors.class);
        when(appExecutors.diskIO()).thenReturn(mock(Executor.class));
        when(baseApp.getAppExecutors()).thenReturn(appExecutors);
        dataAccessFacade = new DataAccessFacade(baseApp);
    }


    @Test
    public void testUndoRemoveItem() {
        GroupItem rootItem = dataAccessFacade.getDataInMemoryStorage().getRootItem();
        AccountItem myItem = new ObjectFactory().getNewAccountItem("Test", 1);
        AccountItem removedItem = new ObjectFactory().getNewAccountItem("Removed", 2);
        AccountItem myItem2 = new ObjectFactory().getNewAccountItem("Test2", 3);
        dataAccessFacade.createNewItem(rootItem, myItem);
        dataAccessFacade.createNewItem(rootItem, removedItem);
        dataAccessFacade.createNewItem(rootItem, myItem2);
        assertThat(rootItem.getChildren().size(), is(3));

        dataAccessFacade.removeItem(2);

        assertThat(rootItem.getChildren().size(), is(2));
        assertThat(rootItem.getChildren().get(0), is(myItem2));
        assertThat(rootItem.getChildren().get(1), is(myItem));

        dataAccessFacade.undoRemoveItem(rootItem, removedItem, 1);

        assertThat(rootItem.getChildren().size(), is(3));
        assertThat(rootItem.getChildren().get(1), is(removedItem));
    }

    @Test
    public void testUndoRemoveLastItem() {
        GroupItem rootItem = dataAccessFacade.getDataInMemoryStorage().getRootItem();
        AccountItem myItem = new ObjectFactory().getNewAccountItem("Test", 1);
        AccountItem removedItem = new ObjectFactory().getNewAccountItem("Removed", 2);
        AccountItem myItem2 = new ObjectFactory().getNewAccountItem("Test2", 3);
        dataAccessFacade.createNewItem(rootItem, removedItem);
        dataAccessFacade.createNewItem(rootItem, myItem);
        dataAccessFacade.createNewItem(rootItem, myItem2);
        assertThat(rootItem.getChildren().size(), is(3));

        dataAccessFacade.removeItem(2);

        assertThat(rootItem.getChildren().size(), is(2));
        assertThat(rootItem.getChildren().get(0), is(myItem2));
        assertThat(rootItem.getChildren().get(1), is(myItem));

        dataAccessFacade.undoRemoveItem(rootItem, removedItem, 2);

        assertThat(rootItem.getChildren().size(), is(3));
        assertThat(rootItem.getChildren().get(2), is(removedItem));
    }

    @Test
    public void testStopOtherItemsAndStartItem() {
        SimpleGroupStructure simpleGroupStructure = new SimpleGroupStructure().invoke();
        GroupItem rootItem = simpleGroupStructure.getRootItem();
        AccountItem myItem = simpleGroupStructure.getMyItem();
        AccountItem myItem2 = simpleGroupStructure.getMyItem2();
        AccountItem myItem3 = simpleGroupStructure.getMyItem3();
        GroupItem group1 = simpleGroupStructure.getGroup1();

        assertThat(myItem.isRunning(), is(false));
        assertThat(myItem2.isRunning(), is(false));
        assertThat(myItem3.isRunning(), is(false));
        assertThat(rootItem.isRunning(), is(false));
        assertThat(group1.isRunning(), is(false));

        dataAccessFacade.stopOtherItemsAndStartItem(myItem);

        assertThat(myItem.isRunning(), is(true));
        assertThat(myItem2.isRunning(), is(false));
        assertThat(myItem3.isRunning(), is(false));
        assertThat(rootItem.isRunning(), is(true));
        assertThat(group1.isRunning(), is(false));

        dataAccessFacade.stopOtherItemsAndStartItem(myItem2);

        assertThat(myItem.isRunning(), is(false));
        assertThat(myItem2.isRunning(), is(true));
        assertThat(myItem3.isRunning(), is(false));
        assertThat(rootItem.isRunning(), is(true));
        assertThat(group1.isRunning(), is(false));

        dataAccessFacade.stopOtherItemsAndStartItem(myItem3);

        assertThat(myItem.isRunning(), is(false));
        assertThat(myItem2.isRunning(), is(false));
        assertThat(myItem3.isRunning(), is(true));
        assertThat(rootItem.isRunning(), is(true));
        assertThat(group1.isRunning(), is(true));

    }

    @Test
    public void testStopItem() {
        SimpleGroupStructure simpleGroupStructure = new SimpleGroupStructure().invoke();
        GroupItem rootItem = simpleGroupStructure.getRootItem();
        AccountItem myItem = simpleGroupStructure.getMyItem();
        AccountItem myItem2 = simpleGroupStructure.getMyItem2();
        AccountItem myItem3 = simpleGroupStructure.getMyItem3();
        GroupItem group1 = simpleGroupStructure.getGroup1();

        assertThat(myItem.isRunning(), is(false));
        assertThat(myItem2.isRunning(), is(false));
        assertThat(myItem3.isRunning(), is(false));
        assertThat(rootItem.isRunning(), is(false));
        assertThat(group1.isRunning(), is(false));

        dataAccessFacade.stopOtherItemsAndStartItem(myItem);
        dataAccessFacade.stopItem(myItem);

        assertThat(myItem.isRunning(), is(false));
        assertThat(myItem2.isRunning(), is(false));
        assertThat(myItem3.isRunning(), is(false));
        assertThat(rootItem.isRunning(), is(false));
        assertThat(group1.isRunning(), is(false));

        dataAccessFacade.stopOtherItemsAndStartItem(myItem3);
        dataAccessFacade.stopItem(rootItem);

        assertThat(myItem.isRunning(), is(false));
        assertThat(myItem2.isRunning(), is(false));
        assertThat(myItem3.isRunning(), is(false));
        assertThat(rootItem.isRunning(), is(false));
        assertThat(group1.isRunning(), is(false));


    }

    @Test
    public void testFindItem() {
        new SimpleGroupStructure().invoke();

        assertThat(dataAccessFacade.findItem(4).getUniqueID(), is(4l));
    }

    @Test(expected = RuntimeException.class)
    public void testFindItemFails() {
        new SimpleGroupStructure().invoke();

        dataAccessFacade.findItem(44);
    }

    @Test
    public void testStartAndChangeItemRunningTimeEntry() {
        SimpleGroupStructure simpleGroupStructure = new SimpleGroupStructure().invoke();
        AccountItem myItem = simpleGroupStructure.getMyItem();

        dataAccessFacade.startAndChangeItemRunningTimeEntry(myItem, -10);
        assertThat(myItem.isRunning(), is(true));
        assertThat(myItem.getTotalTime(), greaterThanOrEqualTo(9l * 1000 * 60));

        dataAccessFacade.startAndChangeItemRunningTimeEntry(myItem, -10);
        assertThat(myItem.getTotalTime(), greaterThanOrEqualTo(19l * 1000 * 60));
        dataAccessFacade.stopItem(myItem);
        //checkt that timeentry cannot start before end of previous time entry
        dataAccessFacade.startAndChangeItemRunningTimeEntry(myItem, -10);
        assertThat(myItem.getTotalTime(), greaterThanOrEqualTo(19l * 1000 * 60));
        assertThat(myItem.getTotalTime(), lessThan(25l * 1000 * 60));

        assertThat(myItem.getTimeEntries().size(), is(2));


    }

    @Test
    public void testUndoRemoveTimeEntry() {
        SimpleGroupStructure simpleGroupStructure = new SimpleGroupStructure().invoke();
        AccountItem myItem = simpleGroupStructure.getMyItem();

        dataAccessFacade.stopOtherItemsAndStartItem(myItem);
        TimeEntry forRemoval = myItem.getTimeEntries().get(0);

        dataAccessFacade.removeTimeEntry(myItem, forRemoval);

        assertThat(myItem.getTimeEntries().size(), is(0));

        dataAccessFacade.undoRemoveTimeEntry(myItem, forRemoval);

        assertThat(forRemoval, is(myItem.getTimeEntries().get(0)));

        dataAccessFacade.stopItem(myItem);
        dataAccessFacade.stopOtherItemsAndStartItem(myItem);
        dataAccessFacade.stopItem(myItem);
        dataAccessFacade.stopOtherItemsAndStartItem(myItem);

        assertThat(myItem.getTimeEntries().size(), is(3));
        forRemoval = myItem.getTimeEntries().get(1);
        dataAccessFacade.removeTimeEntry(myItem, forRemoval);
        assertThat(myItem.getTimeEntries().size(), is(2));
        dataAccessFacade.undoRemoveTimeEntry(myItem, forRemoval);
        assertThat(myItem.getTimeEntries().size(), is(3));

        assertThat(myItem.getTimeEntries().size(), is(3));
        forRemoval = myItem.getTimeEntries().get(2);
        dataAccessFacade.removeTimeEntry(myItem, forRemoval);
        assertThat(myItem.getTimeEntries().size(), is(2));
        dataAccessFacade.undoRemoveTimeEntry(myItem, forRemoval);
        assertThat(myItem.getTimeEntries().size(), is(3));


    }

    private class SimpleGroupStructure {
        private GroupItem rootItem;
        private AccountItem myItem;
        private AccountItem myItem2;
        private AccountItem myItem3;
        private GroupItem group1;

        public GroupItem getRootItem() {
            return rootItem;
        }

        public AccountItem getMyItem() {
            return myItem;
        }

        public AccountItem getMyItem2() {
            return myItem2;
        }

        public AccountItem getMyItem3() {
            return myItem3;
        }

        public GroupItem getGroup1() {
            return group1;
        }

        public SimpleGroupStructure invoke() {
            rootItem = dataAccessFacade.getDataInMemoryStorage().getRootItem();
            final ObjectFactory objectFactory = new ObjectFactory();
            myItem = objectFactory.getNewAccountItem("Test", 1);
            myItem2 = objectFactory.getNewAccountItem("Test2", 3);
            myItem3 = objectFactory.getNewAccountItem("Test3", 4);
            group1 = objectFactory.getNewGroupItem("Group1", 5);
            dataAccessFacade.createNewItem(rootItem, myItem);
            dataAccessFacade.createNewItem(rootItem, myItem2);
            dataAccessFacade.createNewItem(rootItem, group1);
            dataAccessFacade.createNewItem(group1, myItem3);
            return this;
        }
    }
}
