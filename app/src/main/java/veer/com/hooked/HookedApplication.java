package veer.com.hooked;

import android.app.Application;

import veer.com.hooked.di.ApplicationComponent;
import veer.com.hooked.di.ContextModule;
import veer.com.hooked.di.DaggerApplicationComponent;

public class HookedApplication extends Application {

    private ApplicationComponent applicationComponent;
    private static HookedApplication instance;


    public synchronized static HookedApplication getInstance() {
        if (instance == null)
            return new HookedApplication();

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationComponent = DaggerApplicationComponent
                .builder()
                .contextModule(new ContextModule(this))
                .build();

    }


    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}

