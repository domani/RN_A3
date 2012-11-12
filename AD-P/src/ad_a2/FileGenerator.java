/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad_a2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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
            Output = new FileOutputStream(new File(path + "/source"));
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(FileGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
        public FileGenerator(float size){
        this(size,DEFAULT_PATH);
        
    }
    
    
    
    public void checkPath(String aPath){
       File dir = new File(aPath);
        if(!dir.exists()) dir.mkdirs();
    }
    
    
    public void generateNumbers() throws IOException{
        Random rand = new Random();
        for(int i = 0; i <= (size*1000)-4;i+=4){
            
            Output.write(util.intToByte(rand.nextInt(Integer.MAX_VALUE)));
        }
    }
    
    public String getPath(){
        return path;
    }
    
    
}
