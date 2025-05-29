package com.amity.socialcloud.uikit.chat.messages.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.uikit.chat.messages.adapter.AmityMessagePagingAdapter
import com.amity.socialcloud.uikit.chat.messages.composebar.AmityChatRoomComposeBar
import kotlinx.coroutines.launch

private const val SAVED_CHANNEL_ID = "SAVED_CHANNEL_ID"
private const val SAVED_USER_ID = "SAVED_USER_ID"
private const val SAVED_ENABLE_CHAT_TOOLBAR = "SAVED_ENABLE_CHAT_TOOLBAR"
private const val SAVED_ENABLE_CONNECTION_BAR = "SAVED_ENABLE_CONNECTION_BAR"

class AmityChatRoomEssentialViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    var channelId = ""
        set(value) {
            savedState.set(SAVED_CHANNEL_ID, value)
            field = value
        }
    var otherUserId = ""
        set(value) {
            savedState.set(SAVED_USER_ID, value)
            field = value
        }
    var enableChatToolbar = true
        set(value) {
            savedState.set(SAVED_ENABLE_CHAT_TOOLBAR, value)
            field = value
        }

    var enableConnectionBar = true
        set(value) {
            savedState.set(SAVED_ENABLE_CONNECTION_BAR, value)
            field = value
        }

    var composeBar: AmityChatRoomComposeBar = AmityChatRoomComposeBar.DEFAULT
    var customViewHolder: AmityMessagePagingAdapter.CustomViewHolderListener? = null



    fun messageMarkAsRead(channelId: String){
        viewModelScope.launch {
            AmityChatClient.newChannelRepository()
                .getChannel(channelId)
                .subscribe { channel ->
                    channel.markAsRead()
                }
        }
    }
}