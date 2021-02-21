package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import app.adam.basiclibrary.BasicLib;
import app.adam.bossku.R;
import app.adam.bossku.databinding.ActivityMainBinding;
import app.adam.bossku.helper.MaterialSearchView;
import app.adam.bossku.helper.Util;
import app.adam.bossku.interfaces.MainListener;
import app.adam.bossku.interfaces.MaterialSearchViewListener;
import app.adam.bossku.interfaces.SimpleSearchActionsListener;
import app.adam.bossku.interfaces.TagListener;
import app.adam.bossku.interfaces.onSearchListener;
import app.adam.bossku.view.adapter.ListProdukUserAdapter;
import app.adam.bossku.view.adapter.ProdukDiskonAdapter;
import app.adam.bossku.view.adapter.ProdukKategoriAdapter;
import app.adam.bossku.view.adapter.ProdukTerlarisAdapter;
import app.adam.bossku.view.model.Kategori;
import app.adam.bossku.view.model.Product;
import app.adam.bossku.view.model.ProductDiskon;
import app.adam.bossku.view.model.ProductTerlaris;
import app.adam.bossku.view.model.Search;
import app.adam.bossku.view.model.Tags;
import app.adam.bossku.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements
        ListProdukUserAdapter.ProductUserMainListener,
        ProdukDiskonAdapter.ProductDiscountListener,
        ProdukTerlarisAdapter.PopularProductListener,
        MaterialSearchViewListener,
        onSearchListener,
        SimpleSearchActionsListener,
        TagListener, MainListener, ProdukKategoriAdapter.ProductCategoryListener {

    ActivityMainBinding binding;
    MainViewModel viewModel;
    boolean mSearchViewAdded = false;
    MaterialSearchView mSearchView;
    WindowManager mWindowManager;
    boolean searchActive = false;
    private int badgeCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setAction(this);

        viewModel =  new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.setMainListener(this);
        MainViewModel.lib = new BasicLib(this);

        setSupportActionBar(binding.toolbarMain);

        binding.menuBottomMain.getOrCreateBadge(R.id.navigation_transaksi); //menu_bottom_main.removeBadge(R.id.navigation_menu1);
        BadgeDrawable badge = binding.menuBottomMain.getBadge(R.id.navigation_transaksi);
        assert badge != null;
        badge.setNumber(3);

        binding.menuBottomMain.setOnNavigationItemSelectedListener(MenuClick());
        binding.menuBottomMain.setSelectedItemId(R.id.navigation_home);
        binding.rvListItemMain.setHasFixedSize(true);
        binding.rvListItemMain.setLayoutManager(new LinearLayoutManager(this));
        binding.loaderMain.setVisibility(View.VISIBLE);
        binding.loaderMain.setIndeterminateDrawable(new ThreeBounce());
        viewModel.loadData();

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mSearchView = new MaterialSearchView(this);
        mSearchView.setOnSearchListener(this);
        mSearchView.setSearchResultsListener(this);
        mSearchView.setSeachWithList(true);
        mSearchView.setHintText("Search");

        ArrayList<Tags> tags = new ArrayList<>();
        tags.add(new Tags("1","Otomotif"));
        tags.add(new Tags("2","Kuliner"));
        tags.add(new Tags("3","Kosmetik"));
        tags.add(new Tags("4","Komputer"));
        tags.add(new Tags("5","Elektronik"));
        tags.add(new Tags("6","Cendramata"));
        tags.add(new Tags("7","Jasa"));
        tags.add(new Tags("8","Fashion"));
        tags.add(new Tags("9","Agrobisnis"));
        tags.add(new Tags("10","Komputer"));
        mSearchView.addTagKategori(tags,this);

        if (binding.toolbarMain != null) {
            binding.toolbarMain.post(() -> {
                if (!mSearchViewAdded && mWindowManager != null) {
                    mWindowManager.addView(mSearchView, MaterialSearchView.getSearchViewLayoutParams(MainActivity.this));
                    mSearchViewAdded = true;
                }
            });
        }

        Util.checkAndRequestPermissions(this);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);

        if(searchActive)
            mSearchView.display();

        MenuItem menuItem = menu.findItem(R.id.keranjang);
        menuItem.setIcon(Util.buildCounterDrawable(this,R.layout.counter_notif,badgeCount, R.drawable.ic_keranjang));

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                mSearchView.display();
                openKeyboard();
                break;
            case R.id.keranjang:
                badgeCount++;
                invalidateOptionsMenu();
                startActivity(new Intent(MainActivity.this, KeranjangActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        searchActive = false;
        mSearchView.mSearchEditText.setText("");
        mSearchView.hide();
        mSearchView.showProgress(false);

        super.onPause();
    }

    @SuppressLint("NonConstantResourceId")
    public BottomNavigationView.OnNavigationItemSelectedListener MenuClick(){
        return item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    break;
                case R.id.navigation_transaksi:
                    startActivity(new Intent(MainActivity.this, TransaksiActivity.class));
                    break;
                case R.id.navigation_menu_lain:
                    startActivity(new Intent(MainActivity.this, PengaturanActivity.class));
                    break;
            }
            return true;
        };
    }

    private void openKeyboard(){
        new Handler().postDelayed(() -> {
            mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }, 200);
    }

    @Override
    public void SelectItemProduct(Product item) {
        Intent i = new Intent(this, DetailProdukActivity.class);
        i.putExtra("action","MainActivity");
        i.putExtra("produk",item);
        startActivity(i);
    }

    @Override
    public void SelectItemProducDiscount(ProductDiskon item) {
        Toast.makeText(this, item.getNama_produk(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void SelectItemPupularProduct(ProductTerlaris item) {
        Toast.makeText(this, item.getNama_produk(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearch(final String query) {
        mSearchView.showProgress(true);
        new Handler().postDelayed(() -> {
            mSearchView.showProgress(false);
            mSearchView.searchViewResults.mAdapter.notifyDataSetInvalidated();

            if(query.equals("tes")){
                Search item1 = new Search("1","item 1");
                Search item2 = new Search("2","item 2");
                Search item3 = new Search("3","item 3");
                Search item4 = new Search("4","item 4");
                mSearchView.searchViewResults.datas.add(item1);
                mSearchView.searchViewResults.datas.add(item2);
                mSearchView.searchViewResults.datas.add(item3);
                mSearchView.searchViewResults.datas.add(item4);
            }
            else{
                mSearchView.listEmpty();
            }
            mSearchView.searchViewResults.mAdapter.notifyDataSetChanged();
        },3000);
    }

    @Override
    public void onSearchSubmit(String query) {
        Intent i = new Intent(this, CariProdukActivity.class);
        startActivity(i);
    }

    @Override
    public void searchViewOpened() {

    }

    @Override
    public void searchViewClosed() {
    }

    @Override
    public void onCancelSearch() {
        searchActive = false;
        mSearchView.mSearchEditText.setText("");
        mSearchView.hide();
    }

    @Override
    public void onItemClicked(Search item) {
        Intent i = new Intent(this, DetailProdukActivity.class);
        startActivity(i);

        Toast.makeText(this, item.getJudul(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void listEmpty() {
        Toast.makeText(this, "tidak ada data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScroll() {

    }

    @Override
    public void error(String localizedMessage) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onSelectTag(Tags tags) {
        Toast.makeText(this, tags.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessLoadData(final ArrayList<Product> list_item, final ArrayList<Kategori> list_kategori, final ArrayList<ProductDiskon> list_diskon, final ArrayList<ProductTerlaris> list_terlaris) {
        new Handler().postDelayed(() -> {
            ListProdukUserAdapter cardAdapter = new ListProdukUserAdapter(MainActivity.this,MainActivity.this,MainActivity.this,MainActivity.this,MainActivity.this);
            cardAdapter.setListItem(list_item);
            cardAdapter.setListKategori(list_kategori,0);
            cardAdapter.setListDiskon(list_diskon,0);
            cardAdapter.setLisTerlaris(list_terlaris,0);
            cardAdapter.notifyDataSetChanged();
            binding.rvListItemMain.setAdapter(cardAdapter);
            binding.loaderMain.setVisibility(View.GONE);
        },1500);
    }

    @Override
    public void onFailLoadData(String error) {
        Snackbar.make(binding.layoutMain,error,Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(() -> viewModel.loadData(),3000);
    }

    @Override
    public void SelectItemProducCategory(Kategori item) {
        startActivity(
                new Intent(this,CariProdukActivity.class)
                        .putExtra("search",item.getLink())
                        .putExtra("type","kategori")
        );
    }
}