
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * This class calculates the label entropy at the root (i.e. the entropy of the labels before any splits)
 * and the error rate (the percent of incorrectly classified instances) of classifying 
 * using amajority vote (picking the label with the most examples).
 */

/**
 *
 * @author zhanjing
 */
public class inspect {
    public static void main(String[] args) {
        inspect i = new inspect();
        String[][] table = readTable(args[0]);
        System.out.println("entropy: " + i.calculateEntropy(table));
        System.out.println("error: " + i.calulateError(table));
    }
    
    public double calculateEntropy(String[][] tb) {
        double entropy = 0;
        if(tb.length > 0 && tb[0].length > 0) {
            int c1 = 0;
            int c2 = 0;
            int labelCol = tb[0].length - 1;
            double rowNum = tb.length;
            String label1 = tb[0][labelCol];
            for(int i = 0; i < tb.length; i ++) {
                if(tb[i][labelCol].equalsIgnoreCase(label1)) {
                    c1 ++;
                } else {
                    c2 ++;
                }
            }
            entropy = (c1/rowNum)*log((rowNum/c1), 2) + (c2/rowNum)*log((rowNum/c2), 2);
        }
        return entropy;
    }
    
    public double calulateError(String[][] tb) {
        double error = 0;
        if(tb.length > 0 && tb[0].length > 0) {
            int c1 = 0;
            int c2 = 0;
            int labelCol = tb[0].length - 1;
            double rowNum = tb.length;
            String label1 = tb[0][labelCol];
            for(int i = 0; i < tb.length; i ++) {
                if(tb[i][labelCol].equalsIgnoreCase(label1)) {
                    c1 ++;
                } else {
                    c2 ++;
                }
            }
            if(c1 <= c2){
                error = c1/rowNum;
            } else {
                error = c2/rowNum;
            }
        }
        return error;
    }
    
    public static double log(double x, int base) {
        return (Math.log(x) / Math.log(base));
    }
    
    
    public static String[][] readTable(String fileName) {
        int columnNum;
        int rowNum = 0;
        StringBuilder str = new StringBuilder();
        
        try(BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            while((line = in.readLine()) != null) {
                str.append(line + ",");
                rowNum ++;
            }
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
        int words = str.toString().split(",").length;
        columnNum = words / rowNum;
        String[][] tb = new String[rowNum - 1][columnNum];
        for(int i = columnNum; i < words; i ++) {
            tb[(i - columnNum) / columnNum][i % columnNum] =str.toString().split(",")[i];
        }
        return tb;
    }
}
