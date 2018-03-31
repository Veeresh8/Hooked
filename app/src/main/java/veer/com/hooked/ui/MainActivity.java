package veer.com.hooked.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import veer.com.hooked.HookedApplication;
import veer.com.hooked.R;
import veer.com.hooked.database.Story;
import veer.com.hooked.utils.EmptyRecyclerView;
import veer.com.hooked.utils.GeneralUtils;
import veer.com.hooked.utils.SimpleDividerItemDecoration;
import veer.com.hooked.utils.SortActivity;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.rv_stories)
    EmptyRecyclerView recyclverView;
    @BindView(R.id.tv_empty_list_helper)
    TextView emptyView;
    @BindView(R.id.fab_create_story)
    FloatingActionButton fabCreateStory;
    private StoriesViewModel storiesViewModel;
    private Handler handler = new Handler();
    private int LIMIT = 10;
    private StoriesAdapter storiesAdapter;
    public static final String STORY_ID = "story_id";
    public static final String SORT_TYPE = "sort_type";
    private BottomSheetDialog mBottomSheetDialog;
    private View sheetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
        initViewModel();
        initStories(LIMIT);
    }

    private void initUI() {
        recyclverView.setLayoutManager(new LinearLayoutManager(this));
        recyclverView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclverView.setEmptyView(emptyView);
        mBottomSheetDialog = new BottomSheetDialog(this);
        sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sort_options, null);
        mBottomSheetDialog.setContentView(sheetView);
        storiesAdapter = new StoriesAdapter() {
            @Override
            public void loadMore() {
                if (storiesAdapter.getItemCount() < 10)
                    return;

                showSnackBar();
                handler.postDelayed(() -> {
                    LIMIT = LIMIT + 10;
                    initStories(LIMIT);
                }, 3000);

            }

            @Override
            public void onClick(int id) {
                gotoStoryDetails(id);
            }
        };
        recyclverView.setAdapter(storiesAdapter);
    }

    private void gotoStoryDetails(int id) {
        Intent intent = new Intent(this, StoryDetails.class);
        intent.putExtra(STORY_ID, String.valueOf(id));
        startActivity(intent);
    }

    private void showSnackBar() {
        Snackbar.make(findViewById(android.R.id.content), "Loading more stories!", 3000).show();
    }

    private void initStories(int LIMIT) {
        storiesViewModel.getStories(LIMIT).observe(this, stories -> {
            if (stories == null)
                return;

            storiesAdapter.addStories(stories);

        });
    }

    private void initViewModel() {
        HookedApplication.getInstance().getApplicationComponent().inject(this);
        storiesViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoriesViewModel.class);
    }

    @OnClick(R.id.fab_create_story)
    public void onViewClicked() {
        createDialogForStory();
    }

    @OnClick(R.id.action_search)
    public void onSearchClicked() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @OnClick(R.id.action_logout)
    public void onLogout() {
        createLogoutDialog();
    }

    private void createLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }).setNegativeButton("No", null).show();
    }

    @OnClick(R.id.action_sort)
    public void onSort() {

        if (storiesAdapter.getItemCount() == 0) {
            Toast.makeText(this, "No stories to sort", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView titleAsceding = sheetView.findViewById(R.id.tv_title_asc);
        TextView titleDescending = sheetView.findViewById(R.id.tv_title_desc);
        mBottomSheetDialog.show();
        titleAsceding.setOnClickListener(v -> gotoSortActivity(SortActivity.SORT_TYPE_TIMESTAMP_ASC));
        titleDescending.setOnClickListener(v -> gotoSortActivity(SortActivity.SORT_TYPE_TIMESTAMP_DESC));

    }

    private void gotoSortActivity(int type) {
        mBottomSheetDialog.dismiss();
        Intent intent = new Intent(this, SortActivity.class);
        intent.putExtra(SORT_TYPE, type);
        startActivity(intent);
    }

    private void createDialogForStory() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater == null)
            return;

        View view = inflater.inflate(R.layout.layout_create_story, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);

        final EditText edtStory = view.findViewById(R.id.edt_story_title);
        final TextInputLayout tiStory = view.findViewById(R.id.story_layout);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(dialog1 -> {
            Button btnPositive = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
            btnPositive.setOnClickListener(v -> {
                if (edtStory.getText().toString().trim().length() == 0) {
                    tiStory.setError("Invalid Story Title");
                    return;
                }
                saveStory(edtStory.getText().toString().trim());
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    private void saveStory(String storyTitle) {
        Story story = new Story();
        story.setTitle(storyTitle);
        story.setAuthor(GeneralUtils.getFirebaseUserName());
        story.setDate(GeneralUtils.getCurrentDate());
        story.setTimestamp(System.currentTimeMillis());

        storiesViewModel.saveStory(story)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Failed to save story!", Toast.LENGTH_SHORT).show();
                        Log.d(getClass().getSimpleName(), e.getMessage());
                    }
                });

    }
}
