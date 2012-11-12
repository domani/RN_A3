package adp.a2.dateiverwaltung;

import java.util.List;

/**
 * Schnittstelle für den Mehrphasen-Mergesort Algorithmus. Ermöglicht Zurgriff auf die Dateiverwaltung
 * @version 0.9
 * @author Domani
 */

public interface IDataExchange {

    /**
     * Gibt die Anzahl der Elemente auf dem Band zurück
     * @param band int - ID des Bandes
     * @return long - Anzahl Elemente auf dem Band
     */
    public long getBandSize(int band);
    
    /**
     * Gibt eine Liste mit Elementen von Band mit der ID Band zurück;
     * @param countNumbers int - Anzahl der Elemente die zurückgegeben werden
     * @param band int - ID des Bandes
     * @return List<Integer> - Liste mit Elementen
     */
    public List<Integer> getNumbersOfBand(int countNumbers, int band);
    
    /**
     * Gibt die nächste Zahl von dem Band zurück
     * @param band int - ID des Bandes
     * @return int - Nächste Zahl auf dem Band
     */
    public int getNextNumberOfBand(int band);
            
    /**
     * Gibt die ID des aktuell leeren Bandes zurück
     * @return int - ID des leeren Bandes
     */
    public int getEmptyBand();
    
    /**
     * Gibt die Anzahl der zur Verfügung stehenden Bänder zurück
     * @return int - Anzahl der Bänder
     */
     public int getBandCount();
    
    /**
     * Setzt die Anzahl der Bänder. Muss vor generateInitialRuns aufgerufen werden.
     * @param anzahl int - Anzahl der Bänder
     */
     public void setBandCount(int anzahl);
     
    /**
     * Schreibt eine beliebige Anzahl von Elementen auf ein Band
     * @param numbers List<Integer> - Liste mit Elementen
     * @param band int - ID des Bandes
     */
     public void addNumbersToBand(List<Integer> numbers, int band);
     
     /**
      * Schreibt eine einzelne Zahl auf ein Band
      * @param number int - die zu schreibende Zahl
      * @param band int - ID des Bandes
      */
     public void addNumberToBand(int number, int band);

     /**
      * Generiert die Runs und verteilt sie auf die Bänder.
      * Muss am Anfang ausgeführt werden.
      * Benötigt setInitialRunLength
      * Benötigt setBandCount
      */
     public void generateInitalRuns();
     
     /**
      * Setzt die Runlänge, die zum Initialisieren verwendet wird.
      * Muss vor generateInitialRuns ausgeführt werden.
      * @param length int - Länge eines Runs
      */
     public void setInitialRunLength(int length);
     


    
}
