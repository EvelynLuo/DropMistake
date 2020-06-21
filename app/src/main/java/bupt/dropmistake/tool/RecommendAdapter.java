package bupt.dropmistake.tool;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import bupt.dropmistake.R;

public class RecommendAdapter extends ArrayAdapter<Problem> {


    public RecommendAdapter(Context context) {
        super(context, -1);
    }

    @NonNull
    @Override
    public View getView(int position, View container, ViewGroup parent) {
        if (container == null) {
            Context c = getContext();
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            container = inflater.inflate(R.layout.list_recommend, null);
        }
        final Problem entry = getItem(position);

        TextView v = null;
        v = (TextView) container.findViewById(R.id.list_title_rec);
        v.setText("推荐" + String.valueOf(position + 1));
        v = (TextView) container.findViewById(R.id.list_mode_rec);
        v.setText(entry.getMode());
        v = (TextView) container.findViewById(R.id.list_diff_rec);
        v.setText("  " + String.valueOf(entry.getDifficulty()));
        v = (TextView) container.findViewById(R.id.list_kp_rec);
        v.setText(entry.getKlgStr());
        ImageView img = (ImageView) container.findViewById(R.id.list_proimg_rec);
        Picasso.with(getContext()).load(entry.getProblemURL()).into(img);
        ImageView ans = (ImageView) container.findViewById(R.id.ans);
        Picasso.with(getContext()).load(entry.getAnswerURL()).into(ans);
        Button seeAns = (Button) container.findViewById(R.id.rec_see_ans);
        Button add = (Button) container.findViewById(R.id.add_btn);
        seeAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DMINFO", "ProblemAdapter.seeANS设置监听函数");
                if (ans.getVisibility() == View.GONE) {
                    Toast.makeText(getContext(), "显示解析", Toast.LENGTH_LONG).show();
                    ans.setVisibility(View.VISIBLE);

                    seeAns.setText("隐藏解析");
                    return;
                } else {
                    Toast.makeText(getContext(), "收起解析", Toast.LENGTH_LONG).show();
                    ans.setVisibility(View.GONE);
                    seeAns.setText("查看解析");
                    return;
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DMINFO", "ProblemAdapter.add监听");
                //数据库操作
                Neo neo = new Neo();
                String result = neo.addToBook(String.valueOf(entry.getId()));
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                Log.i("DMINFO", result);
                add.setText("添加成功");
                try {
                    neo.close();
                    neo = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return container;
    }


}
