package veer.com.hooked.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StoriesDAO {

    @Query("SELECT * FROM Story LIMIT :limit")
    LiveData<List<Story>> getStoriesList(int limit);

    @Query("SELECT * FROM Story WHERE id =:id")
    LiveData<Story> getStory(long id);

    @Query("SELECT * FROM Story WHERE isFavorite =:bool")
    LiveData<List<Story>> getFavoriteStories(Boolean bool);

    @Query("SELECT * FROM Story ORDER BY Story.timestamp desc")
    LiveData<List<Story>> getLatestStories();

    @Query("SELECT * FROM Story WHERE title LIKE '%' || :searchTitle || '%'")
    LiveData<List<Story>> searchStoriesByTitle(String searchTitle);

    @Query("SELECT * FROM Story ORDER BY  Story.timestamp  asc")
    LiveData<List<Story>> getOldStories();

    @Update
    void updateStory(Story story);

    @Delete
    void deleteStory(Story story);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveStory(Story story);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToFavorites(Story story);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void removeFromFavorites(Story story);

}
