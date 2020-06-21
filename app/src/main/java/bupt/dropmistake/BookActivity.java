package bupt.dropmistake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bupt.dropmistake.tool.Problem;
import bupt.dropmistake.tool.ProblemAdapter;
import bupt.dropmistake.tool.ProblemData;

public class BookActivity extends AppCompatActivity {

    //列表
    private ListView _listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        this._listView = (ListView) this.findViewById(R.id.problem_list);
        /*
        ProblemAdapter data = new ProblemAdapter(getApplicationContext());
        ArrayList<String> as = new ArrayList<String>();
        as.add("几何");
        as.add(" 概率");
        String s = new String("http://image.fclassroom.com/1668/2018/1/272609/title_22_3298x2071_20180130133415967.png");
        Problem p = new Problem(2,as,s,s,"2020-06-18");
        //data.add(new ProblemData(p.getDate(),p.getDifficulty(),p.getKnowledgePoint(),p.getClass(),p.getAnswerURL()));

 this._listView.setAdapter(data);
         */

        ProblemAdapter data = new ProblemAdapter(getApplicationContext());
        String s = new String("http://image.fclassroom.com/1668/2018/1/272609/title_22_3298x2071_20180130133415967.png");
        ProblemData p = new ProblemData("2020-06-18", 2, "几何 概率 ", s, s);
        ArrayList<ProblemData> problemDataArrayList = new ArrayList<ProblemData>();
        /*
        for(){
            ProblemData p = new ProblemData();
            p.set();
            problemDataArrayList.add(p);
        }
         */
        problemDataArrayList.add(p);


        //adapter添加item 和listView绑定
        data.add(p);
        data.add(p);
        data.add(p);
        this._listView.setAdapter(data);
        this._listView.setDivider(null);

    }
}
