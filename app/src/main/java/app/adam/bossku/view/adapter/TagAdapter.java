package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.interfaces.TagListener;
import app.adam.bossku.view.model.ProductDiskon;
import app.adam.bossku.view.model.Tags;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder>{
    private ArrayList<Tags> list_item;

    private TagListener listener;
    private Context context;

    public TagAdapter(Context context, TagListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public ArrayList<Tags> getListItem() {
        return list_item;
    }

    public void setListTag(ArrayList<Tags> listTags) {
        this.list_item = listTags;
    }

    @NonNull
    @Override
    public TagAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_tag, parent, false);
        return (new TagAdapter.TagViewHolder(view,listener));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final TagAdapter.TagViewHolder holder, final int position) {
        final Tags item = getListItem().get(position);

        holder.text_view_tag.setText(item.getTitle());
        holder.text_view_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectTag(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class TagViewHolder extends RecyclerView.ViewHolder{
        TextView text_view_tag;
        TagListener listener;

        TagViewHolder(View itemView, TagListener listener) {
            super(itemView);
            this.listener = listener;
            text_view_tag = itemView.findViewById(R.id.text_view_tag);
        }
    }

}