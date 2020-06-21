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
        //数据库操作
        Neo neo = new Neo();
        arrayList = neo.getUserQusts();
        try {
            neo.close();
            neo = null;
        } catch (Exception e) {
            e.printStackTrace();
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

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("DMINFO", "listView-item被点击，位置：" + position);
        //getValue(position+id+"被点击");
    }

}
