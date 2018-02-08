package ru.surfstudio.android.core.ui.base.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.WorkerThread;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.surfstudio.android.core.R;
import ru.surfstudio.android.core.ui.base.image.RoundedCornersTransformation.CornerType;
import ru.surfstudio.android.core.ui.base.image.blur.BlurTransformation;
import ru.surfstudio.android.logger.Logger;

/**
 * Класс-прокси, реализующий загрузку изображений с необходимостью абстрагироваться от реализации контракта библиотеки
 * Необходимо проксировать абсолютно все запросы на отображение изображения
 */
public final class ImageLoader {

    public static final int NO_SIZE = -1;

    private Context context;
    private View view;
    private String url;
    @DrawableRes
    private int drawableUri;
    @DrawableRes
    private int previewResId;
    @DrawableRes
    private int errorResId;

    private int maxWidth = NO_SIZE;
    private int maxHeight = NO_SIZE;
    private int cornerRadius;
    private int cornerMargin;
    private CornerType cornerType;
    private int blurRadius = 20;
    private int blurSampling = 1;

    private boolean centerCrop;

    private boolean circle;

    @DrawableRes
    private int overlayResId;

    @DrawableRes
    private int maskResId;

    private boolean skipCache;
    private boolean blurTransform;

    private boolean filterBitmapOnScale = false;
    private OnImageLoadedListener listener;
    private OnImageErrorLoadListener errorLoadLisner;
    private RequestListener<Serializable, Bitmap> glideListener = new RequestListener<Serializable, Bitmap>() {
        @Override
        public boolean onException(Exception e, Serializable model, Target<Bitmap> target, boolean isFirstResource) {
            if (view != null) {
                view.setTag(R.id.image_loader_tag, null);
            }

            if (errorLoadLisner != null) {
                errorLoadLisner.onLoadError(e);
            }
            return false;
        }

        @Override
        public boolean onResourceReady(Bitmap resource,
                                       Serializable model,
                                       Target<Bitmap> target,
                                       boolean isFromMemoryCache,
                                       boolean isFirstResource) {
            if (listener != null) {
                listener.onLoadFinish(resource);
            }
            return false;
        }
    };

    private ImageLoader(Context context) {
        this.context = context;
    }

    public static ImageLoader with(Context context) {
        return new ImageLoader(context);
    }

