/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rn_a3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Domani
 */
public class RN_A3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println(makeFile());
    }
    
    
        public static byte[]  makeFile() throws FileNotFoundException, IOException {
        File f = new File("./FC_Timer.java");
        FileInputStream in = new FileInputStream(f);
        int len = (int) f.length();
        byte buf[] = new byte[len];
        in.read(buf, 0, len);
        in.close();
        return buf;
    }
}
