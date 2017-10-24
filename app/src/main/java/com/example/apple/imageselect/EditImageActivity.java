package com.example.apple.imageselect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apple.imageselect.adapter.MyTestAdapter;
import com.example.apple.imageselect.sticker.DrawableSticker;
import com.example.apple.imageselect.sticker.FileUtil;
import com.example.apple.imageselect.sticker.Sticker;
import com.example.apple.imageselect.sticker.StickerView;
import com.example.apple.imageselect.sticker.TextSticker;
import com.example.apple.imageselect.view.ColorPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by apple on 17/10/20.
 */

public class EditImageActivity extends Activity implements View.OnClickListener {
    private final String TAG = "EditImageActivity";

    private StickerView mStickerView;
    private RecyclerView editRecycleView;
    private ImageView ivShow;
    private TextView addText;
    private TextView save;
    private ImageView ivEdit;
    private TextView tvColor;

    private ArrayList<String> selectPath = new ArrayList<>();

    private ItemTouchHelper itemTouchHelper;
    private boolean isMove = false;
    private MyTestAdapter myTestAdapter;
    private String imagePath = null;
    private Sticker mSticker;
    private ArrayList<String> imageList;

    private ColorPicker dialog;

    private Canvas canvas;
    private Paint mPaint;
    private Bitmap mBitmap = null;
    private int mColor = 0;


    private int image[] = {R.drawable.imge, R.drawable.image1, R.drawable.image1, R.drawable.image3,
            R.drawable.image4, R.drawable.image5, R.drawable.image1, R.drawable.image1, R.drawable.image5, R.drawable.image3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        initView();

        initData();
    }

    private void initView() {

        mStickerView = (StickerView) findViewById(R.id.stickerView);
        ivShow = (ImageView) findViewById(R.id.iv_show);
        editRecycleView = (RecyclerView) findViewById(R.id.edit_recycleView);
        addText = (TextView) findViewById(R.id.add_text);
        addText.setOnClickListener(this);
        save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(this);
        ivEdit = (ImageView) findViewById(R.id.iv_edit);
        ivEdit.setOnClickListener(this);
        tvColor = (TextView) findViewById(R.id.tv_color);


        myTestAdapter = new MyTestAdapter(getApplication(), selectPath);

        editRecycleView.setLayoutManager(new GridLayoutManager(this, 5));
        editRecycleView.setAdapter(myTestAdapter);
        editRecycleView.setItemAnimator(new DefaultItemAnimator());

        myTestAdapter.myItemOnClickListener(new MyTestAdapter.MyItemOnClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {

//                if (position == 0) {
//                    Intent intent = new Intent();
//                    intent.setClass(EditImageActivity.this, ImageSelectActivity.class);
//                    startActivityForResult(intent, 1);
//                } else {
//
//                    //Bitmap bitmap = BitmapFactory.decodeFile(selectPath.get(position));
//                   // Drawable drawable = new BitmapDrawable(bitmap);
//                }
                if (mSticker != null) {
                    mStickerView.remove(mSticker);
                }
                Resources res = getResources();
                Drawable drawable = res.getDrawable(image[position]);
                mStickerView.addSticker(new DrawableSticker(drawable));


            }
        });

