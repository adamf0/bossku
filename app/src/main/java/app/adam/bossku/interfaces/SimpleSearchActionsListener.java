package app.adam.bossku.interfaces;

import app.adam.bossku.view.model.Search;

public interface SimpleSearchActionsListener {
    void onItemClicked(Search item);
    void onScroll();
    void error(String localizedMessage);
}
