package veer.com.hooked.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import veer.com.hooked.ui.StoriesRepository;
import veer.com.hooked.ui.StoriesViewModel;


@Singleton
public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private final StoriesRepository storiesRepository;

    @Inject
    public CustomViewModelFactory(StoriesRepository storiesRepository) {
        this.storiesRepository = storiesRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StoriesViewModel.class))
            return (T) new StoriesViewModel(storiesRepository);
        else
            throw new IllegalArgumentException("ViewModel Not Found");

    }
}
