package com.liang530.photopicker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.base.R;
import com.liang530.utils.GlideDisplay;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ShowPicVPActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private final static String PHOTO_URLS = "photoUrlsJson";
    ViewPager photoViewPager;
    TextView photoCount;
    ImageView image_return;
    ArrayList<String> photoUrls;
    private boolean isFile;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic_vp);
        photoViewPager = (ViewPager) findViewById(R.id.viewpager);
        photoCount = (TextView) findViewById(R.id.tv_photo_count);
        image_return = (ImageView) findViewById(R.id.image_return);
        image_return.setOnClickListener(this);
//        String photoUrlsJson = getIntent().getStringExtra(PHOTO_URLS);
//        photoUrls= JSON.parseObject(photoUrlsJson, new TypeReference<List<String>>() {});
        if (getIntent() != null) {
            photoUrls = getIntent().getStringArrayListExtra(PHOTO_URLS);
            isFile = getIntent().getBooleanExtra("isFile", false);
            currentIndex = getIntent().getIntExtra("currentIndex", 0);
        }
        photoViewPager.setAdapter(new ShowPictureAdapter(this, photoUrls));
        photoViewPager.addOnPageChangeListener(this);
        photoCount.setText((currentIndex + 1) + "/" + photoUrls.size());
        photoViewPager.setCurrentItem(currentIndex, false);
    }

    public static void start(Context context, ArrayList<String> photoUrlsJson, int currentIndex, boolean isFile) {
        Intent starter = new Intent(context, ShowPicVPActivity.class);
        starter.putStringArrayListExtra(PHOTO_URLS, photoUrlsJson);
        starter.putExtra("isFile", isFile);
        starter.putExtra("currentIndex", currentIndex);
        context.startActivity(starter);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        photoCount.setText((i + 1) + "/" + photoUrls.size());
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View v) {
        if (R.id.image_return == v.getId()) {
            finish();
        }
    }

    private class ShowPictureAdapter extends PagerAdapter {
        List<String> sixinMessageList;
        private Context context;

        public ShowPictureAdapter(Context context, List<String> sixinMessageList) {
            this.context = context;
            this.sixinMessageList = sixinMessageList;
        }

        @Override
        public int getCount() {
            if (sixinMessageList == null) return 0;
            return sixinMessageList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String goodsImage = sixinMessageList.get(position);
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_photo_show, null);
            PhotoView imageView = (PhotoView) view.findViewById(R.id.iv_showpicture);

            /**
             * 设置点击关闭（非双击，非长按）
             */
            imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    finish();
                }
            });
            /**
             * 设置长按事件
             */
//            imageView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    showDialog(context, position);
//                    return false;
//                }
//            });
            imageView.setZoomable(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            if (isFile) {
                GlideDisplay.displayDefaul(imageView, new File(goodsImage));
//                Picasso.with(context).load(new File(goodsImage)).fit().centerInside().into(imageView);
            } else {
                GlideDisplay.displayDefaul(imageView, goodsImage);
//                Picasso.with(context).load(Urls.imageUrl+goodsImage).fit().centerInside().into(imageView);
            }
//            ImageLoader.getInstance().displayImage(goodsImage.getGoodsImage(),imageView);
            container.addView(view);
            return view;
        }
    }

}
