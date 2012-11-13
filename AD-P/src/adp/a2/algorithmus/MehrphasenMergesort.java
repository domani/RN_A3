package adp.a2.algorithmus;
import java.util.ArrayList;
import java.util.List;

public class MehrphasenMergesort {

	private int[] band0 = new int[20];
	private int[] band1 = new int[20];
	private int[] band2 = new int[20];
	// private int[][] baender= new int[3][20];
	List<List<Integer>> baender = new ArrayList<List<Integer>>();
	private int[] runlaenge = new int[3];
	
	 public MehrphasenMergesort(List<List<Integer>> baender)
	 {
		 this.baender = baender;
	 }

	public List<Integer> algorithm() {
		runlaenge[2] = 2;//von _tmpBand2 
		runlaenge[1] = 2;//von _tmpBand1
		int eingabeband1=1,eingabeband2=0,iAusgBand=2;//TODO 0 und 1 tauschen
		List<Integer> ausgabeband = baender.get(iAusgBand);
		boolean outerLoop = true;
		while (outerLoop)// So lange durchlaufen bis nur noch 1 Band elemente hat.							
		{
			runlaenge[0] = runlaenge[1] + runlaenge[2];
			List<Integer> _tmpBand = baender.get(eingabeband1);// Durch Variablen ersetztrn
			List<Integer> _tmpBand2 = baender.get(eingabeband2);// Durch Variablen ersetztrn
			boolean innerLoop = true;
			while (innerLoop) // bis ein Band leer wird (das nicht das ausgabeband ist)
			{
				int run1 = 0, run2 = 0;
				while (run1 < runlaenge[1] || run2 < runlaenge[2]) {
//					try{
					if (((run1 < runlaenge[1])?_tmpBand.get(0):Integer.MAX_VALUE) <= 
							((run2 < runlaenge[2])?_tmpBand2.get(0):Integer.MAX_VALUE)) {// runlaenge[1] durch  runlaenge i ersetzen
						ausgabeband.add(_tmpBand.remove(0));
						run1++;
					} else {

							ausgabeband.add(_tmpBand2.remove(0));
							run2++;
						
					}
//					}catch(IndexOutOfBoundsException ex){System.out.println("fuck");}
				}// 2 Runs wurden zu einem verschmolzen
				
				int count=0;
				for(List<Integer> elem : baender)
				{
					if (!elem.isEmpty())
						count++;
				}
				if (count == 1)
					return ausgabeband;
				
				int i = _tmpBand.size();
				if (0==_tmpBand.size()) {//Pruefen ob ein Run leer ist
					innerLoop = false;
					int buff = eingabeband1;
					eingabeband1 = iAusgBand;
					runlaenge[1] = runlaenge[0]; 
					iAusgBand = buff;					
					_tmpBand = baender.get(eingabeband1);
					ausgabeband = baender.get(iAusgBand);

				}
				i = _tmpBand2.size();
				if (_tmpBand2.size()==0) {//Pruefen ob ein Run leer ist
					innerLoop = false;
					int buff = eingabeband2;
					eingabeband2 = iAusgBand;
					iAusgBand = buff;
					runlaenge[2] = runlaenge[0];
					_tmpBand2 = baender.get(eingabeband2);
					ausgabeband = baender.get(iAusgBand);

				}
			}
			
		}

		return ausgabeband;
	}

}
