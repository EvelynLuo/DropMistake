package bupt.dropmistake.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import bupt.dropmistake.BookActivity;
import bupt.dropmistake.R;
import bupt.dropmistake.tool.BallData;
import bupt.dropmistake.tool.BallDataAdapter;

public class NotificationsFragment extends Fragment {
    //用户中心
    private NotificationsViewModel notificationsViewModel;
    //列表
    private ListView _listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);

        this._listView = (ListView) root.findViewById(R.id.about_list);
        BallDataAdapter data = new BallDataAdapter(getContext());
        //获得用户错题信息
        /*
         data.add(new BallData(R.string.about_ball_normal_title,
                R.string.about_ball_normal_content,
                R.drawable.ball_normal));
         */
        data.add(new BallData(R.string.about_ball_normal_title,
                R.string.about_ball_normal_content,
                R.mipmap.team));
        data.add(new BallData(R.string.myBook,
                R.string.collection,
                R.mipmap.bicon));

        this._listView.setAdapter(data);
        this._listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Item item = ItemList.get(position)
                //intent.putExtra("url", Item.getXXX());--当不止有收藏夹为tag的错题本时。传递键值对生成不同的错题本页面
                try {
                    Toast.makeText(getContext(), "正在跳转", Toast.LENGTH_LONG).show();
                    System.out.println("跳转我的错题本页面");
                    Log.i("DMINFO", "跳转我的错题本页面");
                    Intent intent = new Intent(getActivity(), BookActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }


}
/*
notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
 */