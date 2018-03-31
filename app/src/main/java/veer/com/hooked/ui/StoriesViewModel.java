package veer.com.hooked.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import veer.com.hooked.database.Story;


public class StoriesViewModel extends ViewModel {

    private StoriesRepository storiesRepository;

    @Inject
    public StoriesViewModel(StoriesRepository storiesRepository) {
        this.storiesRepository = storiesRepository;
    }

    public Completable saveStory(Story story) {
        return storiesRepository.saveStoryToDB(story);
    }

    public LiveData<List<Story>> getStories(int limit) {
        return storiesRepository.getStoriesFromDB(limit);
    }

    public LiveData<List<Story>> performSearch(String query) {
        return storiesRepository.getSearchResults(query);
    }

    public LiveData<List<Story>> getLatestStories() {
        return storiesRepository.getLatestStoriesFromDB();
    }

    public LiveData<List<Story>> getOldStories() {
        return storiesRepository.getOldStoriesFromDB();
    }

    public LiveData<Story> getStory(int id) {
        return storiesRepository.getStoryFromDB(id);
    }
}
