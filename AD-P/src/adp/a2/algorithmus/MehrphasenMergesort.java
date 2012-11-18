package adp.a2.algorithmus;

import adp.a2.dateiverwaltung.DataManager;
import adp.a2.dateiverwaltung.IDataExchange;
import java.util.ArrayList;
import java.util.List;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Mehrphasen Mergesort!
 *
 * @author Jan, Jonny & Loki
 */
public class MehrphasenMergesort {

    private final int RUN_LAENGE = 2, ANZAHL_BAENDER = 3;
    private final boolean DEBUG = true;
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
    public MehrphasenMergesort(IDataExchange dataManager, int AnzBaender, int runLaenge) {
        //this.baender = baender;
        this.dataManager = dataManager;
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
        dataManager.generateInitalRuns();
        if (DEBUG) {
            printBands();
        }


        if (DEBUG) {
            System.out.println(" Anzahl der Runs gesamt" + (dataManager.getRunCount(0) + dataManager.getRunCount(1) + dataManager.getRunCount(2)));
        }

        int ausgabeband = dataManager.getEmptyBand(), eingabeband2 = 1, eingabeband1 = 2;
        if (ausgabeband == 0) {
            eingabeband1 = 1;
            eingabeband2 = 2;
        } else if (ausgabeband == 1) {
            eingabeband1 = 0;
            eingabeband2 = 2;
        } else if (ausgabeband == 2) {
            eingabeband1 = 0;
            eingabeband2 = 1;
        }


        boolean outerLoop = true;
        while (outerLoop)// So lange durchlaufen bis nur noch 1 Band elemente hat.							
        {
//            runlaenge[eingabeband1] = dataManager.getRunSize(eingabeband1);
//            runlaenge[eingabeband2] = dataManager.getRunSize(eingabeband2);
//            runlaenge[ausgabeband] = runlaenge[eingabeband1] + runlaenge[eingabeband2];
            boolean innerLoop = true;
            while (innerLoop) // bis ein Band leer wird (das nicht das ausgabeband ist)
            {
                //System.out.println("eingabeband " + eingabeband1 + "eingabeband2 " + eingabeband2 + "ausgabeband" + ausgabeband);
                
                dataManager.addRunToBand(ausgabeband);
                if (DEBUG) {
                    System.out.println("\t Runs auf AusgabeBand " + dataManager.getRunCount(ausgabeband) + " auf Eingabeband 1: " + dataManager.getRunCount(eingabeband1) + " auf eingabeband 2: " + dataManager.getRunCount(eingabeband2));
                     printBands();
                }
                int runL = dataManager.getRunSize(eingabeband1), runR = dataManager.getRunSize(eingabeband2);
                int links, rechts;
                int run1 = 1, run2 = 1;
                links = dataManager.getNextNumberOfBand(eingabeband1);
                rechts = dataManager.getNextNumberOfBand(eingabeband2);
                boolean linkesBandR, rechtesBandR;                
                linkesBandR = (run1 < runL || links != Integer.MAX_VALUE);
                rechtesBandR = (run2 < runR || rechts != Integer.MAX_VALUE);
                while (linkesBandR || rechtesBandR) {
                    if (DEBUG) {
                        printBands();
                    }
                    //while (!dataManager.runFinished(eingabeband1) || !dataManager.runFinished(eingabeband2)|| links!=Integer.MAX_VALUE || rechts!=Integer.MAX_VALUE) {
                    if (links <= rechts) {
                        dataManager.addNumberToBand(links, ausgabeband);
                        links = (run1 < runL) ? dataManager.getNextNumberOfBand(eingabeband1) : Integer.MAX_VALUE;
                        // links = (!dataManager.runFinished(eingabeband1)) ? dataManager.getNextNumberOfBand(eingabeband1) : Integer.MAX_VALUE;                                              
                        zugriffe++;
                        run1++;
                    } else {
                        dataManager.addNumberToBand(rechts, ausgabeband);

                        rechts = (run2 < runR) ? dataManager.getNextNumberOfBand(eingabeband2) : Integer.MAX_VALUE;
                        //rechts = (!dataManager.runFinished(eingabeband2)) ? dataManager.getNextNumberOfBand(eingabeband2) : Integer.MAX_VALUE;
                        zugriffe++;
                        run2++;
                    }
                    linkesBandR = (run1 < dataManager.getRunSize(eingabeband1) || links != Integer.MAX_VALUE);
                    rechtesBandR = (run2 < dataManager.getRunSize(eingabeband2) || rechts != Integer.MAX_VALUE);
                }// 2 Runs wurden zu einem verschmolzen
                dataManager.endAddRun(ausgabeband);
                dataManager.skipRun(eingabeband1);
                dataManager.skipRun(eingabeband2);
                printBands();
                if (DEBUG) {
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
                    int i = dataManager.getRunCount(ausgabeband);
                    i = dataManager.getRunCount(eingabeband1);
                    i = dataManager.getRunCount(eingabeband2);
                    if (DEBUG) {
                        printBands();
                    }
                    return ausgabeband;
                }
                if (DEBUG) {
                    printBands();
                }
                long i = dataManager.getBandSize(eingabeband1);
                i = dataManager.getRunCount(eingabeband1);
                if (dataManager.getBandSize(eingabeband1) == 0) {//Pruefen ob ein Run leer ist
                    innerLoop = false;
                    int buff = eingabeband1;
                    eingabeband1 = ausgabeband;
//                    runlaenge[1] = runlaenge[0];
                    ausgabeband = buff;
                    dataManager.clearBand(ausgabeband);
                    continue;
                }
                i = dataManager.getBandSize(eingabeband2);
                i = dataManager.getRunCount(eingabeband2);
                if (dataManager.getBandSize(eingabeband2) == 0) {//Pruefen ob ein Run leer ist
                    innerLoop = false;
                    int buff = eingabeband2;
                    eingabeband2 = ausgabeband;
                    ausgabeband = buff;
//                    runlaenge[2] = runlaenge[0];
                    dataManager.clearBand(ausgabeband);
                    continue;
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
