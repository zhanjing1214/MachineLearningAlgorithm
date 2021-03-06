
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/*
 * this class predict whether a song was a ‘hit’, 
 * meaning that it made it onto the Billboard Top 50 — 
 * each instance has a label “Hit?” equal to “yes” (1) or “no” (0). 
 * The attributes are: year of release(multi-valued discrete, range [1900,2000]), 
 * length of recording (continuous, range [0,7]), 
 * jazz (binary discrete, “yes”/“no”), rock and roll (binary discrete, “yes”/“no”)
 * with a tuned Neutral Network Algorithm
 */
/**
 *
 * @author zhanjing
 */
public class NN_music {

    static double[][] x_train; //x1-xp
    static double[][] x_test;
    static double[] y;//y
    static double[][] Wh;//input to hidden layer weight Wji
    static double[][] delta_Wh; //
    static double[] Wk; //hedden layer to output layer weight
    static double[] delta_Wk;
    static double error_k;//error erm for output
    static double[] error_h;//error term for hidden unit
    static double[] output_k;
    static double[][] output_h;

    //java NN_music <training_file> <test_file>
    public static void main(String[] args) {
        readData(args[0], x_train, true);
        readData(args[1], x_test, false);
        iniWeightandOutput(4);
        int n = 0;
        while (n < 150000) {
            n++;
            propagate(x_train);
            backforward();
            calculateE();
        }
        System.out.println("TRAINING COMPLETED! NOW PREDICTING.");
        calculate(x_test);
        
    }

    public static void calculate(double[][] x) {
        //double[] output = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            if (propagateOutput(i, x) >= 0.5) {
                System.out.println("yes");
            } else {
                System.out.println("no");
            }
        }
    }

    public static void calculateE() {
        double E = 0d;
        for (int i = 0; i < x_train.length; i++) {
            E += 0.5d * (y[i] - output_k[i]) * (y[i] - output_k[i]);
        }
        System.out.println(E);
    }

    public static void backforward() {
        delta_Wk = new double[Wk.length];
        delta_Wh = new double[Wh.length][Wh[0].length];
        for (int i = 0; i < x_train.length; i++) {
            backforwardGradient(i);
        }
        for (int j = 0; j < Wk.length; j++) {
            Wk[j] += delta_Wk[j];
            for (int i = 0; i < Wh[0].length; i++) {
                Wh[j][i] += delta_Wh[j][i];
            }
        }
    }

    public static void backforwardGradient(int index) {
        error_h = new double[Wk.length];
        error_k = output_k[index] * (1 - output_k[index]) * (y[index] - output_k[index]);
        for (int j = 0; j < Wk.length; j++) {
            delta_Wk[j] += 0.01d * error_k * output_h[index][j];
            error_h[j] = output_h[index][j] * (1 - output_h[index][j]) * (Wk[j] * error_k);
            for (int i = 0; i < Wh[0].length; i++) {
                delta_Wh[j][i] += 0.01d * error_h[j] * x_train[index][i];
            }
        }
    }

    public static void propagate(double[][] x) {
        output_h = new double[x.length][Wh.length];
        output_k = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            propagateOutput(i, x);
        }
    }
    
    public static double propagateOutput(int index, double[][] x) {
        double[] tempInput_h = new double[output_h[0].length];
        double tempInput_k = 0.0;
        for (int j = 0; j < output_h[0].length; j++) {
            for (int i = 0; i < x[0].length; i++) {
                tempInput_h[j] += Wh[j][i] * x[index][i];
            }
            output_h[index][j] = 1d / (1d + Math.exp(-tempInput_h[j]));
        }

        for (int i = 0; i < output_h[0].length; i++) {
            tempInput_k += Wk[i] * output_h[index][i];
        }
        output_k[index] = 1d / (1d + Math.exp(-tempInput_k));
        return output_k[index];
    }

    public static void iniWeightandOutput(int nUnit) {
        Random r = new Random();
        Wh = new double[nUnit][x_train[0].length];
        Wk = new double[nUnit];
        for (int j = 0; j < Wh.length; j++) {
            for (int i = 0; i < Wh[0].length; i++) {
                Wh[j][i] = r.nextDouble() - 0.5;
            }
        }
        for (int i = 0; i < nUnit; i++) {
            Wk[i] = r.nextDouble() - 0.5;
        }
    }

    public static void readData(String fileName, double[][] x, boolean isTrain) {
        if (isTrain == true) {
            int col_x = 4;
            int col_y = 1;
            int col = col_x + col_y;
            StringBuilder str = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
                String line = "";
                while ((line = in.readLine()) != null) {
                    str.append(line + ",");
                }
            } catch (IOException ex) {
                System.out.print(ex.getMessage());
            }
            int words = str.toString().split(",").length;
            int rowNum = words / (col);
            x = new double[rowNum - 1][col_x + 1];
            y = new double[rowNum - 1];
            for (int i = col; i < words; i++) {
                x[i / (col) - 1][0] = 1.0;
                switch (i % (col)) {
                    case 4:
                        y[i / (col) - 1] = (str.toString().split(",")[i].equalsIgnoreCase("yes")) ? 1.0 : 0.0;
                        break;
                    case 0:
                        x[i / (col) - 1][1] = (Double.parseDouble(str.toString().split(",")[i]) - 1900d) / 100d;
                        break;
                    case 1:
                        x[i / (col) - 1][2] = (Double.parseDouble(str.toString().split(",")[i]) - 0d) / (7d);
                        break;
                    default:
                        x[i / (col) - 1][i % (col) + 1] = (str.toString().split(",")[i].equalsIgnoreCase("yes")) ? 1.0 : 0.0;
                        break;
                }

            }
            x_train = x;
        } else {
            int col_x = 4;
            StringBuilder str = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
                String line = "";
                while ((line = in.readLine()) != null) {
                    str.append(line + ",");
                }
            } catch (IOException ex) {
                System.out.print(ex.getMessage());
            }
            int words = str.toString().split(",").length;
            int rowNum = words / (col_x);
            x = new double[rowNum - 1][col_x + 1];
            for (int i = col_x; i < words; i++) {
                x[i / (col_x) - 1][0] = 1.0;
                switch (i % (col_x)) {
                    case 0:
                        x[i / (col_x) - 1][1] = (Double.parseDouble(str.toString().split(",")[i]) - 1900d) / 99d;
                        break;
                    case 1:
                        x[i / (col_x) - 1][2] = (Double.parseDouble(str.toString().split(",")[i]) - 1.51d) / (6.93 - 1.51);
                        break;
                    default:
                        x[i / (col_x) - 1][i % (col_x) + 1] = (str.toString().split(",")[i].equalsIgnoreCase("yes")) ? 1.0 : 0.0;
                        break;
                }

            }
            x_test = x;
        }

    }

}
