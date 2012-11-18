package adp.a2.algorithmus;

import adp.a2.dateiverwaltung.IDataExchange;

/**
 * Mehrphasen Mergesort!
 *
 * @author Jan, Jonny & Loki
 */
public class MultiBandMehrphasenMergesort {

    private int RUN_LAENGE = 2, ANZAHL_BAENDER = 3;
    public final static boolean DEBUG = true;
    private int[] baender;
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
        baender= new int[ANZAHL_BAENDER-1];
        dataManager.generateInitalRuns();
        if (DEBUG) {
            printBands();
        }


        if (DEBUG) {
            System.out.print(" Anzahl der Runs gesamt ");// + (dataManager.getRunCount(0) + dataManager.getRunCount(1) + dataManager.getRunCount(2)));
            int acc=0;
            for (int i=0; i<baender.length;++i)
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
            baender[i]=bandNr;
            bandNr++;
        }//In Baender stehen nun alle Eingangsbaender.
        

        boolean outerLoop = true;
        while (outerLoop)// So lange durchlaufen bis nur noch 1 Band elemente hat.							
        {
//            runlaenge[eingabeband1] = dataManager.getRunSize(eingabeband1);
//            runlaenge[eingabeband2] = dataManager.getRunSize(eingabeband2);
//            runlaenge[ausgabeband] = runlaenge[eingabeband1] + runlaenge[eingabeband2];
            int linkesBand=0,rechtesBand = 1;//Index auf unser Array
            boolean keinBandLeer = true;
            while (keinBandLeer) // bis ein Band leer wird (das nicht das ausgabeband ist)
            {
                //System.out.println("eingabeband " + eingabeband1 + "eingabeband2 " + eingabeband2 + "ausgabeband" + ausgabeband);
                if(linkesBand==baender.length-1) linkesBand = 0;
                rechtesBand=linkesBand+1;
                if(rechtesBand==baender.length-1) rechtesBand=0;
                dataManager.addRunToBand(ausgabeband);
                if (DEBUG) {
                    //System.out.println("\t Runs auf AusgabeBand " + dataManager.getRunCount(ausgabeband) + " auf Eingabeband 1: " + dataManager.getRunCount(eingabeband1) + " auf eingabeband 2: " + dataManager.getRunCount(eingabeband2));
                     printBands();
                }
                int runL = dataManager.getRunSize(baender[linkesBand]), runR = dataManager.getRunSize(baender[rechtesBand]);
                int run1 = 1, run2 = 1;
                int links = (runL>0)?dataManager.getNextNumberOfBand(baender[linkesBand]):Integer.MAX_VALUE;
                int rechts= (runR>0)?dataManager.getNextNumberOfBand(baender[rechtesBand]):Integer.MAX_VALUE;
                boolean linkerRunNichtZuende, rechterRunNichtZuende;                
                linkerRunNichtZuende = (run1 < runL || links != Integer.MAX_VALUE);
                rechterRunNichtZuende = (run2 < runR || rechts != Integer.MAX_VALUE);
                while (linkerRunNichtZuende || rechterRunNichtZuende) {//DAs ist die Abarbeitung eines Runs
                    if (DEBUG) {
                        printBands();
                    }
                    //while (!dataManager.runFinished(eingabeband1) || !dataManager.runFinished(eingabeband2)|| links!=Integer.MAX_VALUE || rechts!=Integer.MAX_VALUE) {
                    if (links <= rechts) {
                        dataManager.addNumberToBand(links, ausgabeband);
                        links = (run1 < runL) ? dataManager.getNextNumberOfBand(baender[linkesBand]) : Integer.MAX_VALUE;
                        // links = (!dataManager.runFinished(eingabeband1)) ? dataManager.getNextNumberOfBand(eingabeband1) : Integer.MAX_VALUE;                                              
                        zugriffe++;
                        run1++;
                    } else {
                        dataManager.addNumberToBand(rechts, ausgabeband);

                        rechts = (run2 < runR) ? dataManager.getNextNumberOfBand(baender[rechtesBand]) : Integer.MAX_VALUE;
                        //rechts = (!dataManager.runFinished(eingabeband2)) ? dataManager.getNextNumberOfBand(eingabeband2) : Integer.MAX_VALUE;
                        zugriffe++;
                        run2++;
                    }
                    linkerRunNichtZuende = (run1 < runL || links != Integer.MAX_VALUE);
                    rechterRunNichtZuende = (run2 < runR || rechts != Integer.MAX_VALUE);
                }// 2 Runs wurden zu einem verschmolzen
                dataManager.endAddRun(ausgabeband);
                dataManager.skipRun(baender[linkesBand]);
                dataManager.skipRun(baender[rechtesBand]);
                
                if (DEBUG) {
                    printBands();
                    System.out.println(" Anzahl der Runs gesamt" + (dataManager.getRunCount(0) + dataManager.getRunCount(1) + dataManager.getRunCount(2)));
                }
                int countEinRun = 0, countLeer = 0;
                for (int i = 0; i < dataManager.getBandCount(); ++i) {
                    if (dataManager.getRunCount(i) == 1) {
                        countEinRun++;
                    } else if (dataManager.getRunCount(i) == 0) {
                        countLeer++;
                    }
                }
                if (countEinRun == 1 && countLeer == 2) {
                    printBands();
                    return ausgabeband;
                }
                if (DEBUG) {
                    printBands();
                }
                
                for (int i =0;i<baender.length;++i)
                {
                    if(0==dataManager.getBandSize(baender[i]))
                    {
                        keinBandLeer = false;
                        int buff = baender[i];
                        baender[i] = ausgabeband;
                        ausgabeband = buff;
                        dataManager.clearBand(ausgabeband);
                        break;
                    }
                }
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
