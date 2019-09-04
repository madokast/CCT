package zrx.Tools;

public class TableHtml {
    public static void toHtml(int row,int col,double[][] table){
        System.out.println("------------toHtml----------------");
        System.out.println("<table border=\"1px\">");

        for (int i = 0; i < row; i++) {
            System.out.print("<tr>");

            for (int j = 0; j < col; j++) {
                System.out.print("<td>" + table[i][j] + "</td>");
            }

            System.out.print("\r\n<tr>");
        }

        System.out.println("\r\n</table>");


        //System.out.println("<table border=\"1px\">");
        //
        //        for (Vector2d v : vs) {
        //            System.out.print("<tr>");
        //            System.out.print("<td>" + v.x + "</td>");
        //            System.out.print("<td>" + v.y + "</td>");
        //            System.out.print("<tr>");
        //        }
        //
        //        System.out.println("</table>");
    }

    public static void main(String[] args) {
        double[][] table = new double[][]{
                {1,2,3},
                {3,2,1}
        };

        toHtml(2,3,table);
    }
}
