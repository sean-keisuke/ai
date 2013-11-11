/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.reuben.dtanner;

import java.util.Arrays;
import java.util.HashSet;
import junit.framework.TestCase;

/**
 *
 * @author reuben
 */
public class SolverTest extends TestCase {
    
    public SolverTest(String testName)
    {
        super(testName);
    }

    public void testDegreeG3()
    {
//        item0 2 - item1 item4 item2 item5 item3
//        item1 4 - item4 item3 item0 item5 item2
//        item2 6 - item3 item5 item0 item4 item1
//        item3 5 - item5 item2
//        item4 4 - item2
//        item5 5
        //when i add item0 to a bag, that bag can now only contain item0 
        //so it would have constraints against all other items
        //if i add item 4 to a bag, it can now only contain item0, item1, item3 or item5
        //if there was another item, item6, that weighed 5 and had + constraints on item1, item2 and item3
        
        HashSet<Item> items = new HashSet<>();
        Item item0 = new Item("item0", 2, '-', new boolean[] {false, true, true, true, true, true});
        Item item1 = new Item("item1", 4, '-', new boolean[] {true, false, true, true, true, true});
        Item item2 = new Item("item2", 6, '-', new boolean[] {true, true, false, true, true, true, false});
        Item item3 = new Item("item3", 5, '-', new boolean[] {false, false, true, false, false, true});
        Item item4 = new Item("item4", 4, '-', new boolean[] {false, false, true, false, false, false});
        Item item5 = new Item("item5", 5, '\0', null);
        items.add(item0);items.add(item1);items.add(item2);items.add(item3);items.add(item4);items.add(item5);

        
        
        Solver solver = new Solver();
        Item result = solver.degree(items);
        assertEquals(item5, result);
    }
    
    public void testDegreeG1()
    {
        //item0 6
        //item1 6 
        //item2 6 
        //item5 3 
        //item6 3 
        //item7 3 
        //item8 2 
        //item9 2 
        //item10 2 
        //item11 2 
        //item12 2 
        //item13 2 
        //item14 1 
        
        Item item0 = new Item("item0", 6, '\0', null);
        Item item1 = new Item("item1", 6, '\0', null);
        Item item2 = new Item("item2", 6, '\0', null);
        Item item5 = new Item("item5", 3, '\0', null);
        Item item6 = new Item("item6", 3, '\0', null);
        Item item7 = new Item("item7", 3, '\0', null);
        Item item8 = new Item("item8", 2, '\0', null);
        Item item9 = new Item("item9", 2, '\0', null);
        Item item10= new Item("item10", 2, '\0', null);
        Item item11= new Item("item11", 2, '\0', null);
        Item item12= new Item("item12", 2, '\0', null);
        Item item13= new Item("item13", 2, '\0', null);
        Item item14= new Item("item14", 1, '\0', null);
        HashSet<Item> items = new HashSet<>();
        items.add(item0);items.add(item1);items.add(item2);items.add(item5);items.add(item6);items.add(item7);
        items.add(item8);items.add(item9);items.add(item10);items.add(item10);items.add(item11);items.add(item12);
        items.add(item13);items.add(item14);
        
        Solver solver = new Solver();
        Item result = solver.degree(items);
        assertEquals(item0, result);
    }
    
    public void testMRVG3()
    {
        HashSet<Item> items = new HashSet<>();
        Item item0 = new Item("item0", 2, '-', new boolean[] {false, true, true, true, true, true});
        Item item1 = new Item("item1", 4, '-', new boolean[] {true, false, true, true, true, true});
        Item item2 = new Item("item2", 6, '-', new boolean[] {true, true, false, true, true, true, false});
        Item item3 = new Item("item3", 5, '-', new boolean[] {false, false, true, false, false, true});
        Item item4 = new Item("item4", 4, '-', new boolean[] {false, false, true, false, false, false});
        Item item5 = new Item("item5", 5, '\0', null);
        items.add(item0);items.add(item1);items.add(item2);items.add(item3);items.add(item4);items.add(item5);
        
        Solver solver = new Solver();
        Item result = solver.MRV(items);
        assertEquals(item2, result);
    }
    
