package veer.com.hooked.di;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import veer.com.hooked.database.HookedDatabase;
import veer.com.hooked.ui.StoriesRepository;


@Module (includes = ContextModule.class)
public class HookedDatabaseModule {

    @Provides
    @Singleton
    StoriesRepository storiesRepository(HookedDatabase hookedDatabase) {
        return new StoriesRepository(hookedDatabase);
    }

    @Provides
    @Singleton
    HookedDatabase hookedDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), HookedDatabase.class, "Hooked_DB").build();
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(StoriesRepository storiesRepository) {
        return new CustomViewModelFactory(storiesRepository);
    }
}
