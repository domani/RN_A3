package adp.a2.algorithmus;

import adp.a2.dateiverwaltung.IDataExchange;
import java.util.ArrayList;
import java.util.List;

/**
 * Mehrphasen Mergesort!
 *
 * @author Jan, Jonny & Loki
 */
public class MultiBandMehrphasenMergesort {

    private int RUN_LAENGE = 2, ANZAHL_BAENDER = 3;
    public final static boolean DEBUG = true;
    private List<Integer> baenderList;
    //List<List<Integer>> baender = new ArrayList<List<Integer>>();
    int zugriffe = 0;
//    private int[] runlaenge;
    IDataExchange dataManager;

    /**
     * Konstruktor
     *
     * @param dataManager Der Datamanager auf die Die Baender fuer den
     * Algortihmus liegen.
     * @param AnzBaender Die Anzal der Baender auf denen Gearbeitet wird
     * <--NICHT IMPLEMENTIERT
     * @param runLaenge die Startrunlaenge <-- NICHT IMPLEMENTIERT
     */
    public MultiBandMehrphasenMergesort(IDataExchange dataManager, int AnzBaender, int runLaenge) {
        this.ANZAHL_BAENDER = AnzBaender;
        this.dataManager = dataManager;
        this.RUN_LAENGE = runLaenge;
//        runlaenge = new int[ANZAHL_BAENDER];

    }

