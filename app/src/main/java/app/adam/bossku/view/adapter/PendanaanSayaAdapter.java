package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.helper.StatusFunding;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.Pendanaan;

public class PendanaanSayaAdapter extends RecyclerView.Adapter<PendanaanSayaAdapter.PendanaanSayaViewHolder>{
    private ArrayList<Pendanaan> list_item;
    private final PendanaanSayaListener listener;
    private final Context context;

    public PendanaanSayaAdapter(Context context, PendanaanSayaListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<Pendanaan> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Pendanaan> listItem) {
        this.list_item = listItem;
    }

    public void clear(){
        this.list_item.clear();
        notifyDataSetChanged();
    }

    public interface PendanaanSayaListener{
        void SelectItemCancelPendanaanSaya(Pendanaan item);
        void SelectItemBillPendanaanSaya(Pendanaan item);
        void SelectItemPendanaanSaya(Pendanaan item);
    }

    @NonNull
    @Override
    public PendanaanSayaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pendanaan_saya, parent, false);
        return (new PendanaanSayaViewHolder(view,listener));
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull final PendanaanSayaViewHolder holder, final int position) {
        final Pendanaan item = getListItem().get(position);

        holder.txt_status.setText(StatusFunding.getStatus(item.getTransaction_status()));
        holder.txt_nomor_transaksi.setText(item.getTransaction_code());
        holder.txt_nama_brand.setText(item.getNama_brand());
        holder.txt_jumlah_donasi.setText(Util.rupiahFormat(item.getJumlah_donasi()));
        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.listener.SelectItemPendanaanSaya(item);
            }
        });
        holder.btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.listener.SelectItemCancelPendanaanSaya(item);
            }
        });
        holder.btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.listener.SelectItemBillPendanaanSaya(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (getListItem()!=null? getListItem().size():0);
    }

    static class PendanaanSayaViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_brand,txt_jumlah_donasi,txt_nomor_transaksi,txt_status;
        ImageButton btn_batal,btn_bayar;
        CardView items;
        PendanaanSayaListener listener;

        PendanaanSayaViewHolder(View itemView, PendanaanSayaListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_pendanaan_saya);
            txt_nama_brand = itemView.findViewById(R.id.txt_nama_brand_item_row_pendanaan_saya);
            txt_jumlah_donasi = itemView.findViewById(R.id.txt_jumlah_donasi_item_row_pendanaan_saya);
            txt_nomor_transaksi = itemView.findViewById(R.id.txt_nomor_transaksi_item_row_pendanaan_saya);
            txt_status = itemView.findViewById(R.id.txt_status_item_row_pendanaan_saya);
            btn_batal = itemView.findViewById(R.id.btn_batal_item_row_pendanaan_saya);
            btn_bayar = itemView.findViewById(R.id.btn_bayar_item_row_pendanaan_saya);
        }
    }

}

