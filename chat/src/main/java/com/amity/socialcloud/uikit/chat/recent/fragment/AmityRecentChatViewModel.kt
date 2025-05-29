package com.amity.socialcloud.uikit.chat.recent.fragment

import androidx.paging.PagingData
import androidx.paging.filter
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.chat.channel.AmityChannelRepository
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannelFilter
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.chat.message.AmityMessagePreview
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.core.Flowable

class AmityRecentChatViewModel : AmityBaseViewModel() {

    var recentChatItemClickListener: AmityRecentChatItemClickListener? = null

    fun getRecentChat(): Flowable<PagingData<AmityChannel>> {

        val channelRepository: AmityChannelRepository = AmityChatClient.newChannelRepository()
        val types = listOf(AmityChannel.Type.CONVERSATION, AmityChannel.Type.COMMUNITY)

        return channelRepository.getChannels().types(types).includeDeleted(false)
            .filter(AmityChannelFilter.MEMBER).build().query()
            .map {
                it.filter { channel ->
                    !(channel.isDeleted()) && !(channel.getDisplayName().contains("Deleted", true))
                }
            }

    }


    fun getUser(userId: String): Flowable<AmityUser> {
        return AmityCoreClient.newUserRepository().getUser(userId)
    }

}