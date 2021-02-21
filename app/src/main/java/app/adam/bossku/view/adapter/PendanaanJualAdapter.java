package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.PendanaanJual;

public class PendanaanJualAdapter extends RecyclerView.Adapter<PendanaanJualAdapter.PendanaanJualViewHolder>{
    private ArrayList<PendanaanJual> list_item;
    private PendanaanJualListener listener;
    private Context context;

    public PendanaanJualAdapter(Context context,PendanaanJualListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<PendanaanJual> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<PendanaanJual> listItem) {
        this.list_item = listItem;
    }

    public interface PendanaanJualListener{
        void SelectItemPendanaanJual(PendanaanJual item);
    }

    @NonNull
    @Override
    public PendanaanJualViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pendanaan_terjual, parent, false);
        return new PendanaanJualViewHolder(view,listener);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull final PendanaanJualViewHolder holder, final int position) {
        final PendanaanJual item = getListItem().get(position);

        String url = item.getUrl();
        //holder.img_foto.setBackground();
        holder.txt_nama_brand.setText(": "+item.getNama_brand());
        holder.txt_total_pendanaan.setText(": "+Util.rupiahFormat(item.getTotal_pendanaan()));
        holder.txt_total_lembar.setText(": "+item.getTotal_lembar()+" Lembar");
        holder.txt_sisa_lembar.setText(": "+item.getSisa_lembar()+" Lembar");
        holder.txt_harga_satuan.setText(": "+Util.rupiahFormat(item.getHarga_satuan()));
        holder.txt_peminat.setText(": "+item.getTotal_peminat()+" Investor");
        if(item.getStatus()==-1) {
            holder.txt_status.setText(": Dibatalkan");
        }
        else if(item.getStatus()==0) {
            holder.txt_status.setText(": Menunggu Pendanaan Selesai");
        }
        else{
            holder.txt_status.setText(": Pendanaan Telah Terima");
        }

        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.SelectItemPendanaanJual(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class PendanaanJualViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_brand,txt_total_pendanaan,txt_total_lembar,txt_sisa_lembar,txt_harga_satuan,txt_peminat,txt_status;
        ImageView img_foto;
        CardView items;
        PendanaanJualListener listener;

        PendanaanJualViewHolder(View itemView, PendanaanJualListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_pendanaan_terjual);
            img_foto = itemView.findViewById(R.id.img_foto_item_row_pendanaan_terjual);
            txt_nama_brand = itemView.findViewById(R.id.txt_nama_brand_item_row_pendanaan_terjual);
            txt_total_pendanaan = itemView.findViewById(R.id.txt_total_pendanaan_item_row_pendanaan_terjual);
            txt_total_lembar = itemView.findViewById(R.id.txt_total_lembar_item_row_pendanaan_terjual);
            txt_sisa_lembar = itemView.findViewById(R.id.txt_sisa_lembar_item_row_pendanaan_terjual);
            txt_harga_satuan = itemView.findViewById(R.id.txt_harga_lembar_item_row_pendanaan_terjual);
            txt_peminat = itemView.findViewById(R.id.txt_total_peminat_item_row_pendanaan_terjual);
            txt_status = itemView.findViewById(R.id.txt_status_item_row_pendanaan_terjual);
        }
    }

}

