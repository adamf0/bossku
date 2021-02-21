package app.adam.bossku.helper;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import app.adam.bossku.R;
import app.adam.bossku.interfaces.MaterialSearchViewListener;
import app.adam.bossku.interfaces.SimpleSearchActionsListener;
import app.adam.bossku.interfaces.TagListener;
import app.adam.bossku.interfaces.onSearchListener;
import app.adam.bossku.view.adapter.TagAdapter;
import app.adam.bossku.view.model.Search;
import app.adam.bossku.view.model.Tags;

public class MaterialSearchView extends FrameLayout implements View.OnClickListener, MaterialSearchViewListener {
    public final EditText mSearchEditText;
    private final ImageView mClearSearch;
    private final ProgressBar mProgressBar;
    private onSearchListener mOnSearchListener;
    private final View lineDivider;
    private final CardView cardLayout;
    private final RelativeLayout searchLayout;
    private final ImageView backArrowImg;
    private final ListView pencarian_conteiner;
    private final TextView lb_pencarian_conteiner;
    private final Context mContext;
    public SearchViewResults searchViewResults;
    private final FrameLayout progressBarLayout;
    private SimpleSearchActionsListener searchListener;
    private final TextView noResultsFoundText;
    private final RecyclerView rv_tag_kategori;
    private final TextView lb_tag_kategori;
    private boolean isSeachWithList=true;

    public void setHintText(String hint) {
        mSearchEditText.setHint(hint);
    }

    public CardView getCardLayout() {
        return cardLayout;
    }

    public ListView getListview() {
        return pencarian_conteiner;
    }

    public View getLineDivider() {
        return lineDivider;
    }

    final Animation fade_in;
    final Animation fade_out;

//    @Override
//    public void onItemClicked(String item) {
//        this.searchListener.onItemClicked(item);
//    }

    @Override
    public void onItemClicked(Search item) {
        this.searchListener.onItemClicked(item);
    }

    @Override
    public void showProgress(boolean show) {
        Log.i("app-log progressbar", String.valueOf(show));
        if(show) {
            progressBarLayout.setVisibility(VISIBLE);
            mProgressBar.setVisibility(VISIBLE);
            noResultsFoundText.setVisibility(GONE);
        }
        else {
            progressBarLayout.setVisibility(GONE);
        }

    }

    @Override
    public void listEmpty() {
        progressBarLayout.setVisibility(VISIBLE);
        noResultsFoundText.setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
    }

    @Override
    public void onScroll() {
        this.searchListener.onScroll();
    }

    @Override
    public void error(String localizedMessage) {
        this.searchListener.error(localizedMessage);
    }

    public MaterialSearchView(final Context context) {
        this(context, null);
    }

