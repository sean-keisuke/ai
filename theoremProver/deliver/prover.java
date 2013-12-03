import java.io.*;
import java.lang.management.*;
import java.util.*;
import java.math.*;
public class prover 
{

	private class Sentence
	{
		Sentence p1;
		Sentence p2;
		int position;
		private Vector <String> flist;
		boolean subbed;
		private String sentence;
		private Vector <Integer> scores;
		
		Sentence()
		{
			scores=new Vector<Integer>();
			flist=new Vector<String>();
			subbed=false;
		}
		public void genflist()
		{
			if(flist.size()!=0)
				flist=new Vector<String>();
			StringTokenizer exprs=new StringTokenizer(sentence," ");
			StringTokenizer tok;
			int countexprs=exprs.countTokens();
			for(int i=0;i<countexprs;i++)
			{
				String temp=exprs.nextToken();
				tok=new StringTokenizer(temp,"(");
				flist.add(tok.nextToken());
			}
		}
		public int score(Sentence s)
		{
			int result=0;
			for(int i=0;i<flist.size();i++)
				for(int j=0;j<s.flist.size();j++)
				{
					String mine=flist.get(i);
					String other=s.flist.get(j);
					if(mine.equals(other))
					{
						result+=1;
					}else
					{
						mine=mine.replace("!", "");
						other=other.replace("!", "");
						if(mine.equals(other))
						{
							result+=2;
						}
					}
				}
			if(sentence.equals(s.sentence))
			{
				result=0;
			}
			
			return result;
		}
		public void growscores(int size)
		{
			int start=scores.size();
			for(int i=start;i<size;i++)
			{
				scores.add(new Integer(0));
			}
		}
		public boolean equals(Object o)
		{
			Sentence s=(Sentence)o;
			if(this.sentence.equals(s.sentence))
				return true;
						
			return false;
		}
		private boolean subs(Sentence s)
		{
			if(this.sentence.equals(s.sentence))
				return true;
			StringTokenizer tok1=new StringTokenizer(s.sentence," ");
			StringTokenizer tok2=new StringTokenizer(sentence," ");
			Vector<String> v1=new Vector <String> ();
			Vector<String> v2=new Vector <String> ();
			int count1=tok1.countTokens();
			int count2=tok2.countTokens();
			for(int i=0;i<count1;i++)
				v1.add(tok1.nextToken());
			for(int i=0;i<count2;i++)
				v2.add(tok2.nextToken());
			if(v2.containsAll(v1)&&!v1.containsAll(v2))
				return true;
				
			
			return false;
		}
		
	}
	
	private Vector <String> kb;
	private long runi;
	private long huni;
	//returns true if the string represents a constant
	prover(LinkedList <String> list)
	{
		kb=prepare(list);
		runi=0;
		huni=0;
	}
	
	public long getRandunifies()
	{
		return runi;
	}
	
	public long getHunifies()
	{
		return huni;
	}
	
	private Vector<String> prepare(LinkedList <String> list)
	{
		Vector <String> result=new Vector <String> (list.size());
		for(int i=0;i<list.size();i++)
		{
			String temp=list.get(i);
			if(temp!=null)
			{
				temp=tagargs(temp,i);
				result.add(temp);
			}
		}
		return result;
	}
	private String tagargs(String s, int j)
	{
		StringTokenizer tok=new StringTokenizer(s, " ");
		int count=tok.countTokens();		
		String result="";
		for(int i=0;i<count;i++)
		{
			result+=exprtag(tok.nextToken(),j);
			if(i!=count-1)
			{
				result+=" ";
			}
		}
		
		return result;
	}
	private String exprtag(String s, int j)
	{
		StringTokenizer tok=new StringTokenizer(s," ,()");
		int count=tok.countTokens();
		String result=tok.nextToken()+"(";
		
		for(int i=1;i<count;i++)
		{
			if(i!=1)
			{
				result+=",";
			}
			String temp=tok.nextToken();
			if(variable(temp))
				temp+=j;
			result+=temp;
		}
		
		result+=")";
		return result;
	}
	boolean constant(String str)
	{
		if(str.toUpperCase().equals(str))
		{
			return true;
		}
		return false;
	}
	//returns true if the string represents a variable
	boolean variable(String str)
	{
		if(str.toLowerCase().equals(str))
		{
			return true;
		}
		return false;
	}
	//checks to see if the given sentences can be unified
	boolean unifyable(String str1, String str2)
	{
		StringTokenizer tok1=new StringTokenizer(str1, " ,)(!");
		StringTokenizer tok2=new StringTokenizer(str2, " ,)(!");
		int count1=tok1.countTokens();
		int count2=tok2.countTokens();
		if(tok1.nextToken().equals(tok2.nextToken())&&count1==count2)
		{
			return true;
		}
		return false;
	}
	
