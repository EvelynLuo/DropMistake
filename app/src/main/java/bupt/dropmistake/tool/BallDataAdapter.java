package bupt.dropmistake.tool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import bupt.dropmistake.R;


public class BallDataAdapter extends ArrayAdapter<BallData> {

    public BallDataAdapter(Context context) {
        super(context, -1);
    }

    @NonNull
    @Override
    public View getView(int position, View container, ViewGroup parent) {
        if (container == null) {
            Context c = getContext();
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            container = inflater.inflate(R.layout.list_ball, null);
        }
        final BallData entry = getItem(position);

        TextView v = null;
        v = (TextView) container.findViewById(R.id.list_ball_title);
        v.setText(entry.getTitleId());
        v = (TextView) container.findViewById(R.id.list_ball_desc);
        v.setText(entry.getDescId());
        ImageView img = (ImageView) container.findViewById(R.id.image);
        img.setImageResource(entry.getImgId());

        return container;
    }
}