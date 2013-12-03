import java.io.*;
import java.util.*;
public class kbread 
{
	private BufferedReader file;
	private LinkedList <String> list;
	
	public static void main(String []args) throws FileNotFoundException, IOException
	{
		kbread file=new kbread("test1");
		LinkedList <String> list=file.read();
		for (int i=0;i<list.size();i++)
			System.out.println(""+list.get(i));
	}
	
	kbread(String filename) throws FileNotFoundException
	{
		FileReader f=new FileReader(filename);
		file=new BufferedReader(f);
		list=new LinkedList <String> ();
	}
	
	LinkedList <String> read() throws IOException
	{
		String temp;
		temp=file.readLine();
		while (temp!=null)
		{
			if(!(temp.compareTo("\n")==0||temp.compareTo("\r")==0||temp.length()<=2))
			{
				list.add(temp);
				temp=file.readLine();
			}else
			{
				list.add(null);
				temp=file.readLine();
			}
			
		}
		return list;
	}

}
