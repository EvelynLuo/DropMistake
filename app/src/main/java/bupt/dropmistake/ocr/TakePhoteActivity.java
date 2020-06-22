package bupt.dropmistake.ocr;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.edmodo.cropper.CropImageView;
import bupt.dropmistake.ocr.camear.CameraPreview;
import bupt.dropmistake.ocr.camear.FocusView;
import bupt.dropmistake.ocr.utils.Utils;
import bupt.dropmistake.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 拍照界面
 * Created by Administrator on 2016/12/8.
 */
public class TakePhoteActivity extends AppCompatActivity implements CameraPreview.OnCameraStatusListener, SensorEventListener {


    private             Context context;
    //true:横屏   false:竖屏
    public static final boolean isTransverse = true;

    private static final String TAG       = "TakePhoteActivity";
    public static final  Uri    IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    private String PATH;

    private CameraPreview  mCameraPreview;
    private CropImageView mCropImageView;
    private RelativeLayout mTakePhotoLayout;
    private LinearLayout   mCropperLayout;
    private ImageView      btnClose;
    private ImageView      btnShutter;
    private Button         btnAlbum;
    private ImageView      btnStartCropper;
    private ImageView      btnCloseCropper;


    /**
     * 旋转文字
     */
    private boolean isRotated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置横屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_take_phote);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;
        PATH = getExternalCacheDir() + "/AndroidMedia/";

        btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(onClickListener);
        btnShutter = findViewById(R.id.btn_shutter);
        btnShutter.setOnClickListener(onClickListener);
        btnAlbum = findViewById(R.id.btn_album);
        btnAlbum.setOnClickListener(onClickListener);

        btnStartCropper = findViewById(R.id.btn_startcropper);
        btnStartCropper.setOnClickListener(cropcper);
        btnCloseCropper = findViewById(R.id.btn_closecropper);
        btnCloseCropper.setOnClickListener(cropcper);

        mTakePhotoLayout = findViewById(R.id.take_photo_layout);
        mCameraPreview = findViewById(R.id.cameraPreview);
        FocusView focusView = findViewById(R.id.view_focus);

        mCropperLayout = findViewById(R.id.cropper_layout);
        mCropImageView = findViewById(R.id.CropImageView);
        mCropImageView.setGuidelines(2);

        mCameraPreview.setFocusView(focusView);
        mCameraPreview.setOnCameraStatusListener(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTransverse) {
            if (!isRotated) {
                TextView tvHint = findViewById(R.id.hint);
                ObjectAnimator animator = ObjectAnimator.ofFloat(tvHint, "rotation", 0f, 90f);
                animator.setStartDelay(800);
                animator.setDuration(500);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();

                ImageView btnShutter = findViewById(R.id.btn_shutter);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(btnShutter, "rotation", 0f, 90f);
                animator1.setStartDelay(800);
                animator1.setDuration(500);
                animator1.setInterpolator(new LinearInterpolator());
                animator1.start();

                View view = findViewById(R.id.crop_hint);
                AnimatorSet animSet = new AnimatorSet();
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "rotation", 0f, 90f);
                ObjectAnimator moveIn = ObjectAnimator.ofFloat(view, "translationX", 0f, -50f);
                animSet.play(animator2).before(moveIn);
                animSet.setDuration(10);
                animSet.start();

                ObjectAnimator animator3 = ObjectAnimator.ofFloat(btnAlbum, "rotation", 0f, 90f);
                animator3.setStartDelay(800);
                animator3.setDuration(500);
                animator3.setInterpolator(new LinearInterpolator());
                animator3.start();
                isRotated = true;
            }
        } else {
            if (!isRotated) {
                View view = findViewById(R.id.crop_hint);
                AnimatorSet animSet = new AnimatorSet();
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "rotation", 0f, 90f);
                ObjectAnimator moveIn = ObjectAnimator.ofFloat(view, "translationX", 0f, -50f);
                animSet.play(animator2).before(moveIn);
                animSet.setDuration(10);
                animSet.start();
                isRotated = true;
            }
        }
        mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 拍照界面
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_close: //关闭相机
                    finish();
                    break;
                case R.id.btn_shutter: //拍照
                    if (mCameraPreview != null) {
                        mCameraPreview.takePicture();
                    }
                    break;
                case R.id.btn_album: //相册
                    Intent intent = new Intent();
                    /* 开启Pictures画面Type设定为image */
                    intent.setType("image/*");
                    /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    /* 取得相片后返回本画面 */
                    startActivityForResult(intent, 1);
                    break;
            }
        }
    };

    /**
     * 截图界面
     */
    private View.OnClickListener cropcper = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_closecropper:
                    showTakePhotoLayout();
                    break;
                case R.id.btn_startcropper:
                    //获取截图并旋转90度
                    Bitmap cropperBitmap = mCropImageView.getCroppedImage();

                    Bitmap bitmap;
                    bitmap = Utils.rotate(cropperBitmap, -90);

                    // 系统时间
                    long dateTaken = System.currentTimeMillis();
                    // 图像名称
                    String filename = DateFormat.format("yyyy-MM-dd kk.mm.ss", dateTaken).toString() + ".jpg";
                    Uri uri = insertImage(getContentResolver(), filename, dateTaken, PATH, filename, bitmap, null);

                    Intent intent = new Intent(context, ShowCropperedActivity.class);
                    intent.setData(uri);
                    intent.putExtra("path", PATH + filename);
                    intent.putExtra("width", bitmap.getWidth());
                    intent.putExtra("height", bitmap.getHeight());
