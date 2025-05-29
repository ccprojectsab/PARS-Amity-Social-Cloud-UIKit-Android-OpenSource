package com.amity.socialcloud.uikit.chat.recent.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.api.chat.member.query.AmityChannelMembershipFilter
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.chat.message.AmityMessagePreview
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityItemRecentMessageBinding
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.common.utils.AmityDateUtils
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.collect
import kotlinx.coroutines.withContext

class AmityRecentChatViewHolder(
    itemView: View,
    private val listener: AmityRecentChatItemClickListener?,
    private val lifecycleOwner: LifecycleOwner? = null
) : RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewPagingDataAdapter.Binder<AmityChannel> {

    private val binding: AmityItemRecentMessageBinding? = DataBindingUtil.bind(itemView)

    private val memberCount: TextView = itemView.findViewById(R.id.tvMemberCount)
    private val name: TextView = itemView.findViewById(R.id.tvDisplayName)
    private val avatar: ShapeableImageView = itemView.findViewById(R.id.ivAvatar)
    private val unreadCount: TextView = itemView.findViewById(R.id.tvUnreadCount)
    private val message: TextView = itemView.findViewById(R.id.tvMessage)

    private fun getUser(userId: String): Flowable<AmityUser> {
        val userRepository = AmityCoreClient.newUserRepository()
        return userRepository.getUser(userId)
    }

   /* private val currentUser: AmityUser
        get() = AmityCoreClient.getCurrentUser().blockingFirst()*/
    private val currentUser: AmityUser?
        get() {
            try {
                return AmityCoreClient.getCurrentUser().blockingFirst()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    fun markMessageAsRead(message: AmityMessage) {
        message.markRead()
    }
    override fun bind(data: AmityChannel?, position: Int) {

        if (data != null) {
            if (data.getDisplayName().isNotEmpty()) {
                name.text = data.getDisplayName()
            } else {
                name.text =
                    itemView.context.getString(com.amity.socialcloud.uikit.common.R.string.amity_anonymous)
            }
            getChannelMessagePreview(data)
            setUpAvatarView(data)
            setupUnreadCount(data)

            binding?.tvTime?.text =
                AmityDateUtils.getMessageTime(data.getLastActivity().millis)
            memberCount.text =
                String.format(
                    itemView.context.getString(R.string.amity_member_count),
                    data.getMemberCount()
                )
            val metadata: JsonObject? = data.getMetadata()
            Log.d("Mytag", "getChannelMessagePreview: $metadata")
            itemView.setOnClickListener {

                /*  val updatedData = metadata?.addProperty(AmityConstants.CHAT_USER_ARCHIVE, "true")
                  Log.d("Mytag", "bindupdate : $updatedData")*/
                listener?.onRecentChatItemClick(data.getChannelId())
            }

            val isDirectChat =
                metadata?.get(AmityConstants.CHAT_METADATA_IS_DIRECT_CHAT)?.asBoolean == true
            val otherUserId = metadata?.get(AmityConstants.CHAT_METADATA_USER_IDS)
                ?.asJsonArray
                ?.find { it.asString != currentUser?.getUserId() }
                ?.asString
            if (isDirectChat && otherUserId != null) {
                lifecycleOwner?.lifecycleScope?.launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val userDetails = getUser(otherUserId).blockingFirst()
                            withContext(Dispatchers.Main) {
                                userDetails.getDisplayName()?.let { userName ->

                                    name.text = userName
                                }
                                userDetails.getAvatar()?.let { image ->
                                    Glide.with(itemView.context)
                                        .load(image.getUrl(AmityImage.Size.MEDIUM))
                                        .placeholder(R.drawable.amity_chat_ic_profile)
                                        .centerCrop()
                                        .into(avatar)
                                }
                            }
                        } catch (e: Exception) {
                            // Handle the exception here
                            e.printStackTrace() // You can log the exception or perform other error-handling tasks
                        }
                    }
                }
            }

            /* if (metadata != null && metadata.size() > 0) {
                 if (data.getMemberCount() == 2 && metadata.has("userIds")) {
                     metadata.let { it ->
                         val jsonArray = it.getAsJsonArray("userIds")
                         val userIdToFetch = jsonArray.lastOrNull { jsonElement ->
                             jsonElement.asString != currentUser.getUserId()
                         }?.asString

                         userIdToFetch.let { userId ->
                             getUser(userId.toString())

                                 .doOnNext { channelMembers ->

                                     val title = channelMembers.getDisplayName()
                                     val displayPic = channelMembers.getAvatar()
                                     name.text = title
                                     Glide.with(itemView.context)
                                         .load(displayPic?.getUrl(AmityImage.Size.MEDIUM))
                                         .placeholder(R.drawable.amity_chat_ic_profile)
                                         .centerCrop()
                                         .into(avatar)

                                 }.subscribe()

                             //    val displayName = userId?.let { it1 -> getUser(it1).blockingFirst() }
                             //   val avatar1 = userId?.let { it1 -> getUser(it1).blockingFirst() }
                             *//*  Glide.with(itemView.context)
                                  .load(avatar1?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM))
                                  .placeholder(R.drawable.amity_ic_default_avatar_group_chat)
                                  .centerCrop()
                                  .into(avatar)
                              name.text = displayName?.getDisplayName()*//*
                            unreadCount.text = data.getUnreadCount().toString()
                        }

                    }
                } else {
                    if (data.getDisplayName().isNotEmpty()) {
                        name.text = data.getDisplayName()
                    } else {
                        name.text =
                            itemView.context.getString(com.amity.socialcloud.uikit.common.R.string.amity_anonymous)
                    }
                    setUpAvatarView(data)
                }
            } else {

                //   name.text = data.getMetadata()
                if (data.getDisplayName().isNotEmpty()) {
                    name.text = data.getDisplayName()
                } else {
                    name.text =
                        itemView.context.getString(com.amity.socialcloud.uikit.common.R.string.amity_anonymous)
                }
                setUpAvatarView(data)
            }
            setupUnreadCount(data)
*/
        }
    }


    private fun getChannelMessagePreview(channel: AmityChannel) {
        val messagePreview: AmityMessagePreview? = channel.getMessagePreview()
        messagePreview?.let { preview ->
            val data = (preview.getData() as? AmityMessage.Data.TEXT)?.getText()
            val dataType = preview.getDataType().name
            val image = (preview.getData() as? AmityMessage.Data.IMAGE).toString()
            val name = preview.getUser()?.getDisplayName()
            Log.d("Mytag", "getChannelMessagePreview: $name")
            message.text = when (dataType) {
                "IMAGE" -> name?.plus(": Skickade ett foto.") ?: ""
                "TEXT" -> name?.plus(": $data") ?: data
                "AUDIO" -> name?.plus(": Skickade ett röstmeddelande.") ?: ""
                else -> {
                    name?.plus("")?:""
                }
            }
        }
    }

    private fun setUpAvatarView(data: AmityChannel) {
        val defaultAvatar: Int = when (data.getChannelType()) {
            AmityChannel.Type.STANDARD -> {
                //setupNameView(data)
                R.drawable.amity_ic_default_avatar_group_chat
            }

            AmityChannel.Type.PRIVATE -> {
                //setupNameView(data)
                R.drawable.amity_ic_default_avatar_private_community_chat
            }

            AmityChannel.Type.CONVERSATION -> {
                R.drawable.amity_chat_ic_profile
            }

            else -> {
                R.drawable.amity_ic_default_avatar_publc_community_chat
            }
        }

        avatar.setBackgroundColor(
            AmityColorPaletteUtil.getColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.amity.socialcloud.uikit.common.R.color.amityColorPrimary
                ),
                AmityColorShade.SHADE3
            )
        )

        Glide.with(itemView.context)
            .load(data.getAvatar()?.getUrl(AmityImage.Size.MEDIUM))
            .placeholder(defaultAvatar)
            .centerCrop()
            .into(avatar)
    }

    private fun setupNameView(data: AmityChannel) {
        var leftDrawable = R.drawable.amity_ic_community_public
        if (data.getChannelType() == AmityChannel.Type.PRIVATE)
            leftDrawable = R.drawable.amity_ic_community_private
        val rightDrawable = 0
//        if (data.verified)
//            rightDrawable = R.drawable.amity_ic_verified
        name.text = data.getDisplayName()
        name.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, rightDrawable, 0);
    }

    private fun getSenderName(item: AmityMessage): String {
        return if (item.getCreatorId() == AmityCoreClient.getUserId()) {
            "ME"
        } else {
            item.getCreator()?.getDisplayName()
                ?: itemView.context.getString(com.amity.socialcloud.uikit.common.R.string.amity_anonymous)
        }
    }

    private fun setupUnreadCount(data: AmityChannel) {
        if (data.getUnreadCount() > 0) {
            unreadCount.visibility = View.VISIBLE
            unreadCount.text = data.getUnreadCount().toString()
        } else {
            unreadCount.visibility = View.GONE
        }
    }
}