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
import app.adam.bossku.view.model.Bank;

public class PilihBankAdapter extends RecyclerView.Adapter<PilihBankAdapter.PilihBankViewHolder>{
    private ArrayList<Bank> list_item;
    private final BankListener listener;
    private Context context;

    public PilihBankAdapter(Context context, BankListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<Bank> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Bank> listItem) {
        this.list_item = listItem;
    }

    public interface BankListener{
        void SelectItemBank(Bank item);
    }

    @NonNull
    @Override
    public PilihBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pilih_bank, parent, false);
        return (new PilihBankViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PilihBankViewHolder holder, final int position) {
        final Bank item = getListItem().get(position);

        holder.txt_metode.setText(item.getBank_name());
        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.SelectItemBank(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class PilihBankViewHolder extends RecyclerView.ViewHolder{
        TextView txt_metode;
        ImageView img_metode;
        ConstraintLayout items;
        BankListener listener;

        PilihBankViewHolder(View itemView, BankListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_pilih_bank);
            txt_metode = itemView.findViewById(R.id.txt_metode_item_row_pilih_bank);
            img_metode = itemView.findViewById(R.id.img_metode_item_row_pilih_bank);
        }
    }

}

