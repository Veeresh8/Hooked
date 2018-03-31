package veer.com.hooked.di;

import javax.inject.Singleton;

import dagger.Component;
import veer.com.hooked.ui.MainActivity;
import veer.com.hooked.ui.SearchActivity;
import veer.com.hooked.ui.StoryDetails;
import veer.com.hooked.ui.StoriesViewModel;
import veer.com.hooked.utils.SortActivity;


@Singleton
@Component(modules = HookedDatabaseModule.class)
public interface ApplicationComponent {
    void inject(StoriesViewModel storiesViewModel);

    void inject(MainActivity mainActivity);

    void inject(SearchActivity searchActivity);

    void inject(StoryDetails storyDetails);

    void inject(SortActivity sortActivity);
}
