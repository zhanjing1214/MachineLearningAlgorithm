
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * this class implements the List-Then-Eliminate Algorithm with a test of PartB5.txt
 */

/**
 *
 * @author zhanjing
 */
public class partB {
    int colNum;
    int rowNum;
    String positive = "High";
    String negative = "Low";
    ArrayList<String[]> sampleSpace;
    ArrayList<String[]> hpSpace;
    
    public static void main(String[] args) {
        partB l = new partB();
        l.initial();
        l.deleteHypothesis(l.hpSpace, l.readTable("4Cat-Train.labeled"));
        System.out.println((int)Math.pow(2, l.colNum - 1));
        System.out.println((int)Math.pow(2, (int)Math.pow(2, l.colNum - 1)));
        System.out.println(l.hpSpace.size());
        l.voteHypothesis(l.readTable(args[0]));
    }
    
    public void initial(){
        String[] gender = new String[2];
        gender[0] = "Male"; gender[1] = "Female";
        String[] age = new String[2];
        age[0] = "Young"; age[1] = "Old";
        String[] student = new String[2];
        student[0] = "Yes"; student[1] = "No";
        String[] declined = new String[2];
        declined[0] = "Yes"; declined[1] = "No";
        sampleSpace = new ArrayList<>();
        for(int i = 0; i < 2; i ++) {
            for (int j = 0; j < 2; j ++) {
                for (int m = 0; m < 2; m ++) {
                    for(int n = 0; n < 2; n ++) {
                        String[] sample = new String[4];
                        sample[0] = gender[i];
                        sample[1] = age[j];
                        sample[2] = student[m];
                        sample[3] = declined[n];
                        sampleSpace.add(sample);
                    }
                }
            }
        }
        
        hpSpace = new ArrayList<>();
        for(int i = 0; i < 65536; i ++) {
            String hypo = Integer.toBinaryString(i);
            while(hypo.length() != 16) {
                hypo = "0" + hypo;
            }
            String[] hp = new String[16];
            for(int j = 0; j < hypo.length(); j ++) {
                if(hypo.charAt(j) == '1') {
                    hp[j] = "High";
                } else {
                    hp[j] = "Low";
                }
            }
            hpSpace.add(hp);
        }
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
       
        String[] words = str.toString().split("\t");
        columnNum = (words.length) / instanceNum;
        colNum = columnNum;
        rowNum = instanceNum;
        
        String[][] tb = new String[instanceNum][columnNum];
        for(int i = 0; i < (words.length); i ++) {
            tb[i / columnNum][i % columnNum] = words[i].split(" ")[1];
        }
        return tb;
    }
    
    public void deleteHypothesis(ArrayList<String[]> hpSpace, String[][] table) {
       // System.out.println(hpSpace.size());
        for(int i = 0; i < rowNum; i ++) {
            int hpSize = hpSpace.size();
            for(int j = hpSize - 1; j >= 0 ; j --) {
                int index = 0;
                for(int m = 0; m < sampleSpace.size(); m ++) {
                    if(sampleSpace.get(m)[0].equalsIgnoreCase(table[i][0]) &&
                            sampleSpace.get(m)[1].equalsIgnoreCase(table[i][1]) &&
                            sampleSpace.get(m)[2].equalsIgnoreCase(table[i][2]) &&
                            sampleSpace.get(m)[3].equalsIgnoreCase(table[i][3]) ) {
                        index = m;
                    }
                }
                if(!hpSpace.get(j)[index].equalsIgnoreCase(table[i][colNum - 1])) {
                    hpSpace.remove(j);
                }
            }
        }
        //System.out.println(hpSpace.size());
    }
    
    public void voteHypothesis(String[][] testData) {
        
        for(int i = 0; i < rowNum; i ++) {
            int high = 0;
            int low = 0;
            for(int j = 0; j < hpSpace.size(); j ++) {
                int index = 0;
                for(int m = 0; m < sampleSpace.size(); m ++) {
                    if(sampleSpace.get(m)[0].equalsIgnoreCase(testData[i][0]) &&
                            sampleSpace.get(m)[1].equalsIgnoreCase(testData[i][1]) &&
                            sampleSpace.get(m)[2].equalsIgnoreCase(testData[i][2]) &&
                            sampleSpace.get(m)[3].equalsIgnoreCase(testData[i][3]) ) {
                        index = m;
                    }
                }
                if(hpSpace.get(j)[index].equalsIgnoreCase(this.positive)) {
                    high ++;
                } else {
                    low ++;
                }
            }
            System.out.println(high + " " + low);
        }
    }
}
