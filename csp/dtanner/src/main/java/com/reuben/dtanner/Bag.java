package com.reuben.dtanner;

import java.util.HashSet;

/**
 *
 * @author reuben
 */
public class Bag {
    
    private int capacity;
    private int currentSize;
    private HashSet<Item> items;
    
    public Bag(int capacity)
    {
        this.capacity = capacity;
        currentSize = 0;
        items = new HashSet<Item>();
    }
    
    /**
     * canAdd needs to be used before this method is called
     * @param item 
     */
    public void add(Item item)
    {
        items.add(item);
        currentSize += item.getSize();
    }
    
    public boolean isFull()
    {
        return capacity == currentSize;
    }

    public boolean canAdd(Item i)
    {
        if (i.getSize() > capacity - currentSize || isFull()) return false;
        for (Item item : items)
            if (!item.canPackWith(i.getIndex()) || !i.canPackWith(item.getIndex()))
                return false;
        return true;
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
