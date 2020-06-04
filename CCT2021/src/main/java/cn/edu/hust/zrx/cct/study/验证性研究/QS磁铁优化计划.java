package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.study.前45偏转段2.A0423月报;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Description
 * TODO
 * <p>
 * Data
 * 20:55
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class QS磁铁优化计划 {


    @run
    public void test(){
        String s = data202004271();
        String[] split = s.split("\n");
        System.out.println();
        System.out.println(split.length);
        int i = 1;
        for (String s1 : split) {
            //READ INPUT_FILE LINE; DL1 := RE(LINE);
            //READ INPUT_FILE LINE; QS1_LEN := RE(LINE);
            //READ INPUT_FILE LINE; QS2_LEN := RE(LINE);
            //READ INPUT_FILE LINE; GAP1 := RE(LINE);
            //READ INPUT_FILE LINE; GAP2 := RE(LINE);
            //READ INPUT_FILE LINE; CCT1_ANG := RE(LINE);
            //READ INPUT_FILE LINE; CCT2_ANG := RE(LINE);
            //READ INPUT_FILE LINE; CCT12_n := RE(LINE);
            //READ INPUT_FILE LINE; QS1_Q := RE(LINE);
            //READ INPUT_FILE LINE; QS2_Q := RE(LINE);
            //READ INPUT_FILE LINE; QS1_S := RE(LINE);
            //READ INPUT_FILE LINE; QS2_S := RE(LINE);


            //READ INPUT_FILE LINE; DL2 := RE(LINE);
            //READ INPUT_FILE LINE; QS3_LEN := RE(LINE);
            //READ INPUT_FILE LINE; GAP3 := RE(LINE);
            //READ INPUT_FILE LINE; CCT3_ANG := RE(LINE);
            //READ INPUT_FILE LINE; CCT4_ANG := RE(LINE);
            //READ INPUT_FILE LINE; CCT5_ANG := RE(LINE);
            //READ INPUT_FILE LINE; CCT345_n := RE(LINE);
            //READ INPUT_FILE LINE; QS3_Q := RE(LINE);
            //READ INPUT_FILE LINE; QS3_S := RE(LINE);

            String[] split1 = s1.split(",");
            //DL1,QS1_LEN,QS2_LEN,GAP1,GAP2,CCT1_ANG,    CCT12_N,QS1_Q,QS2_Q,QS1_S,QS2_S

            System.out.println(i++);
            System.out.println(split1[0]);//DL1
            System.out.println(split1[1]);//QS1L
            System.out.println(split1[2]);//QS2L
            System.out.println(split1[3]);//G1
            System.out.println(split1[4]);//G2
            System.out.println(split1[5]);//CCT1A

            double cct1_a = Double.parseDouble(split1[5]);
            System.out.println(22.5-10-cct1_a);

            System.out.println(split1[6]);//CCT 12 N
            System.out.println(split1[7]);//QS1Q
            System.out.println(split1[8]);//QS2Q
            System.out.println(split1[9]);//QS1S
            System.out.println(split1[10]);//QS2S

            System.out.println("1.7174666339862152\n" +
                    "0.247893549954087\n" +
                    "0.198544192465594\n" +
                    "0.62847499672104\n" +
                    "21.2223170579606\n" +
                    "21.649207945318363\n" +
                    "-4.82432409415565\n" +
                    "-0.252570830004029\n" +
                    "-0.359999999999999");
        }
    }

    private String data202004271(){
        return "5.000000E-01,1.817479E-01,2.249141E-01,1.506236E-01,1.777292E-01,5.660567E+00,-2.001417E+01,3.823444E-01,7.622692E-01,-7.340292E-02,8.786459E-02\n" +
                "9.864234E-01,1.802435E-01,2.275603E-01,1.611990E-01,1.484171E-01,5.343672E+00,-1.831226E+01,4.239874E-01,7.315445E-01,7.983335E-02,5.285627E-02\n" +
                "5.101136E-01,1.818151E-01,2.279641E-01,1.507957E-01,1.760079E-01,5.643318E+00,-1.950680E+01,3.663179E-01,7.624007E-01,-5.707466E-02,5.499087E-02\n" +
                "5.016717E-01,1.807176E-01,2.249347E-01,1.625008E-01,1.367606E-01,5.012417E+00,-1.840303E+01,3.856530E-01,7.753537E-01,-8.332880E-02,8.530367E-02\n" +
                "7.217432E-01,1.778684E-01,2.352227E-01,1.609218E-01,1.372422E-01,5.948249E+00,-1.881406E+01,3.711866E-01,7.645034E-01,2.162935E-02,5.534138E-02\n" +
                "1.300000E+00,1.825831E-01,2.327217E-01,1.568999E-01,1.613314E-01,5.541969E+00,-1.613887E+01,3.623899E-01,7.371859E-01,2.499699E-02,6.128023E-02\n" +
                "5.093354E-01,1.658142E-01,2.279641E-01,1.507957E-01,1.760079E-01,5.003430E+00,-1.757205E+01,4.076308E-01,7.770472E-01,-5.707466E-02,5.403754E-02\n" +
                "1.264972E+00,1.792812E-01,2.213396E-01,1.614591E-01,1.557975E-01,5.501481E+00,-1.731630E+01,3.996076E-01,7.725812E-01,9.000000E-02,4.658020E-02\n" +
                "9.846376E-01,1.818404E-01,2.275369E-01,1.340229E-01,1.576379E-01,5.121008E+00,-1.937098E+01,4.277520E-01,7.775161E-01,-8.434725E-02,7.751725E-02\n" +
                "1.264972E+00,1.792812E-01,2.213396E-01,1.614591E-01,1.568926E-01,5.587375E+00,-1.731630E+01,3.996076E-01,7.725812E-01,9.000000E-02,4.543877E-02\n" +
                "1.285979E+00,1.828441E-01,2.180725E-01,1.585741E-01,1.487026E-01,5.015350E+00,-1.830156E+01,3.906962E-01,7.869153E-01,8.015566E-02,8.403179E-02\n" +
                "1.285979E+00,1.827625E-01,2.180725E-01,1.588740E-01,1.527224E-01,5.013034E+00,-1.881102E+01,3.906962E-01,7.879482E-01,-3.810793E-02,8.385940E-02\n" +
                "9.826262E-01,1.752674E-01,2.355912E-01,1.340229E-01,1.574393E-01,5.121008E+00,-1.937482E+01,4.277520E-01,7.765050E-01,-8.434725E-02,7.751725E-02\n" +
                "7.520210E-01,1.792239E-01,2.221699E-01,1.611927E-01,1.565996E-01,5.503329E+00,-1.730882E+01,3.906970E-01,7.722020E-01,1.027449E-02,8.768409E-02\n" +
                "1.113175E+00,1.827694E-01,2.328039E-01,1.593656E-01,1.487162E-01,5.470326E+00,-1.662301E+01,3.632308E-01,7.527802E-01,-9.000000E-02,6.290817E-02\n" +
                "1.235123E+00,1.818055E-01,2.269567E-01,1.508277E-01,1.606212E-01,5.110226E+00,-1.873537E+01,3.630000E-01,7.759301E-01,-3.883915E-02,8.782438E-02\n" +
                "6.275879E-01,2.452798E-01,2.115819E-01,1.599357E-01,1.604471E-01,5.847908E+00,-1.934463E+01,2.544804E-01,7.597518E-01,-4.424699E-02,8.564080E-02\n" +
                "7.565703E-01,1.818151E-01,2.230038E-01,1.507957E-01,1.740739E-01,5.358081E+00,-1.953716E+01,3.666328E-01,7.755552E-01,-3.208088E-02,5.562385E-02\n" +
                "9.697930E-01,1.827694E-01,2.327217E-01,1.444961E-01,1.690308E-01,5.465792E+00,-1.662301E+01,3.632308E-01,7.525338E-01,2.999671E-02,6.290817E-02\n" +
                "9.649771E-01,1.653517E-01,2.223868E-01,1.611931E-01,1.749826E-01,5.524872E+00,-1.772541E+01,4.019894E-01,7.755535E-01,-7.707100E-02,5.476814E-02\n" +
                "9.157389E-01,1.653517E-01,2.223868E-01,1.611990E-01,1.749826E-01,5.524872E+00,-1.733911E+01,4.019894E-01,7.755535E-01,-7.707100E-02,5.350796E-02\n" +
                "1.300000E+00,1.816910E-01,2.196684E-01,1.519010E-01,1.480141E-01,4.841021E+00,-1.843285E+01,4.121319E-01,7.800139E-01,9.000000E-02,8.355411E-02\n" +
                "9.826262E-01,1.752674E-01,2.355516E-01,1.340229E-01,1.538492E-01,5.010866E+00,-1.937482E+01,4.327696E-01,7.766508E-01,-8.538053E-02,5.457597E-02\n" +
                "8.986161E-01,1.813214E-01,2.259446E-01,1.518949E-01,1.621046E-01,5.050274E+00,-1.879470E+01,3.827733E-01,7.755075E-01,-8.197623E-02,8.784021E-02\n" +
                "1.300000E+00,1.816910E-01,2.244514E-01,1.405373E-01,1.530926E-01,4.856667E+00,-1.845968E+01,4.121145E-01,7.800139E-01,9.000000E-02,8.407565E-02\n" +
                "9.139808E-01,1.813214E-01,2.259446E-01,1.524575E-01,1.618854E-01,5.051042E+00,-1.879470E+01,3.825296E-01,7.753972E-01,-8.197623E-02,8.783340E-02\n" +
                "5.257890E-01,1.818151E-01,2.230038E-01,1.507957E-01,1.740739E-01,5.362469E+00,-1.953716E+01,3.663179E-01,7.755552E-01,-4.875913E-02,5.562385E-02\n" +
                "1.300000E+00,1.807291E-01,2.287821E-01,1.518970E-01,1.480141E-01,5.012981E+00,-1.843285E+01,4.127774E-01,7.722982E-01,9.000000E-02,8.347514E-02\n" +
                "1.281716E+00,1.803238E-01,2.288165E-01,1.564714E-01,1.511961E-01,5.011589E+00,-1.877246E+01,3.625855E-01,7.601449E-01,-8.999889E-02,8.523585E-02\n" +
                "5.001325E-01,1.817479E-01,2.249275E-01,1.507958E-01,1.777292E-01,5.660567E+00,-1.740335E+01,3.823444E-01,7.622692E-01,-7.340292E-02,8.786459E-02";}

    private String data20200427(){
        return "1.243344E+00,1.815538E-01,2.195648E-01,1.515362E-01,1.482515E-01,5.012998E+00,-1.885035E+01,4.121319E-01,7.923592E-01,9.000000E-02,8.355742E-02\n" +
                "5.000000E-01,1.795031E-01,2.249141E-01,1.505573E-01,1.367606E-01,4.972545E+00,-1.851187E+01,3.860673E-01,7.755762E-01,-8.978054E-02,8.424334E-02\n" +
                "9.623462E-01,1.797802E-01,2.107427E-01,1.615769E-01,1.670138E-01,5.144180E+00,-1.771714E+01,3.940484E-01,8.035269E-01,-9.000000E-02,8.905331E-02\n" +
                "1.074609E+00,1.706709E-01,2.334517E-01,1.568355E-01,1.676799E-01,5.465799E+00,-1.737002E+01,3.561651E-01,7.765704E-01,-6.703776E-02,8.904661E-02\n" +
                "6.854718E-01,1.778468E-01,2.275494E-01,1.562290E-01,1.358342E-01,5.471857E+00,-2.071641E+01,3.841058E-01,7.922252E-01,-8.929056E-02,7.668722E-02\n" +
                "5.093354E-01,1.658142E-01,2.279641E-01,1.507957E-01,1.760079E-01,5.003430E+00,-1.757205E+01,4.076308E-01,7.770472E-01,-5.707466E-02,5.403754E-02\n" +
                "1.300000E+00,1.816910E-01,2.196684E-01,1.519010E-01,1.480141E-01,4.841021E+00,-1.843285E+01,4.121319E-01,7.800139E-01,9.000000E-02,8.355411E-02\n" +
                "1.281716E+00,1.807167E-01,2.288165E-01,1.518969E-01,1.511961E-01,5.011589E+00,-1.877246E+01,3.625855E-01,7.601449E-01,-8.999889E-02,9.000000E-02\n" +
                "1.124112E+00,1.799956E-01,2.319860E-01,1.660829E-01,1.684702E-01,5.473811E+00,-2.013936E+01,3.803711E-01,7.353913E-01,-8.938747E-02,8.824650E-02\n" +
                "6.275879E-01,2.452798E-01,2.115819E-01,1.599357E-01,1.604471E-01,5.847908E+00,-1.934463E+01,2.544804E-01,7.597518E-01,-4.424699E-02,8.564080E-02\n" +
                "1.120500E+00,1.806778E-01,2.228800E-01,1.623287E-01,1.675983E-01,5.027545E+00,-1.884855E+01,3.826248E-01,7.622158E-01,-9.000000E-02,9.000000E-02\n" +
                "5.101136E-01,1.818151E-01,2.279641E-01,1.507957E-01,1.760079E-01,5.643318E+00,-1.950680E+01,3.663179E-01,7.624007E-01,-5.707466E-02,5.499087E-02\n" +
                "7.217432E-01,1.778684E-01,2.352227E-01,1.609218E-01,1.372422E-01,5.948249E+00,-1.881406E+01,3.711866E-01,7.645034E-01,2.162935E-02,5.534138E-02\n" +
                "6.035399E-01,2.427514E-01,2.117969E-01,1.341088E-01,1.681367E-01,5.793177E+00,-1.933736E+01,2.574481E-01,7.919429E-01,-8.750685E-02,8.809798E-02\n" +
                "5.941494E-01,1.766286E-01,2.275233E-01,1.594837E-01,1.774072E-01,5.421692E+00,-1.848998E+01,3.412371E-01,7.885849E-01,2.510591E-02,6.118216E-02\n" +
                "6.035399E-01,2.427514E-01,2.117969E-01,1.341306E-01,1.681367E-01,5.801125E+00,-1.937709E+01,2.535236E-01,7.919429E-01,-8.750685E-02,8.809798E-02\n" +
                "1.073157E+00,1.811970E-01,2.392937E-01,1.597858E-01,1.521412E-01,5.354137E+00,-2.000340E+01,3.708630E-01,7.332182E-01,8.776555E-02,8.925629E-02\n" +
                "9.697930E-01,1.827694E-01,2.327217E-01,1.444961E-01,1.690308E-01,5.465792E+00,-1.662301E+01,3.632308E-01,7.525338E-01,2.999671E-02,6.290817E-02\n" +
                "1.225823E+00,1.829276E-01,2.300708E-01,1.341878E-01,1.654531E-01,4.802763E+00,-1.833286E+01,3.644401E-01,7.925825E-01,-8.750927E-02,8.808419E-02\n" +
                "7.685295E-01,1.719401E-01,2.244958E-01,1.444690E-01,1.692011E-01,5.493416E+00,-1.957715E+01,3.773532E-01,8.204817E-01,-8.513983E-02,8.900565E-02\n" +
                "9.826262E-01,1.752674E-01,2.355912E-01,1.340229E-01,1.574393E-01,5.121008E+00,-1.937482E+01,4.277520E-01,7.765050E-01,-8.434725E-02,7.751725E-02\n" +
                "1.300000E+00,1.825831E-01,2.327217E-01,1.568999E-01,1.613314E-01,5.541969E+00,-1.613887E+01,3.623899E-01,7.371859E-01,2.499699E-02,6.128023E-02\n" +
                "1.285979E+00,1.827625E-01,2.180725E-01,1.588740E-01,1.527224E-01,5.013034E+00,-1.881102E+01,3.906962E-01,7.879482E-01,-3.810793E-02,8.385940E-02\n" +
                "7.692201E-01,1.802542E-01,2.405038E-01,1.397552E-01,1.564958E-01,5.017325E+00,-1.885004E+01,3.570699E-01,7.520892E-01,-8.775770E-02,8.162952E-02\n" +
                "1.243344E+00,1.814634E-01,2.195648E-01,1.522506E-01,1.690586E-01,5.012998E+00,-1.881075E+01,4.121319E-01,7.929274E-01,9.000000E-02,8.355742E-02\n" +
                "7.731389E-01,1.806875E-01,2.278711E-01,1.614857E-01,1.685933E-01,5.105085E+00,-1.887507E+01,3.837538E-01,7.353276E-01,-7.700642E-02,8.784763E-02\n" +
                "7.751523E-01,1.806875E-01,2.246301E-01,1.579199E-01,1.692011E-01,5.111425E+00,-1.957736E+01,4.224957E-01,7.370453E-01,-7.700642E-02,8.943246E-02\n" +
                "8.986161E-01,1.813214E-01,2.259446E-01,1.518949E-01,1.621046E-01,5.050274E+00,-1.879470E+01,3.827733E-01,7.755075E-01,-8.197623E-02,8.784021E-02\n" +
                "1.112362E+00,1.776711E-01,2.270553E-01,1.595350E-01,1.469406E-01,5.359961E+00,-2.031865E+01,3.848526E-01,7.696810E-01,-7.026757E-02,8.678066E-02\n" +
                "1.225823E+00,1.829276E-01,2.300708E-01,1.340139E-01,1.566271E-01,4.815114E+00,-1.833286E+01,3.644401E-01,7.925825E-01,-8.396047E-02,8.801173E-02\n" +
                "1.243344E+00,1.815434E-01,2.217170E-01,1.522005E-01,1.521638E-01,5.016644E+00,-1.885035E+01,3.571205E-01,7.925879E-01,-8.728864E-02,9.000000E-02\n" +
                "6.854718E-01,1.778468E-01,2.196873E-01,1.562290E-01,1.567000E-01,5.471857E+00,-2.071641E+01,3.841058E-01,7.929423E-01,-8.396344E-02,7.668722E-02\n" +
                "1.128940E+00,1.804956E-01,2.246363E-01,1.674547E-01,1.691936E-01,5.463962E+00,-2.015836E+01,3.803951E-01,7.353913E-01,1.918927E-02,8.785778E-02\n" +
                "1.270617E+00,1.855771E-01,2.195648E-01,1.536102E-01,1.482515E-01,5.578350E+00,-1.741938E+01,4.121117E-01,7.919971E-01,9.000000E-02,8.349824E-02\n" +
                "5.521170E-01,1.790878E-01,2.249141E-01,1.505573E-01,1.367606E-01,4.972545E+00,-1.851187E+01,3.862280E-01,7.752839E-01,-9.000000E-02,8.523128E-02\n" +
                "1.047521E+00,1.815529E-01,2.319048E-01,1.599280E-01,1.491420E-01,5.363892E+00,-1.994604E+01,3.861372E-01,7.543230E-01,8.946367E-02,8.925629E-02\n" +
                "5.279703E-01,1.806580E-01,2.193369E-01,1.517008E-01,1.480141E-01,4.760364E+00,-1.766733E+01,4.119712E-01,7.658981E-01,9.000000E-02,5.445956E-02\n" +
                "1.065681E+00,1.802931E-01,2.392937E-01,1.595672E-01,1.521412E-01,5.354137E+00,-2.000340E+01,3.708328E-01,7.332016E-01,8.776555E-02,8.925700E-02\n" +
                "6.035399E-01,2.427514E-01,2.117969E-01,1.341306E-01,1.681367E-01,5.801159E+00,-1.937709E+01,2.535071E-01,7.919429E-01,-8.750685E-02,8.103462E-02\n" +
                "1.172276E+00,1.767952E-01,2.391799E-01,1.310728E-01,1.560358E-01,4.790705E+00,-1.853576E+01,4.113837E-01,7.402174E-01,1.824895E-02,5.497861E-02\n" +
                "9.157389E-01,1.653517E-01,2.223868E-01,1.611990E-01,1.749826E-01,5.524872E+00,-1.733911E+01,4.019894E-01,7.755535E-01,-7.707100E-02,5.350796E-02\n" +
                "7.666789E-01,1.802542E-01,2.405252E-01,1.397552E-01,1.564990E-01,5.017325E+00,-1.885004E+01,3.570699E-01,7.520892E-01,-8.775770E-02,8.150640E-02\n" +
                "5.101136E-01,1.817960E-01,2.279638E-01,1.507957E-01,1.760079E-01,5.643318E+00,-1.759518E+01,3.663179E-01,7.677000E-01,8.553465E-02,8.703386E-02\n" +
                "6.854718E-01,1.778468E-01,2.343127E-01,1.562290E-01,1.483262E-01,5.471857E+00,-1.620243E+01,3.420168E-01,7.922252E-01,-8.929056E-02,7.690749E-02\n" +
                "5.093354E-01,1.659648E-01,2.222952E-01,1.507957E-01,1.743306E-01,4.924106E+00,-1.757205E+01,4.076308E-01,7.766551E-01,2.806284E-02,5.403754E-02\n" +
                "9.157389E-01,1.654876E-01,2.275052E-01,1.611699E-01,1.484212E-01,5.340978E+00,-1.831212E+01,3.937251E-01,7.760130E-01,7.984329E-02,8.923527E-02\n" +
                "1.225823E+00,1.829276E-01,2.300708E-01,1.502483E-01,1.567788E-01,4.815737E+00,-1.843642E+01,3.643758E-01,7.925825E-01,-8.765516E-02,8.790580E-02\n" +
                "1.074609E+00,1.663492E-01,2.334517E-01,1.568355E-01,1.676799E-01,5.012994E+00,-1.881789E+01,4.127418E-01,7.765704E-01,9.000000E-02,8.904661E-02\n" +
                "1.230103E+00,1.780625E-01,2.319860E-01,1.684489E-01,1.691945E-01,5.467319E+00,-2.013936E+01,3.759935E-01,7.354362E-01,1.909232E-02,8.941686E-02\n" +
                "1.235123E+00,1.818055E-01,2.274394E-01,1.508277E-01,1.602453E-01,5.110226E+00,-1.873537E+01,3.630000E-01,7.756563E-01,8.744229E-02,8.978126E-02\n" +
                "1.300000E+00,1.816910E-01,2.197333E-01,1.519010E-01,1.480141E-01,4.841021E+00,-1.843285E+01,4.121319E-01,7.724902E-01,9.000000E-02,8.347514E-02\n" +
                "7.971139E-01,1.776711E-01,2.276686E-01,1.545539E-01,1.417708E-01,5.359961E+00,-2.034515E+01,4.226644E-01,7.696810E-01,-7.026757E-02,8.940526E-02\n" +
                "1.243344E+00,1.871260E-01,2.217170E-01,1.522005E-01,1.523472E-01,5.652572E+00,-1.746023E+01,3.571205E-01,7.925879E-01,-8.705673E-02,9.000000E-02\n" +
                "1.139734E+00,1.798753E-01,2.246363E-01,1.660190E-01,1.476716E-01,5.473889E+00,-1.646268E+01,3.803951E-01,7.367059E-01,-8.418910E-02,8.942811E-02\n" +
                "8.939937E-01,1.806460E-01,2.230637E-01,1.509792E-01,1.675983E-01,5.026844E+00,-1.884960E+01,3.826248E-01,7.625265E-01,-9.000000E-02,9.000000E-02\n" +
                "5.016717E-01,1.791621E-01,2.249141E-01,1.505573E-01,1.367606E-01,5.385117E+00,-1.682079E+01,3.856530E-01,7.747710E-01,-7.999850E-02,8.523128E-02\n" +
                "8.986161E-01,1.813214E-01,2.259446E-01,1.518949E-01,1.368860E-01,5.046754E+00,-1.830933E+01,3.827733E-01,7.753283E-01,-8.927893E-02,8.784021E-02\n" +
                "1.047520E+00,1.811217E-01,2.250535E-01,1.599280E-01,1.491418E-01,5.311003E+00,-1.994604E+01,3.861372E-01,7.543230E-01,8.946367E-02,8.925629E-02\n" +
                "9.864234E-01,1.802435E-01,2.275603E-01,1.611990E-01,1.484171E-01,5.343672E+00,-1.831226E+01,4.239874E-01,7.315445E-01,7.983335E-02,5.285627E-02\n" +
                "1.139452E+00,1.778687E-01,2.269737E-01,1.660190E-01,1.688476E-01,5.473889E+00,-2.028263E+01,3.846288E-01,7.367059E-01,-6.974953E-02,8.785778E-02\n" +
                "1.281368E+00,1.807278E-01,2.286543E-01,1.519010E-01,1.478481E-01,4.841021E+00,-1.843226E+01,4.121319E-01,7.800139E-01,-8.836407E-02,8.355411E-02\n" +
                "9.826262E-01,1.752674E-01,2.355516E-01,1.340229E-01,1.744974E-01,5.121008E+00,-1.937482E+01,4.277520E-01,7.766508E-01,-8.434725E-02,5.457597E-02\n" +
                "5.016717E-01,1.807176E-01,2.249347E-01,1.625008E-01,1.367606E-01,5.012417E+00,-1.840303E+01,3.856530E-01,7.753537E-01,-8.332880E-02,8.530367E-02\n" +
                "6.035399E-01,2.467578E-01,2.117969E-01,1.338473E-01,1.676082E-01,5.801125E+00,-1.937709E+01,2.535236E-01,7.909411E-01,-8.751515E-02,8.809798E-02\n" +
                "6.524384E-01,1.754514E-01,2.343127E-01,1.562290E-01,1.483262E-01,5.471857E+00,-1.616175E+01,3.420168E-01,7.922252E-01,-8.922237E-02,7.379951E-02\n" +
                "9.139808E-01,1.813214E-01,2.259446E-01,1.524575E-01,1.618854E-01,5.051042E+00,-1.879470E+01,3.825296E-01,7.753972E-01,-8.197623E-02,8.783340E-02\n" +
                "7.520210E-01,1.792239E-01,2.221699E-01,1.611927E-01,1.565996E-01,5.503329E+00,-1.730882E+01,3.906970E-01,7.722020E-01,1.027449E-02,8.768409E-02\n" +
                "5.257890E-01,1.818151E-01,2.230038E-01,1.507957E-01,1.740739E-01,5.362469E+00,-1.953716E+01,3.663179E-01,7.755552E-01,-4.875913E-02,5.562385E-02\n" +
                "9.864234E-01,1.766037E-01,2.299412E-01,1.454677E-01,1.692924E-01,5.343672E+00,-1.831226E+01,4.239874E-01,7.315445E-01,8.264804E-02,8.983475E-02\n" +
                "1.285979E+00,1.828441E-01,2.180725E-01,1.585741E-01,1.487026E-01,5.015350E+00,-1.830156E+01,3.906962E-01,7.869153E-01,8.015566E-02,8.403179E-02\n" +
                "9.649771E-01,1.653517E-01,2.223868E-01,1.611931E-01,1.749826E-01,5.524872E+00,-1.772541E+01,4.019894E-01,7.755535E-01,-7.707100E-02,5.476814E-02\n" +
                "8.900617E-01,1.806460E-01,2.320954E-01,1.509792E-01,1.676006E-01,5.026844E+00,-1.887501E+01,3.826248E-01,7.351974E-01,-9.000000E-02,8.942527E-02\n" +
                "9.846376E-01,1.818404E-01,2.275369E-01,1.340229E-01,1.576379E-01,5.121008E+00,-1.937098E+01,4.277520E-01,7.775161E-01,-8.434725E-02,7.751725E-02\n" +
                "6.083430E-01,1.751519E-01,2.221699E-01,1.614268E-01,1.566081E-01,5.542841E+00,-1.730882E+01,3.903264E-01,7.739163E-01,-7.796080E-02,6.265188E-02\n" +
                "1.264972E+00,1.792812E-01,2.213396E-01,1.614591E-01,1.557975E-01,5.501481E+00,-1.731630E+01,3.996076E-01,7.725812E-01,9.000000E-02,4.658020E-02\n" +
                "7.581074E-01,1.813214E-01,2.259446E-01,1.518949E-01,1.621046E-01,5.050274E+00,-1.880004E+01,3.828620E-01,7.860402E-01,-3.384165E-02,8.784021E-02\n" +
                "1.028198E+00,1.725603E-01,2.203959E-01,1.535314E-01,1.307823E-01,5.588197E+00,-1.736174E+01,3.633570E-01,8.295501E-01,-3.958718E-02,8.734256E-02\n" +
                "7.935709E-01,1.776398E-01,2.188068E-01,1.545659E-01,1.417708E-01,5.359961E+00,-1.727100E+01,4.226644E-01,7.667441E-01,-7.351900E-02,8.940526E-02\n" +
                "8.315382E-01,1.679400E-01,2.180286E-01,1.581873E-01,1.749268E-01,5.024155E+00,-1.886653E+01,3.781711E-01,8.204817E-01,8.763726E-02,8.537221E-02\n" +
                "1.113175E+00,1.827694E-01,2.328039E-01,1.593656E-01,1.487162E-01,5.470326E+00,-1.662301E+01,3.632308E-01,7.527802E-01,-9.000000E-02,6.290817E-02\n" +
                "1.116543E+00,1.806778E-01,2.407149E-01,1.413378E-01,1.675983E-01,5.017481E+00,-1.885009E+01,3.570332E-01,7.622513E-01,-9.000000E-02,8.970972E-02\n" +
                "5.000000E-01,1.817479E-01,2.249141E-01,1.506236E-01,1.777292E-01,5.660567E+00,-2.001417E+01,3.823444E-01,7.622692E-01,-7.340292E-02,8.786459E-02\n" +
                "1.172276E+00,1.768457E-01,2.396689E-01,1.292451E-01,1.560358E-01,5.474570E+00,-1.648286E+01,4.113837E-01,7.402595E-01,-8.496185E-02,5.834225E-02\n" +
                "1.043727E+00,1.905242E-01,2.290284E-01,1.519441E-01,1.489080E-01,4.847973E+00,-1.877441E+01,4.109503E-01,7.453361E-01,-9.000000E-02,8.059445E-02\n" +
                "1.225823E+00,1.829276E-01,2.300708E-01,1.340227E-01,1.566470E-01,4.815114E+00,-1.833286E+01,3.644099E-01,7.764691E-01,-8.396047E-02,8.811787E-02\n" +
                "5.093354E-01,1.659648E-01,2.222952E-01,1.508336E-01,1.738312E-01,4.924106E+00,-1.770202E+01,4.080011E-01,7.766551E-01,2.806284E-02,5.403754E-02\n" +
                "9.649771E-01,1.653517E-01,2.196416E-01,1.613669E-01,1.749826E-01,5.010134E+00,-1.772541E+01,4.019894E-01,7.755535E-01,9.000000E-02,5.476814E-02\n" +
                "1.281716E+00,1.803238E-01,2.288165E-01,1.564714E-01,1.511961E-01,5.011589E+00,-1.877246E+01,3.625855E-01,7.601449E-01,-8.999889E-02,8.523585E-02\n" +
                "5.257890E-01,1.815600E-01,2.230004E-01,1.507957E-01,1.484499E-01,5.372355E+00,-1.953716E+01,4.113653E-01,7.930612E-01,9.000000E-02,5.562385E-02\n" +
                "1.300000E+00,1.816910E-01,2.244514E-01,1.405373E-01,1.530926E-01,4.856667E+00,-1.845968E+01,4.121145E-01,7.800139E-01,9.000000E-02,8.407565E-02\n" +
                "1.150258E+00,1.778687E-01,2.269737E-01,1.660190E-01,1.688476E-01,5.474732E+00,-2.026453E+01,3.846288E-01,7.367059E-01,-7.641895E-02,8.785778E-02\n" +
                "1.110379E+00,1.777938E-01,2.228800E-01,1.623287E-01,1.482567E-01,5.027545E+00,-1.894135E+01,3.826248E-01,7.622158E-01,-8.862836E-02,9.000000E-02\n" +
                "1.225823E+00,1.828600E-01,2.286680E-01,1.502116E-01,1.567788E-01,4.815737E+00,-1.843642E+01,3.648413E-01,7.932090E-01,-8.765516E-02,8.790580E-02\n" +
                "1.073157E+00,1.811970E-01,2.289936E-01,1.596216E-01,1.521677E-01,5.354137E+00,-1.566797E+01,3.708630E-01,7.332182E-01,9.000000E-02,8.924084E-02\n" +
                "8.939937E-01,1.806460E-01,2.230637E-01,1.509792E-01,1.768493E-01,5.026844E+00,-1.884960E+01,3.827967E-01,7.629496E-01,2.522267E-02,9.000000E-02\n" +
                "6.792420E-01,1.803285E-01,2.190694E-01,1.311707E-01,1.567000E-01,5.475815E+00,-1.885495E+01,4.616100E-01,7.930418E-01,-8.389718E-02,5.548304E-02\n" +
                "5.001325E-01,1.817479E-01,2.249275E-01,1.507958E-01,1.777292E-01,5.660567E+00,-1.740335E+01,3.823444E-01,7.622692E-01,-7.340292E-02,8.786459E-02\n" +
                "1.139452E+00,1.778687E-01,2.274565E-01,1.660190E-01,1.684718E-01,5.473889E+00,-2.028263E+01,3.846288E-01,7.364320E-01,8.019524E-02,8.981465E-02\n" +
                "7.565703E-01,1.818151E-01,2.230038E-01,1.507957E-01,1.740739E-01,5.358081E+00,-1.953716E+01,3.666328E-01,7.755552E-01,-3.208088E-02,5.562385E-02\n" +
                "6.853063E-01,1.807215E-01,2.271184E-01,1.516545E-01,1.567000E-01,5.471857E+00,-1.885495E+01,3.841058E-01,7.929423E-01,-8.389718E-02,8.144060E-02\n" +
                "8.900617E-01,1.778407E-01,2.342669E-01,1.509792E-01,1.669769E-01,5.473749E+00,-1.624407E+01,3.826248E-01,7.334061E-01,-9.000000E-02,8.942527E-02\n" +
                "8.986161E-01,1.799764E-01,2.259446E-01,1.518949E-01,1.645995E-01,5.012230E+00,-1.879470E+01,3.692734E-01,7.754936E-01,-8.197623E-02,8.784021E-02\n" +
                "7.685295E-01,1.719401E-01,2.197128E-01,1.518941E-01,1.692011E-01,5.477771E+00,-1.955032E+01,3.773707E-01,8.204817E-01,-8.513983E-02,8.848411E-02\n" +
                "9.826262E-01,1.752674E-01,2.355516E-01,1.340229E-01,1.538492E-01,5.010866E+00,-1.937482E+01,4.327696E-01,7.766508E-01,-8.538053E-02,5.457597E-02\n" +
                "1.300000E+00,1.807291E-01,2.287821E-01,1.518970E-01,1.480141E-01,5.012981E+00,-1.843285E+01,4.127774E-01,7.722982E-01,9.000000E-02,8.347514E-02\n" +
                "8.907708E-01,1.753992E-01,2.407831E-01,1.340229E-01,1.574393E-01,5.025750E+00,-1.936189E+01,4.277520E-01,7.355727E-01,-8.434725E-02,8.912139E-02\n" +
                "1.264972E+00,1.792812E-01,2.213396E-01,1.614591E-01,1.568926E-01,5.587375E+00,-1.731630E+01,3.996076E-01,7.725812E-01,9.000000E-02,4.543877E-02\n" +
                "1.300000E+00,1.816763E-01,2.197700E-01,1.519010E-01,1.486362E-01,4.841021E+00,-1.843286E+01,4.121319E-01,7.724902E-01,-7.678244E-02,8.353926E-02\n" +
                "6.260512E-01,1.729465E-01,2.275494E-01,1.563452E-01,1.669880E-01,5.464738E+00,-2.071641E+01,3.840916E-01,7.906423E-01,-8.929056E-02,7.668722E-02\n" +
                "1.235123E+00,1.818055E-01,2.269567E-01,1.508277E-01,1.606212E-01,5.110226E+00,-1.873537E+01,3.630000E-01,7.759301E-01,-3.883915E-02,8.782438E-02";
    }

    public static void main(String[] args) throws Exception {
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor();
        Object newInstance = constructor.newInstance();

        Class<run> runAnnotation = run.class;

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(runAnnotation))
                .sorted((m1, m2) -> Integer.compare(
                        m2.getAnnotation(runAnnotation).value(),
                        m1.getAnnotation(runAnnotation).value()
                ))
                .limit(1)
                .forEach(method -> {
                    Logger.getLogger().info("运行{}", method.toString());
                    try {
                        method.invoke(newInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface run {
        int value() default 0;

        String code() default "";
    }
}
