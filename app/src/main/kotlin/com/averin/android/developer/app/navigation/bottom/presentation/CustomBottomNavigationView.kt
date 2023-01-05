package com.averin.android.developer.app.navigation.bottom.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.R
import com.averin.android.developer.app.main.navigation.BottomTab
import com.averin.android.developer.app.main.navigation.BottomTab1
import com.averin.android.developer.app.main.navigation.BottomTab2
import com.averin.android.developer.app.main.navigation.BottomTab3
import com.averin.android.developer.app.main.navigation.BottomTab4
import com.averin.android.developer.app.main.navigation.BottomTab5
import com.averin.android.developer.base.NO_VALUE_INT
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.extension.android.view.setNavigationIcon
import com.averin.android.developer.databinding.WNavigationViewBinding

class CustomBottomNavigationView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WNavigationViewBinding::bind)
    private val ltItem1: ViewGroup by lazy { binding.ltItem1 }
    private val ltItem2: ViewGroup by lazy { binding.ltItem2 }
    private val ltItem3: ViewGroup by lazy { binding.ltItem3 }
    private val ltItem4: ViewGroup by lazy { binding.ltItem4 }
    private val ltItem5: ViewGroup by lazy { binding.ltItem5 }
    private val ivIcon1: ImageView by lazy { binding.ivIcon1 }
    private val ivIcon2: ImageView by lazy { binding.ivIcon2 }
    private val ivIcon3: ImageView by lazy { binding.ivIcon3 }
    private val ivIcon4: ImageView by lazy { binding.ivIcon4 }
    private val ivIcon5: ImageView by lazy { binding.ivIcon5 }
    private val tvTitle1: TextView by lazy { binding.tvTitle1 }
    private val tvTitle2: TextView by lazy { binding.tvTitle2 }
    private val tvTitle3: TextView by lazy { binding.tvTitle3 }
    private val tvTitle4: TextView by lazy { binding.tvTitle4 }
    private val tvTitle5: TextView by lazy { binding.tvTitle5 }

    private var menuItems: List<NavigationItemView> = listOf()

    var notificationCount: Int = 0
        set(value) {
            field = value
            menuItems[TAB_4].refresh()
        }

    var onTabClickListener: ((BottomTab) -> Unit)? = null

    init {
        View.inflate(context, R.layout.w_navigation_view, this)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomBottomNavigationView, 0, 0)
        try {
            /* nav menu 1 start */
            ivIcon1.setNavigationIcon(a.getResourceId(R.styleable.CustomBottomNavigationView_navIcon1, NO_VALUE_INT))
            tvTitle1.text = a.getString(R.styleable.CustomBottomNavigationView_navTitle1)
            /* nav menu 1 end */

            /* nav menu 2 start */
            ivIcon2.setNavigationIcon(a.getResourceId(R.styleable.CustomBottomNavigationView_navIcon2, NO_VALUE_INT))
            tvTitle2.text = a.getString(R.styleable.CustomBottomNavigationView_navTitle2)
            /* nav menu 2 end */

            /* nav menu 3 start */
            ivIcon3.setNavigationIcon(a.getResourceId(R.styleable.CustomBottomNavigationView_navIcon3, NO_VALUE_INT))
            tvTitle3.text = a.getString(R.styleable.CustomBottomNavigationView_navTitle3)
            /* nav menu 3 end */

            /* nav menu 4 start */
            ivIcon4.setNavigationIcon(a.getResourceId(R.styleable.CustomBottomNavigationView_navIcon4, NO_VALUE_INT))
            tvTitle4.text = a.getString(R.styleable.CustomBottomNavigationView_navTitle4)
            /* nav menu 4 end */

            /* nav menu 5 start */
            ivIcon5.setNavigationIcon(a.getResourceId(R.styleable.CustomBottomNavigationView_navIcon5, NO_VALUE_INT))
            tvTitle5.text = a.getString(R.styleable.CustomBottomNavigationView_navTitle5)
            /* nav menu 5 end */

            /* whole navigation panel start */
            menuItems = listOf(
                NavigationItemView(BottomTab1, ltItem1, ivIcon1, tvTitle1),
                NavigationItemView(BottomTab2, ltItem2, ivIcon2, tvTitle2),
                NavigationItemView(BottomTab3, ltItem3, ivIcon3, tvTitle3),
                NavigationItemView(BottomTab4, ltItem4, ivIcon4, tvTitle4),
                NavigationItemView(BottomTab5, ltItem5, ivIcon5, tvTitle5)
            )
            /* whole navigation panel end */

            /* select first item by default start */
            setCurrentTab(TAB_1)
            /* select first item by default end */
        } finally {
            a.recycle()
        }
    }

    fun setCurrentTab(id: Int, isWithoutNotify: Boolean = true) {
        if (id < 0 || id > menuItems.size - 1) {
            throw IllegalArgumentException("position=$id")
        }
        for (i in menuItems.indices) {
            val menuItem: NavigationItemView = menuItems[i]
            menuItem.isSelected = id == menuItem.bottomTab.menuId
        }
        if (isWithoutNotify) {
            onTabClickListener?.invoke(menuItems[id].bottomTab)
        }
    }

    private inner class NavigationItemView(
        val bottomTab: BottomTab,
        private val view: ViewGroup,
        private val imageView: ImageView,
        private val textView: TextView,
    ) {
        var isSelected = false
            set(value) {
                field = value
                refresh()
            }

        fun refresh() {
            val tabTxtColor = if (isSelected) {
                context.getColorKtx(R.color.colorPrimary)
            } else {
                context.getColorKtx(R.color.typography_title)
            }
            textView.setTextColor(tabTxtColor)

            val tabIconRes = generateIconForTab()
            imageView.setImageResource(tabIconRes)
        }

        @DrawableRes
        private fun generateIconForTab(): Int {
            return when (this.bottomTab.menuId) {
                TAB_1 -> {
                    if (isSelected) {
                        R.drawable.ic_tab_vacancy_selected
                    } else {
                        R.drawable.ic_tab_vacancy_default
                    }
                }
                TAB_2 -> {
                    if (isSelected) {
                        R.drawable.ic_tab_candidates_selected
                    } else {
                        R.drawable.ic_tab_candidates_default
                    }
                }
                TAB_3 -> {
                    if (isSelected) {
                        R.drawable.ic_tab_feed_selected
                    } else {
                        R.drawable.ic_tab_feed_default
                    }
                }
                TAB_4 -> {
                    if (isSelected) {
                        if (notificationCount > 0) {
                            R.drawable.ic_tab_unread_selected
                        } else {
                            R.drawable.ic_tab_chat_selected
                        }
                    } else {
                        if (notificationCount > 0) {
                            R.drawable.ic_tab_unread_default
                        } else {
                            R.drawable.ic_tab_chat_default
                        }
                    }
                }
                TAB_5 -> {
                    if (isSelected) {
                        R.drawable.ic_tab_more_selected
                    } else {
                        R.drawable.ic_tab_more_default
                    }
                }
                else -> throw IllegalArgumentException("bottomTabId=${this.bottomTab.menuId}")
            }
        }

        init {
            view.setOnClickListener { setCurrentTab(bottomTab.menuId) }
        }
    }

    companion object {
        const val TAB_1 = 0
        const val TAB_2 = 1
        const val TAB_3 = 2
        const val TAB_4 = 3
        const val TAB_5 = 4
    }
}
