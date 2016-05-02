package com.system.util.collection;

import org.junit.*;
import static org.junit.Assert.assertNotNull;

public class CollectionUtilsTests {

    @Test
    public void iteratorNotNullTest() {
        assertNotNull(CollectionUtils.getIterator(null));
        assertNotNull(null);
    }
}