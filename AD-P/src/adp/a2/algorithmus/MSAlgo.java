package adp.a2.algorithmus;

import adp.a2.dateiverwaltung.IDataExchange;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 *
 * @author menze_000
 */
public class MSAlgo
{
    private IDataExchange dm;
    private int countBands;
    private int startRunLength;
    
    public static int zugriffe = 0;
    public static long RESERVED_MEMORY_MB = 100;
    public static int MAX_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 4;

    
    private List<Thread> threadList;
    private long freeMemory;
    
    public MSAlgo(IDataExchange pDE, int pCountBands, int pStartRunLength)
    {
        this.dm = pDE;
        this.countBands = pCountBands;
        this.startRunLength = pStartRunLength;
        
        freeMemory = (Runtime.getRuntime().freeMemory() / 1000000) - RESERVED_MEMORY_MB;
        this.threadList = new ArrayList();
        
        dm.setBandCount(countBands);
        dm.setInitialRunLength(startRunLength);
        dm.generateInitalRuns();
    }
    
    public void MehrphasenMergeSort()
    {
        int phasen = 0;
        
        while(!onlyOneBandHasNumbers())
        {
           
            int out = dm.getEmptyBand();
            int one = getBandWithRuns(new int[] {});
            int two = getBandWithRuns(new int[] {one});
            zugriffe += 3;
            ++phasen;
            
            System.out.println("Phase: " + phasen + " - Freier Speicher: " + (Runtime.getRuntime().freeMemory() / 1000000) + " MB");
            
            int THREAD_COUNT = Math.min(dm.getRunCount(one), dm.getRunCount(two));
            zugriffe += 2;
            
            if(THREAD_COUNT > MAX_THREAD_COUNT) THREAD_COUNT = MAX_THREAD_COUNT;
            
            while(!oneBandisEmpty(one, two))
            {
                threadList.clear();
                
                for(int i = 0; i < THREAD_COUNT; ++i)
                {
                    int runA = dm.getNextRun(one);
                    int runB = dm.getNextRun(two);
                    zugriffe += 2;

                    if(runA != -1 && runB != -1)
                    {
                        Thread _t = mergeRuns(one, two, out, runA, runB, dm);
                        threadList.add(_t);
                        _t.start();
                    }
                }
                
                for(Thread t : threadList)
                {
                    try 
                    {
                        t.join();
                    } 
                    catch (InterruptedException ex) 
                    {
                        Logger.getLogger(MSAlgo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
    
    private static Thread mergeRuns(final int bandA, final int bandB, final int bandOut, final int runA, final int runB, final IDataExchange dm)
    {
        return new Thread(new Runnable() 
        {

            @Override
            public void run() 
            {
                int runOut = dm.addRunToBand(bandOut, (dm.getRunSize(bandA, runA) + dm.getRunSize(bandB, runB)));

                int a = -1;
                int b = -1;
                
                while(!(dm.getRunSize(bandA, runA) == 0 && dm.getRunSize(bandB, runB) == 0))
                {
                    if(a == -1)
                    {
                        ++zugriffe;
                        a = (dm.getRunSize(bandA, runA) != 0) ? dm.getNextNumberOfBand(bandA, runA) : -1;
                    }
                    if(b == -1) 
                    {
                        ++zugriffe;
                        b = (dm.getRunSize(bandB, runB) != 0) ? dm.getNextNumberOfBand(bandB, runB) : -1;
                    }
                    
                    if(a == -1 && b != -1)
                    {
                        dm.addNumberToBand(b, bandOut, runOut);
                        ++zugriffe;
                        b = -1;
                    }
                    else if(b == -1 && a != -1)
                    {
                        dm.addNumberToBand(a, bandOut, runOut);
                        ++zugriffe;
                        a = -1;
                    }
                    else if(a <= b)
                    {
                        dm.addNumberToBand(a, bandOut, runOut);
                        ++zugriffe;
                        a = -1;
                    }
                    else if(b < a)
                    {
                        dm.addNumberToBand(b, bandOut, runOut);
                        ++zugriffe;
                        b = -1;
                    }
                    else
                    {
                        System.out.println("AAAAAHHHHH a " + a + ", b " + b);
                    }
                }
                
                dm.endAddRun(bandOut, runOut);
            }
        });
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