/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adp.a2.dateiverwaltung;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Domani
 */
public final class FiboGenerator {
    
    private FiboGenerator(){
        
    }

    public static List<Integer> berechnefibo(int runsGes, int ordnung) {
        int stopp = 0;
        List<Integer> result = new ArrayList();
        //die ersten zahlen inkl.0 bis ordnung-2 einer ordnung sind immer null
        //bsp: 4.ordnung: 0 ,0,0,1,1,2
        //mathematisch: 0 ≤ n ≤ k-2, dann fibk(n) = 0, also f(0),f(1),f(2)
        for (int n = 0; n < ordnung-2; n++) {
            result.add(0);
        }
        //die ersten beiden fibozahlen nach 0 sind immer 1
        //mathematisch: fibk (1) = 1, falls n = k-1; aber: hier eine stelle weiter vorn
        result.add(1);
        result.add(1);
        
        int n, j = 1;
        while(stopp <= runsGes){
        int sum = 0;
        //f(ordnung) = 1, ergo f(ordnung+1) = erste richtig zu berechnende zahl
        n = ordnung+j;
        //fibk (n) = fibk (n-1) + fibk ( n-2) + … + fibk ( n-k);
        for(int i = 1; i <= ordnung;i++){
            if( (n-i) < 0) break;
            sum += result.get(n-i-1);
        }
        result.add(sum);
        j++;
        
        for(int i = ordnung-1;i >= 0; i--){
            stopp += result.get(result.size()-i-1);
            if(stopp >= runsGes) break;
            else stopp = 0;
        }
        }
        return result;
    }
}
