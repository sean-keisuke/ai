package com.reuben.dtanner;

import java.util.List;

/**
 * @author reuben
 */
public class Item {

    private String name;
    private int size;
    private Boolean posneg;
    private HashSet<String> items;

    public Item(String name, int size, Boolean posneg)
    {
        this.name = name;
        this.size = size;
        this.posneg = posneg;
        items = new HashSet<String>();
    }
    
    public void addConstraint(Item item)
    {
        items.add(item);
    }
    
    public boolean canPackWith(Item i)
    {
        if (posneg == null) return true;
        return items.contains(i) ? true : false; 
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public Boolean getPosneg()
    {
        return posneg;
    }

    public void setPosneg(Boolean posneg)
    {
        this.posneg = posneg;
    }

    public HashSet<String> getItems()
    {
        return items;
    }

    public void setItems(HashSet<String> items)
    {
        this.items = items;
    }
    
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
