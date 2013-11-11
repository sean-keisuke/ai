/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reuben.dtanner;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author reuben
 */
public class Solver {

    public String solve(Bag[] bags, HashSet<Item> items)
    {
//        return debugItems(items);
        String result = "failure";
        
        if (items.isEmpty()) { return successString(bags); }
        
        Item restrictiveItem = MRV(items);
        
        for (Bag bag : LCV(bags, restrictiveItem))
        {
            if (bag.add(restrictiveItem))
            {
                items.remove(restrictiveItem);
                if (!failure(bags, items))
                {
                    result = solve(bags, items);
                    if (!result.equals("failure"))
                    {
                        return result;
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
    
    private String debugItems(HashSet<Item> items)
    {
        String tostring = "";
        for (Item item : items)
        {
            tostring += item.debugToString();
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
            tied.add(heaviest);
            return degree(tied);
        }
        return heaviest;
    }
    
    public Bag[] LCV(Bag[] bags, Item item)
    {
        //orders the bags in such a way that the item being added is the least constraining
        //this would involve probably leaving the most space in the bag
        //and by maximizing the amount of falses in the constraint array
        for (Bag bag : bags)
        {
            bag.add(item);
        }
        Arrays.sort(bags);
        for (Bag bag : bags)
        {
            bag.remove(item);
        }
        return bags;
    }
    
    public Item degree(HashSet<Item> items)
    {
        Item highestDegree = new Item("item0", 0, '\0', null);
        int oldHighestConstraintCount = 0;
        for (Item current : items)
        {
            if (current.isWith() == null) continue;
            
            int curConstraintCount = 0; 
            for (Item others : items)
            {
                if (others.getConstraints() != null && current.getIndex() < others.getConstraints().length && others.getConstraints()[current.getIndex()])
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
        
        if (highestDegree.equals(new Item("item0", 0, '\0', null)))
        {
            for (Item item : items)
            {
                highestDegree = item;
                break;
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
        if (items.isEmpty()) return false;
        for (Bag bag : bags)
        {
            for (Item item : items)
            {
                if (bag.canAdd(item))
                    return false;
            }
        }
        return true;
    }
}
