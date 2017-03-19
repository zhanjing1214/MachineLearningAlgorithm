
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * this class implements the Find-S algorithm with a test set of partA4.txt
 */

/**
 *
 * @author zhanjing
 */
public class partA {
    int colNum;
    int rowNum;
    String positive = "high";
    String negative = "low";
    String hpFile = "partA4.txt";
    String[] hypothesis;
    
    public static void main(String[] args) {
        partA find = new partA();
        String[][] t = find.readTable("9Cat-Train.labeled");
        System.out.println((int)Math.pow(2, find.colNum - 1));
        System.out.println(find.countDigit(2, (int)Math.pow(2, find.colNum - 1)));
        System.out.println(1 + (int)Math.pow(3, find.colNum - 1));
        find.trainData(t);
        String[][] testData = find.readTable("9Cat-Dev.labeled");
        System.out.println(find.testHypothesisbyError(testData));
        String[][] input = find.readTable(args[0]);
        find.testHypothesisbyOutcome(input);
    }
    
    public int countDigit(int m, int n) {
        int count;
        if (m == 0) {
            count = 0;
        } else {
            double r = 1.0;
            count = 1;
            for(int i = 0; i < n; i ++) {
                r = m * r;
                while(r / 10 >= 1) {
                    count ++;
                    r = r / 10;
                }
            }
        }
        return count;
    }
    
    public String[][] readTable(String fileName) {
        int columnNum;
        int instanceNum = 0;
        StringBuilder str = new StringBuilder();
        
        try(BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            while((line = in.readLine()) != null) {
                str.append(line + "\t");
                instanceNum ++;
            }
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
        //System.out.println(instanceNum);
        String[] words = str.toString().split("\t");
        columnNum = (words.length) / instanceNum;
        colNum = columnNum;
        rowNum = instanceNum;
        /*
        String[][] table = new String[instanceNum][attributeNum];
        for(int i = 0; i < (words.length); i ++) {
            table[i / attributeNum][i % attributeNum] = words[i];
        }*/
        
        //create table for attibutes and results
        String[][] tb = new String[instanceNum][columnNum];
        for(int i = 0; i < (words.length); i ++) {
            tb[i / columnNum][i % columnNum] = words[i].split(" ")[1];
        }
        /*
        for(int i = 0 ; i < instanceNum; i ++) {
            for(int j = 0; j < columnNum; j ++) {
                System.out.print(tb[i][j] + " ");
            }
            System.out.println();
        }*/
        return tb;
    }
    
    public void printToFile(String str) {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(this.hpFile))) {
                out.write(str);
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
    }
    
    public void trainData(String[][] table) {
        hypothesis = new String[colNum - 1];
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < this.rowNum; i ++) {
            if(table[i][this.colNum-1].equals(this.positive)) {
                for(int j = 0; j < this.colNum - 1; j ++) {
                    if(hypothesis[j] == null) {
                        hypothesis[j] = table[i][j];
                    } else if(!hypothesis[j].equalsIgnoreCase(table[i][j])) {
                        hypothesis[j] = "?";
                    }    
                }
            }
            if((i + 1) % 30 == 0){
                for(int j = 0; j < hypothesis.length; j ++) {
                    sb.append(hypothesis[j]);
                    if(j != (hypothesis.length - 1)) {
                        sb.append("\t");
                    } else {
                        sb.append("\n");
                    }
                }
            }
        }
        printToFile(sb.toString());
    }
    
    public double testHypothesisbyError(String[][] table) {
        double error =0.0;
        for(int i = 0; i < table.length; i ++) {
            String predict = positive;
            for(int j = 0; j < (table[0].length - 1); j ++) {
                if(hypothesis[j] == null) {
                    predict = negative;
                    break;
                } else if (hypothesis[j] != "?") {
                    if (!hypothesis[j].equalsIgnoreCase(table[i][j])) {
                        predict = negative;
                        break;
                    }
                }
            }
            if(!predict.equalsIgnoreCase(table[i][table[0].length-1])) {
                error ++;
            }
        }
        double errorRate = (double)Math.round((error/table.length)*100)/100;
        return errorRate;
    }
    
    public void testHypothesisbyOutcome(String[][] table) {
        for(int i = 0; i < table.length; i ++) {
            String predict = positive;
            for(int j = 0; j < (table[0].length - 1); j ++) {
                if(hypothesis[j] == null) {
                    predict = negative;
                    break;
                } else if (hypothesis[j] != "?") {
                    if (!hypothesis[j].equalsIgnoreCase(table[i][j])) {
                        predict = negative;
                        break;
                    }
                }
            }
            System.out.println(predict);
        }
    }
}
