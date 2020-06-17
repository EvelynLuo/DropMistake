package bupt.dropmistake.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import bupt.dropmistake.R;
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