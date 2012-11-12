/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad_a2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
                output = new FileOutputStream(f);
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

    public void setZahlenVonBand(List<Integer> run, String band) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public List getZahlenVonBand(int band) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public FileOutputStream getOutputStream()
    {
        return output;
    }
    
    public FileInputStream getInputStream()
    {
        return input;
    }
    
}
