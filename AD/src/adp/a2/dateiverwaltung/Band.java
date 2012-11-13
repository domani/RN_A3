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
    private int position;



    public Band(int name, File f){
        this.name = name;
        this.f = f;
        position = 0;
        
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
    }
    
    public boolean leer(){
        return (f.length()==0);
    }

    public void addNumbers(List<Integer> numbers) 
    {
        for(Integer number : numbers) addNumber(number);
    }
    
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


    public List<Integer> getNumbers(final int countNumber) 
    {
        return new ArrayList<Integer>()
        {{
            for(int i = 0; i < countNumber; ++i)
            {
                byte[] b = new byte[4];
                try 
                {
                    input.read(b, position, 4);
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
                }
                increasePosition();
                add(Util.byteAryToInt(b));
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
        f.delete();
        try 
        {
            f.createNewFile();
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
    
}
