package com.amity.socialcloud.uikit.community.setting

import android.graphics.fonts.Font
import android.graphics.fonts.FontFamily
import androidx.annotation.DimenRes
import com.amity.socialcloud.uikit.community.R
import io.reactivex.rxjava3.core.Flowable

sealed class AmitySettingsItem {
    class Header(val title: Int) : AmitySettingsItem()

    class TextContent(
        val icon: Int? = null,
        val title: Int,
        val description: Int? = null,
        val titleTextColor: Int = com.amity.socialcloud.uikit.common.R.color.upstraColorBase,
        val font: Int = com.amity.socialcloud.uikit.common.R.font.recoleta_regular,
        val isTitleBold: Boolean = false,
        val callback: () -> Unit) : AmitySettingsItem()

    class NavigationContent(
        val icon: Int? = null,
        val iconNavigation: Int? = R.drawable.amity_ic_chevron_right,
        val title: Int,
        val value: Int? = null,
        val description: Int? = null,
        val isTitleBold: Boolean = false,
        val font: Int = com.amity.socialcloud.uikit.common.R.font.recoleta_regular,
        val callback: () -> Unit) : AmitySettingsItem()

    class ToggleContent(
            val icon: Int? = null,
            val title: Int,
            val description: Int? = null,
            val isToggled: Flowable<Boolean>,
            val isTitleBold: Boolean = false,
            val font: Int = com.amity.socialcloud.uikit.common.R.font.recoleta_regular,
            val callback: (Boolean) -> Unit) : AmitySettingsItem()

    class RadioContent(
        val choices: List<Pair<Int, Boolean>>,
        val callback: (Int) -> Unit) : AmitySettingsItem()

    class Margin(@DimenRes val margin: Int): AmitySettingsItem()

    object Separator : AmitySettingsItem()

}