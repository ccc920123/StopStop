package com.cdjysd.stopstop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;

import com.cdjysd.stopstop.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 * 类名: ImageUtility
 * <br/>功能描述: 图像处理类
 * <br/>作者: 陈渝金
 * <br/>时间: 2016/11/9
 * <br/>最后修改者:
 * <br/>最后修改内容:
 */

public class ImageUtility {
    /**
     * 方法名称: convertBitmapToString
     * <br/>方法详述: 将bitmap转换成成base64的字符串
     * <br/>参数:
     * <br/>返回值:
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
    }

    /**
     * 方法名称: convertBitmapStringToByteArray
     * <br/>方法详述: 将bitmap转换成base64的字节流
     * <br/>参数:
     * <br/>返回值:
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    public static byte[] convertBitmapStringToByteArray(String bitmapByteString) {
        return Base64.decode(bitmapByteString, Base64.DEFAULT);
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(base64Data)) {
            byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return bitmap;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 方法名称: adjustPhotoRotation
     * <br/>方法详述: 旋转bitmap
     * <br/>参数:
     * <br/>返回值:
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    /**
     * 方法名称:
     * <br/>方法详述: 添加水印
     * <br/>参数:context,照片byte,title 水印
     * <br/>返回值: Bitmap
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    public static Bitmap waterMarkPicture(Context context, byte[] data,
                                          String title, String buttom) {
        Bitmap bitmap = decodeSampledBitmapFromByte(context, data);
        Canvas canvas_temp = new Canvas(bitmap);
        TextPaint paint = new TextPaint();
        paint.setColor(Color.RED);
        paint.setTextSize(context.getResources().getDimensionPixelSize(
                R.dimen.dp_18));
        paint.setAntiAlias(true);
        StaticLayout layout = new StaticLayout(title, paint, bitmap.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//        String date_text = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                .format(new Date());
//        canvas_temp.drawText(String.format("%s", title),
//                bitmap.getWidth() / 20, bitmap.getHeight() / 15, paint);
        canvas_temp.save();
        canvas_temp.translate(bitmap.getWidth() / 50, bitmap.getHeight() / 55);//从20，20开始画
        layout.draw(canvas_temp);
        paint.setTextSize(context.getResources().getDimensionPixelSize(
                R.dimen.dp_12));
        StaticLayout layout_buttom = new StaticLayout(buttom, paint, bitmap.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas_temp.translate(bitmap.getWidth() / 50, bitmap.getHeight() - bitmap.getHeight() / 10);//从20，20开始画
        layout_buttom.draw(canvas_temp);
        canvas_temp.restore();//别忘了restore

        return bitmap;
    }

    /**
     * 方法名称:
     * <br/>方法详述: 添加水印
     * <br/>参数:context,照片byte,title 水印，rotation旋转角度
     * <br/>返回值: Bitmap
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    // int rotation,
    public static Bitmap waterMarkPicture(Context context, byte[] data,
                                          String title, int rotation) {
        Bitmap bitmap = decodeSampledBitmapFromByte(context, data);
        if (rotation != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);// 旋转图片

            bitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(),
                    oldBitmap.getHeight(), matrix, false);

            oldBitmap.recycle();
        }
        Canvas canvas_temp = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(context.getResources().getDimensionPixelSize(
                R.dimen.dp_18));
        // String date_text = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        // .format(new Date());
        canvas_temp.drawText(String.format("%s", title),
                bitmap.getWidth() / 20, bitmap.getHeight() / 15, paint);

        return bitmap;
    }

    /**
     * 方法名称:
     * <br/>方法详述: 添加水印
     * <br/>参数:context,照片byte,title 水印，rotation旋转角度
     * <br/>返回值: Bitmap
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    // int rotation,
    public static Bitmap waterMarkPicture(Context context, Bitmap bitmap,
                                          String title, String buttom, int rotation) {
        if (rotation != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);// 旋转图片

            bitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(),
                    oldBitmap.getHeight(), matrix, false);

            oldBitmap.recycle();
        }
        Canvas canvas_temp = new Canvas(bitmap);
//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setTextSize(context.getResources().getDimensionPixelSize(
//                R.dimen.dp_16));
//        // String date_text = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        // .format(new Date());
//        canvas_temp.drawText(String.format("%s", title),
//                0, 0, paint);
        TextPaint paint = new TextPaint();
        paint.setColor(Color.RED);
        paint.setTextSize(context.getResources().getDimensionPixelSize(
                R.dimen.dp_18));
        paint.setAntiAlias(true);
        StaticLayout layout = new StaticLayout(title, paint, bitmap.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//        String date_text = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                .format(new Date());
//        canvas_temp.drawText(String.format("%s", title),
//                bitmap.getWidth() / 20, bitmap.getHeight() / 15, paint);
        canvas_temp.save();
        canvas_temp.translate(bitmap.getWidth() / 50, bitmap.getHeight() / 55);//从20，20开始画
        layout.draw(canvas_temp);
        StaticLayout layout_buttom = new StaticLayout(buttom, paint, bitmap.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas_temp.translate(bitmap.getWidth() / 50, bitmap.getHeight() - bitmap.getHeight() / 15);//从20，20开始画
        layout_buttom.draw(canvas_temp);
        canvas_temp.restore();//别忘了restore
        return bitmap;
    }

    /**
     * 方法名称: waterMarkPicture
     * <br/>方法详述: 通过byte 得到bitmap
     * <br/>参数: context   byte
     * <br/>返回值: Bitmap
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    public static Bitmap waterMarkPicture(Context context, byte[] data) {
        Bitmap bitmap = decodeSampledBitmapFromByte(context, data);

        return bitmap;
    }

    /**
     * 方法名称: savePicture
     * <br/>方法详述: 将图片保存到sd内存卡
     * <br/>参数: context,bitmap 照片可以用waterMarkPicture（）转换成bitmap，
     * <br/>phoneName：照片名称，phonePath：照片路径。
     * <br/>返回值:String图片地址
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    public static String savePicture(Context context, Bitmap bitmap, String phoneName, String phonePath) {
        int cropHeight;
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(),
                bitmap.getHeight(), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        File mediaStorageDir = new File(phonePath);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//                .format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + phoneName + ".jpg");

        // Saving the bitmap
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            FileOutputStream stream = new FileOutputStream(mediaFile);
            stream.write(out.toByteArray());
            stream.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return mediaFile.getAbsolutePath();
    }

    /**
     * 方法名称: decodeSampledBitmapFromPath
     * <br/>方法详述: 根据图片路径，将图片转换成Bitmap，同时按照指定的宽度和高度等比压缩图片
     * <br/>参数:
     * <br/>返回值: Bitmap
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
                                                     int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inMutable = true;
        options.inBitmap = BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 方法名称: decodeSampledBitmapFromByte
     * <br/>方法详述: 将byte处理图片 共上面调用
     * <br/>参数: context，byte
     * <br/>返回值:Bitmap
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    public static Bitmap decodeSampledBitmapFromByte(Context context,
                                                     byte[] bitmapBytes) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int reqWidth, reqHeight;
        Point point = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(point);
            reqWidth = point.x;
            reqHeight = point.y;
        } else {
            reqWidth = display.getWidth();
            reqHeight = display.getHeight();
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inMutable = true;
        options.inBitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0,
                bitmapBytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false; // If set to true, the decoder will
        // return null (no bitmap), but the
        // out... fields will still be set,
        // allowing the caller to query the
        // bitmap without having to allocate
        // the memory for its pixels.
        options.inPurgeable = true; // Tell to gc that whether it needs free
        // memory, the Bitmap can be cleared
        options.inInputShareable = true; // Which kind of reference will be used
        // to recover the Bitmap data after
        // being clear, when it will be used
        // in the future
        return BitmapFactory.decodeByteArray(bitmapBytes, 0,
                bitmapBytes.length, options);
    }

    /**
     * Calculate an inSampleSize for use in a
     * {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from
     * {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that is a power of 2 and will result in the
     * final decoded bitmap having a width and height equal to or larger than
     * the requested width and height
     * <p>
     * The function rounds up the sample size to a power of 2 or multiple of 8
     * because BitmapFactory only honors sample size this way. For example,
     * BitmapFactory downsamples an image by 2 even though the request is 3. So
     * we round up the sample size to avoid OOM.
     */
    /**
     * 方法名称: calculateInSampleSize
     * <br/>方法详述: 等比压缩
     * <br/>参数:
     * <br/>返回值:  得到等比压缩值
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        int initialInSampleSize = computeInitialSampleSize(options, reqWidth,
                reqHeight);

        int roundedInSampleSize;
//        if (initialInSampleSize <= 8) {
//            roundedInSampleSize = 1;
//            while (roundedInSampleSize < (initialInSampleSize)) {
//                // Shift one bit to left
//                roundedInSampleSize <<= 1;
//            }
        if (initialInSampleSize <= 4) {
            roundedInSampleSize = initialInSampleSize;
        } else {
            roundedInSampleSize = (initialInSampleSize + 3) / 4 * 2;
        }

        return roundedInSampleSize;
    }

    /**
     * 方法名称: calculateInSampleSize
     * <br/>方法详述: 智能计算压缩值
     * <br/>参数:
     * <br/>返回值:  得到智能计算压缩值
     * <br/>异常抛出 Exception:
     * <br/>异常抛出 NullPointerException:
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int reqWidth, int reqHeight) {
        // Raw height and width of image
        final double height = options.outHeight;
        final double width = options.outWidth;

        final long maxNumOfPixels = reqWidth * reqHeight;
        final int minSideLength = Math.min(reqHeight, reqWidth);

        int lowerBound = (maxNumOfPixels < 0) ? 1 : (int) Math.ceil(Math
                .sqrt(width * height / maxNumOfPixels));
        int upperBound = (minSideLength < 0) ? 128 : (int) Math.min(
                Math.floor(width / minSideLength),
                Math.floor(height / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if (maxNumOfPixels < 0 && minSideLength < 0) {
            return 1;
        } else if (minSideLength < 0) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }


    public static String bitmapToBaseString(Bitmap bitmap) {

        ByteArrayOutputStream currentPhotoBAOS = null;
        String baseString = null;
        try {
            currentPhotoBAOS = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, currentPhotoBAOS)) {
                // originBitmap.recycle();
                currentPhotoBAOS.flush();
                byte[] imageBytes = currentPhotoBAOS.toByteArray();
                currentPhotoBAOS.close();
                currentPhotoBAOS = null;
                baseString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                imageBytes = null;

            }

        } catch (Exception e) {
            baseString = "";
        } finally {
            if (currentPhotoBAOS != null) {
                try {
                    currentPhotoBAOS.close();
                    currentPhotoBAOS = null;
                } catch (Exception ee) {
                }
            }
        }
        return baseString;

    }


    public static Bitmap waterMarkPictureAndCrop(Context context, byte[] data, String title, String buttom, float[] regionPos) {

        Bitmap bitmaporg = decodeSampledBitmapFromByte(context, data);
        Bitmap bitmap=Bitmap.createBitmap(bitmaporg.copy(Bitmap.Config.ARGB_4444,false), (int)(regionPos[0]*bitmaporg.getWidth()), (int)((regionPos[1])*bitmaporg.getHeight()), bitmaporg.getWidth(), (int)((regionPos[3]-regionPos[1])*bitmaporg.getHeight()));
       bitmaporg.recycle();
        Canvas canvas_temp = new Canvas(bitmap);
        TextPaint paint = new TextPaint();
        paint.setColor(Color.RED);
        paint.setTextSize(context.getResources().getDimensionPixelSize(
                R.dimen.dp_10));
        paint.setAntiAlias(true);
        StaticLayout layout = new StaticLayout(title, paint, bitmap.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//        String date_text = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                .format(new Date());
//        canvas_temp.drawText(String.format("%s", title),
//                bitmap.getWidth() / 20, bitmap.getHeight() / 15, paint);
        canvas_temp.save();
        canvas_temp.translate(bitmap.getWidth() / 50, bitmap.getHeight() / 55);//从20，20开始画
        layout.draw(canvas_temp);
        paint.setTextSize(context.getResources().getDimensionPixelSize(
                R.dimen.dp_10));
        StaticLayout layout_buttom = new StaticLayout(buttom, paint, bitmap.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas_temp.translate(bitmap.getWidth() / 50, bitmap.getHeight() - bitmap.getHeight() / 25);//从20，20开始画
        layout_buttom.draw(canvas_temp);
        canvas_temp.restore();//别忘了restore

        return bitmap;
    }
}