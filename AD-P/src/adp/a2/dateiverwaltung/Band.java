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
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Domani
 */
public class Band {
    
    //TODO output / FileOutputStrean rausnehmen
    
    int name;
    private File f;
    private RandomAccessFile rFile;
    private FileOutputStream output;
    private Run aktRun;
    private List<Run> runQueue;
    private String path;


    public Band(int name,String path){
        this.name = name;
        this.f = new File(path);
        try 
        {
            this.rFile = new RandomAccessFile(f, "rw");
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.path = path;
        
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
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean leer(){
        return (f.length()==0);
    }
    
    public void addRun()
    {
        aktRun = new Run(0, f.length());
        runQueue.add(aktRun);
    }
    
    public void endRun()
    {
        aktRun = null;
    }
  
    /**
     * @param number 
     */
    public void addNumber(int number) 
    {
        try 
        {
            aktRun.size++;
            rFile.seek(aktRun.endRun);
            rFile.write(Util.intToByte(number));
            aktRun.calcEndPosition();

        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public long size()
    {
        int x = 0;
        for(int i = 0; i < runQueue.size(); ++i)
        {
            x += runQueue.get(0).size;
        }
        if(aktRun != null ) x += aktRun.size;
        return x;
    }
    
    public int getRunCount()
    {
        return runQueue.size();
    }
    
    private void setNextRun()
    {
        aktRun = (runQueue.size() > 0 ) ? runQueue.remove(0) : null;
    }

    /**
     * @param countNumber
     * @return 
     */
    public int getNumber()
    {
        if(runFinished()) setNextRun();
        try 
        {
            rFile.seek(aktRun.position);
            byte[] buffer = new byte[4];
            for(int i = 0; i < 4; ++i)
            {
                buffer[i] = rFile.readByte();
            }
            aktRun.position += 4;
            aktRun.size--;
            
            return Util.byteAryToInt(buffer);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public int getRunSize()
    {
        if(aktRun == null) setNextRun();
        return aktRun.size;
    }
    
    public boolean runFinished()
    {
        if(aktRun == null && runQueue.size() > 0) setNextRun();
        
        if(aktRun.size == 0)
        {
            setNextRun();
            return true;
        }
        return false;
    }
    
    public void clearBand()
    {
        runQueue.clear();
        try 
        {
            output.close();
            output = null;
            
            f.delete();
            
            
            f = null;

            
            f = new File(path);
            f.createNewFile();
            
            output = new FileOutputStream(f, true);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    class Run
    {
        public int size;
        public long position;
        public long endRun;
        
        Run(int size, long position)
        {
            this.size = size;
            this.position = position;
            calcEndPosition();
        }
        
        public void calcEndPosition()
        {
            this.endRun = position + (4 * size);
        }
    }
}
