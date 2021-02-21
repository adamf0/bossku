package app.adam.bossku.interfaces;

import app.adam.bossku.view.model.Search;

public interface MaterialSearchViewListener {
    void onItemClicked(Search item);
    void showProgress(boolean show);
    void listEmpty();
    void onScroll();
    void error(String localizedMessage);
}
