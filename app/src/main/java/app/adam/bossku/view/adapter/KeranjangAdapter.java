package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.view.model.Keranjang;

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.KeranjangViewHolder>{
    private ArrayList<Keranjang> list_item;
    private final ItemKeranjangAdapter.ItemKeranjangListener listener_itemKeranjang;
    private Context context;

    public KeranjangAdapter(Context context, ItemKeranjangAdapter.ItemKeranjangListener listener_itemKeranjang) {
        this.context = context;
        this.listener_itemKeranjang = listener_itemKeranjang;
    }
    public ArrayList<Keranjang> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Keranjang> listItem) {
        this.list_item = listItem;
    }

    @NonNull
    @Override
    public KeranjangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_keranjang, parent, false);
        return (new KeranjangViewHolder(view,listener_itemKeranjang));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final KeranjangViewHolder holder, final int position) {
        final Keranjang item = getListItem().get(position);

        holder.txt_nama_toko.setText(item.getNama_toko());
        if(item.getItemKeranjang().size()>0){
            holder.list_produk.setHasFixedSize(true);
            holder.list_produk.setLayoutManager(new LinearLayoutManager(context));

            ItemKeranjangAdapter cardAdapter = new ItemKeranjangAdapter(context,position,holder.listener_itemKeranjang);
            cardAdapter.setListItem(item.getItemKeranjang());
            cardAdapter.notifyDataSetChanged();
            holder.list_produk.setAdapter(cardAdapter);
        }
        //holder.img_produk.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class KeranjangViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_toko;
        RecyclerView list_produk;
        CardView items;
        ItemKeranjangAdapter.ItemKeranjangListener listener_itemKeranjang;

        KeranjangViewHolder(View itemView, ItemKeranjangAdapter.ItemKeranjangListener listener_itemKeranjang) {
            super(itemView);
            this.listener_itemKeranjang = listener_itemKeranjang;

            items = itemView.findViewById(R.id.item_row_keranjang);
            txt_nama_toko = itemView.findViewById(R.id.txt_nama_toko_item_row_keranjang);
            list_produk = itemView.findViewById(R.id.rv_list_item_row_keranjang);

        }
    }

}

