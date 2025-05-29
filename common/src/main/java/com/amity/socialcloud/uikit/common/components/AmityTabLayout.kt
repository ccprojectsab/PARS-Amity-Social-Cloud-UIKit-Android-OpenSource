package com.amity.socialcloud.uikit.common.components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.common.common.setBackgroundColor
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AmityTabLayout : ConstraintLayout {

    private lateinit var mAdapter: AmityFragmentStateAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val rootView = LayoutInflater.from(context).inflate(R.layout.amity_tab_layout, this, true)
        tabLayout = rootView.findViewById(R.id.tab_header)
        viewPager2 = rootView.findViewById(R.id.viewpager)
        val divider = rootView.findViewById<View>(R.id.divider)
        divider.setBackgroundColor(
            ContextCompat.getColor(context, R.color.amityColorPrimary),
            AmityColorShade.SHADE4
        )
    }

    fun setAdapter(adapter: AmityFragmentStateAdapter) {
        mAdapter = adapter
        viewPager2.adapter = mAdapter

        tabLayout.setTabTextColors(
            ContextCompat.getColor(context, R.color.amitytabunselected), ContextCompat.getColor(
                context,
                R.color.amityColorBase
            )
        )
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = mAdapter.getTitle(position)
        }.attach()
    }

    fun setPageChangeListener(callback: ViewPager2.OnPageChangeCallback) {
        viewPager2.registerOnPageChangeCallback(callback)
    }

    fun setOffscreenPageLimit(limit: Int) {
        viewPager2.offscreenPageLimit = limit
    }

    fun switchTab(position: Int, smoothScroll: Boolean = true) {
        try {
            //tabLayout.selectTab(tabLayout.getTabAt(position))
            viewPager2.setCurrentItem(position, smoothScroll)
        } catch (ex: Exception) {
            Log.e(this.javaClass.simpleName, "switchTab: ${ex.message}", ex)
        }
    }

    fun disableSwipe() {
        viewPager2.isUserInputEnabled = false
    }
}