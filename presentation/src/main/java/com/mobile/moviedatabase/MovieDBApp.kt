package com.mobile.moviedatabase

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.mobile.data.module.NetworkModule
import com.mobile.data.module.RepositoryModule
import com.mobile.data.module.StorageModule
import com.mobile.moviedatabase.core.base.BaseFragment
import com.mobile.moviedatabase.core.di.DaggerMainComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MovieDBApp : Application(), HasAndroidInjector, Application.ActivityLifecycleCallbacks {

    @Inject
    internal lateinit var dispatchingAndroidInjectorAny: DispatchingAndroidInjector<Any>

    private val appComponent by lazy {
        DaggerMainComponent.builder()
            .application(this)
            .networkModule(NetworkModule(this))
            .storageModule(StorageModule(this))
            .repositoryModule(RepositoryModule())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        registerActivityLifecycleCallbacks(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjectorAny

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        dispatchingAndroidInjectorAny.maybeInject(activity)

        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(
                            fm: FragmentManager,
                            fragment: Fragment,
                            savedInstanceState: Bundle?
                        ) {
                            if (fragment is BaseFragment) {
                                dispatchingAndroidInjectorAny.maybeInject(fragment)
                            }
                        }
                },
                true)
        }
    }

    override fun onActivityResumed(activity: Activity) {

    }
}