/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adp.a2.dateiverwaltung;

import java.util.List;

/**
 *
 * @author Domani
 */

//TODO JavaDoc fürs Interface

public interface IDataExchange {
    //bänder = dateien -> String path des bandes
    //runs = arraylist
    //andere: verwalten rungröße 
    
    //gib uns x zahlen (eher runs?) für band y
    public long getBandSize(int band);
    
    //alle runs, die auf dem band liegen
    public List<Integer> getNumbersOfBand(int countNumbers, int band);
    
    /**
     *
     * @param band
     * @return
     */
    public int getNextNumberOfBand(int band);
            
    //gib uns das leere band
    public int getEmptyBand();
    
    //gib uns anzahl der bänder
     public int getBandAnzahl();
    
    //set anzahl der bänder
     public void setBandAnzahl(int anzahl);
     
    //schreibe auf band (arraylist mit zahlen, welches band)
     public void addNumbersToBand(List<Integer> numbers, int band);
     
     public void addNumberToBand(int number, int band);
     
    //init verteile zahlen (runs?) auf bänder
    // für initi: die geben usn paras und wir berechnen verteilung
    //zu beginn: auf welches band wie viele runs bzw welche runs , wie viele bänder -> fibo
     public void generateInitalRuns();
     
    //angabe runlänge -> anzahl zahlen in einem un immer gleich
     //public int getRunlaenge();
     
     public void setInitialRunLength(int length);
     


    
}
