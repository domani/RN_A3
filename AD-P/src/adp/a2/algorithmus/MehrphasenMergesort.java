package adp.a2.algorithmus;
import adp.a2.dateiverwaltung.DataManager;
import adp.a2.dateiverwaltung.IDataExchange;
import java.util.ArrayList;
import java.util.List;

/**
 * Mehrphasen Mergesort!
 * @author Jan, Jonny & Loki
 */
public class MehrphasenMergesort {
    
        private final int RUN_LAENGE = 2, ANZAHL_BAENDER = 3;
	//List<List<Integer>> baender = new ArrayList<List<Integer>>();
	private int[] runlaenge = new int[ANZAHL_BAENDER];        
	IDataExchange dataManager;
        /**
         * Konstruktor
         * @param dataManager Der Datamanager auf die Die Baender fuer den Algortihmus liegen.
         * @param AnzBaender Die Anzal der Baender auf denen Gearbeitet wird <--NICHT IMPLEMENTIERT
         * @param runLaenge die Startrunlaenge <-- NICHT IMPLEMENTIERT
         */
	 public MehrphasenMergesort(IDataExchange dataManager, int AnzBaender,int runLaenge)
	 {
		 //this.baender = baender;
                 this.dataManager = dataManager;                 
	 }

         /**
          * DerAlgortihmus
          * @return Gibt den Index des Bandes Zurueck, auf dem die Sortierte Zahlenfolge liegt.
          */
	public int algorithm() {
            dataManager.setInitialRunLength(RUN_LAENGE);
            dataManager.setBandCount(ANZAHL_BAENDER);
            dataManager.generateInitalRuns();
            

		runlaenge[2] = RUN_LAENGE;//von _tmpBand2 
		runlaenge[1] = RUN_LAENGE;//von _tmpBand1
		int eingabeband1=0,eingabeband2=1,ausgabeband=2;//TODO 0 und 1 tauschen
		boolean outerLoop = true;
		while (outerLoop)// So lange durchlaufen bis nur noch 1 Band elemente hat.							
		{
			runlaenge[2] = runlaenge[1] + runlaenge[0];
			boolean innerLoop = true;
			while (innerLoop) // bis ein Band leer wird (das nicht das ausgabeband ist)
			{
				int run1 = 0, run2 = 0;
				while (run1 < runlaenge[1] || run2 < runlaenge[2]) {

                                    int links = (run1 < runlaenge[1])?dataManager.getNextNumberOfBand(eingabeband1):Integer.MAX_VALUE;
                                    int rechts = (run2 < runlaenge[2])?dataManager.getNextNumberOfBand(eingabeband2):Integer.MAX_VALUE;
					if (links <= rechts)
							 {
						dataManager.addNumberToBand(links, ausgabeband);
						run1++;
					} else {
							dataManager.addNumberToBand(rechts, ausgabeband);
							run2++;						
					}
				}// 2 Runs wurden zu einem verschmolzen
				
				int count=0;
				for(int i=0; i<dataManager.getBandCount();++i)
				{                                    
					if (dataManager.getBandSize(i)!=0)
						count++;
				}
				if (count == 1)
					return ausgabeband;
				
				if (0==dataManager.getBandSize(eingabeband1)) {//Pruefen ob ein Run leer ist
					innerLoop = false;
					int buff = eingabeband1;
					eingabeband1 = ausgabeband;
					runlaenge[1] = runlaenge[0]; 
					ausgabeband = buff;
                                        dataManager.rewindBand(eingabeband1);
                                        continue;
				}
				if (dataManager.getBandSize(eingabeband2)==0) {//Pruefen ob ein Run leer ist
					innerLoop = false;
					int buff = eingabeband2;
					eingabeband2 = ausgabeband;
					ausgabeband = buff;
					runlaenge[2] = runlaenge[0];
                                        dataManager.rewindBand(eingabeband2);
                                        continue;
				}
			}
			
		}

		return ausgabeband;
	}

}
