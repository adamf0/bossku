package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.view.model.PesanBarang;

public class PembayaranBarangAdapter extends RecyclerView.Adapter<PembayaranBarangAdapter.PembayaranBarangViewHolder>{
    private ArrayList<PesanBarang> list_item;
    private Context context;

    public PembayaranBarangAdapter(Context context) {
        this.context = context;
    }
    public ArrayList<PesanBarang> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<PesanBarang> listItem) {
        this.list_item = listItem;
    }

    @NonNull
    @Override
    public PembayaranBarangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pembayaran_barang, parent, false);
        return new PembayaranBarangViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PembayaranBarangViewHolder holder, final int position) {
        final PesanBarang item = getListItem().get(position);

        holder.txt_nama_produk.setText(item.getNama_produk());
        holder.txt_jumlah_beli.setText(String.format("%s x %s",item.getJml_beli(),item.getHarga_satuan()));
        holder.txt_total_harga.setText(String.format("Rp %s",item.getTotal_harga()));
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class PembayaranBarangViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_produk,txt_total_harga,txt_jumlah_beli;

        PembayaranBarangViewHolder(View itemView) {
            super(itemView);

            txt_nama_produk = itemView.findViewById(R.id.txt_nama_produk_item_row_pembayaran_barang);
            txt_total_harga = itemView.findViewById(R.id.txt_total_harga_item_row_pembayaran_barang);
            txt_jumlah_beli = itemView.findViewById(R.id.txt_jumlah_beli_item_row_pembayaran_barang);

        }
    }

}

