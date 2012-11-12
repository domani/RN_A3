/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adp.a2;

import java.nio.ByteBuffer;

/**
 *
 * @author Domani
 */
public class Util 
{
    public static int byteAryToInt(byte[] ary)
    {
        return ByteBuffer.wrap(ary).getInt();
    }
    
    public static byte[] intToByte(int number)
    {
        return ByteBuffer.allocate(4).putInt(number).array();
        
    }
    
    
}
