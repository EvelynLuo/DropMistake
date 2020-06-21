package bupt.dropmistake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import bupt.dropmistake.tool.Neo;
import bupt.dropmistake.tool.PaperAdapter;
import bupt.dropmistake.tool.Problem;
import bupt.dropmistake.tool.ProblemAdapter;
import bupt.dropmistake.tool.ProblemData;

public class PaperActivity extends AppCompatActivity {

    //列表
    private ListView _listView;
    private int diff;
    private int fresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

        Intent intent = getIntent();
        diff = intent.getIntExtra("diff",0);
        fresh=intent.getIntExtra("fresh",0);
        Log.i("paper","diff:"+diff+" fresh:"+fresh);

        this._listView = (ListView) this.findViewById(R.id.paper_list);
        Neo test=new Neo();
        PaperAdapter data = new PaperAdapter(getApplicationContext());
        ArrayList<ProblemData> problemDataArrayList = new ArrayList<ProblemData>();
        ArrayList<Problem> list=test.getUserQusts();
        Log.i("url","size of list:"+list.size());
        for (int i=0;i<list.size();i++){
            Problem problem=list.get(i);
            //String s = new String("http://image.fclassroom.com/1668/2018/1/272609/title_22_3298x2071_20180130133415967.png");
            ProblemData p = new ProblemData("", problem.difficulty,"prob"+i, problem.problemURL, "");
            Log.i("url",problem.problemURL);
            problemDataArrayList.add(p);
            //adapter添加item 和listView绑定
            data.add(p);
        }
        this._listView.setAdapter(data);
        this._listView.setDivider(null);
    }
}