    public void testMRVG1()
    {
        Item item0 = new Item("item0", 6, '\0', null);
        Item item1 = new Item("item1", 6, '\0', null);
        Item item2 = new Item("item2", 6, '\0', null);
        Item item5 = new Item("item5", 3, '\0', null);
        Item item6 = new Item("item6", 3, '\0', null);
        Item item7 = new Item("item7", 3, '\0', null);
        Item item8 = new Item("item8", 2, '\0', null);
        Item item9 = new Item("item9", 2, '\0', null);
        Item item10= new Item("item10", 2, '\0', null);
        Item item11= new Item("item11", 2, '\0', null);
        Item item12= new Item("item12", 2, '\0', null);
        Item item13= new Item("item13", 2, '\0', null);
        Item item14= new Item("item14", 1, '\0', null);
        HashSet<Item> items = new HashSet<>();
        items.add(item0);items.add(item1);items.add(item2);items.add(item5);items.add(item6);items.add(item7);
        items.add(item8);items.add(item9);items.add(item10);items.add(item10);items.add(item11);items.add(item12);
        items.add(item13);items.add(item14);
        
        Solver solver = new Solver();
        Item result = solver.degree(items);
        assertEquals(item0, result);
    }
    
//    public void testAddConstraints()
//    {
//        Bag bag = new Bag(10, 8);
//        //item6 4 + item7 item5 item4 item2 item0
//        //item7 1 - item2 item1 item4 item6 item5
//        //bag both of these and it should be constrained against 1, 2, 3, 4, 5, 6
//        Item item6 = new Item("item6", 4, '+', new boolean[] {false, true, false, true, false, false, true, false});
//        Item item7 = new Item("item7", 1, '-', new boolean[] {false, true, true, false, true, true, true});
//        bag.add(item6);bag.add(item7);
//        
//        for (int i = 1; i < 7; i++)
//        {
//            assertEquals(bag.getDomain()[i], true);
//        }
//    }
      public void testRemove()
      {
        Bag bag1 = new Bag(10, 10);
        Item item6 = new Item("item6", 4, '+', new boolean[] {false, true, false, true, false, false, true, false});
        Item item7 = new Item("item7", 1, '-', new boolean[] {false, true, true, false, true, true, false});
        bag1.add(item7);
        boolean[] domain = bag1.getDomain();
        int size = bag1.remainingCapacity();
        HashSet<Item> items = bag1.getItems();
        
        if (bag1.add(item6))
        {
            assertNotSame(bag1.getDomain(), domain);
            assertNotSame(bag1.remainingCapacity(), size);
            assertNotSame(bag1.getItems(), items);
            bag1.remove(item6);
            assertEquals(bag1.getDomain(), domain);
            assertEquals(bag1.remainingCapacity(), size);
            assertEquals(bag1.getItems(), items);
        }
      }
      
//    public void testLCV()
//      {
//        Bag bag1 = new Bag(10, 10);
//         Bag oldbag1 = new Bag(10, 10);
//        Bag bag2 = new Bag(10, 10);
//        Bag oldbag2= new Bag(10, 10);
//        Item item6 = new Item("item6", 4, '+', new boolean[] {false, true, false, true, false, false, true, true, true});
//        Item item7 = new Item("item7", 1, '-', new boolean[] {false, true, true, false, true, true, false});
//        
//        bag1.add(item6);bag2.add(item7);
//        oldbag1.add(item6);oldbag2.add(item7);
//        Bag[] bags = { bag1, bag2};
//        
//        Solver solver = new Solver();
//        
//        //at this point bag1 and bag2 have 5 weight left
//        //bag 1 has 5 constraints left
//        //bag 2 has 7 constraints left
//        //when we add item8, both bags now have equal weight of 7 
//        //two constraints are added to both making it so that the order should be bag2, bag1
//        //item 8 2 - item8, item9
//        Item item8 = new Item("item8", 2, '-', new boolean[] {false, false, false, false, false, false, false, false, true, true});
//        
//        bags = solver.LCV(bags, item8);
//        
//        assertEquals(Arrays.equals(bags, new Bag[] {oldbag2, oldbag1}), true);
//      }
}