    private static Bitmap getBitmap(VectorDrawableCompat vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public ImageLoader url(String url) {
        this.url = url;
        return this;
    }

    public ImageLoader url(@DrawableRes int drawableUri) {
        this.drawableUri = drawableUri;
        return this;
    }

    public ImageLoader centerCrop(boolean centerCrop) {
        this.centerCrop = centerCrop;
        return this;
    }

    public ImageLoader circle(boolean circle) {
        this.circle = circle;
        return this;
    }

    /**
     * устанавливает максимальный размер изображения
     * отношение ширины и высоты останется неизменным
     */
    public ImageLoader maxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    /**
     * устанавливает максимальный размер изображения
     * отношение ширины и высоты останется неизменным
     */
    public ImageLoader maxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    /**
     * радиус для обрезания углов с отступом
     *
     * @param radius радиус угла в пикселях
     * @param margin отступ от края в пикселях
     */
    public ImageLoader roundedCorners(int radius, int margin, CornerType cornerType) {
        this.cornerRadius = radius;
        this.cornerMargin = margin;
        this.cornerType = cornerType;
        return this;
    }

    public ImageLoader roundedAllCorners(int radius, int margin) {
        this.cornerRadius = radius;
        this.cornerMargin = margin;
        this.cornerType = CornerType.ALL;
        return this;
    }

    public ImageLoader blur(boolean blurTransform) {
        this.blurTransform = blurTransform;
        return this;
    }

    public ImageLoader blur(int radius, int sampling) {
        blurTransform = true;
        blurRadius = radius;
        blurSampling = sampling;
        return this;
    }

    /**
     * Наложение drawable с {@code PorterDuff.Mode.DST_ATOP}
     */
    public ImageLoader overlay(@DrawableRes int resId) {
        overlayResId = resId;
        return this;
    }

    /**
     * Масковая трансформация, может работать с NinePatch
     */
    public ImageLoader mask(@DrawableRes int resId) {
        maskResId = resId;
        return this;
    }

    public ImageLoader preview(@DrawableRes int resId) {
        this.previewResId = resId;
        return this;
    }

    public ImageLoader error(@DrawableRes int resId) {
        this.errorResId = resId;
        return this;
    }

    public ImageLoader skipCache(boolean skipCache) {
        this.skipCache = skipCache;
        return this;
    }

    /**
     * Фильтровать ли изображения при масштабировании
     *
     * @see Bitmap#createScaledBitmap(Bitmap, int, int, boolean)
     */
    public ImageLoader filterBitMapOnScale(boolean filterBitmapOnScale) {
        this.filterBitmapOnScale = filterBitmapOnScale;
        return this;
    }

    @WorkerThread
    public Bitmap get() {
        Bitmap result;
        try {
            result = Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .into(NO_SIZE, NO_SIZE)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            if (errorResId == 0) {
                throw new IllegalStateException("Error load bitmap", e);
            }
            if (errorLoadLisner != null) {
                errorLoadLisner.onLoadError(e);
            }
            result = getFromResource(errorResId);
        }
        return result;
    }

    public void downloadOnly() {
        Glide.with(context)
                .load(url)
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        Logger.d("Image loaded to cache " + (resource != null ? resource.getAbsolutePath() : "null" ));
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        if (errorLoadLisner != null) {
                            errorLoadLisner.onLoadError(e);
                        }
                    }
                });
    }

    public void in(ImageView imageView) {
        view = imageView;

        if (imageView.getTag(R.id.image_loader_tag) != null &&
                imageView.getTag(R.id.image_loader_tag).equals(url)) {
            return;
        }

        imageView.setTag(R.id.image_loader_tag, url);
        bitmapRequestBuilder().into(imageView);
    }

    public void in(View view) {
        this.view = view;

        if (view.getTag(R.id.image_loader_tag) != null &&
                view.getTag(R.id.image_loader_tag).equals(url)) {
            return;
        }

        view.setTag(R.id.image_loader_tag, url);
        bitmapRequestBuilder().into(new ViewTarget<View, Bitmap>(view) {
            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                view.setBackground(getCompatDrawable(errorResId));
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                view.setBackground(new BitmapDrawable(context.getResources(), resource));
            }
        });
    }

    public void in(SimpleTarget simpleTarget) {
        bitmapRequestBuilder().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                simpleTarget.onResourceReady(getFromResource(errorResId), null);

            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                simpleTarget.onResourceReady(resource, glideAnimation);
            }
        });
    }

    public ImageLoader clear(ImageView imageView) {
        Glide.clear(imageView);
        return this;
    }

    private Drawable getCompatDrawable(int previewResId) {
        return previewResId != 0
                ? ContextCompat.getDrawable(context, previewResId)
                : null;
    }

    public ImageLoader listener(OnImageLoadedListener listener) {
        this.listener = listener;
        return this;
    }

    public ImageLoader errorlistener(OnImageErrorLoadListener errorLoadListener) {
        this.errorLoadLisner = errorLoadListener;
        return this;
    }

    @WorkerThread
    private Bitmap getFromResource(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawableCompat) {
            return getBitmap((VectorDrawableCompat) drawable);
        } else if (drawable instanceof VectorDrawable) {
            //noinspection NewApi
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type " + drawable.getClass().getSimpleName());
        }
    }

    private BitmapRequestBuilder bitmapRequestBuilder() {
        List<Transformation> transforms = new ArrayList<>();
        transforms.add(new SizeTransformation(context,
                maxWidth, maxHeight, filterBitmapOnScale));

        if (centerCrop) {
            transforms.add(new CenterCrop(context));
        }

        if (circle) {
            transforms.add(new CircleCropTransformation(context));
        }

        if (cornerRadius > 0 || cornerMargin > 0) {
            transforms.add(new RoundedCornersTransformation(context,
                    cornerRadius, cornerMargin, cornerType));
        }

        if (blurTransform) {
            transforms.add(new BlurTransformation(context, blurRadius, blurSampling));
        }

        if (overlayResId > 0) {
            transforms.add(new OverlayTransformation(context, overlayResId));
        }

        // масковая трансформация должна накладываться последней
        // для обрезания конечного результата
        if (maskResId > 0) {
            transforms.add(new MaskTransformation(context, maskResId));
        }

        return Glide.with(context)
                .load(drawableUri != 0 ? drawableUri : url)
                .asBitmap()
                .skipMemoryCache(skipCache)
                .diskCacheStrategy(skipCache ? DiskCacheStrategy.NONE : DiskCacheStrategy.ALL)
                .placeholder(getCompatDrawable(previewResId))
                .error(getCompatDrawable(errorResId))
                .animate(R.anim.fade_in) // workaround https://github.com/bumptech/glide/issues/363
                .listener(glideListener)
                .transform(transforms.toArray(new Transformation[transforms.size()]));
    }

    public interface OnImageLoadedListener {
        void onLoadFinish(Bitmap bitmap);
    }

    public interface OnImageErrorLoadListener {
        void onLoadError(Throwable throwable);
    }
}