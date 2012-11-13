package a2;

import java.util.ArrayList;
import java.util.List;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<List<Integer>> l = new ArrayList<List<Integer>>();
		l.add(new ArrayList<Integer>(){{
			add(1);
			add(3);
			add(5);
			add(200);
			add(4);
			add(20);
			add(1024);
			add(1337);
			add(333);
			add(666);			
		}});
		
		l.add(new ArrayList<Integer>(){{
			add(1);
			add(8);
			add(23);
			add(42);			
			add(13);
			add(14);			
			add(87);
			add(1002);
			add(102);
			add(200);
			add(4);
			add(20);
			add(5);
			add(314);
		}});
		l.add(new ArrayList<Integer>());
		MehrphasenMergesort m = new MehrphasenMergesort(l);
		System.out.println(m.algorithm());
		
		//ArrayList<String> graph1_shortestPath1 = 
	}

}
