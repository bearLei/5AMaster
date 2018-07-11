package unit.preview;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.puti.education.R;
import com.puti.education.util.FileUtils;

import java.io.File;
import java.util.ArrayList;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.PhotoDraweeView;


/**
 * 原来是一次性加载完毕所有的图片；更改后，刚进来只加载对应的图片，然后翻页时再加载对应图片
 * 这个只适合size较小的情况，如果size较大，会占用大量内存
 */
public class PreviewActivity extends Activity {
    public static String TAG = "PreviewActivity";
    private MultiTouchViewPager VPager;

    protected int mLocation = 0;//刚开始的时候指向第几页
    protected ArrayList<String> mUrlList = new ArrayList<>();//图片的URL列表
    protected ArrayList<String> mThumbUrlList = new ArrayList<>();//缩略图的URL列表

    private TextView TPreTitle;//底部标题

    private final ArrayList<PhotoDraweeView> mPhotoList = new ArrayList<>();//viewpager的每个页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        parseIntent();//解析出mLocation 和 mUrlList

        VPager = (MultiTouchViewPager) findViewById(R.id.view_pager);
        TPreTitle = (TextView) findViewById(R.id.preview_title);


        initPhotoViews();//初始化页面

        if (mUrlList.size() > 0) {
            loadImage();//初始化viewpager并加载图片
        } else {
            return;
        }

        showTitle();//展示底部标题
    }

    /*解析intent*/
    protected void parseIntent() {
        mLocation = getIntent().getIntExtra("location", 0);
        mUrlList = getIntent().getStringArrayListExtra("list");
//        mThumbUrlList = getIntent().getStringArrayListExtra("thumbList");
    }

    /*初始化views*/
    private void initPhotoViews() {
        if (mUrlList.size() <= 0)
            return;
        mPhotoList.clear();
        for (int i = 0; i < mUrlList.size(); i++) {
            PhotoDraweeView photoview = new PhotoDraweeView(this);
            mPhotoList.add(i, photoview);
        }
    }

    /*初始化viewpager并加载图片*/
    private void loadImage() {
        MyPageAdapter adapter = new MyPageAdapter(mUrlList);
        VPager.setAdapter(adapter);
        //设置PageChangeListener一定要在setAdapter之后
        VPager.addOnPageChangeListener(mPageChangeListener);
        VPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.dp_10));
        VPager.setCurrentItem(mLocation, false);
    }

    private void showTitle() {
        if (mUrlList.size() > 1) {
            TPreTitle.setVisibility(View.VISIBLE);
            TPreTitle.setText(mLocation + 1 + "/" + mUrlList.size());
        } else {
            TPreTitle.setVisibility(View.GONE);
        }
    }



    private final ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        public void onPageSelected(int position) {//当进入一个页面后，更换标题，加载图片
            mLocation = position;
            showTitle();
            initPhotoIfNeed(mPhotoList.get(position), position);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /*加载图片*/
    private void initPhotoIfNeed(final PhotoDraweeView photoDraweeView, int position) {
        if (photoDraweeView == null || photoDraweeView.getController() != null)//当已经有Controller后就不要管了
            return;
        //设置进度条
//        photoDraweeView.getHierarchy().setProgressBarImage(new CircleProgressDrawable());
        //开始加载图片
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        Uri heighResUri = null;
        if (mUrlList.size() >= position && mUrlList.get(position) != null) {
             heighResUri = Uri.parse(mUrlList.get(position));
        }
//        controller.setUri(ImageRequest.fromUri(""));
        if (heighResUri != null) {
            controller.setImageRequest(ImageRequest.fromUri(heighResUri));
        }
        if (mThumbUrlList != null
                && mThumbUrlList.size() > 0
                && mThumbUrlList.size() == mUrlList.size()) { //先展示缩略图
            Uri lowResUri = Uri.parse(mThumbUrlList.get(position));
            controller.setLowResImageRequest(ImageRequest.fromUri(lowResUri));
        }
        controller.setOldController(photoDraweeView.getController());
        controller.setControllerListener(new PutiFrescoBaseControllerListener<ImageInfo>(heighResUri) {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());

            }
        });
        photoDraweeView.setController(controller.build());
    }

    class MyPageAdapter extends PagerAdapter {

        private final int size;

        public MyPageAdapter(ArrayList<String> list) {
            size = list == null ? 0 : list.size();
        }

        public int getCount() {
            return size;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            PhotoDraweeView photoDraweeView = mPhotoList.get(position);

            container.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }
            });


            //加载刚进来时对应的图片
            if (mLocation == position)
                initPhotoIfNeed(photoDraweeView, position);
            return photoDraweeView;
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }



    private void saveImage(Bitmap bitmap) {
        String save_url = FileUtils.getTempDirPath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
        if (null != bitmap) {
            FileUtils.saveBitmap(bitmap, save_url, 90);
//            ToastUtil.showToast("已成功保存到" + FileUtils.getTempDirPath() + "目录");
            MediaScannerConnection.scanFile(PreviewActivity.this, new String[]{save_url}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
        }
    }

}
