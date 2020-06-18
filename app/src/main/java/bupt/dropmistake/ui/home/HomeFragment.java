package bupt.dropmistake.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import bupt.dropmistake.BookActivity;
import bupt.dropmistake.OcrActivity;
import bupt.dropmistake.R;
import bupt.dropmistake.tool.BallData;
import bupt.dropmistake.tool.BallDataAdapter;

public class HomeFragment extends Fragment {
    //主页
    private HomeViewModel homeViewModel;
    //列表
    private ListView _listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //搜索框文字搜索
        final TextView textView = root.findViewById(R.id.editText);
        //相机icon底圆
        final ImageView circle = root.findViewById(R.id.circle);
        //拍照搜题btn
        final ImageButton ocr = root.findViewById(R.id.OCRbtn);
        //我的错题本
        this._listView = (ListView) root.findViewById(R.id.list);
        BallDataAdapter data = new BallDataAdapter(getContext());
        data.add(new BallData(R.string.myBook,
                R.string.collection,
                R.mipmap.bicon));
        this._listView.setAdapter(data);
        //点击跳转
        this._listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Item item = ItemList.get(position)
                //intent.putExtra("url", Item.getXXX());--当不止有收藏夹为tag的错题本时。传递键值对生成不同的错题本页面
                Intent intent = new Intent(getActivity(), BookActivity.class);
                startActivity(intent);
            }
        });
        //动态变化
        ocr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//点击按钮
                    //重新设置按下去时的按钮图片
                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.mipmap.c2));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {//松开按钮
                    //再修改为正常抬起时的图片
                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.mipmap.c1));
                }
                return false;
            }
        });
        //跳转到ORCActivity
        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OcrActivity.class);
                startActivity(intent);
            }
        });



        return root;
    }
}


/*
 homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
 */