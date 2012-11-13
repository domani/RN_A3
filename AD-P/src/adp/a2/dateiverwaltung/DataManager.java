/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adp.a2.dateiverwaltung;

import adp.a2.Util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Domani
 */
public class DataManager implements IDataExchange 
{
    //TODO files ordner löschen beim start

    private int bandAnzahl; //4 eingabe + 1 ausgabe
    private long elemAnz;
    private int runLaenge = 3;
    private Map<Integer,Band> bandmap;
    private FileGenerator fileGenerator; 
    
    public DataManager()
    {
        fileGenerator = new FileGenerator(0.576f);
        bandmap = new HashMap();
        elemAnz = fileGenerator.getFileSize()/4;
    }

    
    @Override
    public int getBandCount() {
        return bandAnzahl;
    }

    //benötigen runGröße = Länge des Runs
    public int getRunLaenge() {
        return runLaenge;
    }

    //wie viele elemente sollens werden? wo liegen die elemente?
    public long getElementAnzahl() {
        return elemAnz;
    }

    private long getRunsGesamt() {
        if(elemAnz % runLaenge == 0){
        return (elemAnz / runLaenge);
        }
        else {
            return (elemAnz/ runLaenge)+1;
        }

    }

    @Override
    public void generateInitalRuns() {
        //TODO fertigstellen
        List<Integer> runsAufList = FiboGenerator.berechnefibo((int) getRunsGesamt(), bandAnzahl-1);
        //benötige letzten k positionen vom runsauflist
        System.out.println((int)getRunsGesamt() + "" + runsAufList);
        int j = 0;
        
        int rest = runsAufList.get(runsAufList.size() - 1) - (int)getRunsGesamt();
        for (int i = bandAnzahl-1; i > 0; i--) 
        {
            int tmp = runsAufList.get(runsAufList.size() - 2 - j);
            System.out.println(tmp);
            for(int a = 0; a < (tmp-rest); a++)
            {
                List<Integer> run = new ArrayList();
                for(int f = 0; f < runLaenge; ++f)
                {

                    byte[] buffer = new byte[4];
                    for(int x = 0; x < 4; ++x) 
                    {
                        try 
                        {
                            buffer[x] = (byte) fileGenerator.getSource().read();
                        }
                        catch (IOException ex) 
                        {
                            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    int elem = Util.byteAryToInt(buffer);
                    
                    if(elem > -1) run.add(elem);
                }
                Collections.sort(run);

                bandmap.get(i).addRun();
                for(Integer elem : run) if(run.size() > 0 ) bandmap.get(i).addNumber(elem);
                bandmap.get(i).endRun();

            }
            for(int e = 0; e < rest; ++e)
            {
                bandmap.get(i).addRun();
                bandmap.get(i).endRun();
                
            }
            rest = 0;
            ++j;
            
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
    public void setBandCount(int anzahl) {
        this.bandAnzahl = anzahl;
        for(int i = 0; i <= bandAnzahl; i++) {
            bandmap.put(i, new Band(i,fileGenerator.getPath()+ "/"+i));
        }
    }

    @Override
    public void addNumberToBand(int number, int band) {
        bandmap.get(band).addNumber(number);
    }

    @Override
    public int getNextNumberOfBand(int band)
    {
        return bandmap.get(band).getNumber();
    }

    @Override
    public int getRunSize(int band)
    {
        return bandmap.get(band).getRunSize();
    }

    @Override
    public int getRunCount(int band)
    {
        return bandmap.get(band).getRunCount();
    }

    @Override
    public void addRunToBand(int band)
    {
        bandmap.get(band).addRun();
    }

    @Override
    public boolean runFinished(int band) 
    {
        return bandmap.get(band).runFinished();
    }
    
    @Override
    public void endAddRun(int band)
    {
        bandmap.get(band).endRun();
    }

    @Override
    public void clearBand(int band) 
    {
        bandmap.get(band).clearBand();
    }    
}
