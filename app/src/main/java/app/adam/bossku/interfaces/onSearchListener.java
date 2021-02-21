package app.adam.bossku.interfaces;

public interface onSearchListener {
    void onSearch(String query);
    void onSearchSubmit(String query);
    void searchViewOpened();
    void searchViewClosed();
    void onCancelSearch();
}
