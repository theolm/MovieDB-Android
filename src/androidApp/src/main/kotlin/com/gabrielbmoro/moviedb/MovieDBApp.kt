package com.gabrielbmoro.moviedb

import android.app.Application
import com.gabrielbmoro.moviedb.core.di.coreModule
import com.gabrielbmoro.moviedb.data.di.androidDataModule
import com.gabrielbmoro.moviedb.details.di.featureDetailsModule
import com.gabrielbmoro.moviedb.di.appModule
import com.gabrielbmoro.moviedb.domain.di.domainModule
import com.gabrielbmoro.moviedb.movies.di.featureMoviesModule
import com.gabrielbmoro.moviedb.data.di.dataModule
import com.gabrielbmoro.moviedb.search.di.featureSearchMovieModule
import com.gabrielbmoro.moviedb.wishlist.di.featureWishlistModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MovieDBApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@MovieDBApp)

            modules(
                appModule,
                coreModule,
                domainModule,
                androidDataModule,
                dataModule,
                featureDetailsModule,
                featureMoviesModule,
                featureSearchMovieModule,
                featureWishlistModule
            )
        }
    }
}
