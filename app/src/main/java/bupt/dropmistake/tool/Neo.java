package bupt.dropmistake.tool;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Neo implements AutoCloseable {
    //Neo Server = new Neo(url, user, pwd);
    private static final String uri = "bolt://39.97.253.73:7687";
    private static final String password = "20200702";
    private static final String user = "neo4j";

    public String[] klgs = new String[10];
    public String[] klgcs = new String[3];
    public double[] frc = new double[3];
    Matrix matrix;
    private Driver driver;
    private Session session;
    private String userId = null;


    public Neo() {
        try {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
            session = driver.session();
            Log.i("DropLog", "1.0)连接数据库成功");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void close() throws Exception {
        driver.close();
    }

    public Double[][] iteration(double[][] matrix1) {
        double[] standard = new double[matrix.getRow()];
        double[] e = new double[matrix.getRow()];
        Matrix nowMatrix = new Matrix(matrix.getRow());
        for (int i = 0; i < standard.length; i++) {
            standard[i] = 0.00000001;
            e[i] = (1.0 / matrix.getRow()) * 0.15;
        }

        for (int i = 0; i < matrix1.length; i++) {
            int count = 0;
            for (int j = 0; j < matrix1[i].length; j++) {
                if (matrix1[j][i] != 0)
                    count += matrix1[j][i];
            }
            for (int j = 0; j < matrix1[i].length; j++) {
                if (matrix1[j][i] != 0)
                    matrix1[j][i] = matrix1[j][i] / count;
            }
        }

        do {
            matrix = nowMatrix;
            nowMatrix = matrix.mutipleMatrix(matrix1).mutipleNumber(0.85).add(e);
        } while (!(nowMatrix.subtraction(matrix).less(standard)));

        Double[][] output = new Double[matrix.getRow()][2];
        for (int i = 0; i < matrix.getRow(); i++) {
            output[i][0] = nowMatrix.getMatrix()[i];//
            output[i][1] = (double) i;//
        }
        Arrays.sort(output, new Comparator<Double[]>() {
            @Override
            public int compare(Double[] x, Double[] y) {
                if (x[0] < y[0]) {
                    return 1;
                } else if (x[0] > y[0]) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return output;
    }


    public String formatlabel(String prime) {
        int flag = prime.indexOf("a");
        if (flag == -1) {
            prime = prime.replace("\"", "");// 将第一列取出,先把引号去掉
            prime = prime.replace('|', '_'); // 最后把分级符'|'变成'_'
            return "a" + prime;
        } else {
            return prime.substring(flag, flag + 16);
        }
    }

    public String[] mPagerank() {
        Session session = driver.session();

        StatementResult idResult = session
                .run("match(n:Qust) unwind labels(n) as l with l,id(n) as idn where l=~'a.*' and( l='" + klgs[0]
                        + "' or l='" + klgs[1] + "' or l='" + klgs[2] + "') return distinct idn");
        BidiMap<Integer, String> index = new DualHashBidiMap<>();
        int count;
        for (count = 0; idResult.hasNext(); count++) {
            Record temp = idResult.next();
            index.put(count, temp.get(0).toString());
        }

        StatementResult array = session
                .run("match(n:Qust) unwind labels(n) as l with l,id(n) as idn where l=~'a.*' and (l='" + klgs[0]
                        + "' or l='" + klgs[1] + "' or l='" + klgs[2]
                        + "')  with collect(distinct idn) as id  match (n:Qust)-[r]->(q)<-[l]-(m:Qust) where id(m) in id  and id(n) in id and id(n)<id(m) return id(n),id(m),count(r) order by count(r) desc limit 500");

        //System.out.println("query OK");// debug

        double[][] matrix = new double[count][count];
        this.matrix = new Matrix(count);

        while (array.hasNext()) {
            Record temp = array.next();
            int i = index.getKey(temp.get(0).toString());
            int j = index.getKey(temp.get(1).toString());
            ;
            int rcount = Integer.parseInt(temp.get(2).toString());
            matrix[i][j] = rcount;
        }
        //System.out.println("fuzhi OK");// debug

        for (int row = 0; row < count; row++)
            for (int line = 0; line < row; line++)
                matrix[row][line] = matrix[line][row];

        Double[][] output = iteration(matrix);// 存储了已排序的结果

//            for (int i = 0; i < count; i++) {
//                System.out.println(output[i][0] + " " + index.get(output[i][1].intValue()));
//            }

        StatementResult uResult = session
                .run("match(n) where id(n)=" + index.get(output[0][1].intValue()) +
                        " or id(n)=" + index.get(output[1][1].intValue()) + " or id(n)=" +
                        index.get(output[2][1].intValue()) + " return n.png");

        String[] resultString = new String[6];
        for (int i = 0; i < 6; i += 2) {
            Record result = uResult.next();
            String[] url = result.get(0).toString().split("null");
            resultString[i] = "http://image.fclassroom.com" + url[0].substring(1, url[0].length() - 2);
            resultString[i + 1] = "http://image.fclassroom.com" + url[1].substring(2, url[1].length() - 1);
        }
        return resultString;
    }


    public String[] key2klg(String keys) {
        Session session = driver.session();

        int key_num = keys.length() - (keys.replaceAll(" ", "")).length() + 1;
        String[] key = new String[key_num];

        String run = "match(m:Word)-[r]->(n),(m:Word)<-[l]-(n) where ";
        for (int i = 0; i < key_num; i++) {

            int end = keys.indexOf(" ");
            if (end == -1) {
                key[i] = keys;
                run += "m.word=\"" + key[i] + "\"";
            } else {
                key[i] = keys.substring(0, end);
                run += "m.word=\"" + key[i] + "\" or ";
                keys = keys.substring(end + 1);
            }
        }
        //System.out.println(run);

        StatementResult klgResult = session
                .run(run + " return n.klg3,n.klg,sum(r.frc*l.frc)as sum order by sum desc limit 3");

        int i = 0;
        double sumfrc = 0;
        while (klgResult.hasNext()) {
            Record temp = klgResult.next();
            klgs[i] = formatlabel(temp.get(1).toString());
            klgcs[i] = temp.get(0).toString();
            frc[i] = Double.parseDouble(temp.get(2).toString());

            sumfrc += frc[i];
            ++i;
        }
//        for (int j = 0; j < 3; j++) {
//            System.out.println("知识点：" + klgs[j] + " " + klgcs[j] + "  可能性：" + frc[j] / sumfrc);
//        }
        if (klgs[0] != null) return mPagerank();
        else return null;
    }

    public void key2qust() {
        Session session = driver.session();
        Scanner in = new Scanner(System.in);
        String keys = in.nextLine();
        in.close();
        keys = keys.replace(" ", ":");

        StatementResult array = session.run("match(n:Qust:" + keys + ") return id(n)");
        int[] index = new int[100];
        int count;
        for (count = 0; array.hasNext(); count++) {
            Record id = array.next();
            index[count] = Integer.parseInt(id.get(0).toString());
        }

        double[][] matrix = new double[count][count];
        this.matrix = new Matrix(count);

        int row = 0;
        int line = 0;
        for (; row < count; row++) {
            for (line = row + 1; line < count; line++) {
                StatementResult temp = session.run("match(n)-[r*2..2]-(m) where id(n)=" + index[row] + " and id(m)="
                        + index[line] + " return count(r)");
                matrix[row][line] = Integer.parseInt((temp.next().get(0).toString()));
            }
        }

        for (row = 1; row < count; row++)
            for (line = 0; line < row; line++)
                matrix[row][line] = matrix[line][row];

//        for (int i = 0; i < count; i++) {
//            System.out.println(i + ":" + index[i]);
//        }

//        for (int i = 0; i < count; i++) {
//            for (int j = 0; j < count; j++)
//                System.out.print(matrix[i][j] + " ");
//            System.out.println();
//        }

        Double[][] output = iteration(matrix);// 存储了已排序的结果

//        for (int i = 0; i < count; i++) {
//            System.out.println(output[i][0] + " " + index[output[i][1].intValue()]);
//        }


        StatementResult uResult = session
                .run("match(n) where id(n)=" + index[output[0][1].intValue()] +
                        " or id(n)=" + index[output[1][1].intValue()] + " or id(n)=" +
                        index[output[2][1].intValue()] + " return n.png,id(n),n.klg3");

        Record result = uResult.next();
        String url = result.get(0).toString();
        int uindex = url.indexOf("||");
        System.out.println("http://image.fclassroom.com" + url.substring(1, uindex));
        System.out.println(result.get(1).toString() + "," + result.get(2).toString());
    }

    public ArrayList<String[]> getUserQusts() {
        if (userId == null) {
            System.out.println("Please Log in first!");
            return null;
        } else {
            StatementResult proResult = session
                    .run("match(n:User)-[r]->(m:Qust) where id(n)=" + userId + " return m.png");

            ArrayList<String[]> result = new ArrayList<>();

            while (proResult.hasNext()) {
                Record temp = proResult.next();
                String[] pngUrl = temp.get(0).toString().split("null");
                pngUrl[0] = "http://image.fclassroom.com" + pngUrl[0].substring(1, pngUrl[0].length() - 2);
                pngUrl[1] = "http://image.fclassroom.com" + pngUrl[1].substring(2, pngUrl[1].length() - 1);
                result.add(pngUrl);
            }
            return result;
        }

    }

    public String[] getKlgcs() {
        String[] result = new String[3];
        for (int i = 0; i < 3; i++) {
            result[i] = "知识点" + i + "-" + klgcs[i] + " " + frc[i];
        }
        return result;
    }

}
