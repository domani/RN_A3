/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad_a2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Domani
 */
public class DataManager implements IDataExchange {

    private int bandAnzahl; //4 eingabe + 1 ausgabe
    private int elemAnz = 144;
    private int runLaenge = 3;
    private Map<Integer,Band> bandmap;
    private FileGenerator fileGenerator; 

    
    public DataManager(){
        fileGenerator = new FileGenerator();
        bandmap = new HashMap();
    }
    
    @Override
    public int getBandAnzahl() {
        return bandAnzahl;
    }

    //benötigen runGröße = Länge des Runs
    public int getRunLaenge() {
        return runLaenge;
    }

    //wie viele elemente sollens werden? wo liegen die elemente?
    public int getElementAnzahl() {
        return elemAnz;
    }

    private int getRunsGesamt() {
        return (elemAnz / runLaenge);
    }

    @Override
    public void verteileAufBänder() {
        List<Integer> runsAufList = FiboGenerator.berechnefibo(getRunsGesamt(), bandAnzahl);
        //benötige letzten k positionen vom runsauflist
        for (int i = 0; i <= bandAnzahl; i++) {
            int runAnzahl = runsAufList.get(runsAufList.size() - 1 - i);
            //
            //setRunAufBand(List<Integer> run, String band)  
        }



    }

    @Override
    public int getRunlaenge() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRunLaenge(int lange) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getZahlenAnzahlAufBand(int band) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    public int getLeeresBand() {
       for(Map.Entry<Integer,Band> elem : bandmap.entrySet()){
           if(elem.getValue().leer()) return elem.getKey();
       }
       return -1;
    }

    @Override
    public void setBandAnzahl(int anzahl) {
        this.bandAnzahl = anzahl;
        for(int i = 0; i <= bandAnzahl; i++) {
            bandmap.put(i, new Band(i,new File(fileGenerator.getPath()+ "/"+i)));
        }
    }

    @Override
    public void setZahlenVonBand(List<Integer> run, String band) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List getZahlenVonBand(int band) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
