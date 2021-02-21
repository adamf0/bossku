package app.adam.bossku.interfaces;

import java.util.ArrayList;

import app.adam.bossku.view.model.Kategori;
import app.adam.bossku.view.model.Product;
import app.adam.bossku.view.model.ProductDiskon;
import app.adam.bossku.view.model.ProductTerlaris;

public interface MainListener {
    void onSuccessLoadData(ArrayList<Product> list_item, ArrayList<Kategori> list_kategori, ArrayList<ProductDiskon> list_diskon, ArrayList<ProductTerlaris> list_terlaris);
    void onFailLoadData(String error);
}