    /**
     * DerAlgortihmus
     *
     * @return Gibt den Index des Bandes Zurueck, auf dem die Sortierte
     * Zahlenfolge liegt.
     */
    public int algorithm() {
        dataManager.setInitialRunLength(RUN_LAENGE);
        dataManager.setBandCount(ANZAHL_BAENDER);
        baenderList = new ArrayList<Integer>(ANZAHL_BAENDER-1);
        dataManager.generateInitalRuns();
        if (DEBUG) {
            printBands();
        }


        if (DEBUG) {
            System.out.print(" Anzahl der Runs gesamt ");// + (dataManager.getRunCount(0) + dataManager.getRunCount(1) + dataManager.getRunCount(2)));
            int acc=0;
            for (int i=0; i<ANZAHL_BAENDER-1;++i)
            {
                acc+=dataManager.getRunCount(i);
            }
            System.out.println(acc);
        }
        int ausgabeband = dataManager.getEmptyBand();
        for (int i=0,bandNr=0;i<ANZAHL_BAENDER-1;++i)//Das Ausgabeband wird nicht mitgezaehlt
        {
            if(bandNr == ausgabeband)
                bandNr++;
            baenderList.add(bandNr);
            //baender[i]=bandNr;
            bandNr++;
        }//In Baender stehen nun alle Eingangsbaender.
        

        boolean outerLoop = true;
        while (outerLoop)// So lange durchlaufen bis nur noch 1 Band elemente hat.							
        {
//            runlaenge[eingabeband1] = dataManager.getRunSize(eingabeband1);
//            runlaenge[eingabeband2] = dataManager.getRunSize(eingabeband2);
//            runlaenge[ausgabeband] = runlaenge[eingabeband1] + runlaenge[eingabeband2];
            int bandLinks=0,bandRechts = 1;//Index auf unser Array
            boolean keinBandLeer = true;
            while (keinBandLeer) // bis ein Band leer wird (das nicht das ausgabeband ist)
            {
                //System.out.println("eingabeband " + eingabeband1 + "eingabeband2 " + eingabeband2 + "ausgabeband" + ausgabeband);
                if(bandLinks==baenderList.size()) bandLinks = 0;
                bandRechts=bandLinks+1;
                if(bandRechts==baenderList.size()) bandRechts=0;
                dataManager.addRunToBand(ausgabeband);
                if (DEBUG) {
                    //System.out.println("\t Runs auf AusgabeBand " + dataManager.getRunCount(ausgabeband) + " auf Eingabeband 1: " + dataManager.getRunCount(eingabeband1) + " auf eingabeband 2: " + dataManager.getRunCount(eingabeband2));
                     printBands();
                }
                int runL = dataManager.getRunSize(baenderList.get(bandLinks)), runR = dataManager.getRunSize(baenderList.get(bandRechts));
                int run1 = 1, run2 = 1;
                int zahlLinks = (runL>0)?dataManager.getNextNumberOfBand(baenderList.get(bandLinks)):Integer.MAX_VALUE;
                int zahlRechts= (runR>0)?dataManager.getNextNumberOfBand(baenderList.get(bandRechts)):Integer.MAX_VALUE;
                boolean runUnfertigLinks, runUnfertigRechts;                
                runUnfertigLinks = (run1 < runL || zahlLinks != Integer.MAX_VALUE);
                runUnfertigRechts = (run2 < runR || zahlRechts != Integer.MAX_VALUE);
                while (runUnfertigLinks || runUnfertigRechts) {//DAs ist die Abarbeitung eines Runs
                    if (DEBUG) {
                        printBands();
                    }
                    //while (!dataManager.runFinished(eingabeband1) || !dataManager.runFinished(eingabeband2)|| links!=Integer.MAX_VALUE || rechts!=Integer.MAX_VALUE) {
                    if (zahlLinks <= zahlRechts) {
                        dataManager.addNumberToBand(zahlLinks, ausgabeband);
                        zahlLinks = (run1 < runL) ? dataManager.getNextNumberOfBand(baenderList.get(bandLinks)) : Integer.MAX_VALUE;
                        // links = (!dataManager.runFinished(eingabeband1)) ? dataManager.getNextNumberOfBand(eingabeband1) : Integer.MAX_VALUE;                                              
                        zugriffe++;
                        run1++;
                    } else {
                        dataManager.addNumberToBand(zahlRechts, ausgabeband);

                        zahlRechts = (run2 < runR) ? dataManager.getNextNumberOfBand(baenderList.get(bandRechts)) : Integer.MAX_VALUE;
                        //rechts = (!dataManager.runFinished(eingabeband2)) ? dataManager.getNextNumberOfBand(eingabeband2) : Integer.MAX_VALUE;
                        zugriffe++;
                        run2++;
                    }
                    runUnfertigLinks = (run1 < runL || zahlLinks != Integer.MAX_VALUE);
                    runUnfertigRechts = (run2 < runR || zahlRechts != Integer.MAX_VALUE);
                }// 2 Runs wurden zu einem verschmolzen
                dataManager.endAddRun(ausgabeband);
                dataManager.skipRun(baenderList.get(bandLinks));
                dataManager.skipRun(baenderList.get(bandRechts));
                
                if (DEBUG) {
                    printBands();
                    //System.out.println(" Anzahl der Runs gesamt" + (dataManager.getRunCount(0) + dataManager.getRunCount(1) + dataManager.getRunCount(2)));
                }
                int countEinRun = 0, countLeer = 0;
                for (int i = 0; i < dataManager.getBandCount(); ++i) {
                    if (dataManager.getRunCount(i) == 1) {
                        countEinRun++;
                    } else if (dataManager.getRunCount(i) == 0) {
                        countLeer++;
                    }
                }
                if (countEinRun == 1 && countLeer == ANZAHL_BAENDER-1) {
                    printBands();
                    return ausgabeband;
                }
                if (DEBUG) {
                    printBands();
                }
                
                for (int i =0, emptyBands=0;i<baenderList.size();++i)
                {
                    if(0==dataManager.getBandSize(baenderList.get(i)))
                    {
                        emptyBands++;
                        if(emptyBands<2){
                        keinBandLeer = false;
                        int buff = baenderList.get(i);
                        baenderList.set(i, ausgabeband);
                        ausgabeband = buff;
                        dataManager.clearBand(ausgabeband);
                        }else{
                            baenderList.remove(i);
                        }
                        
                    }
                }
                
                bandLinks++;
                
            }

        }

        return ausgabeband;
    }
    
    
    
    private void printBands()
    {
        System.out.println("_____________________________________");
             for (int i = 0; i < dataManager.getBandCount(); ++i) {
                dataManager.printBand(i);
            }
    }
}
