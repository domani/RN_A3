/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adp.a2.dateiverwaltung;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void generateInitalRuns() {
        //TODO fertigstellen
        List<Integer> runsAufList = FiboGenerator.berechnefibo(getRunsGesamt(), bandAnzahl);
        //benötige letzten k positionen vom runsauflist
        for (int i = 0; i <= bandAnzahl; i++) {
            int runAnzahl = runsAufList.get(runsAufList.size() - 1 - i);
            //
            //setRunAufBand(List<Integer> run, String band)  
        }



    }

    @Override
    public void setInitialRunLength(int length) {
        runLaenge = length;
    }

    @Override
    public long getBandSize(int band) 
    {
        return bandmap.get(band).size();
    }



    @Override
    public int getEmptyBand() {
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
    public void addNumbersToBand(List<Integer> numbers, int band) {
        bandmap.get(band).addNumbers(numbers);
    }
    
    @Override
    public void addNumberToBand(int number, int band) {
        bandmap.get(band).addNumber(number);
    }

    @Override
    public List<Integer> getNumbersOfBand(int countNumbers, int band)
    {
        return bandmap.get(band).getNumbers(countNumbers);
    }
    @Override
    public int getNextNumberOfBand(int band)
    {
        return bandmap.get(band).getNumbers(1).get(0);
    }
}