        imagePath = getIntent().getStringExtra("image");
        imageList = getIntent().getStringArrayListExtra("imageList");
//
//        Glide.with(getApplication())
//                .load(imagePath)
//                .into(ivShow);
    }


    private void initData() {

        for (int i = 0; i < 10; i++) {
            selectPath.add("/storage/emulated/0/DCIM/Camera/IMG_20171014_150621.jpg");
        }

        myTestAdapter.setData(selectPath);
        editRecycleView.setAdapter(myTestAdapter);
        myTestAdapter.notifyDataSetChanged();


        mStickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                if (sticker instanceof DrawableSticker) {
                    mSticker = sticker;
                }
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                if (sticker instanceof TextSticker) {
                    showInputMethod();
                }
            }
        });


        //触摸底部图片可换位置
        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition != 0 && toPosition != 0 && selectPath != null) {
                    if (fromPosition < toPosition) {
                        //分别把中间所有的item的位置重新交换
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(selectPath, i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(selectPath, i, i - 1);
                        }
                    }
                    isMove = true;
                    myTestAdapter.notifyItemMoved(fromPosition, toPosition);
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position == 0) {
                    myTestAdapter.notifyDataSetChanged();
                } else {
                    if (!isMove && selectPath != null && selectPath.size() > 0) {
                        selectPath.remove(position);
                    }
                    myTestAdapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //左右滑动时改变Item的透明度
                    final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (isMove) {
                    isMove = false;
                    myTestAdapter.notifyDataSetChanged();
                }
            }
        };

        itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(editRecycleView);

        //loadSticker(R.drawable.haizewang_90);
        //addText("双击添加文字");

        /**
         * 添加手绘图片
         */
        Matrix matrix = new Matrix();
        matrix.postScale(1, 1);
        mBitmap = BitmapFactory.decodeFile(imagePath).copy(Bitmap.Config.ARGB_8888, true);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        canvas = new Canvas(mBitmap);
        mPaint = new Paint();
        if (mColor!=0) {
            mPaint.setColor(mColor);
        }else {
            mPaint.setColor(Color.RED);
        }
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setTextSize(30);
        ivShow.setImageMatrix(matrix);
        ivShow.setImageBitmap(mBitmap);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        ivShow.setOnTouchListener(new View.OnTouchListener() {

            float downx = 0;
            float downy = 0;
            float upx = 0;
            float upy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downx = event.getX();
                        downy = event.getY();
                        if (mColor!=0){
                            mPaint.setColor(mColor);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        upx = event.getX();
                        upy = event.getY();
                        canvas.drawLine(downx, downy, upx, upy, mPaint);
                        ivShow.invalidate();
                        downx = upx;
                        downy = upy;
                        break;
                    default:
                        break;
                }

                return true;
            }
        });

    }

    /**
     * 添加图片
     * @param image
     */
    private void loadSticker(int image) {
        Drawable drawable = ContextCompat.getDrawable(this, image);
        mStickerView.addSticker(new DrawableSticker(drawable));
    }

    /**
     * 添加文字
     * @param aa
     */
    private void addText(String aa) {
        // Drawable bubble = ContextCompat.getDrawable(this, R.drawable.bubble);

        mStickerView.addSticker(
                new TextSticker(getApplicationContext())
                        //  .setDrawable(bubble)
                        .setText(aa)
                        .setMaxTextSize(16)
                        .resizeText()
                , Sticker.Position.TOP);
    }

    private void showInputMethod() {
        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_text:

                addText("双击添加文字");

                break;

            case R.id.save://返回编辑后的数据给ImageShowActivity
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
                }

                File file = FileUtil.getNewFile(EditImageActivity.this, "Sticker");
                if (file != null) {
                    mStickerView.save(file);
                    Toast.makeText(getApplication(), "保存成功保存到" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < imageList.size(); i++) {
                        if (imageList.get(i).equals(imagePath)) {
                            imageList.set(i, file.getPath());
                            Log.e(TAG, "imageList=" + imageList.get(i));

                            Glide.with(getApplication())
                                    .load(imageList.get(i))
                                    .into(ivShow);
                        }
                    }

                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("editImage", imageList);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplication(), "文件为空！", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.iv_edit:
                dialog = new ColorPicker(EditImageActivity.this, getResources().getColor(R.color.colorPrimary), getResources().getString(R.string.app_name), new ColorPicker.OnColorChangedListener() {
                    @Override
                    public void colorChanged(int color) {
                        tvColor.setTextColor(color);
                        mStickerView.setColor(color);
                        mColor = color;
                    }
                });

                dialog.show();

                break;

        }
    }

}