//                  intent.putExtra("cropperImage", bitmap);
                    startActivity(intent);
                    bitmap.recycle();
                    finish();
                    break;
            }
        }
    };

    /**
     * 拍照成功后回调
     * 存储图片并显示截图界面
     */
    @Override
    public void onCameraStopped(byte[] data) {
        Log.i("TAG", "==onCameraStopped==");
        // 创建图像
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        if (!isTransverse) {
            bitmap = Utils.rotate(bitmap, 90);
        }
        // 系统时间
        long dateTaken = System.currentTimeMillis();
        // 图像名称
        String filename = DateFormat.format("yyyy-MM-dd kk.mm.ss", dateTaken).toString() + ".jpg";
        // 存储图像（PATH目录）
        Uri source = insertImage(getContentResolver(), filename, dateTaken, PATH, filename, bitmap, data);

        //准备截图
        bitmap = Utils.rotate(bitmap, 90);
        mCropImageView.setImageBitmap(bitmap);
        showCropperLayout();
    }

    /*
     * 获取图片回调
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //与拍照保持一致方便处理
                bitmap = Utils.rotate(bitmap, 90);
                mCropImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        showCropperLayout();
    }

    /**
     * 存储图像并将信息添加入媒体数据库
     */
    private Uri insertImage(ContentResolver cr, String name, long dateTaken,
                            String directory, String filename, Bitmap source, byte[] jpegData) {
        OutputStream outputStream = null;
        String filePath = directory + filename;
        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(directory, filename);
            if (file.createNewFile()) {
                outputStream = new FileOutputStream(file);
                if (source != null) {
                    source.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                } else {
                    outputStream.write(jpegData);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Throwable t) {
                }
            }
        }
        ContentValues values = new ContentValues(7);
        values.put(MediaStore.Images.Media.TITLE, name);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, filePath);
        return cr.insert(IMAGE_URI, values);
    }

    private void showTakePhotoLayout() {
        mTakePhotoLayout.setVisibility(View.VISIBLE);
        mCropperLayout.setVisibility(View.GONE);
    }

    private void showCropperLayout() {
        mTakePhotoLayout.setVisibility(View.GONE);
        mCropperLayout.setVisibility(View.VISIBLE);
        mCameraPreview.start();   //继续启动摄像头
    }


    private float         mLastX       = 0;
    private float         mLastY       = 0;
    private float         mLastZ       = 0;
    private boolean       mInitialized = false;
    private SensorManager mSensorManager;
    private Sensor        mAccel;


    /**
     * 位移 自动对焦
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        }
        float deltaX = Math.abs(mLastX - x);
        float deltaY = Math.abs(mLastY - y);
        float deltaZ = Math.abs(mLastZ - z);

        if (deltaX > 0.8 || deltaY > 0.8 || deltaZ > 0.8) {
            mCameraPreview.setFocus();
        }
        mLastX = x;
        mLastY = y;
        mLastZ = z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
