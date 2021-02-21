package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.ProductToko;

public class ProdukTokoAdapter extends RecyclerView.Adapter<ProdukTokoAdapter.ProdukTokoViewHolder>{
    private ArrayList<ProductToko> list_item;
    private final ProductTokoListener listener;
    private Context context;

    public ProdukTokoAdapter(Context context, ProductTokoListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<ProductToko> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<ProductToko> listItem) {
        this.list_item = listItem;
    }

    public interface ProductTokoListener{
        void SelectItemProduc(ProductToko item);
        void DeleteItemProduc(ProductToko item);
        void EditItemProduc(ProductToko item);
    }

    @NonNull
    @Override
    public ProdukTokoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_produk_toko_saya, parent, false);
        return (new ProdukTokoViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ProdukTokoViewHolder holder, final int position) {
        final ProductToko item = getListItem().get(position);

        String url = item.getGambar_produk();
        holder.txt_nama_produk.setText(item.getNama_produk());
        if(item.getDiskon_produk()>0) {
            holder.txt_diskon_produk.setText(Util.rupiahFormat(item.getDiskon_produk()));

            String harga_lama = Util.rupiahFormat(item.getHarga_produk());
            String harga_baru = Util.rupiahFormat( (item.getHarga_produk()-item.getDiskon_produk()) );
            String harga = harga_baru+" "+harga_lama;

            SpannableString span = new SpannableString(harga);
            ForegroundColorSpan txtColor = new ForegroundColorSpan(Color.parseColor("#FF9E9D9D"));
            StrikethroughSpan strike = new StrikethroughSpan();

            span.setSpan(strike, harga.indexOf(harga_lama), harga.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(txtColor, harga.indexOf(harga_lama), harga.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.txt_harga_produk.setText(span);
        }
        else{
            holder.txt_harga_produk.setText(Util.rupiahFormat(item.getHarga_produk()));
            holder.txt_diskon_produk.setVisibility(View.INVISIBLE);
            holder.icon_discount.setVisibility(View.INVISIBLE);
        }
        //holder.img_produk.setImageBitmap();
        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.SelectItemProduc(item);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.DeleteItemProduc(item);
            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.EditItemProduc(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class ProdukTokoViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_produk,txt_harga_produk,txt_diskon_produk;
        ImageView img_produk,icon_discount;
        ImageButton btn_edit,btn_delete;
        CardView items;
        ProductTokoListener listener;

        ProdukTokoViewHolder(View itemView, ProductTokoListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_list_produk_toko_saya);
            txt_nama_produk = itemView.findViewById(R.id.txt_nama_produk_item_list_produk_toko_saya);
            txt_harga_produk = itemView.findViewById(R.id.txt_harga_produk_item_list_produk_toko_saya);
            txt_diskon_produk = itemView.findViewById(R.id.txt_diskon_produk_item_list_produk_toko_saya);
            img_produk = itemView.findViewById(R.id.img_produk_item_list_produk_toko_saya);
            icon_discount = itemView.findViewById(R.id.img_icon_diskon_produk_item_list_produk_toko_saya);
            btn_delete = itemView.findViewById(R.id.btn_hapus_item_list_produk_toko_saya);
            btn_edit = itemView.findViewById(R.id.btn_edit_item_list_produk_toko_saya);
        }
    }

}

