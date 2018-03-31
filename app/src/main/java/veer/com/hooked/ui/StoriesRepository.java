package veer.com.hooked.ui;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import veer.com.hooked.database.HookedDatabase;
import veer.com.hooked.database.Story;

public class StoriesRepository {

    private HookedDatabase hookedDatabase;

    @Inject
    public StoriesRepository(HookedDatabase hookedDatabase) {
        this.hookedDatabase = hookedDatabase;
    }

    Completable saveStoryToDB(final Story story) {
        return Completable.fromAction(() -> hookedDatabase.storiesDAO().saveStory(story));
    }

    LiveData<List<Story>> getStoriesFromDB(int limit) {
        return hookedDatabase.storiesDAO().getStoriesList(limit);
    }

    LiveData<List<Story>> getLatestStoriesFromDB() {
        return hookedDatabase.storiesDAO().getLatestStories();
    }

    LiveData<List<Story>> getOldStoriesFromDB() {
        return hookedDatabase.storiesDAO().getOldStories();
    }

    LiveData<List<Story>> getSearchResults(String query) {
        return hookedDatabase.storiesDAO().searchStoriesByTitle(query);
    }

    LiveData<Story> getStoryFromDB(int id) {
        return hookedDatabase.storiesDAO().getStory(id);
    }
}