	//applys a given substitution to the string
	String apply(String changes,String str)
	{
		StringTokenizer tok=new StringTokenizer(changes, "- ");
		int count=tok.countTokens();
		String result=str;
		for(int i=0;i<count/2;i++)
		{
			String target=tok.nextToken();
			String replace=tok.nextToken();
			result=result.replace(target, replace);
		}
		return result;
	}
	
	//unifying the set of arguments
	private String argsunify(String str1, String str2)
	{
		StringTokenizer tok1=new StringTokenizer(str1, " ,)(!");
		StringTokenizer tok2=new StringTokenizer(str2, " ,)(!");
		String result="";
		int count1=tok1.countTokens();
		//int count2=tok2.countTokens();
		
		String t1=tok1.nextToken();
		String t2=tok2.nextToken();
		if(constant(t1)&&constant(t2))
		{
			if(!t1.equals(t2))
			{
				return null;
			}
		}else if(variable(t1))
		{
			result+=t1+"-"+t2;
		}else if(variable(t2))
		{
			result+=t2+"-"+t1;
		}
		String temp1=remaining(tok1);
		String temp2=remaining(tok2);
		temp1=apply(result,temp1);
		temp2=apply(result,temp2);
		if(count1-1==0)
			return " "+result+ " ";
		else
		{
			String temp=argsunify(temp1,temp2);
			if (temp==null)
				return temp;
			else return " "+result+" "+temp+" ";
		}
	}
	
	//returns the remaining string in the tokenizer
	String remaining(StringTokenizer t1)
	{
		int count1=t1.countTokens();
		String result="";
		for(int i=0;i<count1;i++)
			result+=","+t1.nextToken();
		return result;
	}
	
	//unifies given expressions
	String exprunify(String str1, String str2)
	{
		StringTokenizer tok1=new StringTokenizer(str1, " ,)(!");
		StringTokenizer tok2=new StringTokenizer(str2, " ,)(!");
		String result="";
		int count1=tok1.countTokens()-1;
		int count2=tok2.countTokens()-1;
		String temp1=tok1.nextToken();
		String temp2=tok2.nextToken();
		if(count1!=count2||!temp1.equals(temp2))
			return null;
		//tok1.nextToken();
		//tok2.nextToken();
		result=argsunify(remaining(tok1),remaining(tok2));
		
		
		return result;
	}
	
	//applies the substitution to all the strings in the vector
	Vector <String> applyV(String changes, Vector <String> v1)
	{
		Vector <String> result=new Vector <String> (3);
		for(int i=0;i<v1.size();i++)
		{
			String temp=v1.get(i);
			temp=apply(changes,temp);
			result.add(temp);
		}
		return result;
	}
	
	//combines the Vectors of strings into one big string
	//will compress redundant or contradicting expressions
	String combine(Vector <String> v1, Vector <String> v2)
	{
		String result="";
		Vector <String> full=new Vector <String> (v1);
		full.addAll(v2);
		compress(full);
		for(int i=0;i<full.size();i++)
		{
			if(i!=0)
				result+=" ";
			result+=full.get(i);
		}
		/*for(int j=0;j<v2.size();j++)
			result+=v2.get(j)+" ";*/
		return result;
	}
	
	private void compress(Vector <String> v)
	{
		for(int i=0;i<v.size();i++)
			for(int j=i;j<v.size();j++)
			{
				if(i<0)
					i++;
				if(j<0)
					j++;
				if(!v.get(i).equals(v.get(j)))
				{
					
					String temp1=v.get(i);
					String temp2=v.get(j);
					temp1=temp1.replace("!", "");
					temp2=temp2.replace("!", "");
					if(temp1.equals(temp2))
					{
						if(i>j)
						{
							v.remove(i);
							v.remove(j);
							i--;
							j--;
						}else
						{
							v.remove(j);
							v.remove(i);
							//if(i!=0)
							i--;
							//if(j!=0)
							j--;
						}
					}
				}else
				{
					if(i!=j)
					{
						v.remove(j);
						if(i>j)
							i--;
						//if(j!=0)
						j--;
					}
						
					
				}
			}
	}
	
