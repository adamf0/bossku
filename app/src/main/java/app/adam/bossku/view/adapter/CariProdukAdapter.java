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
import app.adam.bossku.view.model.Product;

public class CariProdukAdapter extends RecyclerView.Adapter<CariProdukAdapter.SearchProductViewHolder>{
    private ArrayList<Product> list_item;
    private ProductListener listener;
    private Context context;

    public CariProdukAdapter(Context context, ProductListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<Product> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Product> listItem) {
        this.list_item = listItem;
    }

    public interface ProductListener{
        void SelectItemProduct(Product item);
    }

    @NonNull
    @Override
    public SearchProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_cari_produk_main, parent, false);
        return (new SearchProductViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final SearchProductViewHolder holder, final int position) {
        final Product item = getListItem().get(position);

        String url = item.getGambar_produk();
        holder.txt_nama_produk.setText(item.getNama_produk());
        holder.txt_harga_produk.setText(String.format("Rp %s",item.getHarga_produk()));
        holder.txt_nama_toko.setText(item.getNama_toko());
        holder.txt_daerah_toko.setText(item.getDaerah_toko());
        Glide.with(context)
                .load(Route.storage + item.getGambar_produk())
//                .placeholder(R.drawable.ic_noimage)
//                .error(R.drawable.ic_noimage)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .into(holder.img_produk);

        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.SelectItemProduct(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class SearchProductViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_produk,txt_harga_produk,txt_nama_toko,txt_daerah_toko;
        ImageView img_produk;
        CardView items;
        ProductListener listener;

        SearchProductViewHolder(View itemView, ProductListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_list_cari_produk_main);
            txt_nama_produk = itemView.findViewById(R.id.txt_nama_produk_item_list_cari_produk_main);
            txt_harga_produk = itemView.findViewById(R.id.txt_harga_produk_item_list_cari_produk_main);
            txt_nama_toko = itemView.findViewById(R.id.txt_nama_toko_item_list_cari_produk_main);
            txt_daerah_toko = itemView.findViewById(R.id.txt_daerah_toko_item_list_cari_produk_main);
            img_produk = itemView.findViewById(R.id.img_produk_item_list_cari_produk_main);
        }
    }

}

