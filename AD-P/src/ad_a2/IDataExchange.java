/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad_a2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Domani
 */
public interface IDataExchange {
    //bänder = dateien -> String path des bandes
    //runs = arraylist
    //andere: verwalten rungröße 
    
    //gib uns x zahlen (eher runs?) für band y
    public int getZahlenAnzahlAufBand(int band);
    
    //alle runs, die auf dem band liegen
    public List getZahlenVonBand(int band);
            
    //gib uns das leere band
    public int getLeeresBand();
    
    //gib uns anzahl der bänder
     public int getBandAnzahl();
    
    //set anzahl der bänder
     public void setBandAnzahl(int anzahl);
     
    //schreibe auf band (arraylist mit zahlen, welches band)
     public void setZahlenVonBand(List<Integer> run, String band);
     
    //init verteile zahlen (runs?) auf bänder
    // für initi: die geben usn paras und wir berechnen verteilung
    //zu beginn: auf welches band wie viele runs bzw welche runs , wie viele bänder -> fibo
     public void verteileAufBänder();
     
    //angabe runlänge -> anzahl zahlen in einem un immer gleich
     public int getRunlaenge();
     
     public void setRunLaenge(int lange);
     


    
}
