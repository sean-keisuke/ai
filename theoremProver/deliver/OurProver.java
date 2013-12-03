
public class KB
{
   private class Parameter
   {
       String name;
   }
   private class Predicate
   {
       boolean neg;
       String name;
       ArrayList<Parameter> params;
   }
   private class Sentence
   {
       ArrayList<Predicate> preds;
       boolean fromRefuted;
   }

   private ArrayList<Sentence> performResolution(Sentence s1, Sentence s2)
   {
       ArrayList<Sentence> rval = new ArrayList<Sentence>();

       for each pred p1 in s1
           for each pred p2 in s2
               if !p1==p2 (unification)
                   then add sent to rval with everything but p1 and p2, doing whatever var replacement
       return rval;
   }


   public KB(String fname)
   {}


}



