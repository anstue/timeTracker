package com.ti_zero.com.apptime.data;

import com.ti_zero.com.apptime.AppExecutors;
import com.ti_zero.com.apptime.BaseApp;
import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executor;

import static org.hamcrest.MatcherAssert.assertThat;
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
        assertThat(rootItem.getChildren().size(),is(3));

        dataAccessFacade.removeItem(2);

        assertThat(rootItem.getChildren().size(),is(2));
        assertThat(rootItem.getChildren().get(0),is(myItem2));
        assertThat(rootItem.getChildren().get(1),is(myItem));

        dataAccessFacade.undoRemoveItem(rootItem,removedItem, 1);

        assertThat(rootItem.getChildren().size(),is(3));
        assertThat(rootItem.getChildren().get(1),is(removedItem));
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
        assertThat(rootItem.getChildren().size(),is(3));

        dataAccessFacade.removeItem(2);

        assertThat(rootItem.getChildren().size(),is(2));
        assertThat(rootItem.getChildren().get(0),is(myItem2));
        assertThat(rootItem.getChildren().get(1),is(myItem));

        dataAccessFacade.undoRemoveItem(rootItem,removedItem, 2);

        assertThat(rootItem.getChildren().size(),is(3));
        assertThat(rootItem.getChildren().get(2),is(removedItem));
    }
}
