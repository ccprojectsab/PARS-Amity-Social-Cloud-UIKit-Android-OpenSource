package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemPollMultipleAnswersBinding
import com.google.android.material.card.MaterialCardView

class AmityPollMultipleAnswersViewHolder(
    context: Context,
    isEnabled: Boolean,
    voteCallback: (answerId: String, isSelected: Boolean, holder: MaterialCardView?) -> Unit
) : AmityPollAnswerViewHolder(
    context,
    R.layout.amity_item_poll_multiple_answers,
    isEnabled,
    voteCallback
) {

    override fun bind(data: AmityPollAnswer) {
        val binding = AmityItemPollMultipleAnswersBinding.bind(itemView)

        binding.voteCheckBox.isChecked = false
        binding.voteCheckBox.text = data.data
        binding.voteCheckBox.setTextColor(
            when (isEnabled) {
                true -> ContextCompat.getColor(context, com.amity.socialcloud.uikit.common.R.color.amityColorBlack)
                false -> ContextCompat.getColor(context, com.amity.socialcloud.uikit.common.R.color.amityPlaceHolderDarkColor)
            }
        )

        binding.voteCheckBox.buttonTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                context, when (isEnabled) {
                    true -> com.amity.socialcloud.uikit.common.R.color.amityColorPrimary
                    false -> com.amity.socialcloud.uikit.common.R.color.amityPlaceHolderDarkColor
                }
            )
        )

        binding.voteCardView.isEnabled = isEnabled
        binding.voteCardView.strokeColor =
            ContextCompat.getColor(context, com.amity.socialcloud.uikit.common.R.color.upstraMessageBubbleInverse)
        binding.voteCardView.setOnClickListener {
            binding.voteCheckBox.isChecked = !binding.voteCheckBox.isChecked
            binding.voteCardView.strokeColor = when (binding.voteCheckBox.isChecked) {
                true -> ContextCompat.getColor(context, com.amity.socialcloud.uikit.common.R.color.amityColorPrimary)
                false -> ContextCompat.getColor(context, com.amity.socialcloud.uikit.common.R.color.upstraMessageBubbleInverse)
            }
            voteCallback.invoke(data.id, binding.voteCheckBox.isChecked, null)
        }
    }
}