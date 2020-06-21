package bupt.dropmistake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import bupt.dropmistake.tool.Neo;
import bupt.dropmistake.tool.ProblemAdapter;
import bupt.dropmistake.tool.Problem;

public class BookActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //列表
    private ListView _listView;
    //错题本中的错题集合
    private ArrayList<Problem> arrayList = new ArrayList<Problem>();



    //获取通信--输入
    public void getValue(String value) {
        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
<<<<<<< HEAD

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
=======
        //数据库操作
        Neo neo = new Neo();
        arrayList = neo.getUserQusts();
        try {
            neo.close();
            neo = null;
        } catch (Exception e) {
            e.printStackTrace();
>>>>>>> 214e3a41b07e833da3582c0fc6db5cc21c624b42
        }


        this._listView = (ListView) this.findViewById(R.id.problem_list);
        //adapter添加item 和listView绑定
        ProblemAdapter data = new ProblemAdapter(getApplicationContext());
        for (Problem x : arrayList) {
            data.add(x);
        }
        this._listView.setAdapter(data);
        this._listView.setDivider(null);
        this._listView.setOnItemClickListener(this::onItemClick);

<<<<<<< HEAD
=======
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("DMINFO", "listView-item被点击，位置：" + position);
        //getValue(position+id+"被点击");
>>>>>>> 214e3a41b07e833da3582c0fc6db5cc21c624b42
    }

}
