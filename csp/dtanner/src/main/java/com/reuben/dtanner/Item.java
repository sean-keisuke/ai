package com.reuben.dtanner;

import java.util.List;

/**
 * @author reuben
 */
public class Item {

    private String name;
    private int size;
    private char posneg;
    private List<String> items;

    public Item(String name, int size, char posneg, List<String> items)
    {
        this.name = name;
        this.size = size;
        this.posneg = posneg;
        this.items = items;
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

    public char getPosneg()
    {
        return posneg;
    }

    public void setPosneg(char posneg)
    {
        this.posneg = posneg;
    }

    public List<String> getItems()
    {
        return items;
    }

    public void setItems(List<String> items)
    {
        this.items = items;
    }
}
