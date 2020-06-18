package bupt.dropmistake.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import bupt.dropmistake.R;

public class ProblemAdapter extends ArrayAdapter<ProblemData> {

    private static final int IO_BUFFER_SIZE = 4000000;

    public ProblemAdapter(Context context) {
        super(context, -1);
    }

    @NonNull
    @Override
    public View getView(int position, View container, ViewGroup parent) {
        if (container == null) {
            Context c = getContext();
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            container = inflater.inflate(R.layout.list_problem, null);
        }
        final ProblemData entry = getItem(position);

        TextView v = null;
        v = (TextView) container.findViewById(R.id.list_title);
        v.setText("错题" + String.valueOf(position + 1));
        v = (TextView) container.findViewById(R.id.list_date);
        v.setText(entry.get_dateId());
        v = (TextView) container.findViewById(R.id.list_diff);
        v.setText("  " + String.valueOf(entry.get_diffId()));
        v = (TextView) container.findViewById(R.id.list_kp);
        v.setText(entry.get_kpId());
        ImageView img = (ImageView) container.findViewById(R.id.list_proimg);
        //img.setImageResource(entry.get_imgId());
        Picasso.with(getContext()).load(entry.get_imgId()).into(img);
        return container;
    }


}
