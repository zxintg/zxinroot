package com.zxin.root.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zxin.root.R;
import com.zxin.root.glide.ProgressModelLoader;
import com.zxin.root.util.logger.LogUtils;
import com.zxin.root.view.CircleProgress;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ImageUtil {
    private static final LogUtils.Tag TAG = new LogUtils.Tag("ImageUtil");
    public static final int IMG_120 = 180;
    public static final int IMG_720 = 720;
    public static final int IMG_360 = 360;
    public static final int IMG_1280 = 1280;

    private static volatile ImageUtil util = null;
    private Context mContext = null;

    private ImageUtil(Context mContext) {
        this.mContext = mContext;
    }

    public static ImageUtil getInstance(Context mContext) {
        if (util == null)
            synchronized (ImageUtil.class) {
                if (util == null)
                    util = new ImageUtil(mContext.getApplicationContext());
            }
        return util;
    }

    //加载本地
    public Drawable getBitMapForUrl(String path,int width, int height) {
        try {
            return new BitmapDrawable(Glide.with(mContext)
                    .load(path)
                    .asBitmap() //必须
                    .centerCrop()
                    .into(width, height)
                    .get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Drawable loadImageView(int path,  int width, int height) {
        try {
            return new BitmapDrawable( Glide.with(mContext)
                    .load(path)
                    .asBitmap()
                    .fitCenter()
                    .into(width, height)
                    .get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadImageView(int path, ImageView mImageView) {
        Glide.with(mContext).load(path).into(mImageView);
    }


    //加载本地圆形图片
    public void loadCircleImageView(String path, ImageView mImageView, int errorImageView) {
        Glide.with(mContext).load(path).bitmapTransform(new CropCircleTransformation(mImageView.getContext())).error(errorImageView).into(mImageView);
    }

    //加载本地圆角图片
    public void loadCircularImageView(String path, ImageView mImageView, int default_iamge, int errorImageView, int dp) {
        Glide.with(mContext).load(path).bitmapTransform(new GlideRoundTransform(mImageView.getContext(), dp)).error(errorImageView).into(mImageView);
    }

    //加载本地圆角图片(指定大小)
    public void loadCircularImageView(String path, int width, int height, ImageView mImageView, int errorImageView, int dp) {
        Glide.with(mContext).load(path).override(width, height).bitmapTransform(new GlideRoundTransform(mImageView.getContext(), dp)).error(errorImageView).into(mImageView);
    }

    //默认加载
    public void loadImageViews(int path, ImageView mImageView) {
        Glide.with(mContext).load(path).into(mImageView);
    }

    //加载指定大小
    public void loadImageViewSize(String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext).load(path).override(width, height).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public void loadImageViewLoding(String path, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    /****
     * 加载自定义圆形图片
     * @param path
     * @param mImageView
     * @param lodingImage
     */
    public void loadImageViewLoding(String path, ImageView mImageView, int lodingImage) {
        Glide.with(mContext).load(path).asBitmap().placeholder(lodingImage).into(mImageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public void loadImageViewLodingSize(String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).override(width, height).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置跳过内存缓存
    public void loadImageViewCache(String path, ImageView mImageView) {
        Glide.with(mContext).load(path).skipMemoryCache(true).into(mImageView);
    }

    //设置下载优先级
    public void loadImageViewPriority(String path, ImageView mImageView) {
        Glide.with(mContext).load(path).priority(Priority.NORMAL).into(mImageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public void loadImageViewDiskCache(String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    /**
     * api也提供了几个常用的动画：比如crossFade()
     */

    //设置加载动画
    public void loadImageViewAnim(String path, int anim, ImageView mImageView) {
        Glide.with(mContext).load(path).animate(anim).into(mImageView);
    }

    /*****
     * 加载进度条
     * @param path
     * @param mImageView
     */
    public void loadImageViewProgress(String path,ImageView mImageView,int initImage,int errorImageView) {
        CircleProgress.Builder builder = new CircleProgress.Builder();
        builder.setProgressColor(UiUtils.getInstance(mContext).getColor(R.color.color_ffffff));
        final CircleProgress progress = builder.build();
        progress.setTextShow(false);
        progress.inject(mImageView);
        Glide.with(mContext).using(new ProgressModelLoader(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progress.setLevel(msg.arg1);
                progress.setMaxValue(msg.arg2);
            }
        })).load(path).error(errorImageView).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public void loadImageViewThumbnail(String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */

    //设置动态转换
    public void loadImageViewCrop(String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }

    //设置动态GIF加载方式
    public void loadImageViewDynamicGif(String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asGif().into(mImageView);
    }

    //设置静态GIF加载方式
    public void loadImageViewStaticGif(String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asBitmap().into(mImageView);
    }

    //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘

    //设置监听请求接口
    public void loadImageViewListener(String path, ImageView mImageView, RequestListener<String, GlideDrawable> requstlistener) {
        Glide.with(mContext).load(path).listener(requstlistener).into(mImageView);
    }

    //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排

    //设置要加载的内容
    public void loadImageViewContent(String path, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(mContext).load(path).centerCrop().into(simpleTarget);
    }

    //清理磁盘缓存
    public void GuideClearDiskCache() {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public void GuideClearMemory() {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }

    /**
     * 给TextView、EditText、RadioButton设置CompoundDrawable
     *
     * @param view
     * @param widthPX
     * @param resId
     * @param gravity
     */
    public void setCompoundDrawable(View view, int widthPX, int resId, int gravity, int padPx) {
        int mPadPx = padPx == 0 ? 5 : padPx;
        if (resId > 0) {
            int width = (int) (widthPX * SystemInfoUtil.getInstance(mContext).getScreenDensity());
            BitmapDrawable drawable = (BitmapDrawable) mContext.getResources().getDrawable(resId);
            drawable.setBounds(0, 0, width, (int) (width * (drawable.getBitmap().getHeight() / (float) drawable.getBitmap().getWidth())));
            if (view instanceof TextView) {
                TextView compoundView = (TextView) view;
                compoundView.setCompoundDrawablePadding((int) (mPadPx * SystemInfoUtil.getInstance(mContext).getScreenDensity()));
                Drawable[] drawables = compoundView.getCompoundDrawables();
                switch (gravity) {
                    case Gravity.LEFT:
                        compoundView.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3]);
                        break;
                    case Gravity.TOP:
                        compoundView.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
                        break;
                    case Gravity.RIGHT:
                        compoundView.setCompoundDrawables(drawables[0], drawables[1], drawable, drawables[3]);
                        break;
                    case Gravity.BOTTOM:
                        compoundView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawable);
                        break;
                }
            } else if (view instanceof EditText) {
                EditText compoundView = (EditText) view;
                compoundView.setCompoundDrawablePadding((int) (mPadPx * SystemInfoUtil.getInstance(mContext).getScreenDensity()));
                Drawable[] drawables = compoundView.getCompoundDrawables();
                switch (gravity) {
                    case Gravity.LEFT:
                        compoundView.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3]);
                        break;
                    case Gravity.TOP:
                        compoundView.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
                        break;
                    case Gravity.RIGHT:
                        compoundView.setCompoundDrawables(drawables[1], drawables[2], drawable, drawables[3]);
                        break;
                    case Gravity.BOTTOM:
                        compoundView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawable);
                        break;
                }
            } else if (view instanceof RadioButton) {
                RadioButton compoundView = (RadioButton) view;
                compoundView.setCompoundDrawablePadding((int) (mPadPx * SystemInfoUtil.getInstance(mContext).getScreenDensity()));
                Drawable[] drawables = compoundView.getCompoundDrawables();
                switch (gravity) {
                    case Gravity.LEFT:
                        compoundView.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3]);
                        break;
                    case Gravity.TOP:
                        compoundView.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
                        break;
                    case Gravity.RIGHT:
                        compoundView.setCompoundDrawables(drawables[1], drawables[2], drawable, drawables[3]);
                        break;
                    case Gravity.BOTTOM:
                        compoundView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawable);
                        break;
                }
            }
        }
    }

    /**
     * 将一个bitmap保存为一个图片文件
     *
     * @param filePath
     * @return
     */
    public Uri getUriFromPath(String filePath) {
        try {
            Bitmap bitmap = processExifBitmap(filePath);
            if (bitmap != null) {
                File f = new File(filePath);
                if (f.exists()) {
                    f.delete();
                }
                f.createNewFile();
                FileOutputStream fOut = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 65, fOut);
                fOut.flush();
                fOut.close();
                return Uri.fromFile(f);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.parse(filePath);
    }

    /**
     * 根据文件路径获得转正之后的Bitmap
     *
     * @param filePath
     * @return
     */
    private Bitmap processExifBitmap(String filePath) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        int wRatio = (int) Math.ceil(op.outWidth / (float) SystemInfoUtil.getInstance(mContext).getScreenWidth());
        int hRatio = (int) Math.ceil(op.outHeight / (float) SystemInfoUtil.getInstance(mContext).getScreenHeight());
        op.inSampleSize = 1;
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getInstance(mContext).getTakePhotoFile().getPath(), op);
        int digree = getDigreeFromFilePath(filePath);
        if (digree != 0) {
            LogUtils.d(TAG,"读取图片中相机方向信息 旋转图片：" + digree);
            // 旋转图片
            Matrix m = new Matrix();
            m.postRotate(digree);
            if (bitmap != null) {
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            }
        }
        return bitmap;
    }

    /**
     * 根据路径获得图片的角度
     *
     * @param filePath
     * @return
     */
    public int getDigreeFromFilePath(String filePath) {
        int digree = 0;
        try {
            ExifInterface exif = new ExifInterface(filePath);
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return digree;
    }

    /**
     * 保存图片到图库
     */
    public boolean saveImageToGallery(Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AClassPhoto";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*****
     * 圆形图片
     * @param imageView
     * @param imageUrl
     */
    public void setItemRoundImageViewOnlyDisplay(String myPackageNam,ImageView imageView, String imageUrl) {
        if (!imageUrl.contains("http://") && !imageUrl.contains("https://")
                && !new File(imageUrl).exists()
                && !imageUrl.contains(myPackageNam)
                && (imageUrl.contains(".jpg") || imageUrl.contains(".jpeg") || imageUrl.contains(".png") || imageUrl.contains(".gif"))) {
            imageUrl = "http://img.shjujiao.com/" + imageUrl;
        }
        Glide.with(mContext)
                .load(imageUrl)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /****
     * 网络图片虚化
     * @param url
     * @param radius
     */
    public void loadRSBlurImage(String url,final ImageView imageView,final int radius){
        Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                loadRSBlurImage(resource,imageView,radius);
            }
        });
    }

    /****
     * Bitmap设置虚化效果 利用ScriptIntrinsicBlur，就可以简单高效地实现高斯模糊效果，可通过参数radius设置虚化的程度。
     * @param source
     * @param radius
     * @return
     */
    public void loadRSBlurImage(Bitmap source,ImageView imageView, int radius){
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);// 创建输出图片
        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(mContext);// 构建一个RenderScript对象
        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));// 创建高斯模糊脚本
        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, source);// 创建用于输入的脚本类型
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);// 创建用于输出的脚本类型
        //Set the radius of the blur: 0 < radius <= 25
        blurScript.setRadius(radius);// 设置模糊半径，范围0f<radius<=25f
        //Perform the Renderscript
        blurScript.setInput(allIn);// 设置输入脚本类型
        blurScript.forEach(allOut);// 执行高斯模糊算法，并将结果填入输出脚本类型中
        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);// 将输出内存编码为Bitmap，图片大小必须注意
        //recycle the original bitmap
        source.recycle();
        //After finishing everything, we destroy the Renderscript.
        rs.destroy();// 关闭RenderScript对象，API>=23则使用rs.releaseAllContexts()
        imageView.setImageBitmap(outBitmap);
    }

}
