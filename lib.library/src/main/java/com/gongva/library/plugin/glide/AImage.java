package com.gongva.library.plugin.glide;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.hik.core.android.api.ScreenUtil;
import com.hik.core.android.view.ViewUtil;
import com.gongva.library.R;
import com.gongva.library.app.TinkerApplicationCreate;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片加载工具类
 */
public class AImage {

    private static int defaultResId = /*R.drawable.ic_img_default*/0;
    public static String RES_PIC_URL_START = "res://";

    public static void setDefaultResId(int defaultResId) {
        AImage.defaultResId = defaultResId;
    }

    public static RequestOptions getOptions() {
        return new RequestOptions().placeholder(defaultResId);
    }

    private static RequestOptions getOptions(int defaultResId) {
        return new RequestOptions().placeholder(defaultResId);
    }

    //For resource
    public static void load(int res, ImageView imageView) {
        imageView.setImageResource(res);
    }

    public static void loadCenterCrop(int res, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(res);
    }

    //For hik url
    public static void load(String url, ImageView imageView) {
        load(url, null, imageView);
    }

    public static void load(String url, ImageView imageView, int defaultResId) {
        load(url, getOptions(defaultResId), imageView);
    }

    public static void load(String url, ImageView imageView, Drawable drawable) {
        load(url, new RequestOptions().placeholder(drawable), imageView);
    }

    public static void load(String url, ImageView imageView, RequestListener listener) {
        load(url, getOptions(), imageView, listener);
    }

    public static void loadRoundImage(String url, ImageView imageView, int imageRadius, Drawable drawable) {
        load(url, RequestOptions.bitmapTransform(new GlideRoundTransform(imageView.getContext(), imageRadius)).placeholder(drawable), imageView, null);
    }

    public static void loadCenterCrop(String url, ImageView imageView) {
        load(url, getOptions().centerCrop(), imageView);
    }

    public static void loadCenterInside(String url, ImageView imageView) {
        load(url, getOptions().centerInside(), imageView);
    }

    public static void loadCenterCrop(String url, ImageView imageView, int defaultResId) {
        load(url, getOptions(defaultResId).centerCrop(), imageView);
    }

    public static void loadCenterCrop(String url, ImageView imageView, Drawable drawable) {
        load(url, getOptions().centerCrop().error(drawable), imageView);
    }

    public static void load(String url, RequestOptions options, ImageView imageView) {
        load(url, options, imageView, null);
    }

    public static void loadHead(String url, ImageView imageView) {
        load(url, getOptions(R.drawable.ic_head_default).centerCrop(), imageView);
    }

    public static void load(String url, RequestOptions options, ImageView imageView, RequestListener listener) {
        load(url, null, options, imageView, listener);
    }

    public static void load(String url, Map<String, String> header, RequestOptions options, ImageView imageView, RequestListener listener) {
        if (options == null) options = new RequestOptions().placeholder(defaultResId);
        if (url != null && url.startsWith(RES_PIC_URL_START)) {
            int resId = Integer.valueOf(url.substring(RES_PIC_URL_START.length()));
            Drawable src = TinkerApplicationCreate.getApplication().getResources().getDrawable(resId);
            imageView.setImageDrawable(src);
            if (listener != null) {
                listener.onResourceReady(null,
                        null, null,
                        null, false);
            }
            return;
        }
        Activity imageActivity = ViewUtil.getActivityFromView(imageView);
        if (imageActivity != null && !imageActivity.isDestroyed()) {
            if (header != null && URLUtil.isNetworkUrl(url)) {
                options.diskCacheStrategy(DiskCacheStrategy.NONE);
                GlideUrl glideUrl = new GlideUrl(url, () -> header);
                Glide.with(imageActivity).load(glideUrl).apply(options).listener(listener).into(imageView);
            } else {
                Glide.with(imageActivity).load(url).apply(options).listener(listener).into(imageView);
            }
        }
    }

    public static Bitmap loadResizeBitmap(ImageView imageView, String uri, MixedTransform mixedTransform) {
        try {
            Activity imageActivity = ViewUtil.getActivityFromView(imageView);
            return Glide.with(imageActivity).asBitmap().load(uri).transform(mixedTransform).submit().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap loadResizeBitmap(Activity activity, String uri, MixedTransform mixedTransform) {
        try {
            return Glide.with(activity).asBitmap().load(uri).transform(mixedTransform).submit().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保持原始比例加载图片
     *
     * @param imageView
     * @param imageUrl
     * @param defaultWidth 图片显示的宽度
     */
    public static void loadKeepOriginalSize(ImageView imageView, String imageUrl, int defaultWidth) {
        if (imageView == null || TextUtils.isEmpty(imageUrl)) return;
        Activity activity = ViewUtil.getActivityFromView(imageView);
        if (defaultWidth == 0) defaultWidth = ScreenUtil.getScreenWidth(activity);
        int finalImageWidth = defaultWidth;
        Observable.create((ObservableOnSubscribe<ImageViewHolder>) e -> {
            ImageViewHolder holder = new ImageViewHolder();
            holder.imageView = imageView;
            holder.width = finalImageWidth;
            try {
                Bitmap bitmapTemp = AImage.loadResizeBitmap(imageView, imageUrl, new MixedTransform(finalImageWidth, ScreenUtil.getScreenHeight(activity)));
                if (bitmapTemp != null) {
                    int bitmapWidth = bitmapTemp.getWidth();
                    int bitmapHeight = bitmapTemp.getHeight();
                    bitmapTemp.recycle();
                    bitmapTemp = null;
                    int realHeight = bitmapHeight * finalImageWidth / bitmapWidth;
                    holder.height = (int) (realHeight - 0.5);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.onNext(holder);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(holder -> {
            if (holder != null && holder.imageView != null) {
                ViewGroup.LayoutParams p = holder.imageView.getLayoutParams();
                p.width = holder.width;
                p.height = holder.height;
                holder.imageView.setLayoutParams(p);
                load(imageUrl, holder.imageView);//这里为什么要重新load而不是使用下载的bitmap，因为要支持gif
            }
        });
    }

    // For file
    public static void loadCenterCropResizeFileWithQuality(String path, ImageView imageView, int width, int height, int quality, DecodeFormat format) {
        RequestOptions options = defaultResId == 0 ? getOptions(R.drawable.ic_img_default) : getOptions();
        options.format(format).encodeQuality(quality).override(width, height).centerCrop();
        Glide.with(imageView.getContext()).load(path).apply(options).into(imageView);
    }

    public static void loadCenterCropResizeFile(String path, ImageView imageView, int width, int height) {
        RequestOptions options = defaultResId == 0 ? getOptions(R.drawable.ic_img_default) : getOptions();
        options.override(width, height).centerCrop();
        load(path, options, imageView, null);
    }

    // static classes
    public static class ImageViewHolder {
        public ImageView imageView;
        public int width;//for image
        public int height;//for image
    }
}
