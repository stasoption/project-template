package com.averin.android.developer.baseui.widget.recycler

import android.os.Parcelable
import android.util.SparseArray
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Запоминает и восстанавливает состояние скролла.
 * Используется для элементов списка в которых есть [RecyclerView], которому требуется сохранять позицию.
 * Необходимо вызвать [saveState] в onViewRecycled и [restoreState] в onBind
 */
class NestedScrollKeeper {
    private val states = SparseArray<Parcelable?>()

    fun saveState(key: Int, recyclerView: RecyclerView) {
        (recyclerView.layoutManager as? LinearLayoutManager)?.let {
            states[key] = it.onSaveInstanceState()
        }
    }

    fun restoreState(key: Int, recyclerView: RecyclerView) {
        val state = states[key] ?: return
        (recyclerView.layoutManager as? LinearLayoutManager)?.let {
            it.onRestoreInstanceState(state)
            states.delete(key)
        }
    }

    fun clear() {
        states.clear()
    }
}
