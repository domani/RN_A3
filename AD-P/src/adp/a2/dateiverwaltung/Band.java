/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adp.a2.dateiverwaltung;

import adp.a2.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Domani
 */
public class Band {
    int name;
    File f;
    private FileOutputStream output;
    private FileInputStream input;
    private long position;
    private List<Run> runQueue;
    private String path;


    public Band(int name,String path){
        this.name = name;
        this.f = new File(path);
        this.path = path;
        position = 0;
        runQueue = new ArrayList();
        
        if(!f.exists())
        {
            try 
            {
                f.createNewFile();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try 
        {
            output = new FileOutputStream(f, true);
            input = new FileInputStream(f);
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean leer(){
        return (f.length()==0);
    }
    
    public void addRun(List<Integer> run)
    {
        runQueue.add(new Run(run.size(), f.length()));
        for(Integer number : run) addNumber(number);
    }

    /**
     * @deprecated Benutze addRun
     * @param numbers 
     */
    public void addNumbers(List<Integer> numbers) 
    {
        for(Integer number : numbers) addNumber(number);
    }
    
    /**
     * @deprecated use addRun
     * @param number 
     */
    public void addNumber(int number) 
    {
        try 
        {
            output.write(Util.intToByte(number));
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public long size()
    {
        return f.length() / 4;
    }
    
    public int getRunCount()
    {
        return runQueue.size();
    }
    
    public int getNextRunSize()
    {
        return runQueue.get(0).size;
    }
    
    public List<Integer> getNextRun()
    {
        final Run run = runQueue.remove(0);
        
        List<Integer> elems = new ArrayList<Integer>()
        {{
            for(int i = 0; i < run.size; ++i)
            {
                byte[] buffer = new byte[4];
                
                for(int x = 0; x < 4; ++x) 
                {
                    try 
                    {
                        buffer[x] = (byte) input.read();
                    }
                    catch (IOException ex) 
                    {
                        Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                run.position +=4;
                increasePosition();
                add(Util.byteAryToInt(buffer));
            }
        }};
        
        if(runQueue.isEmpty()) clearBand();
        
        return elems;
    }

    /**
     * @deprecated use getNextRun
     * @param countNumber
     * @return 
     */
    public List<Integer> getNumbers(final int countNumber) 
    {
        return new ArrayList<Integer>()
        {{
            for(int i = 0; i < countNumber; ++i)
            {
                byte[] buffer = new byte[4];
                for(int x = 0; x < 4; ++x) 
                {
                    try 
                    {
                        buffer[x] = (byte) input.read();
                    }
                    catch (IOException ex) 
                    {
                        Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                increasePosition();
                
                add(Util.byteAryToInt(buffer));
            }
        }};
    }
    
    public FileOutputStream getOutputStream()
    {
        return output;
    }
    
    public FileInputStream getInputStream()
    {
        return input;
    }
    
    public void clearBand()
    {
        //TODO tedten ob strams valide bleiben

        runQueue.clear();
        try 
        {
            output.close();
            input.close();
            
            output = null;
            input = null;
            
            f.delete();
            
            
            f = null;

            
            f = new File(path);
            f.createNewFile();
            
            output = new FileOutputStream(f, true);
            input = new FileInputStream(f);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void increasePosition()
    {
        position += 4;
    }
 
    class Run
    {
        public int size;
        public long position;
        
        Run(int size, long position)
        {
            this.size = size;
            this.position = position;
        }
    }
}
