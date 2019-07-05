package zrx.base;

public class Vector3d {
    public double x;
    public double y;
    public double z;

    public final void selfMatmul(double[][] r) {
        double xx = r[0][0] * x + r[0][1] * y + r[0][2] * z;
        double yy = r[1][0] * x + r[1][1] * y + r[1][2] * z;
        double zz = r[2][0] * x + r[2][1] * y + r[2][2] * z;

        this.x = xx;
        this.y = yy;
        this.z = zz;
    }

    public final static Vector3d corss(Vector3d a, Vector3d b) {
        return new Vector3d(a.y * b.z - a.z * b.y,
                -a.x * b.z + a.z * b.x,
                a.x * b.y - a.y * b.x);
    }

    public final static Vector3d add(Vector3d p0, Vector3d p1) {
        return new Vector3d(p0.x + p1.x, p0.y + p1.y, p0.z + p1.z);
    }

    public final static Vector3d subtract(Vector3d p0, Vector3d p1) {
        return new Vector3d(p0.x - p1.x, p0.y - p1.y, p0.z - p1.z);
    }

    public final static Vector3d dot(Vector3d p, double a) {
        return new Vector3d(p.x * a, p.y * a, p.z * a);
    }

    public final static Vector3d dot(double a, Vector3d p) {
        return new Vector3d(p.x * a, p.y * a, p.z * a);
    }

    public final static double dot(Vector3d a, Vector3d b) {
        return a.x * b.x +
                a.y * b.y +
                a.z * b.z;
    }

    public final void addSelf(Vector3d p) {
        x += p.x;
        y += p.y;
        z += p.z;
    }

    public final void subtractSelf(Vector3d p) {
        x -= p.x;
        y -= p.y;
        z -= p.z;
    }

    public final double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Deprecated
    public Vector3d() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Deprecated
    public Vector3d(Vector3d p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    @Override
    public String toString() {
        return "["+x+' '+y+' '+z+']';
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        Vector3d p1 = new Vector3d(1, 1, 1);
        Vector3d p2 = new Vector3d(1, 2, 3);
        System.out.println("p2 = " + p2);
        System.out.println(p1.length());


        System.out.println(add(p1, p2));
        System.out.println(subtract(p1, p2));

        p2.subtractSelf(p1);
        System.out.println(p2);

        System.out.println("-----------------");
        System.out.println("p2 = " + p2);
        double[][] doubles = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        p2.selfMatmul(doubles);
        System.out.println("p2 = " + p2);
    }
}
