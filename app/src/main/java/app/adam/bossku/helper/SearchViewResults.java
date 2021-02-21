package app.adam.bossku.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.AbsListView;
import android.widget.ListView;
import java.util.ArrayList;

import app.adam.bossku.interfaces.MaterialSearchViewListener;
import app.adam.bossku.view.adapter.CariAdapter;
import app.adam.bossku.view.model.Search;

public class SearchViewResults implements AbsListView.OnScrollListener {
    private static final int TRIGGER_SEARCH = 1;
    private static final long SEARCH_TRIGGER_DELAY_IN_MS = 400;
    private String sequence;
    //public RequestQueue queue;
    public Handler mHandler;
    public CariAdapter mAdapter;
    private MaterialSearchViewListener mListener;
    public ArrayList<Search> datas = new ArrayList<>();

    SearchViewResults(final Context context, String searchQuery, MaterialSearchViewListener MaterialSearchViewListener) {
        sequence = searchQuery;
        mAdapter = new CariAdapter(context,datas, MaterialSearchViewListener);
        mHandler = new Handler(msg -> {
            if (msg.what == TRIGGER_SEARCH) {
                clearAdapter();
                String sequence = (String) msg.obj;
                //getData(sequence);
            }
            return false;
        });
    }

    void setListView(ListView listView) {
        listView.setOnScrollListener(this);
        listView.setAdapter(mAdapter);
        updateSequence();
    }

    void updateSequence(String s) {
        sequence = s;
        updateSequence();
    }

    private void updateSequence() {
//        if (queue != null) {
//            queue.cancelAll("request");
//        }
        if (mHandler != null) {
            mHandler.removeMessages(TRIGGER_SEARCH);
        }
        if (!sequence.isEmpty()) {
            Message searchMessage = new Message();
            searchMessage.what = TRIGGER_SEARCH;
            searchMessage.obj = sequence;
            mHandler.sendMessageDelayed(searchMessage, SEARCH_TRIGGER_DELAY_IN_MS);
        } else {
            clearAdapter();
        }
    }

//    private void getData(final String query){
//        mListener.showProgress(true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mListener.showProgress(false);
//                mAdapter.notifyDataSetInvalidated();
//
//                if(query.equals("tes")){
//                    Search item1 = new Search("1","item 1");
//                    Search item2 = new Search("2","item 2");
//                    Search item3 = new Search("3","item 3");
//                    Search item4 = new Search("4","item 4");
//                    datas.add(item1);
//                    datas.add(item2);
//                    datas.add(item3);
//                    datas.add(item4);
//                }
//                else{
//                    mListener.listEmpty();
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//        },3000);
//    }

    private void clearAdapter() {
        mAdapter.clear();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
            mListener.onScroll();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    void setSearchProvidersListener(MaterialSearchViewListener listener) {
        this.mListener = listener;
    }
}


