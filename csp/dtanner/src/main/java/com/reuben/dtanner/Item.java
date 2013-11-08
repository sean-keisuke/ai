package com.reuben.dtanner;

/**
 * @author reuben
 */
public class Item{

    private int index;
    private int size;
    private Boolean with;
    private boolean[] items;

    public Item(String name, int size, char posneg, boolean[] items)
    {
        this.index = Integer.parseInt(name.substring(4));
        this.size = size;
        this.with = posneg == '\0' ? null : posneg == '+' ? true : false ;
        this.items = items;
    }
    
    public boolean canPackWith(int item)
    {
        if (with == null) return true;
        if (item >= items.length) return !with;
        else return with == items[item];
    }

    @Override
    public int hashCode()
    {
        return 31*this.index;
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.index == ((Item)obj).index;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public Boolean isWith()
    {
        return with;
    }

    public void setWith(Boolean with)
    {
        this.with = with;
    }

    public boolean[] getItems()
    {
        return items;
    }

    public void setItems(boolean[] items)
    {
        this.items = items;
    }
    
    @Override
    public String toString()
    {
        return "item" + this.index;
    }
    
    public String debugToString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("item").append(this.index).append(" ").append(this.size).append(" ");
        if (with != null)
        {
            sb.append(with ? "+" : "-").append(" ");
            for (int i = 0; i < items.length; i++)
            {
                if (items[i])
                {
                    sb.append("item").append(i).append(" ");
                }
            }
            sb.append("\n");
            return sb.toString();
        }
        else
        {
            sb.append("\n");
            return sb.toString();
        }
    }
}
