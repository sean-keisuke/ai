/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reuben.dtanner;

import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author reuben
 */
public class Solver {
    
    private Bag[] initialBags;
    private HashSet<Item> initialItems;
    
    public Solver(Bag[] bags, HashSet<Item> items)
    {
        this.initialBags = bags;
        this.initialItems = items;
    }
    
    public String solve()
    {
        return solve(initialBags, initialItems);
    }

    public String solve(Bag[] bags, HashSet<Item> items)
    {
        String result = "failure";
        
        if (items.isEmpty()) { return successString(bags); }
        //may need to also check if the bags are in a successful state as well
        
        Item restrictiveItem = MRV(items);
        
        for (Bag bag : LCV(bags, restrictiveItem))
        {
            if (bag.add(restrictiveItem))
            {
                items.remove(restrictiveItem);
                bag = modifyBagConstraints(bag); //this is the inference step
                
                //need to make sure that the bag being modified is still backed by Bag[] bags
                if (!failure(bags, items))
                {
                    result = solve(bags, items);
                    if (!result.equals("failure"))
                    {
                        return successString(bags);
                    }
                }
                break;
            }
        }
        return result;
    }
    
    private String successString(Bag[] bags)
    {
        String tostring = "";
        for (Bag bag : bags)
        {
            tostring += bag.toString();
        }
        return tostring;
        
    }

    public Item MRV(HashSet<Item> items)
    {
        HashSet<Item> tied = new HashSet<Item>();
        Item heaviest = new Item("item0", Integer.MIN_VALUE, '\0', null);
        for (Item item : items)
        {
            if (item.getSize() > heaviest.getSize())
            {
                heaviest = item;
            }
        }
        for (Item item : items)
        {
            if (item.getSize() == heaviest.getSize() && !item.equals(heaviest))
            {
                tied.add(item);
            }
        }
        if (!tied.isEmpty())
        {
            return degree(tied);
        }
        return heaviest;
    }
    
    private Bag[] LCV(Bag[] bags, Item item)
    {
        //orders the bags in such a way that the item being added is the least constraining
        //this would involve probably leaving the most space in the bag
        //and by maximizing the amount of falses in the constraint array
        return null;
    }
    
    public Item degree(HashSet<Item> items)
    {
        Item highestDegree = new Item("item0", 0, '\0', null);
        int oldHighestConstraintCount = 0;
        for (Item current : items)
        {
            int curConstraintCount = 0; 
            for (Item others : items)
            {
                if (others.getConstraints() != null && others.getConstraints()[current.getIndex()])
                {
                    curConstraintCount++;
                }
            }
            if (curConstraintCount > oldHighestConstraintCount)
            {
                highestDegree = current;
                oldHighestConstraintCount = curConstraintCount;
            }
        }
        
        return highestDegree;
    }

    private Bag modifyBagConstraints(Bag bag)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean failure(Bag[] bags, HashSet<Item> items)
    {
        //failure would imply that there are items left but none can be added to any bags
        //which could be interpreted as bags with domains that mismatch the remaining items
        for (Bag bag : bags)
        {
            for (Item item : items)
            {
                
            }
        }
        return true;
    }
}
