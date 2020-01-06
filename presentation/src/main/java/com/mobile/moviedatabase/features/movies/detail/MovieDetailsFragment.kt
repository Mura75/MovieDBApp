package com.mobile.moviedatabase.features.movies.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.ui.NavigationUI
import com.mobile.data.NetworkConstants
import com.mobile.domain.Movie

import com.mobile.moviedatabase.R
import com.mobile.moviedatabase.core.base.BaseFragment
import com.mobile.moviedatabase.core.di.GlideApp
import com.mobile.moviedatabase.core.utils.AppConstants
import com.mobile.moviedatabase.features.login.AuthViewModel
import org.koin.android.ext.android.inject
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment() {

    companion object {
        fun newInstance(data: Bundle? = null) = MovieDetailsFragment().apply {
            arguments = data
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var ivPoster: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvGenre: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvDescription: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setData()
    }

    override fun bindViews(view: View) = with(view) {
        progressBar = findViewById(R.id.progressBar)
        ivPoster = findViewById(R.id.ivPoster)
        tvName = findViewById(R.id.tvName)
        tvGenre = findViewById(R.id.tvGenre)
        tvDate = findViewById(R.id.tvDate)
        tvRating = findViewById(R.id.tvRating)
        tvDescription = findViewById(R.id.tvDescription)
    }

    override fun setData() {
        arguments?.getInt(AppConstants.MOVIE_ID)?.let { id ->
            viewModel.getMovieDetail(movieId = id)
        }
        viewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is MovieDetailViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is MovieDetailViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                }
                is MovieDetailViewModel.State.Result -> {
                    GlideApp.with(this)
                        .load("${NetworkConstants.BACKDROP_BASE_URL}${result.movie.backdropPath}")
                        .into(ivPoster)

                    tvName.text = result.movie.title
                    tvDescription.text = result.movie.overview
                    tvGenre.text = result.movie.genres?.first()?.name
                    tvDate.text = result.movie.releaseDate
                    tvRating.text = "${result.movie.voteAverage}/10"
                }
                is MovieDetailViewModel.State.Error -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
                is MovieDetailViewModel.State.IntError -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
