package com.hik.core.android.view.imageview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * 图片处理器
 *
 * @author gongwei
 * @time 2019/10/29
 * @mail shmily__vivi@163.com
 */
public class BitmapUtil {
    private static final Rect sOldBounds = new Rect();
    private static final Canvas sCanvas = new Canvas();

    static {
        sCanvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
    }

    public BitmapUtil() {
    }

    public static Bitmap getBitmapResources(Context context, int resId) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, resId);
    }

    public static Bitmap getBitmap(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static Bitmap getBitmapURL(String url) {
        try {
            URL _url = new URL(url);
            return BitmapFactory.decodeStream(_url.openStream());
        } catch (IOException var2) {
            return null;
        }
    }

    public static boolean saveFile(Bitmap bitmap, String path) {
        return saveFile(bitmap, path, Bitmap.CompressFormat.JPEG, 100);
    }

    public static boolean saveFile(Bitmap bitmap, String path, int quality) {
        return saveFile(bitmap, path, Bitmap.CompressFormat.JPEG, quality);
    }

    public static boolean saveFile(Bitmap bitmap, String path, Bitmap.CompressFormat format, int quality) {
        try {
            OutputStream out = new FileOutputStream(path);
            bitmap.compress(format, quality, out);
            out.flush();
            return true;
        } catch (IOException var5) {
            return false;
        }
    }

    public static int getFileExifRotation(String filepath) {
        try {
            ExifInterface exifInterface = new ExifInterface(filepath);
            int result = exifInterface.getAttributeInt("Orientation", 0);
            switch (result) {
                case 3:
                    return 180;
                case 4:
                case 5:
                case 7:
                default:
                    return 0;
                case 6:
                    return 90;
                case 8:
                    return 270;
            }
        } catch (IOException var3) {
            return 0;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotationDegrees) {
        if (bitmap != null && rotationDegrees != 0) {
            Bitmap tempBitmap = bitmap;
            if (bitmap.getWidth() != 0 && bitmap.getHeight() != 0) {
                Matrix matrix = new Matrix();
                matrix.setRotate((float) rotationDegrees, (float) bitmap.getWidth() / 2.0F, (float) bitmap.getHeight() / 2.0F);
                tempBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                bitmap = null;
            }

            return tempBitmap;
        } else {
            return bitmap;
        }
    }

    public static Bitmap bytes2Bimap(byte[] b) {
        return b.length == 0 ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bout);
        return bout.toByteArray();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    public static Bitmap drawableToBitmapByCanvas(Drawable drawable) {
        synchronized (sCanvas) {
            Bitmap.Config config = drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), config);
            Canvas canvas = sCanvas;
            canvas.setBitmap(bitmap);
            sOldBounds.set(drawable.getBounds());
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            drawable.setBounds(sOldBounds);
            canvas.setBitmap((Bitmap) null);
            return bitmap;
        }
    }

    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bitmap);
        return bd;
    }

    public static Bitmap alphaBitmap(Bitmap src, int alpha) {
        int[] argb = new int[src.getWidth() * src.getHeight()];
        src.getPixels(argb, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
        alpha = alpha * 255 / 100;

        for (int i = 0; i < argb.length; ++i) {
            argb[i] = alpha << 24 | argb[i] & 16777215;
        }

        src = Bitmap.createBitmap(argb, src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        return src;
    }

    public static Bitmap scaledBitmap(String filePath, int dstSize) {
        return scaledBitmap(filePath, dstSize, dstSize);
    }

    public static Bitmap scaledBitmap(String filePath, int dstSize, boolean convert) {
        return scaledBitmap(filePath, dstSize, dstSize);
    }

    public static Bitmap scaledBitmap(String filePath, int dstWidth, int dstHeight) {
        return scaledBitmap(filePath, dstWidth, dstHeight, Bitmap.Config.RGB_565);
    }

    public static Bitmap scaledBitmap(String filePath, int dstWidth, int dstHeight, Bitmap.Config config) {
        Bitmap bitmap = null;
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, decodeOptions);
        int optionsWidth = decodeOptions.outWidth;
        int optionsHeight = decodeOptions.outHeight;
        if (optionsWidth <= dstWidth && optionsHeight <= dstHeight) {
            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inPreferredConfig = config;
            bitmap = BitmapFactory.decodeFile(filePath, decodeOptions);
        } else {
            int desiredWidth = getResizedDimension(dstWidth, dstHeight, optionsWidth, optionsHeight);
            int desiredHeight = getResizedDimension(dstHeight, dstWidth, optionsHeight, optionsWidth);
            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inPreferredConfig = config;
            decodeOptions.inSampleSize = findBestSampleSize(optionsWidth, optionsHeight, desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeFile(filePath, decodeOptions);
            if (tempBitmap == null || tempBitmap.getWidth() <= desiredWidth && tempBitmap.getHeight() <= desiredHeight) {
                bitmap = tempBitmap;
            } else {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            }
        }

        return rotateBitmap(bitmap, getFileExifRotation(filePath));
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        } else {
            double ratio;
            if (maxPrimary == 0) {
                ratio = (double) maxSecondary / (double) actualSecondary;
                return (int) ((double) actualPrimary * ratio);
            } else if (maxSecondary == 0) {
                return maxPrimary;
            } else {
                ratio = (double) actualSecondary / (double) actualPrimary;
                int resized = maxPrimary;
                if ((double) maxPrimary * ratio > (double) maxSecondary) {
                    resized = (int) ((double) maxSecondary / ratio);
                }

                return resized;
            }
        }
    }

    private static int findBestSampleSize(int optionsWidth, int optionsHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) optionsWidth / (double) desiredWidth;
        double hr = (double) optionsHeight / (double) desiredHeight;
        double ratio = Math.min(wr, hr);

        float n;
        for (n = 1.0F; (double) (n * 2.0F) <= ratio; n *= 2.0F) {
        }

        return (int) n;
    }

    public static Bitmap createReflectImage(Bitmap originalImage, int reflectHeight) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0F, -1.0F);
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height - reflectHeight, width, reflectHeight, matrix, false);
        Bitmap bitmapCanvas = Bitmap.createBitmap(width, reflectHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapCanvas);
        canvas.drawBitmap(reflectionImage, 0.0F, 0.0F, (Paint) null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0.0F, 0.0F, 0.0F, (float) bitmapCanvas.getHeight(), -2147483648, 0, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0.0F, 0.0F, (float) width, (float) bitmapCanvas.getHeight(), paint);
        return bitmapCanvas;
    }

    public static Bitmap createReflectImage(Bitmap originalImage, int reflectHeight, int reflectGap) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int reflectTop = height + reflectGap;
        Matrix matrix = new Matrix();
        matrix.preScale(1.0F, -1.0F);
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height - reflectHeight, width, reflectHeight, matrix, false);
        Bitmap bitmapCanvas = Bitmap.createBitmap(width, reflectTop + reflectHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCanvas);
        canvas.drawBitmap(originalImage, 0.0F, 0.0F, (Paint) null);
        canvas.drawBitmap(reflectionImage, 0.0F, (float) reflectTop, (Paint) null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0.0F, (float) reflectTop, 0.0F, (float) bitmapCanvas.getHeight(), 1073741824, 0, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0.0F, (float) height, (float) width, (float) bitmapCanvas.getHeight(), paint);
        return bitmapCanvas;
    }
}
