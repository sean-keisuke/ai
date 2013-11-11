
/**
 * @author reuben
 */
public class Item{

    private int index;
    private int size;
    private Boolean with;
    private boolean[] constraints;

    public Item(String name, int size, char posneg, boolean[] constraints)
    {
        this.index = Integer.parseInt(name.substring(4));
        this.size = size;
        this.with = posneg == '\0' ? null : posneg == '+' ? true : false ;
        this.constraints = constraints;
    }
    
    public boolean canPackWith(int item)
    {
        if (with == null) return true;
        if (item >= constraints.length) return !with;
        else return with == constraints[item];
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

    public boolean[] getConstraints()
    {
        return constraints;
    }

    public void setConstraints(boolean[] constraints)
    {
        this.constraints = constraints;
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
            for (int i = 0; i < constraints.length; i++)
            {
                if (constraints[i])
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
    }}