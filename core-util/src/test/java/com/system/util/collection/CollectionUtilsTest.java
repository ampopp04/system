package com.system.util.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CollectionUtilsTest {

    @Test
    public void iterableNotNullTest() {
        List arrayList = null;
        assertNotNull(CollectionUtils.getIterable(arrayList));
    }

    @Test
    public void iterableExistsTest() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");
        stringList.add("test2");

        assertNotNull(CollectionUtils.getIterable(stringList));
    }

    @Test
    public void iterableExists1Test() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");

        assertNotNull(CollectionUtils.getIterable(stringList));
    }

    @Test
    public void iterableSize1Test() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");

        Iterable<String> iterable = CollectionUtils.getIterable(stringList);
        assertNotNull(iterable);

        assertEquals(1, CollectionUtils.getSize(iterable));
    }

    @Test
    public void iterableSize2Test() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");
        stringList.add("test2");

        Iterable<String> iterable = CollectionUtils.getIterable(stringList);
        assertNotNull(iterable);
        assertEquals(2, CollectionUtils.getSize(iterable));
    }
}