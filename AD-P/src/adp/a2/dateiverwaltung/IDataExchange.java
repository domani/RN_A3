package adp.a2.dateiverwaltung;

/**
 * Schnittstelle für den Mehrphasen-Mergesort Algorithmus. Ermöglicht Zurgriff auf die Dateiverwaltung
 * @version 0.9.1 =)
 * @author Dominike, Jan M.
 */

public interface IDataExchange 
{

    /**
     * Gibt die Anzahl der Elemente auf dem Band zurück
     * @param band int - ID des Bandes
     * @return long - Anzahl Elemente auf dem Band
     */
    public long getBandSize(int band);
    
    /**
     * Gibt die nächste Zahl von dem Band zurück und loescht diese vom Band
     * @param band int - ID des Bandes
     * @return int - Nächste Zahl auf dem Band
     */
    public int getNextNumberOfBand(int band, int runID);
    
    /**
     * Gibt die Anzahl der Elemente des nächsten Runs von Band mit der ID band zurück
     * @param Band int - ID des Bandes
     * @return int - Anzahl der Elemente
     */
    public int getRunSize(int band, int runID);
    
    /**
     * Gibt die Anzahl der Runs auf dem Band mit der ID band zurück
     * @param Band int - ID des Bandes
     * @return int - Anzahl der Runs auf dem Band
     */
    public int getRunCount(int band);
            
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
      * Schreibt eine einzelne Zahl auf ein Band
      * @param number int - die zu schreibende Zahl
      * @param band int - ID des Bandes
      */
     public void addNumberToBand(int number, int band, int runID);
     
     /**
      * Schreibt einen Run auf ein Band
      * @param runSize int - Größe des neuen Runs
      * @param band int - ID des Bandes
      */
     public int addRunToBand(int band, int runSize);

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
     
     public boolean runFinished(int band, int runID);
     
     public void clearBand(int band);
     
     public void printBand(int band);
     
     /**
      * @deprecated 
      * @param band
      * @param runID 
      * @see getNextRun
      */
     public void skipRun(int band, int runID);
     
     public int getNextRun(int Band);
}
