package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.helper.Route;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.Pendanaan;

public class PendanaanAdapter extends RecyclerView.Adapter<PendanaanAdapter.PendanaanViewHolder>{
    private ArrayList<Pendanaan> list_item;
    private final PendanaanListener listener;
    private final Context context;
    boolean isInvestor=true;

    public PendanaanAdapter(Context context, boolean isInvestor, PendanaanListener listener) {
        this.context = context;
        this.listener = listener;
        this.isInvestor = isInvestor;
    }
    public ArrayList<Pendanaan> getListItem() {
        return list_item;
    }

    public void clear(){
        this.list_item.clear();
        notifyDataSetChanged();
    }

    public void setListItem(ArrayList<Pendanaan> listItem) {
        this.list_item = listItem;
    }

    public interface PendanaanListener{
        void SelectItemPendanaan(Pendanaan item);
    }

    @NonNull
    @Override
    public PendanaanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pendanaan, parent, false);
        return (new PendanaanViewHolder(view,listener));
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull final PendanaanViewHolder holder, final int position) {
        final Pendanaan item = getListItem().get(position);

        if(isInvestor) {
            String url = item.getUrl();
            Glide.with(context)
                    .load(Route.storage + url)
//                    .placeholder(R.drawable.ic_noimage)
//                    .error(R.drawable.ic_noimage)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(holder.img_foto);

            holder.txt_nama_brand.setText(item.getNama_brand());
            holder.txt_nama_umkm.setText(item.getNama_umkm());
            holder.txt_harga_satuan.setText(Util.rupiahFormat(item.getHarga_satuan()));
            holder.txt_sisa_waktu.setText(String.format("Sisa Waktu %s Hari", item.getSisa_waktu()));
            holder.txt_total_pendanaan.setText(Util.rupiahFormat(item.getTotal_pendanaan()));
            holder.txt_total_dividen.setText(String.format("%s Bulan", item.getDividen()));
            holder.txt_jumlah_pendanaa.setText(String.format("%.3f", item.getProgress()) + "%");
            holder.pb_jumlah_pendanaa.setProgress((int) item.getProgress());

            holder.items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.SelectItemPendanaan(item);
                }
            });
        }
        else{
            String url = item.getUrl();
            Glide.with(context)
                    .load(Route.storage + url)
//                    .placeholder(R.drawable.ic_noimage)
//                    .error(R.drawable.ic_noimage)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(holder.img_foto);

            holder.txt_nama_brand_ex.setText(item.getNama_brand());
            holder.txt_nama_umkm_ex.setText(item.getNama_umkm());
            holder.txt_total_pendanaan_ex.setText(Util.rupiahFormat(item.getTotal_pendanaan()));
            holder.txt_total_terkumpul_ex.setText(Util.rupiahFormat(item.getTotal_terkumpul()));

            holder.items_ex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.SelectItemPendanaan(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (getListItem()!=null? getListItem().size():0);
    }

    static class PendanaanViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_brand,txt_nama_umkm,txt_harga_satuan,txt_sisa_waktu,txt_total_pendanaan,txt_total_dividen,txt_jumlah_pendanaa;
        TextView txt_nama_brand_ex,txt_nama_umkm_ex,txt_total_pendanaan_ex,txt_total_terkumpul_ex;
        ProgressBar pb_jumlah_pendanaa;
        ImageView img_foto;
        CardView items,items_ex;
        PendanaanListener listener;

        PendanaanViewHolder(View itemView, PendanaanListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_pendanaan);
            items_ex = itemView.findViewById(R.id.item_row_pendanaan_ex);
            txt_nama_brand = itemView.findViewById(R.id.txt_nama_brand_item_row_pendanaan);
            txt_nama_brand_ex = itemView.findViewById(R.id.txt_nama_brand_item_row_pendanaan_ex);
            txt_nama_umkm = itemView.findViewById(R.id.txt_nama_umkm_item_row_pendanaan);
            txt_nama_umkm_ex = itemView.findViewById(R.id.txt_nama_umkm_item_row_pendanaan_ex);
            txt_harga_satuan = itemView.findViewById(R.id.txt_harga_satuan_item_row_pendanaan);
            txt_sisa_waktu = itemView.findViewById(R.id.txt_sisa_waktu_item_row_pendanaan);
            txt_total_pendanaan = itemView.findViewById(R.id.txt_total_pendanaan_item_row_pendanaan);
            txt_total_pendanaan_ex = itemView.findViewById(R.id.txt_total_pendanaan_item_row_pendanaan_ex);
            txt_total_dividen = itemView.findViewById(R.id.txt_total_dividen_item_row_pendanaan);
            txt_jumlah_pendanaa = itemView.findViewById(R.id.txt_jumlah_pendanaa_item_row_pendanaan);
            txt_total_terkumpul_ex = itemView.findViewById(R.id.txt_total_terkumpul_item_row_pendanaan_ex);
            pb_jumlah_pendanaa = itemView.findViewById(R.id.pb_jumlah_pendanaa_item_row_pendanaan);
            img_foto = itemView.findViewById(R.id.img_foto_item_row_pendanaan);

        }
    }

}

