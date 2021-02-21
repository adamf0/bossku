package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.helper.Route;


public class SlideGambarAdapter extends PagerAdapter {
    private final Context context;
    ArrayList<String> list_image;
    private final Integer[] images = {R.drawable.slide1,R.drawable.slide2};
    boolean isDefault;

    public SlideGambarAdapter(Context context,ArrayList<String> list_image,boolean isDefault) {
        this.context = context;
        this.list_image = list_image;
        this.isDefault = isDefault;
    }

    @Override
    public int getCount() {
        return (isDefault? images.length:list_image.size());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View view = layoutInflater.inflate(R.layout.item_slide_produk_detail_produk, null);
        ImageView imageView = view.findViewById(R.id.img_foto_produk_item_slide_produk_detail_produk);

        if(!isDefault) {
            Glide.with(context)
                    .load(Route.storage + list_image.get(position))
//                    .placeholder(R.drawable.ic_noimage)
//                    .error(R.drawable.ic_noimage)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(imageView);
        }
        else {
            imageView.setImageResource(images[position]);
        }
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}