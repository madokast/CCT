package cn.edu.hust.zrx.cct.advanced.comsol;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point3;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description
 * HexahedronModel
 * 六面体模型
 * <p>
 * Data
 * 16:10
 *
 * @author zrx
 * @version 1.0
 */

public class HexahedronModel implements Serializable {

    private String modelName;

    private int hexStartIndex; // 从1开始
    private int modelIndex; // 从1开始

    private List<Point3> point3List0;
    private List<Point3> point3List1;
    private List<Point3> point3List2;
    private List<Point3> point3List3;

    private int size;

    public HexahedronModel(String modelName, int hexStartIndex, int modelIndex, List<Point3> point3List0, List<Point3> point3List1, List<Point3> point3List2, List<Point3> point3List3) {
        Objects.requireNonNull(modelName);
        Objects.requireNonNull(point3List0);
        Objects.requireNonNull(point3List1);
        Objects.requireNonNull(point3List2);
        Objects.requireNonNull(point3List3);

        size = point3List0.size();

        BaseUtils.Equal.requireEqual(size, point3List1.size());
        BaseUtils.Equal.requireEqual(size, point3List2.size());
        BaseUtils.Equal.requireEqual(size, point3List3.size());

        size--; // 立方体数目
        BaseUtils.Equal.requireTrue(size > 0, "size[" + size + "]<=0");

        this.hexStartIndex = hexStartIndex;
        this.modelIndex = modelIndex;
        this.modelName = modelName;
        this.point3List0 = point3List0;
        this.point3List1 = point3List1;
        this.point3List2 = point3List2;
        this.point3List3 = point3List3;

        Logger.getLogger().info("HexahedronModel:size = " + size);


    }

    public static HexahedronModel create(String modelName, int hexStartIndex, int modelIndex,
                                         List<Point3> point3List0, List<Point3> point3List1,
                                         List<Point3> point3List2, List<Point3> point3List3) {
        return new HexahedronModel(modelName, hexStartIndex, modelIndex, point3List0, point3List1, point3List2, point3List3);
    }

    public static HexahedronModel createFirst(String modelName, List<Point3> point3List0, List<Point3> point3List1,
                                              List<Point3> point3List2, List<Point3> point3List3) {
        return create(modelName, 1, 1, point3List0, point3List1, point3List2, point3List3);
    }

    public static HexahedronModel createNext(String currentModelName, HexahedronModel previousHexahedronModel,
                                             List<Point3> point3List0, List<Point3> point3List1,
                                             List<Point3> point3List2, List<Point3> point3List3) {
        return create(currentModelName, previousHexahedronModel.nextHexStartIndex(),
                previousHexahedronModel.nextModelIndex(), point3List0, point3List1, point3List2, point3List3);
    }

