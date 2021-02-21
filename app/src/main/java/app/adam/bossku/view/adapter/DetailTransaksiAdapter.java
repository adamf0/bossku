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
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.PesanBarang;

public class DetailTransaksiAdapter extends RecyclerView.Adapter<DetailTransaksiAdapter.DetailTransaksiViewHolder>{
    private ArrayList<PesanBarang> list_item;
    private Context context;
    boolean isDefault=true;

    public DetailTransaksiAdapter(Context context, boolean isDefault) {
        this.context = context;
        this.isDefault = isDefault;
    }
    public ArrayList<PesanBarang> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<PesanBarang> listItem) {
        this.list_item = listItem;
    }

    @NonNull
    @Override
    public DetailTransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_detail_transaksi, parent, false);
        return new DetailTransaksiViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final DetailTransaksiViewHolder holder, final int position) {
        final PesanBarang item = getListItem().get(position);

        holder.txt_nama_produk.setText(item.getNama_produk());
        holder.txt_jumlah_beli.setText(String.format("%s x %s",item.getJml_beli(), Util.rupiahFormat(item.getHarga_satuan())));
        holder.txt_total_harga.setText(Util.rupiahFormat(item.getTotal_harga()));
        if(isDefault){
           holder.txt_catatan.setVisibility(View.GONE);
           holder.lb_refund.setVisibility(View.GONE);
        }
        else{
            holder.txt_catatan.setVisibility(View.VISIBLE);
            holder.txt_catatan.setText("Catatan: "+item.getCatatan());

            if(item.getRefund()!=null)
                holder.lb_refund.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class DetailTransaksiViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_produk,txt_total_harga,txt_jumlah_beli,txt_catatan,lb_refund;

        DetailTransaksiViewHolder(View itemView) {
            super(itemView);

            txt_nama_produk = itemView.findViewById(R.id.txt_nama_produk_item_row_detail_transaksi);
            txt_total_harga = itemView.findViewById(R.id.txt_total_harga_item_row_detail_transaksi);
            txt_jumlah_beli = itemView.findViewById(R.id.txt_jumlah_beli_item_row_detail_transaksi);
            txt_catatan = itemView.findViewById(R.id.txt_catatan_item_row_detail_transaksi);
            lb_refund = itemView.findViewById(R.id.lb_refund_item_row_detail_transaksi);
        }
    }

}

