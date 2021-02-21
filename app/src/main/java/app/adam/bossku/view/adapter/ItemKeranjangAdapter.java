package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.helper.Route;
import app.adam.bossku.view.model.ItemKeranjang;
import app.adam.bossku.view.ui.KeranjangActivity;

public class ItemKeranjangAdapter extends RecyclerView.Adapter<ItemKeranjangAdapter.ItemKeranjangViewHolder>{
    private ArrayList<ItemKeranjang> list_item;
    private final ItemKeranjangListener listener;
    private final int position_keranjang;
    private Context context;

    public ItemKeranjangAdapter(Context context, int position_keranjang, ItemKeranjangListener listener) {
        this.context = context;
        this.position_keranjang = position_keranjang;
        this.listener = listener;
    }
    public ArrayList<ItemKeranjang> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<ItemKeranjang> listItem) {
        this.list_item = listItem;
    }

    public interface ItemKeranjangListener{
        void ChangeItem(ArrayList<ItemKeranjang> list_item,ItemKeranjang item);
        void SelectItemKeranjang(ItemKeranjang item);
        void PlusItemKeranjang(int position_keranjang, TextView txt_jumlah_beli, ItemKeranjang item);
        void MinusItemKeranjang(int position_keranjang, TextView txt_jumlah_beli, ItemKeranjang item);
        void RemoveItemKeranjang(int position_keranjang,int position_item_keranjang);
    }

    @NonNull
    @Override
    public ItemKeranjangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_keranjang_item, parent, false);
        return (new ItemKeranjangViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ItemKeranjangViewHolder holder, final int position) {
        final ItemKeranjang item = getListItem().get(position);

        holder.txt_nama_produk.setText(item.getNama_produk());
        holder.txt_harga.setText(String.format("Rp %s",item.getHarga_satuan()));
        holder.txt_jumlah.setText(String.valueOf(item.getJumlah_beli()));
        Glide.with(context)
                .load(Route.storage+item.getFoto_produk())
//                .placeholder(R.drawable.ic_noimage)
//                .error(R.drawable.ic_noimage)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .into(holder.img_produk);

        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.listener.SelectItemKeranjang(item);
            }
        });
        holder.btn_tambah.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //item.setJumlah_beli(item.getJumlah_beli()+1);
                holder.listener.PlusItemKeranjang(position_keranjang, holder.txt_jumlah, item);
            }
        });
        holder.btn_kurang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if( item.getJumlah_beli()-1 > 0 ) {
                    //item.setJumlah_beli(item.getJumlah_beli()-1);
                    holder.listener.MinusItemKeranjang(position_keranjang, holder.txt_jumlah, item);
                }
            }
        });
        holder.chk.setSelected(item.getSelected());
        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                item.setSelected(b);
                if(b) {
                    listener.ChangeItem(getListItem(),null);
                }
                else{
                    listener.ChangeItem(getListItem(),item);
                }
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.listener.RemoveItemKeranjang(position_keranjang,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class ItemKeranjangViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_produk,txt_harga,txt_jumlah;
        ImageView img_produk,btn_delete;
        MaterialButton btn_tambah,btn_kurang;
        CheckBox chk;
        CardView items;
        ItemKeranjangListener listener;

        ItemKeranjangViewHolder(View itemView, ItemKeranjangListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_keranjang_item);
            chk = itemView.findViewById(R.id.chk_item_row_keranjang_item);
            txt_nama_produk = itemView.findViewById(R.id.txt_nama_produk_item_row_keranjang_item);
            txt_harga = itemView.findViewById(R.id.txt_harga_satuan_item_row_keranjang_item);
            txt_jumlah = itemView.findViewById(R.id.txt_jumlah_beli_item_row_keranjang_item);
            img_produk = itemView.findViewById(R.id.img_produk_item_row_keranjang_item);
            btn_delete = itemView.findViewById(R.id.btn_hapus_item_row_keranjang_item);
            btn_tambah = itemView.findViewById(R.id.btn_tambah_item_row_keranjang_item);
            btn_kurang = itemView.findViewById(R.id.btn_kurang_item_row_keranjang_item);
        }
    }

}