	//unifies the two FOL strings
	String unify(String str1, String str2)
	{
		StringTokenizer tok1=new StringTokenizer(str1, " ");
		StringTokenizer tok2=new StringTokenizer(str2, " ");
		int count1=tok1.countTokens();
		int count2=tok2.countTokens();
		boolean works=false;
		Vector <String> a1=new Vector <String>(count1);
		Vector <String> a2=new Vector <String>(count2);
		for (int i=0;i<count1;i++)
		{
			a1.add(tok1.nextToken());
		}
		for(int i=0;i<count2;i++)
		{
			a2.add(tok2.nextToken());
		}
		for(int i=0;i<count1;i++)
			for (int j=0;j<count2;j++)
			{
				if(unifyable(a1.get(i),a2.get(j)))
				{
					//works=true;
					String temp=exprunify(a1.get(i),a2.get(j));
					if(temp!=null)
					{
						works=true;
						a1=applyV(temp,a1);
						a2=applyV(temp,a2);
					}
				}
			}
		
		if(works)
		{
			return combine(a1,a2);
		}
		
		return null;
	}
	
	public void printkb()
	{
		for (int i=0;i<kb.size();i++)
		{
			System.out.println(kb.get(i));
		}
	}
	boolean failtest(Vector <Sentence> wkb)
	{
		boolean result=true;
		for(int i=0;i<wkb.size();i++)
		{
			Sentence temp=wkb.get(i);
			if(temp.scores.size()!=wkb.size()||temp.scores.contains(new Integer(0)))
			{
				result=false;
				break;
			}
		}
		
		
		return result;
	}
	
	private void updatescores(Sentence s, Vector <Sentence> wkb)
	{
		int start=s.scores.size();
		/*if(start!=wkb.size())
		{
			temp.growscores(wkb.size());
		}*/
		for(int i=start;i<wkb.size();i++)
		{
			s.scores.add(s.score(wkb.get(i)));
		}
	}
	
	public String hresolution()
	{
		String result="";
		// TODO: fill this baby in
		return result;
	}
	
	public String randresolution()
	{
		long unis=0;
		Vector <Sentence> wkb=new Vector <Sentence>(200);
		boolean done=false;
		int r1,r2;
		long attempted=0;
		Random rand=new Random();
		String result="";
		for(int i=0;i<kb.size();i++)
		{
			Sentence temp=new Sentence();
			temp.growscores(kb.size());
			temp.scores.remove(i);
			temp.scores.add(i,new Integer(1));
			temp.sentence=kb.get(i);
			temp.p1=null;
			temp.p2=null;
			wkb.add(temp);
		}
		
		while(!done)
		{
			r1=rand.nextInt(wkb.size());
			r2=rand.nextInt(wkb.size());
			Sentence temp=wkb.get(r1);
			if(temp.scores.size()<wkb.size())
				temp.growscores(wkb.size());
			while(r1==r2||temp.scores.get(r2).intValue()==1)
			{
				r1=rand.nextInt(wkb.size());
				r2=rand.nextInt(wkb.size());
				//failtest(wkb);
				temp=wkb.get(r1);
				if(temp.scores.size()<wkb.size())
					temp.growscores(wkb.size());
				
			}
			if(wkb.get(r2).scores.size()<wkb.size())
				wkb.get(r2).growscores(wkb.size());
			String uni=unify(wkb.get(r1).sentence,wkb.get(r2).sentence);
			if(uni==null)
			{
				wkb.get(r1).scores.remove(r2);
				wkb.get(r1).scores.add(r2, new Integer(1));
				wkb.get(r2).scores.remove(r1);
				wkb.get(r2).scores.add(r1, new Integer(1));
				attempted++;
			}else if(uni.equals(""))
			{
				Sentence fin=new Sentence();
				fin.p1=wkb.get(r1);
				fin.p2=wkb.get(r2);
				fin.sentence=uni;
				result+=traceparents(fin);
				unis++;
				runi=unis;
				//result+=unis;
				
				done=true;
			}else
			{
				temp=new Sentence();
				
				temp.sentence=uni;
				temp.p1=wkb.get(r1);
				temp.p2=wkb.get(r2);
				boolean nsub=false;
				//boolean osub=false;
				//int old;
				wkb.get(r1).scores.remove(r2);
				wkb.get(r1).scores.add(r2, new Integer(1));
				wkb.get(r2).scores.remove(r1);
				wkb.get(r2).scores.add(r1, new Integer(1));
				
				
				for(int i=0;i<wkb.size();i++)
				{
					if(temp.subs(wkb.get(i)))
					{
						nsub=true;
					}else if(wkb.get(i).subs(temp))
					{
						//wkb.get(i).subbed=true;
						if(!wkb.get(i).equals(temp))
						{
							for(int j=0;j<wkb.size();j++)
							{
								if(wkb.get(j).scores.size()!=wkb.size())
								{
									wkb.get(j).growscores(wkb.size());
								}
								wkb.get(j).scores.remove(i);
							}
							wkb.remove(i);
						}
					}
				}
				if(!nsub)
				{
					temp.growscores(wkb.size()+1);
					temp.scores.remove(wkb.size());
					temp.scores.add(wkb.size(),new Integer(1));
					wkb.add(temp);
				}
				
				unis++;
				attempted++;
			}
			//System.err.println(uni+" "+wkb.size()+" " +attempted);
			if(attempted>=(wkb.size()*wkb.size())/2-wkb.size())
			{
				if(failtest(wkb))
				{
					done=true;
				}
			}
			
		}
		runi=unis;
		return result;
	}
	
