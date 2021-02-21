package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.helper.Route;
import app.adam.bossku.view.model.Kategori;

public class ProdukKategoriAdapter extends RecyclerView.Adapter<ProdukKategoriAdapter.ProdukKategoriViewHolder>{
    private ArrayList<Kategori> list_item;
    private final ProductCategoryListener listener;
    private Context context;

    public ProdukKategoriAdapter(Context context, ProductCategoryListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<Kategori> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Kategori> listItem) {
        this.list_item = listItem;
    }

    public interface ProductCategoryListener{
        void SelectItemProducCategory(Kategori item);
    }

    @NonNull
    @Override
    public ProdukKategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_produk_category_main, parent, false);
        return (new ProdukKategoriViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ProdukKategoriViewHolder holder, final int position) {
        final Kategori item = getListItem().get(position);

        Glide.with(context)
                .load(Route.storage+item.getUrl())
//                .placeholder(R.drawable.ic_noimage)
//                .error(R.drawable.ic_noimage)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .into(holder.img_kategori);
        holder.txt_kategori.setText(item.getTitle());

        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.SelectItemProducCategory(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class ProdukKategoriViewHolder extends RecyclerView.ViewHolder{
        TextView txt_kategori;
        ImageView img_kategori;
        CardView items;
        ProductCategoryListener listener;

        ProdukKategoriViewHolder(View itemView, ProductCategoryListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_list_produk_category_main);
            img_kategori = itemView.findViewById(R.id.img_item_list_produk_category_main);
            txt_kategori = itemView.findViewById(R.id.txt_item_list_produk_category_main);
        }
    }

}

