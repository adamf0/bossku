package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.Kurir;

public class PilihKurirAdapter extends RecyclerView.Adapter<PilihKurirAdapter.PilihKurirViewHolder>{
    private ArrayList<Kurir> list_item;
    private final KurirListener listener;
    private Context context;

    public PilihKurirAdapter(Context context, KurirListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<Kurir> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Kurir> listItem) {
        this.list_item = listItem;
    }

    public interface KurirListener{
        void SelectItemKurir(Kurir item);
    }

    @NonNull
    @Override
    public PilihKurirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pilih_kurir, parent, false);
        return (new PilihKurirViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PilihKurirViewHolder holder, final int position) {
        final Kurir item = getListItem().get(position);

        holder.txt_metode.setText(String.format("%s - %s",item.getName(),item.getService()));
        holder.txt_deskripsi.setText(String.format("%s/Kg (%s)", Util.rupiahFormat(item.getPrice()),item.getEstimation()));
        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.SelectItemKurir(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class PilihKurirViewHolder extends RecyclerView.ViewHolder{
        TextView txt_metode,txt_deskripsi;
        ImageView img_metode;
        ConstraintLayout items;
        KurirListener listener;

        PilihKurirViewHolder(View itemView, KurirListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_pilih_kurir);
            txt_metode = itemView.findViewById(R.id.txt_metode_item_row_pilih_kurir);
            txt_deskripsi = itemView.findViewById(R.id.txt_detail_metode_item_row_pilih_kurir);
            img_metode = itemView.findViewById(R.id.img_metode_item_row_pilih_kurir);
        }
    }

}

