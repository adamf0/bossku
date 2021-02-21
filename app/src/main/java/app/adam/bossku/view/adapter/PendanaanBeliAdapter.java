package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.PendanaanBeli;
import app.adam.bossku.view.model.PendanaanJual;

public class PendanaanBeliAdapter extends RecyclerView.Adapter<PendanaanBeliAdapter.PendanaanBeliViewHolder>{
    private ArrayList<PendanaanBeli> list_item;
    private PendanaanBeliListener listener;
    private Context context;

    public PendanaanBeliAdapter(Context context, PendanaanBeliListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<PendanaanBeli> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<PendanaanBeli> listItem) {
        this.list_item = listItem;
    }

    public interface PendanaanBeliListener{
        void SelectItemPendanaanBeli(PendanaanBeli item);
    }

    @NonNull
    @Override
    public PendanaanBeliViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pendanaan_beli, parent, false);
        return new PendanaanBeliViewHolder(view,listener);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull final PendanaanBeliViewHolder holder, final int position) {
        final PendanaanBeli item = getListItem().get(position);

        String url = item.getUrl();
        //holder.img_foto.setBackground();
        holder.txt_nama_brand.setText(item.getNama_brand());
        holder.txt_nama_umkm.setText(item.getNama_umkm());

        holder.txt_total_persen.setText(item.getTerbeli_persen());
        holder.txt_total_lembar.setText(item.getTerbeli_lembar().intValue()+" Lembar");
        holder.txt_total_rupiah.setText(Util.rupiahFormat(item.getTerbeli_rupiah()));
        holder.btn_lihat.setVisibility(View.GONE);
        if(item.getStatus()==-1) {
            holder.txt_status.setText("Dibatalkan");
        }
        else if(item.getStatus()==0) {
            holder.txt_status.setText("Menunggu Diterima");
        }
        if(item.getStatus()==1) {
            holder.txt_status.setText("Telah Terima");
            holder.btn_lihat.setVisibility(View.VISIBLE);
        }

        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.SelectItemPendanaanBeli(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class PendanaanBeliViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_brand,txt_nama_umkm,txt_total_persen,txt_total_lembar,txt_total_rupiah,txt_status;
        Button btn_lihat;
        ImageView img_foto;
        CardView items;
        PendanaanBeliListener listener;

        PendanaanBeliViewHolder(View itemView,PendanaanBeliListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_pendanaan_beli);
            img_foto = itemView.findViewById(R.id.img_foto_item_row_pendanaan_beli);
            txt_nama_brand = itemView.findViewById(R.id.txt_nama_brand_item_row_pendanaan_beli);
            txt_nama_umkm = itemView.findViewById(R.id.txt_nama_umkm_item_row_pendanaan_beli);

            txt_total_persen = itemView.findViewById(R.id.txt_total_beli_persen_item_row_pendanaan_beli);
            txt_total_lembar = itemView.findViewById(R.id.txt_total_beli_lembar_item_row_pendanaan_beli);
            txt_total_rupiah = itemView.findViewById(R.id.txt_total_beli_item_row_pendanaan_beli);
            txt_status = itemView.findViewById(R.id.txt_status_item_row_pendanaan_beli);
            btn_lihat = itemView.findViewById(R.id.btn_lihat_pembagian_untung_item_row_pendanaan_beli);
        }
    }

}

