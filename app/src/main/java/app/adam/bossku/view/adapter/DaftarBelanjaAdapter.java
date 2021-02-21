package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.view.model.Keranjang;

public class DaftarBelanjaAdapter extends RecyclerView.Adapter<DaftarBelanjaAdapter.DaftarBelanjaViewHolder>{
    private ArrayList<Keranjang> list_item;
    private final ItemDaftarBelanjaAdapter.ItemDaftarBelanjaListener listener_item_daftar_belanja;
    private Context context;
    DaftarBelanjaListener listener;

    public DaftarBelanjaAdapter(Context context, DaftarBelanjaListener listener, ItemDaftarBelanjaAdapter.ItemDaftarBelanjaListener listener_item_daftar_belanja) {
        this.context = context;
        this.listener_item_daftar_belanja = listener_item_daftar_belanja;
        this.listener = listener;
    }
    public ArrayList<Keranjang> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Keranjang> listItem) {
        this.list_item = listItem;
    }

    public interface  DaftarBelanjaListener{
        void CatatanChange(String text, int position_keranjang);
    }
    @NonNull
    @Override
    public DaftarBelanjaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_daftar_belanja, parent, false);
        return (new DaftarBelanjaViewHolder(view,listener,listener_item_daftar_belanja));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final DaftarBelanjaViewHolder holder, final int position) {
        final Keranjang item = getListItem().get(position);

        holder.txt_nama_toko.setText(item.getNama_toko());
        if(item.getItemKeranjang().size()>0){
            holder.list_produk.setHasFixedSize(true);
            holder.list_produk.setLayoutManager(new LinearLayoutManager(context));

            ItemDaftarBelanjaAdapter cardAdapter = new ItemDaftarBelanjaAdapter(context,position,holder.listener_item_daftar_belanja);
            cardAdapter.setListItem(item.getItemKeranjang());
            cardAdapter.notifyDataSetChanged();
            holder.list_produk.setAdapter(cardAdapter);
        }
        holder.edt_catatan.setText(item.getCatatan());
        holder.edt_catatan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setCatatan(s.toString());
                holder.listener.CatatanChange(s.toString(),position);
            }
        });
        //holder.img_produk.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class DaftarBelanjaViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_toko;
        RecyclerView list_produk;
        CardView items;
        EditText edt_catatan;
        ItemDaftarBelanjaAdapter.ItemDaftarBelanjaListener listener_item_daftar_belanja;
        DaftarBelanjaListener listener;

        DaftarBelanjaViewHolder(View itemView, DaftarBelanjaListener listener, ItemDaftarBelanjaAdapter.ItemDaftarBelanjaListener listener_item_daftar_belanja) {
            super(itemView);
            this.listener_item_daftar_belanja = listener_item_daftar_belanja;

            items = itemView.findViewById(R.id.item_row_daftar_belanja);
            txt_nama_toko = itemView.findViewById(R.id.txt_nama_toko_item_row_daftar_belanja);
            list_produk = itemView.findViewById(R.id.rv_item_row_daftar_belanja);
            edt_catatan = itemView.findViewById(R.id.edt_catatan_item_row_daftar_belanja);
            this.listener=listener;
        }
    }

}

