package com.system.util.collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class CollectionUtilsTest {

    @Test
    public void iteratorNotNullTest() {
        assertNotNull(CollectionUtils.getIterator(null));
    }

    @Test
    public void iteratorExistsTest() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");
        stringList.add("test2");

        assertNotNull(CollectionUtils.getIterator(stringList));
    }

    @Test
    public void iteratorExists1Test() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");

        assertNotNull(CollectionUtils.getIterator(stringList));
    }

    @Test
    public void iteratorSize1Test() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");

        Iterator<String> iterator = CollectionUtils.getIterator(stringList);
        assertNotNull(iterator);

        int i = 0;
        for (; iterator.hasNext(); ++i)
            iterator.next();

        assertEquals(1, i);
    }
    
    @Test
    public void iteratorSize2Test() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");
        stringList.add("test2");

        Iterator<String> iterator = CollectionUtils.getIterator(stringList);
        assertNotNull(iterator);

        int i = 0;
        for (; iterator.hasNext(); ++i)
            iterator.next();

        assertEquals(1, i);
    }
}