    public void serialize(String fileName) throws IOException {
        //Object string double[3][8]
        List<Object[]> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String name = "hex" + (i + hexStartIndex);

            Point3 point0 = point3List0.get(i);
            Point3 point1 = point3List1.get(i);
            Point3 point2 = point3List2.get(i);
            Point3 point3 = point3List3.get(i);
            Point3 point4 = point3List0.get(i + 1);
            Point3 point5 = point3List1.get(i + 1);
            Point3 point6 = point3List2.get(i + 1);
            Point3 point7 = point3List3.get(i + 1);

            Point3[] allPoint = {
                    point0,
                    point1,
                    point2,
                    point3,
                    point4,
                    point5,
                    point6,
                    point7
            };

            data.add(new Object[]{name, hexAllPoint(allPoint)});
        }

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName + ".cct"));

        objectOutputStream.writeObject(data);

        objectOutputStream.flush();

        objectOutputStream.close();
    }

    public List<Object[]> serialize() {
        //Object string double[3][8]
        List<Object[]> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String name = "hex" + (i + hexStartIndex);

            Point3 point0 = point3List0.get(i);
            Point3 point1 = point3List1.get(i);
            Point3 point2 = point3List2.get(i);
            Point3 point3 = point3List3.get(i);
            Point3 point4 = point3List0.get(i + 1);
            Point3 point5 = point3List1.get(i + 1);
            Point3 point6 = point3List2.get(i + 1);
            Point3 point7 = point3List3.get(i + 1);

            Point3[] allPoint = {
                    point0,
                    point1,
                    point2,
                    point3,
                    point4,
                    point5,
                    point6,
                    point7
            };

            data.add(new Object[]{name, hexAllPoint(allPoint)});
        }

        return data;
    }

    public String[] hexNames() {
        return serialize().stream().map(o -> (String) o[0]).collect(Collectors.toList()).toArray(String[]::new);
    }

    public double[][][] dataDoubleArrArrArr() {
        return serialize().stream().map(o -> (double[][]) o[1]).collect(Collectors.toList()).toArray(double[][][]::new);
    }

    @Override
    public String toString() {
        List<String> nameList = new ArrayList<>(size);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String name = "hex" + (i + hexStartIndex);
            nameList.add(name);

            Point3 point0 = point3List0.get(i);
            Point3 point1 = point3List1.get(i);
            Point3 point2 = point3List2.get(i);
            Point3 point3 = point3List3.get(i);
            Point3 point4 = point3List0.get(i + 1);
            Point3 point5 = point3List1.get(i + 1);
            Point3 point6 = point3List2.get(i + 1);
            Point3 point7 = point3List3.get(i + 1);

            sb.append(String.format("model.component(\"comp1\").geom(\"geom1\").create(\"%s\", \"Hexahedron\");\n", name));
            hexAllPoint(sb, name, new Point3[]{
                    point0,
                    point1,
                    point2,
                    point3,
                    point4,
                    point5,
                    point6,
                    point7
            });
        }
        sb.append("\n\n");
        sb.append("model.component(\"comp1\").geom(\"geom1\").create(\"sel" + modelIndex + "\", \"ExplicitSelection\");\n");
        sb.append("model.component(\"comp1\").geom(\"geom1\").feature(\"sel" + modelIndex + "\").label(\"" + modelName + "\");\n");

        for (int i = 0; i < size; i++) {
            sb.append("model.component(\"comp1\").geom(\"geom1\").feature(\"sel" + modelIndex + "\").selection(\"selection\").set(\"" + nameList.get(i) + "\", 1);\n");
        }


        return sb.toString();
    }

    //model.component("comp1").geom("geom1").feature("hex1").setIndex("p", 1.1, 2, 4);
    private String hexPoint(String name, double val, int i0, int i1) {
        return String.format(
                "model.component(\"comp1\").geom(\"geom1\").feature(\"%s\").setIndex(\"p\", %f, %d, %d);",
                name, val, i0, i1
        );
    }

    private void hexAllPoint(
            StringBuilder sb,
            String idName,
            Point3[] ps
    ) {
        for (int i = 0; i < 8; i++) {
            sb.append(hexPoint(idName, ps[i].x, 0, i)).append('\n');
            sb.append(hexPoint(idName, ps[i].y, 1, i)).append('\n');
            sb.append(hexPoint(idName, ps[i].z, 2, i)).append('\n');
        }
    }

    private double[][] hexAllPoint(
            Point3[] ps
    ) {
        double[][] ret = new double[3][8];

        for (int i = 0; i < 8; i++) {
            ret[0][i] = ps[i].x;
            ret[1][i] = ps[i].y;
            ret[2][i] = ps[i].z;
        }

        return ret;
    }

    public int nextHexStartIndex() {
        return hexStartIndex + size;
    }

    public int nextModelIndex() {
        return modelIndex + 1;
    }

    public void writeFile(String straightCct) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(straightCct + ".java");

        fileOutputStream.write(toString().getBytes());

        fileOutputStream.flush();

        fileOutputStream.close();
    }
}
