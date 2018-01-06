package com.ti_zero.com.apptime.data.objects;

import com.ti_zero.com.apptime.data.DataInMemoryStorage;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by anstue on 1/6/18.
 */

public class DataInMemoryStorageTest {

    private static final String NAME_1 = "1";
    private static final String NAME_2 = "2";
    private static final String NAME_3 = "3";
    private static long TEST_UUID_1 =1;
    private static long TEST_UUID_2 =2;
    private static long TEST_UUID_3 =3;
    private DataInMemoryStorage dataInMemoryStorage;
    private ObjectFactory objectFactory = new ObjectFactory();


    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        dataInMemoryStorage = new DataInMemoryStorage((GroupItem)new ObjectFactory().getRootItem());
    }

    @Test
    public void testFindItem() {
        fillInMemoryStorage();

        assertThat(dataInMemoryStorage.findItem(TEST_UUID_1).getName(), is(NAME_1));
        assertThat(dataInMemoryStorage.findItem(TEST_UUID_2).getName(), is(NAME_2));
        assertThat(dataInMemoryStorage.findItem(TEST_UUID_3).getName(), is(NAME_3));
    }

    @Test(expected = RuntimeException.class)
    public void testFindItem_shouldFail() {
        assertThat(dataInMemoryStorage.findItem(TEST_UUID_1).getName(), is(NAME_1));
    }

    @Test
    public void testRootItem() {
        assertThat(dataInMemoryStorage.getRootItem().getUniqueID(),is(ObservableWithUUID.ROOT_UUID));
    }

    @Test
    public void testRemoveItemFromRoot() {
        fillInMemoryStorage();

        dataInMemoryStorage.removeItem(TEST_UUID_1);

        assertThat(dataInMemoryStorage.findItem(TEST_UUID_2).getName(), is(NAME_2));
        assertThat(dataInMemoryStorage.findItem(TEST_UUID_3).getName(), is(NAME_3));
        exception.expect(RuntimeException.class);
        dataInMemoryStorage.findItem(TEST_UUID_1);
    }
    @Test
    public void testRemoveItemFromGroup() {
        fillInMemoryStorage();

        dataInMemoryStorage.removeItem(TEST_UUID_3);

        assertThat(dataInMemoryStorage.findItem(TEST_UUID_1).getName(), is(NAME_1));
        assertThat(dataInMemoryStorage.findItem(TEST_UUID_2).getName(), is(NAME_2));
        exception.expect(RuntimeException.class);
        dataInMemoryStorage.findItem(TEST_UUID_3);
    }
    @Test
    public void testRemoveItemRemoveGroup() {
        fillInMemoryStorage();

        dataInMemoryStorage.removeItem(TEST_UUID_2);

        assertThat(dataInMemoryStorage.findItem(TEST_UUID_1).getName(), is(NAME_1));
        exception.expect(RuntimeException.class);
        dataInMemoryStorage.findItem(TEST_UUID_2);
    }

    private void fillInMemoryStorage() {
        AbstractItem item = objectFactory.getNewAccountItem(NAME_1, TEST_UUID_1);
        GroupItem item2 = objectFactory.getNewGroupItem(NAME_2, TEST_UUID_2);
        AbstractItem item3 = objectFactory.getNewAccountItem(NAME_3, TEST_UUID_3);
        item2.addItem(item3);
        dataInMemoryStorage.getRootItem().addItem(item);
        dataInMemoryStorage.getRootItem().addItem(item2);
    }

}
