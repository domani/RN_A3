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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Domani
 */
public class FileGenerator {
    
    //in kb
    public static final float DEFAULT_SIZE = 0.017f;
    public static final String DEFAULT_PATH = "./files";
    private float size;
    private String path;
    private FileOutputStream Output;
    private FileInputStream input;
    private File f;
    
    public FileGenerator()
    {
        this(DEFAULT_SIZE,DEFAULT_PATH);
        
    }
    
    public FileGenerator(float size, String path)
    {
        
        this.size = size;
        this.path = path;
        checkPath(path);
        try 
        {
            f = new File(path + "/source");
            Output = new FileOutputStream(f);
            input = new FileInputStream(f);
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(FileGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            generateNumbers();
        } catch (IOException ex) {
            Logger.getLogger(FileGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        try 
        {
            Output.flush();
            Output.close();
        } catch (IOException ex) {
            Logger.getLogger(FileGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
        public FileGenerator(float size){
        this(size,DEFAULT_PATH);
        
    }
    
    
    
    public final void checkPath(String aPath){
       File dir = new File(aPath);
        if(!dir.exists()) dir.mkdirs();
    }
    
    
    public void generateNumbers() throws IOException{
        Random rand = new Random();
        for(int i = 0; i <= (size*1000)-4;i+=4){
            
            //Output.write(Util.intToByte(rand.nextInt(Integer.MAX_VALUE)));
            Output.write(Util.intToByte(rand.nextInt(15)));
        }
    }
    
    public String getPath(){
        return path;
    }
    
    public FileInputStream getSource()
    {
        return input;
    }
    
    public long getFileSize(){
        return f.length();
    }
    
}
