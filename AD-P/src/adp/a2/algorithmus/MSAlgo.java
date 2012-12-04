package adp.a2.algorithmus;

import adp.a2.dateiverwaltung.IDataExchange;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author menze_000
 */
public class MSAlgo
{
    private IDataExchange dm;
    private int countBands;
    private int startRunLength;
    
    public MSAlgo(IDataExchange pDE, int pCountBands, int pStartRunLength)
    {
        this.dm = pDE;
        this.countBands = pCountBands;
        this.startRunLength = pStartRunLength;
        
        dm.setBandCount(countBands);
        dm.setInitialRunLength(startRunLength);
        dm.generateInitalRuns();
    }
    
    public void MehrphasenMergeSort()
    {
        int zugriffe = 0;
        int phasen = 0;
        
        while(!onlyOneBandHasNumbers())
        {
            int out = dm.getEmptyBand();
            int one = getBandWithRuns(new int[] {});
            int two = getBandWithRuns(new int[] {one});
            
            ++phasen;
            //System.out.println("\n" + c++);
            //printAllBands();
            
            while(!oneBandisEmpty(one, two))
            {
                int runOut = dm.addRunToBand(out);

                int a = -1;
                int b = -1;
                
                //Fiktive Runs ignorieren
                //while(dm.getRunCount(one) > 0 && dm.getRunSize(one) == 0) dm.skipRun(one);
                //while(dm.getRunCount(two) > 0 &&dm.getRunSize(two) == 0) dm.skipRun(two);
                
                int runA = dm.getNextRun(one);
                int runB = dm.getNextRun(two);
                int runSizeSum = dm.getRunSize(one, runA) + dm.getRunSize(two, runB);
                
                List<Integer> _tmpRun = new ArrayList();
                
                //while(!(dm.runFinished(one) && dm.runFinished(two)))
                while(!(dm.getRunSize(one, runA) == 0 && dm.getRunSize(two, runB) == 0))
                {
                    if(a == -1)
                    {
                        ++zugriffe;
                        //a = (!dm.runFinished(one)) ? dm.getNextNumberOfBand(one) : -1;
                        a = (dm.getRunSize(one, runA) != 0) ? dm.getNextNumberOfBand(one, runA) : -1;
                    }
                    if(b == -1) 
                    {
                        ++zugriffe;
                        //b = (!dm.runFinished(two)) ? dm.getNextNumberOfBand(two) : -1;
                        b = (dm.getRunSize(two, runB) != 0) ? dm.getNextNumberOfBand(two, runB) : -1;
                    }
                    
                    if(a == -1 && b != -1)
                    {
                        _tmpRun.add(b);
                        dm.addNumberToBand(b, out, runOut);
                        runSizeSum--;
                        ++zugriffe;
                        b = -1;
                    }
                    else if(b == -1 && a != -1)
                    {
                        _tmpRun.add(a);
                        dm.addNumberToBand(a, out, runOut);
                        runSizeSum--;
                        ++zugriffe;
                        a = -1;
                    }
                    else if(a <= b)
                    {
                        _tmpRun.add(a);
                        dm.addNumberToBand(a, out, runOut);
                        runSizeSum--;
                        ++zugriffe;
                        a = -1;
                    }
                    else if(b < a)
                    {
                        _tmpRun.add(b);
                        dm.addNumberToBand(b, out, runOut);
                        runSizeSum--;
                        ++zugriffe;
                        b = -1;
                    }
                    else
                    {
                        System.out.println("AAAAAHHHHH a " + a + ", b " + b);
                    }
                }
                
                //dm.skipRun(one);
                //dm.skipRun(two);               
                
                dm.endAddRun(out, runOut);

            }
            if(one != -1 && dm.getBandSize(one) == 0)
            {
                dm.clearBand(one);
                ++zugriffe;
            }
            if(two != -1 && dm.getBandSize(two) == 0) 
            {
                dm.clearBand(two);
                ++zugriffe;
            }
            
        }
        
        //System.out.println("Nach dem Mischen");
        System.out.println("Phasen: " + phasen);
        System.out.println("Zugriffe: " + zugriffe);
        printAllBands();
    }
    
    public boolean onlyOneBandHasNumbers()
    {
        int bc = 0;
        int bandCount = dm.getBandCount();
        for(int i = 0; i < bandCount; ++i)
        {
            bc += dm.getRunCount(i);
            if(bc > 1) return false;
        }
        return true;
    }
    
    public int getBandWithRuns(int[] exceptOf)
    {
        int bandCount = dm.getBandCount();
        for(int i = 0; i < bandCount; ++i)
        {
            boolean match = false;
            for(int y = 0; y < exceptOf.length; ++y)
            {   
                if(exceptOf[y] == i) 
                {
                    match = true;
                    break;
                }
            }
            if(match) continue;
            if(dm.getRunCount(i) == 0) continue;
            return i;
        }
        return -1;
    }
    
    public boolean oneBandisEmpty(int one, int two)
    {
        if(one == -1 || two == -1) return true;
        return (dm.getBandSize(one) == 0) || (dm.getBandSize(two) == 0);
    }
    
    public void printAllBands()
    {
        for(int i = 0; i < dm.getBandCount(); ++i)
        {
            dm.printBand(i);
        }
    }
    
}