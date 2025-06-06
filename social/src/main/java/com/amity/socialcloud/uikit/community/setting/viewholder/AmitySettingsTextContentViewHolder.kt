package com.amity.socialcloud.uikit.community.setting.viewholder

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.components.setBold
import com.amity.socialcloud.uikit.common.components.setVisibility
import com.amity.socialcloud.uikit.community.databinding.AmityItemSettingsTextContentBinding
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import com.amity.socialcloud.uikit.AmityLocalisationSocial


class AmitySettingsTextContentViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView),
        AmityBaseRecyclerViewAdapter.IBinder<AmitySettingsItem> {
    private val binding = AmityItemSettingsTextContentBinding.bind(itemView)

    override fun bind(data: AmitySettingsItem?, position: Int) {
        binding.apply {
            when (data) {
                is AmitySettingsItem.TextContent -> {
                    tvTitle.text = AmityLocalisationSocial.getString(data.title)
                    tvTitle.setTextColor(ContextCompat.getColor(context, data.titleTextColor))
                    setBold(tvTitle, data.isTitleBold)
                 /*   val typeface = ResourcesCompat.getFont(context, com.amity.socialcloud.uikit.common.R.font.recoleta_regular)
                    tvTitle.typeface = typeface*/

                    tvDescription.text = data.description?.let { AmityLocalisationSocial.getString(it) }
                    setVisibility(tvDescription, data.description != null)

                    rootView.setOnClickListener { data.callback() }
                }
                else -> {
                }
            }
        }
    }

}