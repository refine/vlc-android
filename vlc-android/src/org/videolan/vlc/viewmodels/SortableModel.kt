package org.videolan.vlc.viewmodels

import android.content.Context
import org.videolan.medialibrary.interfaces.AbstractMedialibrary
import org.videolan.vlc.util.ISortModel
import org.videolan.vlc.util.RefreshModel
import org.videolan.vlc.util.Settings

abstract class SortableModel(protected val context: Context): ScopedModel(), RefreshModel,
    ISortModel
{
    private val settings = Settings.getInstance(context)
    protected open val sortKey : String = this.javaClass.simpleName
    var sort = settings.getInt(sortKey, AbstractMedialibrary.SORT_DEFAULT)
    var desc = settings.getBoolean("${sortKey}_desc", false)

    var filterQuery : String? = null

    fun getKey() = sortKey

    override fun sort(sort: Int) {
        if (canSortBy(sort)) {
            desc = when (this.sort) {
                AbstractMedialibrary.SORT_DEFAULT -> sort == AbstractMedialibrary.SORT_ALPHA
                sort -> !desc
                else -> false
            }
            this.sort = sort
            refresh()
            settings.edit()
                    .putInt(sortKey, sort)
                    .putBoolean("${sortKey}_desc", desc)
                    .apply()
        }
    }

    abstract fun restore()
    abstract fun filter(query: String?)
}
