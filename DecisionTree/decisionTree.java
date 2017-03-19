
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * this class implements decision tree algorithm
 * train and test on two dataset (education and politicians)
 */

/**
 *
 * @author zhanjing
 */
public class decisionTree {
    treeNode root;
    
    // run class: java decisionTree.java trainFileName testFileName
    public static void main(String[] args) {
        String[][] tb = readTable(args[0]);
        
        decisionTree t = new decisionTree();
        ArrayList<String[]> originExamples = t.extractExamples(tb);
        ArrayList<String[]> examples = adjustExamples(originExamples);
        ArrayList<String> attributes = t.extractAttributes(tb);
        String target = t.extractTarget(tb);
        t.trainData(examples, target, attributes);
        t.printDecisionTree(t.root, 0);
        System.out.println("error(train): " + t.calculateError(t.root, examples, attributes));
        
        String[][] tb2 = readTable(args[1]);
        ArrayList<String[]> originExamples2 = extractExamples(tb2);
        ArrayList<String[]> examples2 = adjustExamples(originExamples2);
        System.out.println("error(test): " + t.calculateError(t.root, examples2, attributes));
    }
    
    
    public decisionTree(){
        root = null;
    }
    
    
    public static double calculateError(treeNode root, ArrayList<String[]> examples, ArrayList<String> attributes) {
        double countError = 0;
        for(int i = 0; i < examples.size(); i ++) { 
            treeNode temp = root;
            while(!temp.isLeaf()) {
                String split = temp.getRecord()[0];
                int index = 0;
                for(int j = 0; j < attributes.size(); j ++) {
                    if(attributes.get(j).equalsIgnoreCase(split)){
                        index = j;
                    }
                }
                if(examples.get(i)[index].equalsIgnoreCase("y")) {
                    temp = temp.getLeft();
                } else {
                    temp = temp.getRight();
                }
            }
            String label = temp.getRecord()[0];
            int labelIndex = examples.get(0).length - 1;
            if(!label.equalsIgnoreCase(examples.get(i)[labelIndex])) {
                countError ++;
            }
        }
        double all = examples.size();
        double error = countError / all;
        return error;
    }
    
    
    public void printDecisionTree(treeNode root, int level) {
        if(root != null) {
            if(root.isLeaf()) {
                System.out.println("[" + root.getRecord()[1] + "+/" + root.getRecord()[2] + "-]");
            } else {
                System.out.println("[" + root.getRecord()[1] + "+/" + root.getRecord()[2] + "-]");
                //left
                for(int i = 0; i < level; i ++) {
                    System.out.print("| ");
                }
                System.out.print(root.getRecord()[0] + " = " + getYattLabel(root.getRecord()[0]) + ": ");
                
                
                printDecisionTree(root.getLeft(), level + 1);
                //right
                for(int i = 0; i < level; i ++) {
                    System.out.print("| ");
                }
                System.out.print(root.getRecord()[0] + " = " + getNattLabel(root.getRecord()[0]) + ": ");
                printDecisionTree(root.getRight(), level + 1);
            }
        }
        
    }
    
    
    public String getYattLabel(String att) {
        String Yattlabel;
        if(att.equalsIgnoreCase("M1") ||
                att.equalsIgnoreCase("M2") ||
                att.equalsIgnoreCase("M3") ||
                att.equalsIgnoreCase("M4") ||
                att.equalsIgnoreCase("M5") ||
                att.equalsIgnoreCase("P1") ||
                att.equalsIgnoreCase("P2") ||
                att.equalsIgnoreCase("P3") ||
                att.equalsIgnoreCase("P4") ||
                att.equalsIgnoreCase("F")){
            Yattlabel = "A";
        } else if(att.equalsIgnoreCase("solo") ||
                att.equalsIgnoreCase("vocal") ||
                att.equalsIgnoreCase("original") ||
                att.equalsIgnoreCase("folk") ||
                att.equalsIgnoreCase("classical") ||
                att.equalsIgnoreCase("rhythm") ||
                att.equalsIgnoreCase("jazz") ||
                att.equalsIgnoreCase("rock")) {
            Yattlabel = "yes";
        } else if(att.equalsIgnoreCase("maint") ||
                att.equalsIgnoreCase("safety")) {
            Yattlabel = "high";
        } else if(att.equalsIgnoreCase("doors") ||
                att.equalsIgnoreCase("person")) {
            Yattlabel = "Two";
        } else if(att.equalsIgnoreCase("length")) {
            Yattlabel = "morethan3min";
        } else if(att.equalsIgnoreCase("year")) {
            Yattlabel = "before1950";
        } else if(att.equalsIgnoreCase("tempo")) {
            Yattlabel = "fast";
        } else if(att.equalsIgnoreCase("buying")) {
            Yattlabel = "expensive";
        } else if (att.equalsIgnoreCase("boot")){
            Yattlabel = "large";
        } else {
            Yattlabel = "y";
        }
        return Yattlabel;
    }
    
