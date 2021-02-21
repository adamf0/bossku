package app.adam.bossku.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import app.adam.bossku.R;
import app.adam.bossku.interfaces.MaterialSearchViewListener;
import app.adam.bossku.view.model.Search;

public class CariAdapter extends ArrayAdapter<Search>{
    MaterialSearchViewListener MaterialSearchViewListener;
    private Context mContext;
    private List<Search> dataList;

    public CariAdapter(@NonNull Context context, ArrayList<Search> list, MaterialSearchViewListener MaterialSearchViewListener) {
        super(context, 0 , list);
        this.mContext = context;
        this.dataList = list;
        this.MaterialSearchViewListener = MaterialSearchViewListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.items_row_pencarian,parent,false);

        Search search = dataList.get(position);
        RelativeLayout items = listItem.findViewById(R.id.item_row_pencarian);
        TextView title = listItem.findViewById(R.id.txt_judul_item_row_pencarian);

        title.setText(search.getJudul());
        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=(Integer) view.getTag();
                Search search= getItem(position);
                assert search != null;
                Log.i("Response","item "+search.getJudul());

                MaterialSearchViewListener.onItemClicked(search);
            }
        });
        items.setTag(position);

        return listItem;
    }
}