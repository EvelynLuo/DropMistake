package bupt.dropmistake.ui.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import bupt.dropmistake.MainActivity;
import bupt.dropmistake.OcrActivity;
import bupt.dropmistake.PaperActivity;
import bupt.dropmistake.R;

import static bupt.dropmistake.R.*;

public class DashboardFragment extends Fragment {
    //组卷
    private DashboardViewModel dashboardViewModel;
    private int diff;
    private int fresh;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(layout.fragment_dashboard, container, false);

        Button button=root.findViewById(id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaperActivity.class);
                intent.putExtra("diff", diff);
                intent.putExtra("fresh", fresh);
                startActivity(intent);
            }
        });

        RadioGroup difficulity = (RadioGroup) root.findViewById(id.difficulity);
        // 方法一监听事件,通过获取点击的id来实例化并获取选中状态的RadioButton控件
        difficulity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 获取选中的RadioButton的id
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) root.findViewById(id);
                //choise.setTextColor(Color.WHITE);
                // 获取这个RadioButton的text内容
               // String output = choise.getText().toString();
                diff = Integer.parseInt(choise.getTag().toString());
            }
        });
        RadioGroup freshment=(RadioGroup)root.findViewById(id.freshment);
        freshment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 获取选中的RadioButton的id
                int id = group.getCheckedRadioButtonId();
                RadioButton choise = (RadioButton) root.findViewById(id);
                //fresh = (int) choise.getTag();
                fresh = Integer.parseInt(choise.getTag().toString());
            }
        });


        return root;
    }
}
