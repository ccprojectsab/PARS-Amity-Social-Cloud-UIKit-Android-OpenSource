package com.amity.socialcloud.uikit.chat.home

import androidx.paging.PagingData
import androidx.paging.filter
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.chat.channel.create.AmityChannelCreationType
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatFragmentDelegate
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

private const val MAX_CHANNEL_NAME_LENGTH = 90

class AmityChatHomePageViewModel : AmityBaseViewModel() {

    var recentChatItemClickListener: AmityRecentChatItemClickListener? = null
    var recentChatFragmentDelegate: AmityRecentChatFragmentDelegate? = null

    private val currentUser: AmityUser
        get() = AmityCoreClient.getCurrentUser().blockingFirst()

    fun createChat(
        selectedMembers: MutableList<AmitySelectMemberItem>,
        onChatCreateSuccess: (String) -> Unit,
        onChatCreateFailed: () -> Unit
    ): Completable {


        val isConversation = selectedMembers.count() == 1
        val chatBuilder = AmityChatClient.newChannelRepository()
            .createChannel(displayName = createChatDisplayName(selectedMembers = selectedMembers))

        val metaData = JsonObject().apply {
            addProperty(AmityConstants.CHAT_METADATA_IS_DIRECT_CHAT, isConversation)
            addProperty(
                AmityConstants.CHAT_METADATA_CREATOR_ID, currentUser.getUserId()
            )
            addProperty(AmityConstants.CHAT_SDK_TYPE, "android")

            if (isConversation) {
                add(AmityConstants.CHAT_METADATA_USER_IDS, JsonArray().apply {
                    add(selectedMembers.first().id)
                    add(currentUser.getUserId())
                })
            }
        }

        val amityChannelCreator =/* if (isConversation) {*/
            chatBuilder.conversation(selectedMembers.map { it.id }.toSet())
                .metadata(metaData)
                //.userIds(userIds = selectedMembers.map { it.id })
                .build()
       /* } else {
            chatBuilder
                .community()
                .metadata(metaData)
                .userIds(userIds = selectedMembers.map { it.id })
                .build()
        }*/

        return amityChannelCreator
            .create()
            .doOnSuccess { onChatCreateSuccess.invoke(it.getChannelId()) }
            .doOnError { onChatCreateFailed.invoke() }
            .ignoreElement()
    }

    private fun createChatDisplayName(selectedMembers: MutableList<AmitySelectMemberItem>): String {
        var channelName = ""
        selectedMembers.forEachIndexed { index, amitySelectMemberItem ->
            channelName = if (index == 0) {
                amitySelectMemberItem.name
            } else {
                channelName + " ," + amitySelectMemberItem.name
            }
        }
        if (channelName.length > MAX_CHANNEL_NAME_LENGTH) {
            channelName = channelName.substring(0, MAX_CHANNEL_NAME_LENGTH)
            channelName = "$channelName..."
        }
        return channelName
    }
}