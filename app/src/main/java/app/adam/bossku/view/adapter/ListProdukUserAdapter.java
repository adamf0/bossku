package app.adam.bossku.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import app.adam.basiclibrary.BasicLib;
import app.adam.basiclibrary.TextListener;
import app.adam.bossku.view.model.ProductDiskon;
import app.adam.bossku.view.model.ProductTerlaris;
import app.adam.bossku.view.ui.KategoriActivity;
import app.adam.bossku.R;
import app.adam.bossku.databinding.ItemListMainBinding;
import app.adam.bossku.helper.Route;
import app.adam.bossku.view.model.Kategori;
import app.adam.bossku.view.model.Product;

public class ListProdukUserAdapter extends RecyclerView.Adapter<ListProdukUserAdapter.ProdukDiskonViewHolder>{
    private ArrayList<Product> list_item;
    private ArrayList<Kategori> list_kategori = new ArrayList<>();
    private ArrayList<ProductDiskon> list_diskon = new ArrayList<>();
    private ArrayList<ProductTerlaris> list_terlaris = new ArrayList<>();
    private int position_kategori;
    private int position_diskon;
    private int position_terlaris;
    private final ProductUserMainListener listener;
    private final ProdukTerlarisAdapter.PopularProductListener listener_popular;
    private final ProdukDiskonAdapter.ProductDiscountListener listener_discount;
    private final ProdukKategoriAdapter.ProductCategoryListener listener_category;
    private final Context context;
    BasicLib lib;
    private ItemListMainBinding binding;

    public ListProdukUserAdapter(Context context, ProductUserMainListener listener,ProdukTerlarisAdapter.PopularProductListener listener_popular,ProdukDiskonAdapter.ProductDiscountListener listener_discount,ProdukKategoriAdapter.ProductCategoryListener listener_category) {
        this.context = context;
        this.listener = listener;
        this.listener_popular = listener_popular;
        this.listener_discount = listener_discount;
        this.listener_category = listener_category;
        this.lib = new BasicLib((Activity) context);
    }

    public ArrayList<Product> getListItem() {
        return list_item;
    }
    public void setListItem(ArrayList<Product> listItem) {
        this.list_item = listItem;
    }

    public ArrayList<Kategori> getListKategori() {
        return list_kategori;
    }
    public void setListKategori(ArrayList<Kategori> listKategori,int position_kategori) {
        this.list_kategori = listKategori;
        this.position_kategori = position_kategori;
    }

    public ArrayList<ProductDiskon> getListDiskon() {
        return list_diskon;
    }
    public void setListDiskon(ArrayList<ProductDiskon> listDiskon,int position_diskon) {
        this.list_diskon = listDiskon;
        this.position_diskon = position_diskon;
    }

    public ArrayList<ProductTerlaris> getListTerlaris() {
        return list_terlaris;
    }
    public void setLisTerlaris(ArrayList<ProductTerlaris> listTerlaris,int position_terlaris) {
        this.list_terlaris = listTerlaris;
        this.position_terlaris = position_terlaris;
    }

    public interface ProductUserMainListener{
        void SelectItemProduct(Product item);
    }

    public void select_item_product(Product item){
        listener.SelectItemProduct(item);
    }

    @NonNull
    @Override
    public ProdukDiskonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemListMainBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return (new ProdukDiskonViewHolder(binding));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ProdukDiskonViewHolder holder, final int position) {
        Product item = getListItem().get(position);

        holder.binding.setAction(this);
        holder.binding.setModel(item);

        binding.rvListDiskonProdukMain.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.rvListPopulerProdukMain.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.rvListCategoryMain.setLayoutManager(new GridLayoutManager(context, 4));

        if(item!=null) {
            binding.txtDeskripsiProdukItemListProdukMain.setHtml(item.getDeskripsi_produk());
            Log.i("app-log",Route.storage+item.getGambar_produk());

            Glide.with(context)
                    .load(Route.storage+item.getGambar_produk())
//                    .placeholder(R.drawable.ic_noimage)
//                    .error(R.drawable.ic_noimage)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(binding.imgProdukItemListProdukMain);
        }

        if(getListDiskon()!=null && getListDiskon().size()>0 && position==position_diskon){
            lib.setTextView(binding.txtDetailDiskonProdukMain);
            lib.setFull_text("Lihat Selengkapnya");

            lib.setUnderline(false);
            lib.setColor(lib.getAct().getResources().getColor(R.color.dark));

            lib.setTarget("Lihat Selengkapnya");
            lib.addClick(new TextListener() {
                @Override
                public void onClick() {

                }
            });
            lib.implementLink();

            binding.itemListDiskonProdukMain.setVisibility(View.VISIBLE);
            ProdukDiskonAdapter adapter = new ProdukDiskonAdapter(context,listener_discount);
            adapter.setListItem(getListDiskon());
            adapter.notifyDataSetChanged();
            binding.rvListDiskonProdukMain.setAdapter(adapter);
        }
        if(getListTerlaris()!=null && getListTerlaris().size()>0 && position==position_terlaris){
            lib.setTextView(binding.txtDetailPopulerProdukMain);
            lib.setFull_text("Lihat Selengkapnya");

            lib.setUnderline(false);
            lib.setColor(lib.getAct().getResources().getColor(R.color.dark));

            lib.setTarget("Lihat Selengkapnya");
            lib.addClick(new TextListener() {
                @Override
                public void onClick() {

                }
            });
            lib.implementLink();

            binding.itemListPopulerProdukMain.setVisibility(View.VISIBLE);
            ProdukTerlarisAdapter adapter = new ProdukTerlarisAdapter(context,listener_popular);
            adapter.setListItem(getListTerlaris());
            adapter.notifyDataSetChanged();
            binding.rvListPopulerProdukMain.setAdapter(adapter);
        }
        if(getListKategori()!=null && getListKategori().size()>0 && position==position_kategori){
            lib.setTextView(binding.txtDetailCategoryMain);
            lib.setFull_text("Lihat Selengkapnya");

            lib.setUnderline(false);
            lib.setColor(lib.getAct().getResources().getColor(R.color.dark));

            lib.setTarget("Lihat Selengkapnya");
            lib.addClick(new TextListener() {
                @Override
                public void onClick() {
                    context.startActivity(new Intent(context, KategoriActivity.class));
                }
            });
            lib.implementLink();

            binding.itemListCategoryMain.setVisibility(View.VISIBLE);
            ProdukKategoriAdapter adapter = new ProdukKategoriAdapter(context,listener_category);
            adapter.setListItem(getListKategori());
            adapter.notifyDataSetChanged();
            binding.rvListCategoryMain.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return getListItem().size();
    }

    static class ProdukDiskonViewHolder extends RecyclerView.ViewHolder{
        ItemListMainBinding binding;

        ProdukDiskonViewHolder(ItemListMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

