package com.amity.socialcloud.uikit.chat.messages.viewModel

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.chat.channel.AmityChannelRepository
import com.amity.socialcloud.sdk.api.chat.message.AmityMessageRepository
import com.amity.socialcloud.sdk.api.chat.message.query.AmityMessageQuery
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.chat.message.AmityMessageAttachment
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.components.AmityChatComposeBarClickListener
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class AmityMessageListViewModel : AmityChatMessageBaseViewModel() {

    val text = ObservableField<String>()
    val title = ObservableField<String>()
    val avatarUrl = ObservableField<String>()
    var channelID: String = ""
        set(value) {
            field = value

            messageQuery = AmityChatClient.newMessageRepository()
                .getMessages(value)
                .parentId(null)
                .build()
        }
    var isRVScrolling = false
    val isScrollable = ObservableBoolean(false)
    val stickyDate = ObservableField<String>("")
    val showComposeBar = ObservableBoolean(false)
    val keyboardHeight = ObservableInt(0)
    val isVoiceMsgUi = ObservableBoolean(false)
    val isRecording = ObservableBoolean(false)
    var hasScrolled = false

    lateinit var messageQuery: AmityMessageQuery

    fun toggleRecordingView() {
        isVoiceMsgUi.set(!isVoiceMsgUi.get())
        if (isVoiceMsgUi.get()) {
            triggerEvent(AmityEventIdentifier.SHOW_AUDIO_RECORD_UI)
        }
    }

    fun getChannelType(): Flowable<AmityChannel> {
        val channelRepository: AmityChannelRepository = AmityChatClient.newChannelRepository()
        return channelRepository.getChannel(channelID)
    }

    fun getDisplayNameUser(channelID: String): Flowable<AmityUser> {
        val userRepository = AmityCoreClient.newUserRepository()
        return userRepository.getUser(channelID)
    }

    fun getDisplayName(): Flowable<PagingData<AmityChannelMember>> {
        val channelRepository: AmityChannelRepository = AmityChatClient.newChannelRepository()
        return channelRepository.membership(channelID).getMembers().build().query()
    }

    fun joinChannel(): Completable {
        val channelRepository: AmityChannelRepository = AmityChatClient.newChannelRepository()
        return channelRepository.joinChannel(channelID).ignoreElement()
    }

    fun startReading() {
        AmityChatClient.newSubChannelRepository().startReading(channelID)
    }

    fun stopReading() {
        AmityChatClient.newSubChannelRepository().stopReading(channelID)
    }

    fun getAllMessages(): Flowable<PagingData<AmityMessage>> {
        return messageQuery.query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLatestMessage(): Flowable<AmityMessage> {
        return AmityChatClient.newChannelRepository()
            .getChannel(channelID)
            .firstElement()
            .flatMapPublisher {
                it.latestMessage()
                    .isDeleted(false)
                    .build()
                    .query()
            }
    }

    fun sendMessage() {
        if (!isVoiceMsgUi.get()) {
            val messageRepository: AmityMessageRepository = AmityChatClient.newMessageRepository()
            addDisposable(
                messageRepository.createMessage(channelID).with()
                    .text(text.get()!!.trim())
                    .build().send().observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                            triggerEvent(AmityEventIdentifier.MSG_SEND_SUCCESS)
                        }

                        override fun onError(e: Throwable) {
                            triggerEvent(AmityEventIdentifier.MSG_SEND_ERROR)
                        }
                    })
            )
            text.set("")
        }
    }

    fun sendImageMessage(imageUri: Uri): Completable {
        val messageRepository: AmityMessageRepository = AmityChatClient.newMessageRepository()
        return messageRepository.createMessage(channelID).with()
            .image(imageUri).build().send()
    }

    fun sendAudioMessage(audioFileUri: Uri, duration: Long? = null): Completable {
        val messageRepository: AmityMessageRepository = AmityChatClient.newMessageRepository()


        val audioMessageBuilder = messageRepository.createMessage(channelID)
            .with()
            .audio(audioFileUri)


        if (duration != null) {
            val metaData = JsonObject().apply {
                addProperty(AmityConstants.AUDIO_FILE_DURATION, duration)
            }
            audioMessageBuilder.metadata(metaData)
        }

        val audioMessageCreator = audioMessageBuilder.build()
        return audioMessageCreator.send()
    }

    /*    fun sendFileMessage(fileUri: Uri): Completable {
            val chatRepository: AmityMessageRepository = AmityChatClient.newMessageRepository()

            return chatRepository.createMessage(channelID).with().file(fileUri).build().send()


        }*/

    fun sendFileMessage(
        fileUri: Uri
    ): Completable {
        // Create file message with Uri
        val messageRepository: AmityMessageRepository = AmityChatClient.newMessageRepository()

        val uriAttachment = AmityMessageAttachment.URL(fileUri)
        return messageRepository.createFileMessage(
            subChannelId = channelID,
            attachment = uriAttachment
        )
            .build()
            .send()


        /* // Create file message with fileId
         val fileId: String = "fileId from existing file"
         val fileIdAttachment = AmityMessageAttachment.FILE_ID(fileId)
         messageRepository.createFileMessage(
             subChannelId =channelID ,
             attachment = fileIdAttachment
         )
             .build()
             .send()
             .doOnComplete {
                 // Void
             }
             .doOnError {
                 // Exception
             }
             .subscribe()

         // Create file message with metadata
         messageRepository.createFileMessage(
             subChannelId = channelID,
             attachment = uriAttachment
         )
             .metadata(metadata = JsonObject()) // flexible data structure for additional custom data
             .build()
             .send()
             .doOnComplete {
                 // Void
             }
             .doOnError {
                 // Exception
             }
             .subscribe()*/
    }

    fun toggleComposeBar() {
        triggerEvent(AmityEventIdentifier.TOGGLE_CHAT_COMPOSE_BAR)
    }

    val composeBarClickListener = object : AmityChatComposeBarClickListener {
        override fun onCameraClicked() {
            triggerEvent(AmityEventIdentifier.CAMERA_CLICKED)
        }

        override fun onAlbumClicked() {
            triggerEvent(AmityEventIdentifier.PICK_IMAGE)
        }

        override fun onFileClicked() {
            triggerEvent(AmityEventIdentifier.PICK_FILE)
        }

        override fun onLocationCLicked() {
        }
    }

    fun onRVScrollStateChanged(rv: RecyclerView, newState: Int) {
        isScrollable.set(rv.computeVerticalScrollRange() > rv.height)
        isRVScrolling = if (isScrollable.get()) {
            newState == RecyclerView.SCROLL_STATE_DRAGGING ||
                    newState == RecyclerView.SCROLL_STATE_SETTLING
        } else {
            false
        }
        if (isRVScrolling) {
            hasScrolled = true
        }
    }
}

private const val RECONNECTION_DELAY_SECONDS = 3