package kelijun.com.notes.injector.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kelijun.com.notes.APP;
import kelijun.com.notes.BuildConfig;
import kelijun.com.notes.injector.ContextLifeCycle;
import kelijun.com.sqlite.FinalDb;

/**
 * Created by ${kelijun} on 2018/6/20.
 */
@Module
public class AppModule {
    private final APP app;

    public AppModule(APP app) {
        this.app = app;
    }
    @Provides @Singleton
    APP provideApplication(){
        return app;
    }
    @Provides @Singleton @ContextLifeCycle
    Context provideActivityContext(){
        return app.getApplicationContext();
    }
    @Provides @Singleton
    FinalDb.DaoConfig provideDaoConfig(@ContextLifeCycle("App") Context context) {
        FinalDb.DaoConfig config = new FinalDb.DaoConfig();
        config.setDbName("notes.db");
        config.setDbVersion(2);
        config.setDebug(BuildConfig.DEBUG);
        config.setContext(context);
        config.setDbUpdateListener((db, oldVersion, newVersion) -> {
            if (newVersion == 2 && oldVersion == 1) {
                db.execSQL("ALTER TABLE '" + "notes" + "' ADD COLUMN " +
                        "`createTime`" + " INTEGER;");
                db.execSQL("ALTER TABLE '" + "notes" + "' ADD COLUMN " +
                        "status" + " INTEGER;");
                db.execSQL("ALTER TABLE '" + "notes" + "' ADD COLUMN " +
                        "guid" + " TEXT;");
                db.execSQL("UPDATE '" + "notes" + "' SET type = 0 " +
                        "WHERE type = 1 OR type = 2;");
                db.execSQL("UPDATE '" + "notes" + "' SET type = 1 " +
                        "WHERE type = 3;");
                db.execSQL("UPDATE '" + "notes" + "' SET status = 2 " +
                        "WHERE type = 1;");
            }
        });
        return config;
    }
    @Provides @Singleton
    FinalDb provideFinalDb(FinalDb.DaoConfig config) {
        return FinalDb.create(config);
    }
}
