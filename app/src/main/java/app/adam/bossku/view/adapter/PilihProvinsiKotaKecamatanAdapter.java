package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.view.model.Kecamatan;
import app.adam.bossku.view.model.Kota;
import app.adam.bossku.view.model.Provinsi;

public class PilihProvinsiKotaKecamatanAdapter extends RecyclerView.Adapter<PilihProvinsiKotaKecamatanAdapter.PilihKurirViewHolder>{
    private ArrayList<Provinsi> list_prov;
    private ArrayList<Provinsi> list_prov_filter;
    private ArrayList<Kota> list_kota;
    private ArrayList<Kota> list_kota_filter;
    private ArrayList<Kecamatan> list_kec;
    private ArrayList<Kecamatan> list_kec_filter;
    private final ProvinsiKotaKecamatanListener listener;

    public PilihProvinsiKotaKecamatanAdapter(ProvinsiKotaKecamatanListener listener) {
        this.listener = listener;
    }

    public ArrayList<Provinsi> getList_prov() {
        return list_prov_filter;
    }

    public void setList_prov(ArrayList<Provinsi> list_prov) {
        this.list_prov = list_prov;
        this.list_prov_filter = list_prov;

    }

    public ArrayList<Kota> getList_kota() {
        return list_kota_filter;
    }

    public void setList_kota(ArrayList<Kota> list_kota) {
        this.list_kota = list_kota;
        this.list_kota_filter = list_kota;
    }

    public ArrayList<Kecamatan> getList_kec() {
        return list_kec_filter;
    }

    public void setList_kec(ArrayList<Kecamatan> list_kec) {
        this.list_kec = list_kec;
        this.list_kec_filter = list_kec;
    }

    public interface ProvinsiKotaKecamatanListener{
        void SelectItemProv(Provinsi item);
        void SelectItemKota(Kota item);
        void SelectItemKEc(Kecamatan item);
    }

    @NonNull
    @Override
    public PilihKurirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pilih_provinsi_kota_kecamatan, parent, false);
        return (new PilihKurirViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PilihKurirViewHolder holder, final int position) {
        if(getList_prov() != null && getList_prov().size()>0) {
            holder.txt_metode.setText(getList_prov().get(position).getName());
            holder.items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.listener.SelectItemProv(getList_prov().get(position));
                    Log.i("app-log","listprov klik");
                }
            });
        }
        else if(getList_kota() != null && getList_kota().size()>0) {
            holder.txt_metode.setText(getList_kota().get(position).getFullName());
            holder.items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.listener.SelectItemKota(getList_kota().get(position));
                    Log.i("app-log","listkota klik");
                }
            });
        }
        else if(getList_kec() != null && getList_kec().size()>0) {
            holder.txt_metode.setText(getList_kec().get(position).getName());
            holder.items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.listener.SelectItemKEc(getList_kec().get(position));
                    Log.i("app-log","listkec klik");
                }
            });
        }
        else{
            holder.txt_metode.setText("N/a");
        }
    }

    @Override
    public int getItemCount() {
        if(getList_prov() != null)
            return getList_prov().size();
        else if(getList_kota() != null)
            return getList_kota().size();
        else if(getList_kec() != null)
            return getList_kec().size();
        else
            return -1;
    }

    public Filter getFilter() {
        return new Filter() {
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list_prov_filter = list_prov;
                    list_kota_filter = list_kota;
                    list_kec_filter = list_kec;
                } else {
                    ArrayList<Provinsi> filteredProv = null;
                    ArrayList<Kota> filteredKota = null;
                    ArrayList<Kecamatan> filteredKec = null;

                    if(getList_prov() != null && getList_prov().size()>0){
                        filteredProv = new ArrayList<>();
                        for (Provinsi row : list_prov) {
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredProv.add(row);
                            }
                        }
                    }
                    else if(getList_kota() != null && getList_kota().size()>0){
                        filteredKota = new ArrayList<>();
                        for (Kota row : list_kota) {
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredKota.add(row);
                            }
                        }
                    }
                    else if(getList_kec() != null && getList_kec().size()>0){
                        filteredKec = new ArrayList<>();
                        for (Kecamatan row : list_kec) {
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredKec.add(row);
                            }
                        }
                    }

                    list_prov_filter = filteredProv;
                    list_kota_filter = filteredKota;
                    list_kec_filter = filteredKec;
                }

                FilterResults filterResults = new FilterResults();
                if(getList_prov() != null && getList_prov().size()>0)
                    filterResults.values = list_prov_filter;
                else if(getList_kota() != null && getList_kota().size()>0)
                    filterResults.values = list_kota_filter;
                else if(getList_kec() != null && getList_kec().size()>0)
                    filterResults.values = list_kec_filter;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(list_prov != null && list_prov.size()>0)
                    list_prov_filter = (ArrayList<Provinsi>) filterResults.values;
                else if(list_kota != null && list_kota.size()>0)
                    list_kota_filter = (ArrayList<Kota>) filterResults.values;
                else if(list_kec != null && list_kec.size()>0)
                    list_kec_filter = (ArrayList<Kecamatan>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class PilihKurirViewHolder extends RecyclerView.ViewHolder{
        TextView txt_metode;
        CardView items;
        ProvinsiKotaKecamatanListener listener;

        PilihKurirViewHolder(View itemView, ProvinsiKotaKecamatanListener listener) {
            super(itemView);
            this.listener = listener;
            items = itemView.findViewById(R.id.item_row_pilih_provinsi_kota_kecamatan);
            txt_metode = itemView.findViewById(R.id.txt_provinsi_kota_kecamatan_item_row_pilih_provinsi_kota_kecamatan);
        }
    }

}

