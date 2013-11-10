/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.reuben.dtanner;

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

        
        
        Solver solver = new Solver(null, items);
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
        
        Solver solver = new Solver(null, items);
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
        
        Solver solver = new Solver(null, items);
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
        
        Solver solver = new Solver(null, items);
        Item result = solver.degree(items);
        assertEquals(item0, result);
    }
}
