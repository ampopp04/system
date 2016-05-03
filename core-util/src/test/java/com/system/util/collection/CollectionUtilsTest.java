package com.system.util.collection;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CollectionUtilsTest {

    @Test
    public void iteratorNotNullTest() {
        assertNotNull(CollectionUtils.getIterator(null));
    }
    
    @Test
    public void iteratorExistsTest(){
      List<String> stringList = new ArrayList<>();
      stringList.add("test1");
      stringList.add("test2");
      
      assertNotNull(CollectionUtils.getIterator(stringList));
    }
}