package veer.com.hooked.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;


@Database(entities = {Story.class}, version = 1)
@TypeConverters(CollaboratorsConverter.class)
public abstract class HookedDatabase extends RoomDatabase {
    public abstract StoriesDAO storiesDAO();
}
