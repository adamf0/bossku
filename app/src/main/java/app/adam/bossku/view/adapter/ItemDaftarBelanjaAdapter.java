package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.ItemKeranjang;
import app.adam.bossku.view.model.Keranjang;
import app.adam.bossku.view.model.Kurir;

public class ItemDaftarBelanjaAdapter extends RecyclerView.Adapter<ItemDaftarBelanjaAdapter.ItemDaftarBelanjaViewHolder>{
    private ArrayList<ItemKeranjang> list_item;
    private final ItemDaftarBelanjaListener listener;
    private final int position_daftar_belanja;
    private Context context;

    public ItemDaftarBelanjaAdapter(Context context, int position_daftar_belanja, ItemDaftarBelanjaListener listener) {
        this.context = context;
        this.position_daftar_belanja = position_daftar_belanja;
        this.listener = listener;
    }
    public ArrayList<ItemKeranjang> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<ItemKeranjang> listItem) {
        this.list_item = listItem;
    }

    public interface ItemDaftarBelanjaListener{
        void PlusItemKeranjang(int position_daftar_belanja, int position_daftar_item,TextView txt_jumlah_beli,ItemKeranjang item,TextView txt_berat_beli);
        void MinusItemKeranjang(int position_daftar_belanja, int position_daftar_item,TextView txt_jumlah_beli, ItemKeranjang item, TextView txt_berat_beli);
        void SelectKurir(ItemKeranjang item);
    }

    @NonNull
    @Override
    public ItemDaftarBelanjaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_daftar_belanja_item, parent, false);
        return (new ItemDaftarBelanjaViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ItemDaftarBelanjaViewHolder holder, final int position) {
        final ItemKeranjang item = getListItem().get(position);

        String url = item.getFoto_produk();
        holder.txt_nama_produk.setText(item.getNama_produk());
        holder.txt_harga.setText(String.format("Rp %s",item.getHarga_satuan()));
        holder.txt_jumlah.setText(String.valueOf(item.getJumlah_beli()));
        holder.txt_berat.setText(item.getJumlah_beli()*item.getBerat()+" Gram");

        //holder.img_produk.setImageBitmap();
        holder.btn_tambah.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //item.setJumlah_beli(item.getJumlah_beli()+1);
                holder.listener.PlusItemKeranjang(position_daftar_belanja,position,holder.txt_jumlah,item,holder.txt_berat);
            }
        });
        holder.btn_kurang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if( item.getJumlah_beli()-1 > 0 ) {
                    //item.setJumlah_beli(item.getJumlah_beli()-1);
                    holder.listener.MinusItemKeranjang(position_daftar_belanja,position,holder.txt_jumlah,item,holder.txt_berat);
                }
            }
        });
        if(item.getKurir()!=null){
            holder.txt_kurir.setText(
                    String.format("%s - %s\n%s (%s)" ,
                            item.getKurir().getName(),
                            item.getKurir().getService(),
                            Util.rupiahFormat(item.getKurir().getPrice()),
                            item.getKurir().getEstimation()
                    )
            );
            holder.img_kurir.setImageResource(R.drawable.ic_check);

            if(position>0){
                holder.img_kurir.setVisibility(View.GONE);
                holder.lb_kurir.setVisibility(View.GONE);
                holder.txt_kurir.setVisibility(View.GONE);
                holder.btn_kurir.setVisibility(View.GONE);
            }
            else{
                holder.img_kurir.setVisibility(View.VISIBLE);
                holder.lb_kurir.setVisibility(View.VISIBLE);
                holder.txt_kurir.setVisibility(View.VISIBLE);
                holder.btn_kurir.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.txt_kurir.setText("Pilih metode pengiriman");
            holder.img_kurir.setImageResource(R.drawable.ic_remove);
        }

        holder.btn_kurir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                holder.listener.SelectKurir(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class ItemDaftarBelanjaViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_produk,txt_harga,txt_jumlah,lb_kurir,txt_kurir,txt_berat;
        ImageView img_produk,img_kurir;
        ImageButton btn_kurir;
        MaterialButton btn_tambah,btn_kurang;
        ItemDaftarBelanjaListener listener;

        ItemDaftarBelanjaViewHolder(View itemView, ItemDaftarBelanjaListener listener) {
            super(itemView);
            this.listener = listener;
            txt_nama_produk = itemView.findViewById(R.id.txt_nama_produk_item_row_daftar_belanja_item);
            txt_harga = itemView.findViewById(R.id.txt_harga_satuan_item_row_daftar_belanja_item);
            txt_jumlah = itemView.findViewById(R.id.txt_jumlah_beli_item_row_daftar_belanja_item);
            txt_berat = itemView.findViewById(R.id.txt_berat_total_item_row_daftar_belanja_item);
            img_produk = itemView.findViewById(R.id.img_produk_item_row_daftar_belanja_item);
            btn_tambah = itemView.findViewById(R.id.btn_tambah_item_row_daftar_belanja_item);
            btn_kurang = itemView.findViewById(R.id.btn_kurang_item_row_daftar_belanja_item);
            lb_kurir = itemView.findViewById(R.id.lb_kurir_item_row_daftar_belanja_item);
            txt_kurir = itemView.findViewById(R.id.txt_kurir_item_row_daftar_belanja_item);
            img_kurir = itemView.findViewById(R.id.img_kurir_item_row_daftar_belanja_item);
            btn_kurir = itemView.findViewById(R.id.btn_kurir_item_row_daftar_belanja_item);
        }
    }

}

