package veer.com.hooked.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import veer.com.hooked.HookedApplication;
import veer.com.hooked.R;
import veer.com.hooked.utils.EmptyRecyclerView;
import veer.com.hooked.utils.SimpleDividerItemDecoration;

public class SearchActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private StoriesViewModel storiesViewModel;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.rv_searched_stories)
    EmptyRecyclerView recyclerView;
    private StoriesAdapter storiesAdapter;
    @BindView(R.id.tv_empty_view_helper)
    TextView emptyView;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initUI();
        initViewModel();
        setUpSearch();
    }

    private void initUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        storiesAdapter = new StoriesAdapter() {
            @Override
            public void loadMore() {

            }

            @Override
            public void onClick(int id) {
                gotoStoryDetails(id);
            }
        };
        recyclerView.setAdapter(storiesAdapter);
    }

    private void gotoStoryDetails(int id) {
        Intent intent = new Intent(this, StoryDetails.class);
        intent.putExtra(MainActivity.STORY_ID, String.valueOf(id));
        startActivity(intent);
    }

    private void initViewModel() {
        HookedApplication.getInstance().getApplicationComponent().inject(this);
        storiesViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoriesViewModel.class);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    private void setUpSearch() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView.setHint("Story Title");
        searchView.setVoiceSearch(false);
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {

                } else if (newText.length() > 3) {
                    showSearchResults(newText.trim());
                }
                return false;
            }
        });
    }

    private void showSearchResults(String query) {
        storiesViewModel.performSearch(query).observe(this, storyList -> {
            if (storyList == null)
                return;

            storiesAdapter.clearList();
            storiesAdapter.addStories(storyList);
        });
    }


    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