    public String getNattLabel(String att) {
        String Nattlabel;
        if(att.equalsIgnoreCase("M1") ||
                att.equalsIgnoreCase("M2") ||
                att.equalsIgnoreCase("M3") ||
                att.equalsIgnoreCase("M4") ||
                att.equalsIgnoreCase("M5") ||
                att.equalsIgnoreCase("P1") ||
                att.equalsIgnoreCase("P2") ||
                att.equalsIgnoreCase("P3") ||
                att.equalsIgnoreCase("P4") ||
                att.equalsIgnoreCase("F")){
            Nattlabel = "notA";
        } else if(att.equalsIgnoreCase("solo") ||
                att.equalsIgnoreCase("vocal") ||
                att.equalsIgnoreCase("original") ||
                att.equalsIgnoreCase("folk") ||
                att.equalsIgnoreCase("classical") ||
                att.equalsIgnoreCase("rhythm") ||
                att.equalsIgnoreCase("jazz") ||
                att.equalsIgnoreCase("rock")) {
            Nattlabel = "no";
        } else if(att.equalsIgnoreCase("maint") ||
                att.equalsIgnoreCase("safety")) {
            Nattlabel = "low";
        } else if(att.equalsIgnoreCase("doors") ||
                att.equalsIgnoreCase("person")) {
           Nattlabel = "MoreThanTwo";
        } else if(att.equalsIgnoreCase("length")) {
            Nattlabel = "lessthan3min";
        } else if(att.equalsIgnoreCase("year")) {
            Nattlabel = "after1950";
        } else if(att.equalsIgnoreCase("tempo")) {
            Nattlabel = "slow";
        } else if(att.equalsIgnoreCase("buying")) {
            Nattlabel = "cheap";
        } else if (att.equalsIgnoreCase("boot")){
            Nattlabel = "small";
        } else {
            Nattlabel = "n";
        }
        return Nattlabel;
    }
    
        
    public treeNode trainData(ArrayList<String[]> examples, String target, ArrayList<String> attributes) {
        
        treeNode tempRoot;
        if(allPos(examples)) {
            String[] label = new String[3];
            label[0] = "+"; label[1] = "" + examples.size(); label[2] = "0";
            tempRoot = new treeNode(label);
        } else if(allNeg(examples)) {
            String[] label = new String[3];
            label[0] = "-"; label[2] = "" + examples.size(); label[1] = "0";
            tempRoot = new treeNode(label);
        } else if(attributes.isEmpty()) {
            String[] label = findMajority(examples);
            tempRoot = new treeNode(label);
        } else {
            int best1 = findBestAttribute(examples, attributes);
            if(best1 == -1) {
                String[] label = findMajority(examples);
                tempRoot = new treeNode(label);
            } else {
                String[] split = new String[3];
                split[0] = attributes.get(best1);
                split[1] = findMajority(examples)[1]; split[2] = findMajority(examples)[2];
                tempRoot = new treeNode(split);
                //nextLevel
                ArrayList<String[]> examplesY = new ArrayList();
                ArrayList<String[]> examplesN = new ArrayList();
                int originSize = examples.get(0).length;
                for(int i = 0; i < examples.size(); i ++) {
                    String[] ex = new String[originSize - 1];
                    for(int j = 0; j < originSize; j ++) {
                        if(j == best1) {
                            continue;
                        } else if(j > best1) {
                            ex[j - 1] = examples.get(i)[j];
                        } else {
                            ex[j] = examples.get(i)[j];
                        }
                    }
                    if(examples.get(i)[best1].equalsIgnoreCase("y")){
                        examplesY.add(ex);
                    } else {
                        examplesN.add(ex);
                    }
                }
                //level2 left
                if(examplesY.isEmpty()) {
                    String[] label = findMajority(examples);
                    tempRoot.setLeft(new treeNode(label));
                } else {
                    if(allPos(examplesY)) {
                        String[] label = new String[3];
                        label[0] = "+"; label[1] = "" + examplesY.size(); label[2] = "0";
                        tempRoot.setLeft(new treeNode(label));
                    } else if(allNeg(examplesY)) {
                        String[] label = new String[3];
                        label[0] = "-"; label[2] = "" + examplesY.size(); label[1] = "0";
                        tempRoot.setLeft(new treeNode(label));
                    } else if(attributes.isEmpty()) {
                        String[] label = findMajority(examplesY);
                        tempRoot.setLeft(new treeNode(label));
                    } else {
                        ArrayList<String> nextAtt = new ArrayList();
                        for(int i = 0; i < attributes.size(); i ++) {
                            if(i != best1) {
                                nextAtt.add(attributes.get(i));
                            }
                        }
                        int best2 = findBestAttribute(examplesY, nextAtt);
                        if(best2 == -1) {
                            String[] label = findMajority(examplesY);
                            tempRoot.setLeft(new treeNode(label));
                        } else {
                            String[] split2 = new String[3];
                            split2[0] = nextAtt.get(best2);
                            split2[1] = findMajority(examplesY)[1]; split2[2] = findMajority(examplesY)[2];
                            tempRoot.setLeft(new treeNode(split2));
                            ArrayList<String[]> examplesY1 = new ArrayList();
                            ArrayList<String[]> examplesN1 = new ArrayList();
                            int originSize1 = examplesY.get(0).length;
                            for(int i = 0; i < examplesY.size(); i ++) {
                                String[] ex = new String[originSize1 - 1];
                                for(int j = 0; j < originSize1; j ++) {
                                    if(j == best2) {
                                        continue;
                                    } else if(j > best2) {
                                        ex[j - 1] = examplesY.get(i)[j];
                                    } else {
                                        ex[j] = examplesY.get(i)[j];
                                    }
                                }
                                if(examplesY.get(i)[best2].equalsIgnoreCase("y")){
                                    examplesY1.add(ex);
                                } else {
                                    examplesN1.add(ex);
                                }
                            }
                            String[] label1 = findMajority(examplesY1);
                            tempRoot.getLeft().setLeft(new treeNode(label1));
                            String[] label2 = findMajority(examplesN1);
                            tempRoot.getLeft().setRight(new treeNode(label2));
                        }
                    }
                }
                if(examplesN.isEmpty()) {
                    String[] label = findMajority(examples);
                    tempRoot.setRight(new treeNode(label));
                } else {
                    if(allPos(examplesN)) {
                        String[] label = new String[3];
                        label[0] = "+"; label[1] = "" + examplesN.size(); label[2] = "0";
                        tempRoot.setRight(new treeNode(label));
                    } else if(allNeg(examplesN)) {
                        String[] label = new String[3];
                        label[0] = "-"; label[2] = "" + examplesN.size(); label[1] = "0";
                        tempRoot.setRight(new treeNode(label));
                    } else if(attributes.isEmpty()) {
                        String[] label = findMajority(examplesN);
                        tempRoot.setRight(new treeNode(label));
                    } else {
                        ArrayList<String> nextAtt = new ArrayList();
                        for(int i = 0; i < attributes.size(); i ++) {
                            if(i != best1) {
                                nextAtt.add(attributes.get(i));
                            }
                        }
                        int best2 = findBestAttribute(examplesN, nextAtt);
                        if(best2 == -1) {
                            String[] label = findMajority(examplesN);
                            tempRoot.setRight(new treeNode(label));
                        } else {
                            String[] split2 = new String[3];
                            split2[0] = nextAtt.get(best2);
                            split2[1] = findMajority(examplesN)[1]; split2[2] = findMajority(examplesN)[2];
                            tempRoot.setRight(new treeNode(split2));
                            ArrayList<String[]> examplesY1 = new ArrayList();
                            ArrayList<String[]> examplesN1 = new ArrayList();
                            int originSize1 = examplesN.get(0).length;
                            for(int i = 0; i < examplesN.size(); i ++) {
                                String[] ex = new String[originSize1 - 1];
                                for(int j = 0; j < originSize1; j ++) {
                                    if(j == best2) {
                                        continue;
                                    } else if(j > best2) {
                                        ex[j - 1] = examplesN.get(i)[j];
                                    } else {
                                        ex[j] = examplesN.get(i)[j];
                                    }
                                }
                                if(examplesN.get(i)[best2].equalsIgnoreCase("y")){
                                    examplesY1.add(ex);
                                } else {
                                    examplesN1.add(ex);
                                }
                            }
                            String[] label1 = findMajority(examplesY1);
                            tempRoot.getRight().setLeft(new treeNode(label1));
                            String[] label2 = findMajority(examplesN1);
                            tempRoot.getRight().setRight(new treeNode(label2));
                        }
                    }
                }
                
            }
        }
        root = tempRoot;
        return tempRoot;
    }
    
    
    public int findBestAttribute(ArrayList<String[]> examples, ArrayList<String> attributes) {
        String[] label = findMajority(examples);
        double posNum = Double.parseDouble(label[1]);
        double negNum = Double.parseDouble(label[2]);
        double rowNum = examples.size();
        double p, n;
        if(posNum == 0.0) {
            p = 0;
        } else {
            p = log2((rowNum/posNum), 2);
        }
        if(negNum == 0.0) {
            n = 0;
        } else {
            n = log2((rowNum/negNum), 2);
        }
        double originE = (posNum/rowNum) * p + (negNum/rowNum) * n;
        int best = 0;
        double[] entropies = new double[attributes.size()];
        for(int i = 0; i < attributes.size(); i ++) {
            entropies[i] = calculateEntropy(examples, i);
        }
        double e = entropies[0];
        for(int i = 1; i < attributes.size(); i ++) {
            if(entropies[i] < e) {
                e = entropies[i];
                best = i;
            }
        }
       
        if(originE - e < 0.1) {
            best = -1;
        }
        return best;
    }
    
    
    public double calculateEntropy(ArrayList<String[]> examples, int index) {
        double yPos = 0, yNeg = 0;
        double nPos = 0, nNeg = 0;
        int labelIndex = examples.get(0).length - 1;
        for(int i = 0; i < examples.size() ; i ++) {
            if(examples.get(i)[index].equalsIgnoreCase("y")){
                if(examples.get(i)[labelIndex].equalsIgnoreCase("+")) {
                    yPos ++;
                } else {
                    yNeg ++;
                }
            } else {
                if(examples.get(i)[labelIndex].equalsIgnoreCase("+")) {
                    nPos ++;
                } else {
                    nNeg ++;
                }
            }
        }
        double all = yPos + yNeg + nPos + nNeg;
        double y = yPos + yNeg; double n = nPos + nNeg;
        double yyP, yyN, nnP, nnN;
        if(yPos == 0.0) {
            yyP = 0;
        } else {
            yyP = (yPos/y)*log2((y/yPos), 2);
        }
        if(yNeg == 0.0) {
            yyN = 0;
        } else {
            yyN = (yNeg/y)*log2((y/yNeg), 2);
        }
        if(nPos == 0.0) {
            nnP = 0;
        } else {
            nnP = (nPos/n)*log2((n/nPos), 2);
        }
        if(nNeg == 0.0) {
            nnN = 0;
        } else {
            nnN = (nNeg/n)*log2((n/nNeg), 2);
        }
        double entropy;
        if(all != 0.0) {
            entropy = (y/all)*(yyP + yyN) + (n/all)*(nnP + nnN);
        } else {
            entropy = 0;
        }
        return entropy; 
    }
    
    
    public double log2(double x, double base) {
        return Math.log(x)/Math.log(base);
    }
    
    
    public String[] findMajority(ArrayList<String[]> examples) {
        int index = examples.get(0).length - 1;
        int countPos = 0;
        for(int i = 0; i < examples.size(); i ++) {
            if(examples.get(i)[index].equalsIgnoreCase("+")) {
                countPos ++;
            }
        }
        double exNum = examples.size();
        String[] label = new String[3];
        if(countPos/exNum >= 0.5) {
            label[0] = "+";
        } else {
            label[0] = "-";
        }
        label[1] = "" + countPos; label[2] = "" + ((int)exNum - countPos);
        return label;
    }
    
