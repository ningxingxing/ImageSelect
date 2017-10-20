package com.example.apple.imageselect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apple.imageselect.data.ImageData;
import com.example.apple.imageselect.view.ZoomImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17/10/19.
 */

public class ImageShowActivity extends Activity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvNumber;
    private Button btnFinish;

    private ZoomImageView imageShow;
    private ViewPager viewPager;

    private TextView tvEdit;
    private CheckBox cbSelect;
    private List<String> mShowImageList = new ArrayList<>();
    private ArrayList<ImageData> imageDataArrayList = new ArrayList<>();

    private int currentPosition = 0;
    private boolean isRightSlipe = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);


        initView();
        getData();
        initData();

    }

    private void initView() {

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        tvNumber = (TextView) findViewById(R.id.tv_number);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(this);

        imageShow = (ZoomImageView) findViewById(R.id.image_show);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvEdit.setOnClickListener(this);
        cbSelect = (CheckBox) findViewById(R.id.cb_select);


    }

    private void getData() {

        mShowImageList = getIntent().getStringArrayListExtra("showImage");


    }

    private void initData() {

        tvNumber.setText("1/6");
        cbSelect.setChecked(true);

        for (int i = 0; i < mShowImageList.size(); i++) {
            ImageData id = new ImageData();
            id.setPath(mShowImageList.get(i));
            id.setSelect(true);
            imageDataArrayList.add(id);
        }

        //选中或取消
        cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    imageDataArrayList.get(currentPosition).setSelect(true);
                } else {
                    imageDataArrayList.get(currentPosition).setSelect(false);
                }
            }
        });


        final ImageView[] mImageViews = new ImageView[mShowImageList.size()];

        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ZoomImageView zoomImageView = new ZoomImageView(getApplication());
                File file = new File(mShowImageList.get(position));
                Glide.with(getApplication())
                        .load(file)
                        .into(zoomImageView);

                mImageViews[position] = zoomImageView;
                container.addView(zoomImageView);
                return zoomImageView;
            }

            @Override
            public int getCount() {
                return mShowImageList.size();
            }

            @Override
            public void setPrimaryItem(View container, int position, Object object) {


                tvNumber.setText((position + 1) + "/" + mShowImageList.size());

                currentPosition = position;

               // Log.e("nsc", "position=" + position + "imageDataArrayList.get(position).isSelect()=" + imageDataArrayList.get(position).isSelect());

                if (imageDataArrayList.get(position).isSelect()) {
                    cbSelect.setChecked(true);
                } else if (imageDataArrayList.get(position).isSelect() == false) {
                    cbSelect.setChecked(false);
                }
                super.setPrimaryItem(container, position, object);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

                container.removeView(mImageViews[position]);
            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                //Log.e("nsc","isViewFromObject");
                return view == object;
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:
                finish();

                break;

            case R.id.btn_finish:
                Intent intent  = new Intent();
                intent.putStringArrayListExtra("editFile",getAllEditFile());
                intent.putExtra("isEdit",true);
                setResult(RESULT_OK,intent);
                finish();
                break;

            case R.id.tv_edit:
                Intent intent1 = new Intent(ImageShowActivity.this,EditImageActivity.class);
                intent1.putExtra("image",imageDataArrayList.get(currentPosition).getPath());
                startActivity(intent1);


                break;
        }

    }

    /**
     * get all edit image
     * @return
     */
    private ArrayList<String> getAllEditFile() {

        ArrayList arrayList = new ArrayList();
        if (arrayList != null && arrayList.size() > 0) {
            arrayList.clear();
        }

        if (imageDataArrayList != null && imageDataArrayList.size() > 0) {

            for (int i = 0; i < imageDataArrayList.size(); i++) {

                if (imageDataArrayList.get(i).isSelect()) {
                    arrayList.add(imageDataArrayList.get(i).getPath());
                }
            }

        }

        return arrayList;

    }
}







