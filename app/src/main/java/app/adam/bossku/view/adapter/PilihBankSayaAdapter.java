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
import app.adam.bossku.view.model.Bank;

public class PilihBankSayaAdapter extends RecyclerView.Adapter<PilihBankSayaAdapter.PilihBankSayaViewHolder>{
    private ArrayList<Bank> list_item;
    private final BankSayaListener listener;
    private Context context;

    public PilihBankSayaAdapter(Context context, BankSayaListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<Bank> getListItem() {
        return list_item;
    }

    public void setListItem(ArrayList<Bank> listItem) {
        this.list_item = listItem;
    }

    public interface BankSayaListener{
        void DeleteItemBankSaya(Bank item);
    }

    @NonNull
    @Override
    public PilihBankSayaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_bank_saya, parent, false);
        return (new PilihBankSayaViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PilihBankSayaViewHolder holder, final int position) {
        final Bank item = getListItem().get(position);

        holder.txt_nama_bank.setText(item.getBank_name());
        holder.txt_nomor_rekening.setText(item.getAccount_number());
        holder.txt_atas_nama.setText(item.getAccount_name());
        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.DeleteItemBankSaya(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class PilihBankSayaViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nama_bank,txt_nomor_rekening,txt_atas_nama;
        ImageButton btn_hapus;
        CardView items;
        BankSayaListener listener;

        PilihBankSayaViewHolder(View itemView, BankSayaListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_bank_saya);
            txt_nama_bank = itemView.findViewById(R.id.txt_nama_bank_item_row_bank_saya);
            txt_nomor_rekening = itemView.findViewById(R.id.txt_nomor_rekening_item_row_bank_saya);
            txt_atas_nama = itemView.findViewById(R.id.txt_atas_nama_item_row_bank_saya);
            btn_hapus = itemView.findViewById(R.id.btn_hapus_item_row_bank_saya);
        }
    }

}

