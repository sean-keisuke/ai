package source;

public interface SearchFrontierStorage<C extends State>
{
   public boolean isEmpty();
   public void add(C s);
   public C next();
}
