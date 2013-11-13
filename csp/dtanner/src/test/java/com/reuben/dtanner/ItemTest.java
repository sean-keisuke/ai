///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package com.reuben.dtanner;
//
//import junit.framework.TestCase;
//
///**
// *
// * @author reuben
// */
//public class ItemTest extends TestCase {
//    
//    public ItemTest(String testName)
//    {
//        super(testName);
//    }
//
//    public void testCanPackWith()
//    {
//        //will there be instances where an item will be packed with itself?
//        //need to test the case where there are no constraints
//        
//        //item0 5 + item1 item3
//        boolean[] bools = {false, true, false, true};
//        Item item1 = new Item("item0", 5, '+', bools); 
//        
//        boolean result0 = item1.canPackWith(0);
//        boolean result1 = item1.canPackWith(1);
//        boolean result2 = item1.canPackWith(2);
//        boolean result3 = item1.canPackWith(3);
//        boolean result1000 = item1.canPackWith(1000);
//        assertEquals(result0, false);
//        assertEquals(result1, true);
//        assertEquals(result2, false);
//        assertEquals(result3, true);
//        assertEquals(result1000, false);
//        
//        //item1 5 - item2 item4
//        boolean[] bools1 = {false, false, true, false, true};
//        Item item2 = new Item("item1", 5, '-', bools1); 
//        
//        result0 = item2.canPackWith(0);
//        result1 = item2.canPackWith(1);
//        result2 = item2.canPackWith(2);
//        result3 = item2.canPackWith(3);
//        boolean result4 = item2.canPackWith(4);
//        result1000 = item2.canPackWith(1000);
//        assertEquals(result0, true);
//        assertEquals(result1, true);
//        assertEquals(result2, false);
//        assertEquals(result3, true);
//        assertEquals(result4, false);
//        assertEquals(result1000, true);
//        
//        Item item3 = new Item("item3", 5, '\0', null);
//        result0 = item3.canPackWith(0);
//        result1 = item3.canPackWith(1);
//        result2 = item3.canPackWith(2);
//        result3 = item3.canPackWith(1000);
//        assertEquals(result0, true);
//        assertEquals(result1, true);
//        assertEquals(result2, true);
//        assertEquals(result3, true);
//        assertEquals(result1000, true);
//    }
//    
//}
