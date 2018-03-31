package veer.com.hooked.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
import veer.com.hooked.database.Collaborators;
import veer.com.hooked.database.Story;
import veer.com.hooked.utils.EmptyRecyclerView;
import veer.com.hooked.utils.GeneralUtils;
import veer.com.hooked.utils.SimpleDividerItemDecoration;

public class StoryDetails extends AppCompatActivity {


    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.tv_story_title)
    TextView tvStoryTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_collaborators)
    TextView tvCollaborators;
    @BindView(R.id.tv_total_words)
    TextView tvTotalWords;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.item_root_layout)
    RelativeLayout itemRootLayout;
    @BindView(R.id.rv_story_details)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.tv_empty_view_helper)
    TextView emptyView;

    private StoriesViewModel storiesViewModel;
    private StoriesDetailsAdapter adapter;
    private Story story;
    private List<Collaborators> collaboratorsList = new ArrayList<>();

    @OnClick(R.id.fab_create_story)
    public void onViewClicked() {
        if (canWriteMessage())
            writeMessage();

    }

    private boolean canWriteMessage() {

        if (story.getCollaboratorsList() == null || story.getCollaboratorsList().isEmpty())
            return true;

        if (GeneralUtils.getStoryCount(story) >= 500) {
            Toast.makeText(this, "Max words for a story is 500", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (GeneralUtils.wordsLeftForCurrentUser(story) >= 50) {
            Toast.makeText(this, "You have exhausted your quota", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void writeMessage() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater == null)
            return;

        View view = inflater.inflate(R.layout.layout_create_message, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);

        final EditText edtMessage = view.findViewById(R.id.edt_message);
        final TextInputLayout tiStory = view.findViewById(R.id.messageInputLayout);


        if (story.getCollaboratorsList() != null) {
            tiStory.setCounterMaxLength(GeneralUtils.wordsLeftForCurrentUserCount(story));
            edtMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(GeneralUtils.wordsLeftForCurrentUserCount(story))});
        }


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(dialog1 -> {
            Button btnPositive = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
            btnPositive.setOnClickListener(v -> {
                if (edtMessage.getText().toString().trim().length() == 0) {
                    tiStory.setError("Invalid Message");
                    return;
                }
                saveMessage(edtMessage.getText().toString().trim());
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    private void saveMessage(String message) {
        Collaborators collaborators = new Collaborators();
        collaborators.setAuthor(GeneralUtils.getFirebaseUserName());
        collaborators.setMessage(message);
        collaborators.setDate(GeneralUtils.getCurrentDate());
        collaborators.setUID(GeneralUtils.getFirebaseUID());
        collaborators.setTimestamp(System.currentTimeMillis());
        collaborators.setWordCount(message.length());
        collaboratorsList.add(collaborators);

        story.setCollaboratorsList(collaboratorsList);
        story.setTotalWords(GeneralUtils.getStoryCount(story));

        storiesViewModel.saveStory(story)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(StoryDetails.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(StoryDetails.this, "Failed to save story!", Toast.LENGTH_SHORT).show();
                        Log.d(getClass().getSimpleName(), e.getMessage());
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);
        ButterKnife.bind(this);
        initUI();
        initViewModel();
        checkIntent();
    }

    private void initUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setEmptyView(emptyView);
        adapter = new StoriesDetailsAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void checkIntent() {
        if (getIntent().getStringExtra(MainActivity.STORY_ID) != null) {
            storiesViewModel.getStory(Integer.valueOf(getIntent().getStringExtra(MainActivity.STORY_ID))).observe(this, story -> {
                if (story == null)
                    return;

                setStoryDetails(story);
            });
        }
    }

    private void setStoryDetails(Story story) {
        this.story = story;
        tvAuthor.setText(story.getAuthor());
        tvDate.setText(story.getDate());
        tvStoryTitle.setText(story.getTitle());
        tvTotalWords.setText(GeneralUtils.getWordCountPrefix(story.getTotalWords()));
        if (story.getCollaboratorsList() != null && story.getCollaboratorsList().size() > 0)
            tvCollaborators.setText(GeneralUtils.getCollaboratorPrefix(GeneralUtils.getCollaborators(story.getCollaboratorsList())));
        else
            tvCollaborators.setText(R.string.empty_collaborators);


        if (story.getCollaboratorsList() != null)
            adapter.addCollaborators(story.getCollaboratorsList());
    }

    private void initViewModel() {
        HookedApplication.getInstance().getApplicationComponent().inject(this);
        storiesViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoriesViewModel.class);
    }

}
