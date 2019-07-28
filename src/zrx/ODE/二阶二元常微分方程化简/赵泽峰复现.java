package zrx.ODE.二阶二元常微分方程化简;

import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import zrx.Tools.PrintArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class 赵泽峰复现 {
    public static void main(String[] args) throws Exception{
        File file = new File("src\\zrx\\ODE\\shkl.txt");
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        double[] sList = new double[8337];
        double[] hList = new double[8337];
        double[] kList = new double[8337];
        double[] lList = new double[8337];

        for (int i = 0; i < sList.length; i++) {
            final String[] strings = bufferedReader.readLine().split("\\s+");
            sList[i] = Double.parseDouble(strings[1]);
            hList[i] = Double.parseDouble(strings[2]);
            kList[i] = Double.parseDouble(strings[3]);
            lList[i] = Double.parseDouble(strings[4]);
        }

        FirstOrderIntegrator integrator = new DormandPrince853Integrator(1.0e-10, 0.0001, 1.0e-10, 1.0e-10);
//        integrator = new ClassicalRungeKuttaIntegrator(0.01);
        ODE ode = new ODE(hList,kList,2.0,/*1.0*/1.0);

        double[] y0 = {1.0,0,0,0};
        //-0.9163633624313611
        //-0.466872529695185

//        y0 = new double[]{0,1,0,0};
        //0.3112442438948152
        //-0.9327156201050075

        y0 = new double[]{0,0,0,0};
        //-0.3512611516993143
        //-0.02514199335078313

        integrator.integrate(ode,0.0,y0,14.792+1.88,y0);
        PrintArray.print(y0);

    }
}

// y0 = x
// y1 = x'
// y2 = y
// y3 = y'