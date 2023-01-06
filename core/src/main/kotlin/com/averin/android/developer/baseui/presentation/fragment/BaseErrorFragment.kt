package com.averin.android.developer.baseui.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.error.ErrorView
import com.averin.android.developer.baseui.error.GlobalErrorHandler
import com.averin.android.developer.baseui.extension.androidx.lifecycle.observeSafe
import com.averin.android.developer.baseui.presentation.LoadingObserver
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.BaseErrorViewBinding
import com.averin.android.developer.core.databinding.WLoadingViewBinding

abstract class BaseErrorFragment(contentLayoutId: Int = 0) : BaseFragment(contentLayoutId) {

    protected val loadingBinding by viewBinding(WLoadingViewBinding::bind, R.id.loadingView)
    protected val errorBinding by viewBinding(BaseErrorViewBinding::bind, R.id.errorInfoView)

    var isProgressLoading: Boolean = false
        set(value) {
            field = value
            loadingBinding.loadingView.isVisible = value
        }

    val errorView: ErrorView = object : ErrorView {
        override fun showHttpException(t: Throwable) {
            errorBinding.errorInfoView.titleView.text = t.message
            showErrorView()
        }
        override fun showNetworkError(endpoint: String?, t: Throwable) {
            errorBinding.errorInfoView.titleView.text = getString(R.string.error_network)
            showErrorView()
        }
        override fun showUnexpectedError(t: Throwable) {
            errorBinding.errorInfoView.titleView.text = t.message
            showErrorView()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorBinding.errorInfoView.buttonView.onClickListener = {
            onErrorViewButtonClicked()
        }
        viewModel?.let {
            setupErrorHandling(it.error)
            setupLoading(it.loading)
        }
    }

    @CallSuper
    open fun hideErrorView() {
        getErrorViews().forEach { it.visibility = View.GONE }
        getContentViews().forEach { it.visibility = View.VISIBLE }
    }

    open fun showErrorView() {
        getErrorViews().forEach { it.visibility = View.VISIBLE }
        getContentViews().forEach { it.visibility = View.INVISIBLE }
    }

    protected open fun getErrorViews(): Array<View> = arrayOf(errorBinding.errorInfoView)

    protected open fun getContentViews(): Array<View> = arrayOf()

    protected open fun setupErrorHandling(errorLiveData: LiveData<Throwable?>) {
        val errorHandler = GlobalErrorHandler(errorView)
        errorLiveData.observeSafe(viewLifecycleOwner) {
            errorHandler.handleError(it)
        }
    }

    protected fun setupLoading(loadingLiveData: LiveData<Boolean>?) {
        createLoadingObserver()?.let { throwable ->
            loadingLiveData?.observe(viewLifecycleOwner, throwable)
        }
    }

    open fun createLoadingObserver(): Observer<Boolean>? =
        LoadingObserver(loadingBinding.loadingView)

    @CallSuper
    protected open fun onErrorViewButtonClicked() {
        hideErrorView()
    }
}
