package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.view.model.Alamat;

public class PilihAlamatAdapter extends RecyclerView.Adapter<PilihAlamatAdapter.PilihAlamatViewHolder>{
    private ArrayList<Alamat> list_item;
    private final AlamatListener listener;
    private String activity=null;
    private Context context;

    public PilihAlamatAdapter(Context context, AlamatListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<Alamat> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Alamat> listItem) {
        this.list_item = listItem;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public interface AlamatListener{
        void SelectItemAlamat(Alamat item);
        void DelItemAlamat(Alamat item);
        void DefaultItemAlamat(Alamat item);
    }

    @NonNull
    @Override
    public PilihAlamatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pilih_alamat, parent, false);
        return (new PilihAlamatViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PilihAlamatViewHolder holder, final int position) {
        final Alamat item = getListItem().get(position);

        holder.txt_alamat.setText(item.getName());
        holder.txt_keterangan.setText(
                String.format("%s, %s, %s\n%s, %s\n%s",
                        item.getAddress(),
                        item.getKecamatan().getName(),
                        item.getKota().getFullName(),
                        item.getProvinsi().getName(),
                        item.getKecamatan().getPostal_kode(),
                        (item.getDef()==0? "":"(Utama)")
                )
        ); //alamat kecamatan kota provinsi kodepos
        if(item.getDef()==1)
            holder.btn_utama.setVisibility(View.GONE);
        else
            holder.btn_utama.setVisibility(View.VISIBLE);

        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.listener.SelectItemAlamat(item);
            }
        });
        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.listener.DelItemAlamat(item);
            }
        });

        if(getActivity()!=null && getActivity().equals("PembayaranBarangActivity") || item.getDef()==1)
            holder.btn_utama.setVisibility(View.GONE);
        else
            holder.btn_utama.setVisibility(View.VISIBLE);

        holder.btn_utama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.listener.DefaultItemAlamat(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class PilihAlamatViewHolder extends RecyclerView.ViewHolder{
        TextView txt_alamat,txt_keterangan;
        ImageView img_alamat;
        ImageButton btn_hapus,btn_utama;
        CardView items;
        AlamatListener listener;

        PilihAlamatViewHolder(View itemView, AlamatListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_pilih_alamat);
            txt_alamat = itemView.findViewById(R.id.txt_alamat_item_row_pilih_alamat);
            txt_keterangan   = itemView.findViewById(R.id.txt_keterangan_item_row_pilih_alamat);
            img_alamat = itemView.findViewById(R.id.img_alamat_item_row_pilih_alamat);
            btn_hapus = itemView.findViewById(R.id.btn_hapus_item_row_pilih_alamat);
            btn_utama = itemView.findViewById(R.id.btn_utama_item_row_pilih_alamat);
        }
    }

}

