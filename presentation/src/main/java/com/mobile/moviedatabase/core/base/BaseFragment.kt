package com.mobile.moviedatabase.core.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mobile.moviedatabase.core.di.injectFeature
import dagger.android.support.DaggerFragment

abstract class BaseFragment : DaggerFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //injectFeature()
    }

    protected abstract fun bindViews(view: View)

    protected abstract fun setData()
}