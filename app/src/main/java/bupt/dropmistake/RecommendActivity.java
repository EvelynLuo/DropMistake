package bupt.dropmistake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import bupt.dropmistake.tool.Neo;
import bupt.dropmistake.tool.Problem;
import bupt.dropmistake.tool.ProblemData;
import bupt.dropmistake.tool.RecommendAdapter;

public class RecommendActivity extends AppCompatActivity {

    //列表
    private ListView _listView;

    //获取通信--输入
    public void getValue(String value) {
        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        this._listView = (ListView) this.findViewById(R.id.recommend_list);
        //获取跳转时携带数据
        Intent intent = getIntent();
        String value = intent.getStringExtra("value");
        Neo neo = new Neo();
        ArrayList<Problem> arrayList = new ArrayList<Problem>();
        System.out.println("分词，数据库查找");
        Log.i("DMINFO", "分词，数据库查找");
        arrayList = neo.searchQusts(value);
        getValue(arrayList.toString());

        getValue(value);
        //测试数据——静态
        /*
        String s = new String("http://image.fclassroom.com/1668/2018/1/272609/title_22_3298x2071_20180130133415967.png");
        Problem p = new Problem();
        p.setProblemURL(s);
        p.setDate("2020-06-21");
        p.setKlgStr("几何 概率");
        p.setDifficulty(2);
       // ProblemData p = new ProblemData("2020-06-18", 2, "几何 概率 ", s, s);
        for(int i = 0;i<3;i++){
            arrayList.add(p);
        }
         */
        RecommendAdapter data = new RecommendAdapter(getApplicationContext());
        for (Problem x : arrayList) {
            data.add(x);
        }
        this._listView.setAdapter(data);
        this._listView.setDivider(null);
    }
}
