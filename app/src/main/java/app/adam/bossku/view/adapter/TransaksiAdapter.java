package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
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
import app.adam.bossku.view.model.Transaksi;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>{
    private ArrayList<Transaksi> list_item;
    private final TransaksiListener listener;
    private final Context context;
    boolean isDefault;

    public TransaksiAdapter(Context context, boolean isDefault, TransaksiListener listener) {
        this.context = context;
        this.listener = listener;
        this.isDefault = isDefault;
    }
    public ArrayList<Transaksi> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Transaksi> listItem) {
        this.list_item = listItem;
    }

    public interface TransaksiListener{
        void SelectItemTransaksi(Transaksi item);
    }

    @NonNull
    @Override
    public TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_transaksi, parent, false);
        return (new TransaksiViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final TransaksiViewHolder holder, final int position) {
        final Transaksi item = getListItem().get(position);

        if(isDefault) {
            holder.items.setVisibility(View.VISIBLE);
            holder.items_ex.setVisibility(View.GONE);

            //String url = item.getGambar_produk();
            holder.txt_nomor_transaksi.setText(item.getNomor_transaksi());
            holder.txt_tanggal_pesan.setText(item.getTanggal_pesan());
            if (item.getStatus() == -1) {
                holder.txt_status.setText("dibatalkan");
            } else if (item.getStatus() == 0) {
                holder.txt_status.setText("proses pengiriman");
            } else {
                holder.txt_status.setText("telah terima");
            }
            holder.txt_nama_toko.setText(item.getNama_toko());
            holder.txt_total_tagihan.setText(String.format("Rp %s", item.getTotal_tagihan()));
            //holder.img_produk.setImageBitmap();
            holder.items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.listener.SelectItemTransaksi(item);
                }
            });
        }
        else{
            holder.items.setVisibility(View.GONE);
            holder.items_ex.setVisibility(View.VISIBLE);

            holder.txt_nomor_transaksi_ex.setText(item.getNomor_transaksi());
            holder.txt_nama_produk_ex.setText(TextUtils.join(",",item.getListNameProduct()));
            Glide.with(context)
                    .load(Route.storage + item.getGambar_produk())
//                    .placeholder(R.drawable.ic_noimage)
//                    .error(R.drawable.ic_noimage)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(holder.img_produk_ex);
            holder.items_ex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.listener.SelectItemTransaksi(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class TransaksiViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nomor_transaksi,txt_tanggal_pesan,txt_status,txt_nama_toko,txt_total_tagihan,txt_nomor_transaksi_ex,txt_nama_produk_ex;
        ImageView img_produk,img_produk_ex;
        CardView items,items_ex;
        TransaksiListener listener;

        TransaksiViewHolder(View itemView, TransaksiListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_transaksi);
            txt_nomor_transaksi = itemView.findViewById(R.id.txt_nomor_transaksi_item_row_transaksi);
            txt_tanggal_pesan = itemView.findViewById(R.id.txt_tanggal_item_row_transaksi);
            txt_status = itemView.findViewById(R.id.txt_status_item_row_transaksi);
            txt_nama_toko = itemView.findViewById(R.id.txt_nama_toko_item_row_transaksi);
            txt_total_tagihan = itemView.findViewById(R.id.txt_total_tagihan_item_row_transaksi);
            img_produk = itemView.findViewById(R.id.img_produk_item_row_transaksi);

            items_ex = itemView.findViewById(R.id.item_row_transaksi_ex);
            txt_nomor_transaksi_ex = itemView.findViewById(R.id.txt_nomor_transaksi_ex_item_row_transaksi);
            txt_nama_produk_ex = itemView.findViewById(R.id.txt_nama_produk_ex_item_row_transaksi);
            img_produk_ex = itemView.findViewById(R.id.img_produk_ex_item_row_transaksi);

        }
    }

}

