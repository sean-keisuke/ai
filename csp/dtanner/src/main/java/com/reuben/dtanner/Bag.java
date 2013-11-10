package com.reuben.dtanner;

import java.util.Arrays;
import java.util.HashSet;

/**
 * truth values in constraints represent that a bag cannot have that item in it
 * @author reuben
 */
public class Bag {
    
    private int capacity;
    private int currentSize;
    private HashSet<Item> items;
    private boolean[] domain;
    
    public Bag(int capacity, int domainSize)
    {
        this.capacity = capacity;
        currentSize = 0;
        items = new HashSet<>();
        domain = new boolean[domainSize];
        Arrays.fill(domain, true);
    }
    
    /**
     * canAdd needs to be used before this method is called
     * @param item 
     */
    public boolean add(Item item)
    {
        if (canAdd(item))
        {
            items.add(item);
            currentSize += item.getSize();
            return true;
        }
        return false;
    }
    
    public boolean isFull()
    {
        return capacity == currentSize;
    }

    private boolean canAdd(Item i)
    {
        if (i.getSize() > capacity - currentSize || isFull()) return false;
        for (Item item : items)
            if (!item.canPackWith(i.getIndex()) || !i.canPackWith(item.getIndex()))
                return false;
        return true;
    }
    
    private void addConstraints(Item i)
    {
        boolean[] itemConst = i.getConstraints();
        for (int j = 0; j < domain.length; j++)
        {
            if (itemConst[j])
            {
                
            }
        }
    }
    
    @Override
    public String toString()
    {
        String toString ="";
        for (Item item : items)
        {
            toString += item.toString() + " ";
        }
        toString += "\n";
        return toString;
    }
}
