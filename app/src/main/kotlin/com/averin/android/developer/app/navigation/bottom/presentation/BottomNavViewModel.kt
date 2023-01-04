package com.averin.android.developer.app.navigation.bottom.presentation

import androidx.lifecycle.MutableLiveData
import com.averin.android.developer.app.main.navigation.BottomTab
import com.averin.android.developer.app.main.navigation.BottomTab3
import com.averin.android.developer.baseui.extension.androidx.lifecycle.SingleLiveEvent
import com.averin.android.developer.baseui.presentation.BaseViewModel
import java.util.ArrayDeque
import java.util.Deque

class BottomNavViewModel : BaseViewModel() {
    private val tabStack: Deque<BottomTab> = ArrayDeque()

    val selectedTab = MutableLiveData<BottomTab?>(BottomTab3)
    val updateNotificationEvent = SingleLiveEvent<Int>()

    init {
        tabStack.addFirst(BottomTab3)
    }

    fun selectTab(tab: BottomTab) {
        tabStack.removeLastOccurrence(tab)
        tabStack.addLast(tab)
        selectedTab.value = tab
    }

    fun openPreviousTab(): BottomTab? {
        tabStack.pollLast()
        val previousTab: BottomTab? = tabStack.peekLast()
        selectedTab.value = previousTab
        return previousTab
    }
}
