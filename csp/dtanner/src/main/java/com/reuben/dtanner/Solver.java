/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.reuben.dtanner;

import java.util.HashSet;

/**
 *
 * @author reuben
 */
public class Solver {
    private Bag[] bags;
    private HashSet<Item> items;
    
    public Solver(Bag[] bags, HashSet<Item> items)
    {
        this.bags = bags;
        this.items = items;
    }
    
    public String solve()
    {
        for (Item item : items)
        {
            bagItem(item, bags);
        }
        String result = "";
        for (Bag bag : bags)
        {
            result += bag.toString();
        }
        return result;
    }

    private void bagItem(Item item, Bag[] bags)
    {
        for (Bag bag : bags)
        {
            if (bag.canAdd(item))
            {
                bag.add(item);
                break;
            }
        }
    }
}
