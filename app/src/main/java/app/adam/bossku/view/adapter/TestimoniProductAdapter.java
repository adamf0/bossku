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
import app.adam.bossku.view.model.Product;
import app.adam.bossku.view.model.Testimoni;

public class TestimoniProductAdapter extends RecyclerView.Adapter<TestimoniProductAdapter.TestimoniProductViewHolder>{
    private ArrayList<Testimoni> list_item;
    private Context context;

    public TestimoniProductAdapter(Context context) {
        this.context = context;
    }
    public ArrayList<Testimoni> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Testimoni> listItem) {
        this.list_item = listItem;
    }

    public interface ProductListener{
        void SelectItemProduct(Product item);
    }

    @NonNull
    @Override
    public TestimoniProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_testimoni, parent, false);
        return (new TestimoniProductViewHolder(view));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final TestimoniProductViewHolder holder, final int position) {
        final Testimoni item = getListItem().get(position);

        holder.txt_nama_pengguna.setText(item.getNama_pengguna());
        holder.txt_deskripsi.setText(item.getDeskripsi());
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class TestimoniProductViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_pengguna,txt_deskripsi;
        ImageView img_foto_pengguna;
        CardView items;

        TestimoniProductViewHolder(View itemView) {
            super(itemView);
            items = itemView.findViewById(R.id.item_row_testimoni);
            img_foto_pengguna = itemView.findViewById(R.id.img_foto_pengguna_item_row_testimoni);
            txt_nama_pengguna = itemView.findViewById(R.id.txt_nama_pengguna_item_row_testimoni);
            txt_deskripsi = itemView.findViewById(R.id.txt_deskripsi_item_row_testimoni);
        }
    }

}

