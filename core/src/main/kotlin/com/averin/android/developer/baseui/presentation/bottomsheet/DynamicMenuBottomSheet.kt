package com.averin.android.developer.baseui.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.base.WITHOUT_MENU_ICON
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.extension.android.view.setDrawableTintColor
import com.averin.android.developer.baseui.extension.androidx.fragment.app.getDrawable
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.FrMultiselectMenuBinding

class DynamicMenuBottomSheet : BaseBottomSheetDialog() {

    private val binding by viewBinding(FrMultiselectMenuBinding::bind)
    private val dynamicMenuTitle: String? by lazy {
        arguments?.getString(ARG_DIALOG_TITLE)
    }

    override val layoutRes: Int = R.layout.fr_multiselect_menu

    var items: List<MenuItem> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDynamicMenuTitle.text = dynamicMenuTitle
        binding.tvDynamicMenuTitle.isVisible = !dynamicMenuTitle.isNullOrEmpty()
        binding.ltMenuItems.removeAllViews()
        items.forEach { item ->
            bindItem(item)
        }
    }

    private fun bindItem(item: MenuItem) {
        binding.run {
            val menuView = LayoutInflater.from(context).inflate(R.layout.p_menu_item, null)
            menuView.findViewById<TextView>(R.id.tvMenuItem).run {
                text = getString(item.titleRes)
                setOnClickListener {
                    item.onClickListener.invoke()
                    dismissAllowingStateLoss()
                }
                val iconRes = if (item.iconRes != WITHOUT_MENU_ICON) {
                    getDrawable(item.iconRes)
                } else {
                    null
                }
                setCompoundDrawablesWithIntrinsicBounds(iconRes, null, null, null)
                setDrawableTintColor(context.getColorKtx(R.color.textColorSecondary))
            }
            ltMenuItems.addView(menuView)
        }
    }

    class MenuItem(
        val titleRes: Int,
        val iconRes: Int,
        var onClickListener: (() -> Unit)
    )

    companion object {
        private const val ARG_DIALOG_TITLE = "dynamicMenuTitle"
        fun newInstance(dynamicMenuTitle: String? = null) = DynamicMenuBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_DIALOG_TITLE, dynamicMenuTitle)
            }
        }
    }
}
