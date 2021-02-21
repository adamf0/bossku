package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.view.model.Pengaturan;

public class PengaturanAdapter extends RecyclerView.Adapter<PengaturanAdapter.PengaturanViewHolder>{
    private ArrayList<Pengaturan> list_item;
    private PengaturanListener listener;
    private Context context;

    public PengaturanAdapter(Context context, PengaturanListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<Pengaturan> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Pengaturan> listItem) {
        this.list_item = listItem;
    }

    public interface PengaturanListener{
        void SelectPengaturan(Pengaturan item);
    }

    @NonNull
    @Override
    public PengaturanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pengaturan, parent, false);
        return (new PengaturanViewHolder(view,listener));
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull final PengaturanViewHolder holder, final int position) {
        final Pengaturan item = getListItem().get(position);

        holder.icon.setBackground(item.getIcon());
        holder.judul.setText(item.getJudul());
        holder.deskripsi.setText(item.getDeskripsi());
        if(position==getItemCount()){
            holder.divider.setVisibility(View.INVISIBLE);
        }
        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.listener.SelectPengaturan(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class PengaturanViewHolder extends RecyclerView.ViewHolder{
        TextView judul,deskripsi;
        ImageView icon;
        LinearLayout divider;
        ConstraintLayout items;
        PengaturanListener listener;

        PengaturanViewHolder(View itemView, PengaturanListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_pengaturan);
            divider = itemView.findViewById(R.id.ll_item_row_pengaturan);
            icon = itemView.findViewById(R.id.img_icon_item_row_pengaturan);
            judul = itemView.findViewById(R.id.txt_judul_item_row_pengaturan);
            deskripsi = itemView.findViewById(R.id.txt_deskripsi_item_row_pengaturan);
        }
    }

}

