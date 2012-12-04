/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adp.a2;

import adp.a2.algorithmus.MSAlgo;
import adp.a2.dateiverwaltung.DataManager;

/**
 *
 * @author Domani
 */
public class AD_A2 {

    //public static int[] a = {26,85,50,80,30,70,28,63,57,98,53,10,3};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
               
        DataManager dm = new DataManager();
        
        /*
        dm.setInitialRunLength(3);
        dm.setBandCount(3);
        dm.generateInitalRuns();
        for(int i = 0; i < dm.getBandCount(); ++i)
        {
            
            System.out.println("Band " + i + " - Größe: " + dm.getBandSize(i) );
            int runs = dm.getRunCount(i);
            for(int j = 0; j < runs; j++)
            {
                List<Integer> run = new ArrayList();
                while(!dm.runFinished(i))
                {
                    run.add(dm.getNextNumberOfBand(i));
                }
                
                System.out.println("Run " + j + " " + run.toString());
                
            
            }
        }
        */
        
        MSAlgo algo = new MSAlgo(dm, 3, 3);
        algo.MehrphasenMergeSort();
        
        //System.out.println(FiboGenerator.berechnefibo(25, 3));
        
        
        
        
    }
/*
    public static int[] mergeSort(int left, int right) {
       int[] tmp = new int[a.length];
        if (left < right) {
            int mid = (right + left) / 2;
            mergeSort(left, mid);
            mergeSort((mid + 1), right);
            merge(left, mid, right,tmp);
        }
        return tmp;
    }

    public static void merge(int left, int mid, int right, int[] tmp) {
        int tmp_i = 0;
        int i = left; 
        int j = mid+1;
        while ((i <= mid) && (j <= right)) {
            if (a[i] < a[j]) {
                tmp[tmp_i] = a[i];
                i++;
            } else {
                tmp[tmp_i] = a[j];
                j++;
            }
            tmp_i++;
        }
        while (i <= mid) {
            tmp[tmp_i] = a[i];
            tmp_i++;
            i++;
        }
        while (j <= right) {
            tmp[tmp_i] = a[j];
            tmp_i++;
            j++;
        }
        tmp_i = 0;
        for (i = left; i <= right; i++) {
            a[i] = tmp[tmp_i];
            tmp_i++;
        }
        System.out.println(java.util.Arrays.toString(a));
    }
    */
}
