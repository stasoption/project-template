package com.averin.android.developer.github.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.content.getDrawableKtx
import com.averin.android.developer.baseui.widget.recycler.decoration.DefaultVerticalSpacingItemDecoration
import com.averin.android.developer.baseui.widget.recycler.decoration.DividerItemDecoration
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.WProjectsBinding
import com.averin.android.developer.github.domain.model.GitHubProject
import com.averin.android.developer.github.presentation.vacancies.ProjectsAdapter
import com.facebook.shimmer.ShimmerFrameLayout

class GitHubProjectsView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WProjectsBinding::bind)
    private val vPlaceholder: ShimmerFrameLayout by lazy { binding.vPlaceholder }
    private val rvVacancies: RecyclerView by lazy { binding.rvVacancies }
    private val diffUtil = ProjectsAdapter.ProjectsDiffUtil()
    private val projectsAdapter = ProjectsAdapter { onVacancyClickListener?.invoke(it) }

    var onVacancyClickListener: ((GitHubProject) -> Unit)? = null

    var items: List<GitHubProject> = listOf()
        set(value) {
            field = value
            diffUtil.oldList = projectsAdapter.items
            diffUtil.newList = field
            DiffUtil.calculateDiff(diffUtil, false).dispatchUpdatesTo(projectsAdapter)
            projectsAdapter.items = field.toMutableList()
        }
        get() {
            return projectsAdapter.items
        }

    var isLoading: Boolean = false
        set(value) {
            field = value
            vPlaceholder.isVisible = value
            rvVacancies.isVisible = !value
        }

    init {
        View.inflate(context, R.layout.w_projects, this)
        rvVacancies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = projectsAdapter
            addItemDecoration(
                DefaultVerticalSpacingItemDecoration(
                    context = context,
                    betweenItems = 0
                )
            )
            addItemDecoration(
                DividerItemDecoration(
                    dividerAfterDrawable = context.getDrawableKtx(R.drawable.bg_divider)
                )
            )
        }
    }
}
