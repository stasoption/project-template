package com.averin.android.developer.github.presentation.projects

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.presentation.adapter.BaseAdapter
import com.averin.android.developer.baseui.presentation.adapter.BaseViewHolder
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.LiGithubProjectBinding
import com.averin.android.developer.github.domain.model.GitHubProject

class ProjectsAdapter(
    onItemClickListener: ((GitHubProject) -> Unit)? = null
) : BaseAdapter<GitHubProject, BaseViewHolder>(onItemClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return VacancyViewHolder(parent.inflateViewHolder(R.layout.li_github_project))
    }

    private inner class VacancyViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val binding by viewBinding(LiGithubProjectBinding::bind)
        override fun bindData(position: Int) {
            val project = items[position]
            binding.run {
                tvProjectName.text = project.fullName
                tvProjectVisibility.text = project.visibility
            }
        }
    }

    class ProjectsDiffUtil : DiffUtil.Callback() {
        var oldList = listOf<GitHubProject>()
        var newList = listOf<GitHubProject>()

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
