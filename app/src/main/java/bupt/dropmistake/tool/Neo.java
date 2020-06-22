package bupt.dropmistake.tool;


import android.util.Log;

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

    public ArrayList<Problem> userQusts;

    public Neo() {
        try {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
            session = driver.session();
            Log.i("DropLog", "1.0)连接数据库成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() throws Exception {
        session.closeAsync();
        driver.close();
    }

    // 返回用户错题本所有错题接口
    public ArrayList<Problem> getUserQusts() {
        StatementResult proResult = session
                .run("match(n:User)-[r]->(m:Qust) return m.png,m.klg3,r.date,m.mode,m.hard,id(m)");

        userQusts = new ArrayList<Problem>();

        while (proResult.hasNext()) {
            Record temp = proResult.next();

            String[] pngUrl = temp.get(0).toString().split("null");
            pngUrl[0] = "https://image.fclassroom.com" + pngUrl[0].substring(1, pngUrl[0].length() - 2);
            pngUrl[1] = "https://image.fclassroom.com" + pngUrl[1].substring(2, pngUrl[1].length() - 1);

            String klgs = temp.get(1).toString();
            String date = temp.get(2).toString();
            int mode = temp.get(3).asInt();
            int hard = temp.get(4).asInt();
            int id = temp.get(5).asInt();

            Problem problem = new Problem(getKlgs(klgs), pngUrl[0], pngUrl[1], date, mode, hard, id);

            userQusts.add(problem);
        }
        return userQusts;
    }

    // 搜题并返回结果接口
    public ArrayList<Problem> searchQusts (String toSearch) {
        String[] key = Split.striped(toSearch);

        String run = "match(m:Word)-[r]->(n),(m:Word)<-[l]-(n) where ";
        for (int i = 0; i < key.length; i++) {
            if (i == key.length-1) {
                run += "m.word=\"" + key[i] + "\"";
            } else {
                run += "m.word=\"" + key[i] + "\" or ";
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

    public String addToBook(String id, String date){
        StatementResult already = session.run("match(n:User)-[r]->(m:Qust) where id(m)="+id+" return r");
        if(already.hasNext())
            return "该题已存在";
        else {
            session.run("match(n:User),(m:Qust) where id(m)=" + id + " create (n)-[r:mistake{date:\""+date+"\"}]->(m)");
            return "加入成功";
        }
    }

    public String removeFromBook(String id){
        try {
            session.run("match(n:User)-[r]->(m:Qust) where id(m)="+id+" delete r");
        }
        catch (Exception e){
            e.printStackTrace();
            return "删除失败";
        }
        return "删除成功";
    }

    private Double[][] iteration(double[][] matrix1) {
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


    private String formatlabel(String prime) {
        int flag = prime.indexOf("a");
        if (flag == -1) {
            prime = prime.replace("\"", "");// 将第一列取出,先把引号去掉
            prime = prime.replace('|', '_'); // 最后把分级符'|'变成'_'
            return "a" + prime;
        } else {
            return prime.substring(flag, flag + 16);
        }
    }

    private ArrayList<Problem> mPagerank() {
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
                        index.get(output[2][1].intValue()) + " return n.png,n.hard,n.klg3,n.mode,id(n)");

        ArrayList<Problem> problems = new ArrayList<>();
        for (int i = 0; i < 6; i += 2) {
            Record result = uResult.next();
            String[] url = result.get(0).toString().split("null");
            String problemURL = "https://image.fclassroom.com" + url[0].substring(1, url[0].length() - 2);
            String answerURL = "https://image.fclassroom.com" + url[1].substring(2, url[1].length() - 1);

            int hard = Integer.parseInt(result.get(1).toString());
            String klg = result.get(2).toString();
            int mode = Integer.parseInt(result.get(3).toString());
            int id = result.get(4).asInt();
            problems.add(new Problem(problemURL,answerURL,hard,getKlgs(klg),mode,id));

        }
        return problems;
    }



    private ArrayList<String> getKlgs(String str) {
        ArrayList<String> result = new ArrayList<String>();
        String[] klgs = str.replace("\"","").split("\\^\\.\\^");
        for (String klg : klgs) {
            result.add(klg);
        }
        return result;
    }


//    public static void main(String[] args) throws Exception {
//        Neo test = new Neo();
//        ArrayList<Problem> result = test.searchQusts("如图，在三棱锥S-ABC中，SA=SB=AC=BC=2，AB=2√3，SC=1\n" +
//                "(1)画出二面角S-AB-C的平面角，并求它的度数；" +
//                "(2)求三棱锥S-ABC的体积.");
//        for(Problem problem : result){
//            System.out.println(problem);
//        }
//    }

}
