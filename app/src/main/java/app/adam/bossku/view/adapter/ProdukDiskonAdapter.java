package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
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
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.ProductDiskon;

public class ProdukDiskonAdapter extends RecyclerView.Adapter<ProdukDiskonAdapter.ProdukDiskonViewHolder>{
    private ArrayList<ProductDiskon> list_item;
    private ProductDiscountListener listener;
    private Context context;

    public ProdukDiskonAdapter(Context context, ProductDiscountListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<ProductDiskon> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<ProductDiskon> listItem) {
        this.list_item = listItem;
    }

    public interface ProductDiscountListener{
        void SelectItemProducDiscount(ProductDiskon item);
    }

    @NonNull
    @Override
    public ProdukDiskonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_produk_diskon_main, parent, false);
        return (new ProdukDiskonViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ProdukDiskonViewHolder holder, final int position) {
        final ProductDiskon item = getListItem().get(position);

        String url = item.getGambar_produk();
        holder.txt_nama_produk.setText(item.getNama_produk());

        if(item.getDiskon_produk()>0){
            String harga_lama = Util.rupiahFormat(item.getHarga_produk());
            String harga_baru = Util.rupiahFormat((item.getHarga_produk() - item.getDiskon_produk()));
            String harga = harga_baru + " " + harga_lama;

            SpannableString span = new SpannableString(harga);
            ForegroundColorSpan txtColor = new ForegroundColorSpan(Color.parseColor("#FF9E9D9D"));
            StrikethroughSpan strike = new StrikethroughSpan();

            span.setSpan(strike, harga.indexOf(harga_lama), harga.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(txtColor, harga.indexOf(harga_lama), harga.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.txt_harga_produk.setText(span);
        }
        else{
            holder.txt_harga_produk.setText(Util.rupiahFormat(item.getHarga_produk()));
        }

        holder.txt_diskon_produk.setText(Util.rupiahFormat(item.getDiskon_produk()));
        holder.txt_nama_toko.setText(item.getNama_toko());
        //holder.img_produk.setImageBitmap();
        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.SelectItemProducDiscount(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class ProdukDiskonViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_produk,txt_harga_produk,txt_diskon_produk,txt_nama_toko;
        ImageView img_produk;
        CardView items;
        ProductDiscountListener listener;

        ProdukDiskonViewHolder(View itemView, ProductDiscountListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_list_produk_diskon_main);
            txt_nama_produk = itemView.findViewById(R.id.txt_nama_produk_item_list_produk_diskon_main);
            txt_harga_produk = itemView.findViewById(R.id.txt_harga_produk_item_list_produk_diskon_main);
            txt_diskon_produk = itemView.findViewById(R.id.txt_diskon_produk_item_list_produk_diskon_main);
            txt_nama_toko = itemView.findViewById(R.id.txt_nama_toko_item_list_produk_diskon_main);
            img_produk = itemView.findViewById(R.id.img_produk_item_list_produk_diskon_main);
        }
    }

}

