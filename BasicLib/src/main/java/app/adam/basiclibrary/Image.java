package app.adam.basiclibrary;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class Image{
    private Bitmap image;
    private ImageView imageView;

    protected Activity act;

    public Image(Activity act) {
        this.act = act;
    }

    public Bitmap loadImageAsset(String file_name, String folder) {
        Bitmap bitmap=null;

        AssetManager asset = act.getAssets();
        try {
            InputStream is = asset.open(folder + file_name);
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), option);
            is.reset();

            option.inJustDecodeBounds = false;
            option.inPurgeable = true;
            bitmap = BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), option);
            this.image = bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        return bitmap;
    }
    public Bitmap loadImagePath(String path){
        Bitmap bitmap;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(path, bmOptions);
        this.image = bitmap;
        return bitmap;
    }
    public Bitmap rotateImage(float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        this.image = Bitmap.createBitmap(getImage(), 0, 0, getImage().getWidth(), getImage().getHeight(), matrix, true);
        return image;
    }
    public Bitmap resizeImage(int maxSize) {
        int w = image.getWidth();
        int h = image.getHeight();

        float bitmapRatio = (float) w / (float) h;
        if (bitmapRatio > 1) {
            w = maxSize;
            h = (int) (w / bitmapRatio);
        } else {
            h = maxSize;
            w = (int) (h * bitmapRatio);
        }
        this.image = Bitmap.createScaledBitmap(image, w, h, true);
        return image;
    }
    public Bitmap qualityImage(int quality) {
        Bitmap new_image;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        getImage().compress(Bitmap.CompressFormat.JPEG, quality, bytes);
        new_image = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        this.image = new_image;
        return new_image;
    }
    public String imageToStr(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] imageBytes = stream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public Bitmap getImage() {
        return image;
    }
    public void source(Bitmap image) {
        this.image = image;
    }
    private ImageView getImageView() {
        return imageView;
    }
    public void image(ImageView imageView) {
        this.imageView = imageView;
    }
}
