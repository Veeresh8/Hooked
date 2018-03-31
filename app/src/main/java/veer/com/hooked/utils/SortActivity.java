package veer.com.hooked.utils;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import veer.com.hooked.HookedApplication;
import veer.com.hooked.ui.MainActivity;
import veer.com.hooked.R;
import veer.com.hooked.ui.StoryDetails;
import veer.com.hooked.database.Story;
import veer.com.hooked.ui.StoredAdapter;
import veer.com.hooked.ui.StoriesViewModel;

public class SortActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.rv_stories)
    EmptyRecyclerView recyclverView;
    @BindView(R.id.tv_empty_list_helper)
    TextView emptyView;
    @BindView(R.id.toolbar_header)
    TextView header;
    private StoriesViewModel storiesViewModel;
    private StoredAdapter adapter;
    public static final String STORY_ID = "story_id";

    private List<Story> storyList = new ArrayList<>();
    public static final int SORT_TYPE_TIMESTAMP_DESC = 0;
    public static final int SORT_TYPE_TIMESTAMP_ASC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        ButterKnife.bind(this);
        initUI();
        initViewModel();
        initStories(getIntent().getIntExtra(MainActivity.SORT_TYPE, 0));
    }


    private void initUI() {
        recyclverView.setLayoutManager(new LinearLayoutManager(this));
        recyclverView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclverView.setEmptyView(emptyView);
        adapter = new StoredAdapter(storyList) {
            @Override
            public void loadMore() {

            }

            @Override
            public void onClick(int id) {
                gotoStoryDetails(id);
            }
        };
        recyclverView.setAdapter(adapter);
    }

    private void gotoStoryDetails(int id) {
        Intent intent = new Intent(this, StoryDetails.class);
        intent.putExtra(STORY_ID, String.valueOf(id));
        startActivity(intent);
    }

    private void initStories(int type) {
        if (type == SORT_TYPE_TIMESTAMP_ASC) {
            header.setText("Old Stories");
            storiesViewModel.getOldStories().observe(this, stories -> {
                if (stories == null)
                    return;

                storyList.addAll(stories);
                adapter.notifyDataSetChanged();

            });
        } else if (type == SORT_TYPE_TIMESTAMP_DESC) {
            header.setText("Latest Stories");
            storiesViewModel.getLatestStories().observe(this, stories -> {
                if (stories == null)
                    return;
                storyList.addAll(stories);
                adapter.notifyDataSetChanged();
            });
        }

    }

    private void initViewModel() {
        HookedApplication.getInstance().getApplicationComponent().inject(this);
        storiesViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoriesViewModel.class);
    }
}