	String traceparents(Sentence s)
	{
		String result="";
		if(s.p1!=null)
			result+=traceparents(s.p1);
		if(s.p2!=null)
			result+=traceparents(s.p2);
		if(s.p1!=null)
			result+=s.p1.sentence+"\n";
		if(s.p2!=null)
			result+=s.p2.sentence+"\n";
		if(s.p1!=null&&s.p2!=null)
			result+=s.sentence+"\n\n";
		//result+=s.sentence+ "\n";
		return result;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		kbread reader=new kbread(args[0]);
		int mode=0;
		if(args.length>1)
		{
			try
			{
				mode=Integer.parseInt(args[1]);
			}catch (NumberFormatException e)
			{
				System.err.println("Error parsing mode variable... using default settings");
			}
		}
		if(mode!=1&&mode!=2&&mode!=0)
			mode=0;
		LinkedList <String> list=reader.read();
		prover p=new prover(list);
		RuntimeMXBean manager=ManagementFactory.getRuntimeMXBean();
		String temp=null;
		if(mode==0||mode==1)
			System.out.println("Random Run");
		long start=manager.getUptime();
		long stop;
		long randtime=0;
		long htime=0;
		if(mode==0||mode==1)
		{
			
			try
			{
				temp=p.randresolution();
			}catch (OutOfMemoryError e)
			{
				System.out.println("Not enough memory");
			}
			
			if(temp.equals(""))
				System.out.println("no solution");
			else
				System.out.println(temp);
			//System.out.println("Number of random unifications done: "+ p.getRandunifies());
			stop=manager.getUptime();
			randtime=stop-start;
		}
		if(mode==0||mode==2)
		{
			System.out.println("Heuristic Run");
			start=manager.getUptime();
			try{
				temp=p.hresolution();
			}catch (OutOfMemoryError e)
			{
				System.out.println("Not enough memory");
			}
			
			if(temp.equals(""))
				System.out.println("no solution");
			else
				System.out.println(temp);
			//System.out.println("Number of hueristic unifications done: "+ p.getHunifies());
			stop=manager.getUptime();
			htime=stop-start;
		}
		if(mode==1||mode==0)
			System.out.println("Number of random unifications done: "+p.getRandunifies());
		if(mode==2||mode==0)
			System.out.println("Number of heurisitic unifications done: "+p.getHunifies());
		if(mode==1||mode==0)
			System.out.println("Random time: "+randtime +" milliseconds");
		if(mode==2||mode==0)
			System.out.println("Heurisitic time: "+htime+" milliseconds");
		
		//System.out.
		if(mode==0)
		{
			BigDecimal puni=new BigDecimal((double)(p.getHunifies())/p.getRandunifies());
			BigDecimal ptime=new BigDecimal((double)(htime)/randtime);
			puni=puni.round(new MathContext(2));
			ptime=ptime.round(new MathContext(2));
			String t1=""+puni.doubleValue()*100;
			if(t1.length()>6)
				t1=t1.substring(0, 5);
			String t2=""+ptime.doubleValue()*100;
			if(t2.length()>6)
				t2=t2.substring(0, 5);
			System.out.println("Heuristic unifications "+t1+"% of random");
			System.out.println("Heuristic time "+t2+"% of random");
		}
			//p.unify("P(x0) Q(y0)", "!Q(A)");
			//p.printkb();
		//String temp=p.unify("C(x,V,z) X(y,z)","C(x,V) !X(Y,Z)");
		//System.out.println(temp);

	}

}