    public boolean allPos(ArrayList<String[]> examples) {
        int num = examples.size();
        int pos = 0;
        int index = examples.get(0).length - 1;
        for(int i = 0; i < num; i ++) {
            if(examples.get(i)[index].equalsIgnoreCase("+")) {
                pos ++;
            }
        }
        boolean check = false;
        if(num == pos) {
            check = true;
        }
        return check;
    }
    
    
    public boolean allNeg(ArrayList<String[]> examples) {
        int num = examples.size();
        int neg = 0;
        int index = examples.get(0).length - 1;
        for(int i = 0; i < num; i ++) {
            if(examples.get(i)[index].equalsIgnoreCase("-")) {
                neg ++;
            }
        }
        boolean check = false;
        if(num == neg) {
            check = true;
        }
        return check;
    }
    
    
    
    public static ArrayList<String[]> adjustExamples(ArrayList<String[]> originExamples){
        ArrayList<String[]> adjustExamples = new ArrayList();
        int colNum = originExamples.get(0).length;
        for(int i = 0; i < originExamples.size(); i ++) {
            String[] ex = new String[colNum];
            for(int j = 0; j < colNum; j ++) {
                if(j < colNum - 1) {
                    if(originExamples.get(i)[j].equalsIgnoreCase("A") ||
                            originExamples.get(i)[j].equalsIgnoreCase("y") ||
                            originExamples.get(i)[j].equalsIgnoreCase("before1950") ||
                            originExamples.get(i)[j].equalsIgnoreCase("yes") ||
                            originExamples.get(i)[j].equalsIgnoreCase("morethan3min") ||
                            originExamples.get(i)[j].equalsIgnoreCase("fast") ||
                            originExamples.get(i)[j].equalsIgnoreCase("expensive") ||
                            originExamples.get(i)[j].equalsIgnoreCase("high") ||
                            originExamples.get(i)[j].equalsIgnoreCase("Two") ||
                            originExamples.get(i)[j].equalsIgnoreCase("large") ) {
                        ex[j] = "y";
                    } else {
                        ex[j] = "n";
                    }
                }else {
                    if(originExamples.get(i)[j].equalsIgnoreCase("yes") ||
                            originExamples.get(i)[j].equalsIgnoreCase("democrat") ||
                            originExamples.get(i)[j].equalsIgnoreCase("A") ) {
                        ex[j] = "+";
                    } else {
                        ex[j] = "-";
                    }
                }
            } 
            adjustExamples.add(ex);
        }
        return adjustExamples;
    }
    
    
    public static ArrayList extractExamples(String[][] tb) {
        ArrayList<String[]> examples = new ArrayList();
        if(tb.length > 0) {
            for(int i = 1; i < tb.length; i ++) {
                String[] example = new String[tb[0].length];
                for(int j = 0; j < tb[0].length; j ++) {
                    example[j] = tb[i][j];
                }
                examples.add(example);
            }
        }
        return examples;
    }
    
    
    public static ArrayList extractAttributes(String[][] tb) {
        ArrayList<String> attributes = new ArrayList();
        if(tb.length > 0) {
            for(int i = 0; i < tb[0].length - 1; i ++) {
                attributes.add(tb[0][i]);
            }
        }
        return attributes;
    }
    
    
    public static String extractTarget(String[][] tb) {
        String target;
        if(tb.length > 0) {
            target = tb[0][tb[0].length - 1];
        } else {
            target = null;
        }
        return target;
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
        String[][] tb = new String[rowNum][columnNum];
        for(int i = 0; i < words; i ++) {
            tb[i / columnNum][i % columnNum] =str.toString().split(",")[i];
        }
        return tb;
    }
    
    
    class treeNode {
        String[] record;
        treeNode left;
        treeNode right;
        
        treeNode() {
            
        }
        
        treeNode(String[] r) {
            record = r;
            left = right = null;
        }
        void setRecord(String[] r) {
            record = r;
        }
        
        void setLeft(treeNode l) {
            left = l;
        }
        
        void setRight(treeNode r) {
            right = r;
        }
        
        String[] getRecord() {
            return record;
        }
        
        treeNode getLeft() {
            return left;
        }
        
        treeNode getRight() {
            return right;
        }
        boolean isLeaf() {
            return (left == null && right == null);
        }
    }
}
