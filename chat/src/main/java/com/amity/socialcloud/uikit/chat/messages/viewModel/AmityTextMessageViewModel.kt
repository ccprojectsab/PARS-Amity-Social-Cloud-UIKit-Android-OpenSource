package com.amity.socialcloud.uikit.chat.messages.viewModel

import androidx.databinding.ObservableField
import com.amity.socialcloud.uikit.chat.R

class AmityTextMessageViewModel : AmitySelectableMessageViewModel() {

    val text = ObservableField<String>()
    val senderFillColor = ObservableField<Int>(com.amity.socialcloud.uikit.common.R.color.amityColorPrimary)
    val receiverFillColor = ObservableField<Int>(com.amity.socialcloud.uikit.common.R.color.amityMessageBubbleInverse)
}