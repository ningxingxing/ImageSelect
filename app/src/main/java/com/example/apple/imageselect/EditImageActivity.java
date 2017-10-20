package com.example.apple.imageselect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.apple.imageselect.adapter.MyTestAdapter;
import com.example.apple.imageselect.sticker.DrawableSticker;
import com.example.apple.imageselect.sticker.Sticker;
import com.example.apple.imageselect.sticker.StickerView;
import com.example.apple.imageselect.sticker.TextSticker;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by apple on 17/10/20.
 */

public class EditImageActivity extends Activity{

    private StickerView mStickerView;
    private RecyclerView editRecycleView;
    private ImageView ivShow;

    private ArrayList<String> selectPath = new ArrayList<>();

    private ItemTouchHelper itemTouchHelper;
    private boolean isMove = false;
    private MyTestAdapter myTestAdapter;
    private String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        initView();

        initData();
    }

    private void initView() {

        mStickerView = (StickerView)findViewById(R.id.stickerView);
        ivShow = (ImageView)findViewById(R.id.iv_show);
        editRecycleView = (RecyclerView)findViewById(R.id.edit_recycleView);


        myTestAdapter = new MyTestAdapter(getApplication(), selectPath);

        editRecycleView.setLayoutManager(new GridLayoutManager(this, 5));
        editRecycleView.setAdapter(myTestAdapter);
        editRecycleView.setItemAnimator(new DefaultItemAnimator());

        myTestAdapter.myItemOnClickListener(new MyTestAdapter.MyItemOnClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                mStickerView.removeAllStickers();
                if (position == 0) {
                    Intent intent = new Intent();
                    intent.setClass(EditImageActivity.this, ImageSelectActivity.class);
                    startActivityForResult(intent, 1);
                } else {

                    Bitmap bitmap = BitmapFactory.decodeFile(selectPath.get(position));
                    Drawable drawable = new BitmapDrawable(bitmap);
                    mStickerView.addSticker(new DrawableSticker(drawable));
                }
            }
        });

        imagePath = getIntent().getStringExtra("image");
        Glide.with(getApplication())
                .load(imagePath)
                .into(ivShow);
    }



    private void initData(){

        for (int i = 0; i < 10; i++) {
            selectPath.add("/storage/emulated/0/DCIM/Camera/IMG_20171014_150621.jpg");
        }

        myTestAdapter.setData(selectPath);
        editRecycleView.setAdapter(myTestAdapter);
        myTestAdapter.notifyDataSetChanged();


        mStickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {

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

        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition!=0 && toPosition!=0 && selectPath!=null) {
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
                if (position==0) {
                    myTestAdapter.notifyDataSetChanged();
                }else {
                    if (!isMove && selectPath!=null && selectPath.size()>0) {
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

        loadSticker(R.drawable.haizewang_90);
        addText("sssss");

    }

    private void loadSticker(int image){
        Drawable drawable = ContextCompat.getDrawable(this,image);
        mStickerView.addSticker(new DrawableSticker(drawable));
    }

    private void addText(String aa) {
       // Drawable bubble = ContextCompat.getDrawable(this, R.drawable.bubble);

        mStickerView.addSticker(
                new TextSticker(getApplicationContext())
                        //  .setDrawable(bubble)
                        .setText(aa)
                        .setMaxTextSize(14)
                        .resizeText()
                , Sticker.Position.TOP);
    }

    private void showInputMethod() {
        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