    public MaterialSearchView(final Context context, final AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MaterialSearchView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        final LayoutInflater factory = LayoutInflater.from(context);
        factory.inflate(R.layout.toolbar_pencarian, this);

        mContext = context;
        cardLayout = findViewById(R.id.card_pencarian);
        pencarian_conteiner = findViewById(R.id.material_pencarian_container);
        lb_pencarian_conteiner = findViewById(R.id.lb_pencarian_container);
        searchLayout = findViewById(R.id.view_pencarian);
        lineDivider = findViewById(R.id.line_divider_pencarian);
        noResultsFoundText = findViewById(R.id.textView13);
        mSearchEditText = findViewById(R.id.edit_text_pencarian);
        backArrowImg = findViewById(R.id.image_kembali_pencarian);
        mClearSearch = findViewById(R.id.img_hapus_pencarian);
        progressBarLayout = findViewById(R.id.progressLayout);
        mProgressBar = findViewById(R.id.progressBar);
        rv_tag_kategori = findViewById(R.id.rv_tag_kategori);
        lb_tag_kategori = findViewById(R.id.lb_tag_kategori);
        mSearchEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        fade_in = AnimationUtils.loadAnimation(getContext().getApplicationContext(), android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(getContext().getApplicationContext(), android.R.anim.fade_out);

        noResultsFoundText.setText("Tidak Ditemukan");
        mClearSearch.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        backArrowImg.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isSeachWithList()) {
                    if (mOnSearchListener != null) {
                        mOnSearchListener.onSearch(getSearchQuery());
                        onQuery(getSearchQuery());
                    }
                    toggleClearSearchButton(s);
                }
            }
        });

        mSearchEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)) {
                    final String query = getSearchQuery();
                    if (!TextUtils.isEmpty(query) && mOnSearchListener != null) {
                        mOnSearchListener.onSearchSubmit(query);
                    }
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.image_kembali_pencarian).setOnClickListener(this);
        mClearSearch.setOnClickListener(this);
        setVisibility(View.GONE);
        clearAnimation();
    }
    void initView(){
        searchLayout.setVisibility((isSeachWithList() ? View.VISIBLE:View.GONE));
        lineDivider.setVisibility((isSeachWithList() ? View.VISIBLE:View.GONE));
        lb_tag_kategori.setVisibility((isSeachWithList() ? View.VISIBLE:View.GONE));
        rv_tag_kategori.setVisibility((isSeachWithList() ? View.VISIBLE:View.GONE));
        lb_pencarian_conteiner.setVisibility((isSeachWithList() ? View.VISIBLE:View.GONE));
        pencarian_conteiner.setVisibility((isSeachWithList() ? View.VISIBLE:View.GONE));
        progressBarLayout.setVisibility((isSeachWithList() ? View.VISIBLE:View.GONE));
    }
    public void addTagKategori(ArrayList<Tags> list_tags, TagListener tagListener){
        if(list_tags.size()>0) {
            rv_tag_kategori.setVisibility(View.VISIBLE);
            TagAdapter tagAdapter = new TagAdapter(mContext, tagListener);
            tagAdapter.setListTag(list_tags);

            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setFlexWrap(FlexWrap.WRAP);
            rv_tag_kategori.setLayoutManager(layoutManager);
            rv_tag_kategori.setAdapter(tagAdapter);
        }
    }
    public void setOnSearchListener(final onSearchListener l) {
        mOnSearchListener = l;
    }

    public void setSearchResultsListener(SimpleSearchActionsListener listener) {
        this.searchListener = listener;
    }

    public void setSearchQuery(final String query) {
        mSearchEditText.setText(query);
        toggleClearSearchButton(query);
    }

    public String getSearchQuery() {
        return mSearchEditText.getText() != null ? mSearchEditText.getText().toString() : "";
    }


    public boolean isSearchViewVisible() {
        return getVisibility() == View.VISIBLE;
    }

    // Show the SearchView
    public void display() {
        if (isSearchViewVisible()) return;
        Log.i("app-log display search","1");
        setVisibility(View.VISIBLE);
        mOnSearchListener.searchViewOpened();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Animator animator = ViewAnimationUtils.createCircularReveal(cardLayout, cardLayout.getWidth() - Util.dpToPx(getContext(), 56), Util.dpToPx(getContext(), 23), 0, (float) Math.hypot(cardLayout.getWidth(), cardLayout.getHeight()));
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    searchLayout.setVisibility(View.VISIBLE);
                    searchLayout.startAnimation(fade_in);
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    initView();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            cardLayout.setVisibility(View.VISIBLE);
            if (cardLayout.getVisibility() == View.VISIBLE) {
                animator.setDuration(300);
                animator.start();
                cardLayout.setEnabled(true);
            }
            fade_in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    pencarian_conteiner.setVisibility(View.VISIBLE);
                    initView();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            cardLayout.setVisibility(View.VISIBLE);
            cardLayout.setEnabled(true);

            pencarian_conteiner.setVisibility(View.VISIBLE);
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            initView();
        }
    }

    public void onQuery(String query) {
        Log.i("app-log query",query);
        String trim = query.trim();
        if(TextUtils.isEmpty(trim)){
            progressBarLayout.setVisibility(GONE);
        }
        if (searchViewResults != null) {
            searchViewResults.updateSequence(trim);
        } else {
            searchViewResults = new SearchViewResults(mContext,trim,this);
            searchViewResults.setListView(pencarian_conteiner);
            searchViewResults.setSearchProvidersListener(this);
        }
    }

    public void hide() {
        if (!isSearchViewVisible()) return;
        mOnSearchListener.searchViewClosed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Animator animatorHide = ViewAnimationUtils.createCircularReveal(cardLayout, cardLayout.getWidth() - Util.dpToPx(getContext(), 56), Util.dpToPx(getContext(), 23), (float) Math.hypot(cardLayout.getWidth(), cardLayout.getHeight()), 0);
            animatorHide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    searchLayout.startAnimation(fade_out);
                    searchLayout.setVisibility(View.INVISIBLE);
                    cardLayout.setVisibility(View.GONE);
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchLayout.getWindowToken(), 0);

                    pencarian_conteiner.setVisibility(View.GONE);
                    clearSearch();
                    setVisibility(View.GONE);
                    initView();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorHide.setDuration(300);
            animatorHide.start();
        } else {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchLayout.getWindowToken(), 0);
            searchLayout.startAnimation(fade_out);
            searchLayout.setVisibility(View.INVISIBLE);
            cardLayout.setVisibility(View.GONE);
            clearSearch();
            setVisibility(View.GONE);
            initView();
        }

    }

    public EditText getSearchView(){
        return mSearchEditText;
    }

    public static WindowManager.LayoutParams getSearchViewLayoutParams(final Activity activity) {
        final Rect rect = new Rect();
        final Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        final int statusBarHeight = rect.top;

        final TypedArray actionBarSize = activity.getTheme().obtainStyledAttributes(
                new int[] {R.attr.actionBarSize});
        actionBarSize.recycle();
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                rect.right,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = statusBarHeight;
        return params;
    }

    private void toggleClearSearchButton(final CharSequence query) {
        mClearSearch.setVisibility(!TextUtils.isEmpty(query) ? View.VISIBLE : View.INVISIBLE);
    }

    private void clearSearch() {
        mSearchEditText.setText("");
        mClearSearch.setVisibility(View.INVISIBLE);
    }

    public void onCancelSearch() {
        if (mOnSearchListener != null) {
            mOnSearchListener.onCancelSearch();
        } else {
            hide();
        }
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            onCancelSearch();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.image_kembali_pencarian) {
            onCancelSearch();
        } else if (id == R.id.img_hapus_pencarian) {
            clearSearch();
        }
    }

    public boolean isSeachWithList() {
        return isSeachWithList;
    }

    public void setSeachWithList(boolean seachWithList) {
        isSeachWithList = seachWithList;
    }
}