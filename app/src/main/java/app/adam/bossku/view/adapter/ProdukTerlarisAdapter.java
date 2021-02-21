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

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.view.model.ProductTerlaris;

public class ProdukTerlarisAdapter extends RecyclerView.Adapter<ProdukTerlarisAdapter.ProdukTerlViewHolder>{
    private ArrayList<ProductTerlaris> list_item;
    private PopularProductListener listener;
    private Context context;

    public ProdukTerlarisAdapter(Context context, PopularProductListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<ProductTerlaris> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<ProductTerlaris> listItem) {
        this.list_item = listItem;
    }

    public interface PopularProductListener{
        void SelectItemPupularProduct(ProductTerlaris item);
    }

    @NonNull
    @Override
    public ProdukTerlViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_produk_terlaris_main, parent, false);
        return (new ProdukTerlViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ProdukTerlViewHolder holder, final int position) {
        final ProductTerlaris item = getListItem().get(position);

        String url = item.getGambar_produk();
        holder.txt_nama_produk.setText(item.getNama_produk());
        holder.txt_harga_produk.setText(String.format("Rp %s",item.getHarga_produk()));
        holder.txt_nama_toko.setText(item.getNama_toko());
        //holder.img_produk.setImageBitmap();
        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.SelectItemPupularProduct(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class ProdukTerlViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_produk,txt_harga_produk,txt_nama_toko;
        ImageView img_produk;
        CardView items;
        PopularProductListener listener;

        ProdukTerlViewHolder(View itemView, PopularProductListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_list_produk_terlaris_main);
            txt_nama_produk = itemView.findViewById(R.id.txt_nama_produk_item_list_produk_terlaris_main);
            txt_harga_produk = itemView.findViewById(R.id.txt_harga_produk_item_list_produk_terlaris_main);
            txt_nama_toko = itemView.findViewById(R.id.txt_nama_toko_item_list_produk_terlaris_main);
            img_produk = itemView.findViewById(R.id.img_produk_item_list_produk_terlaris_main);
        }
    }

}

