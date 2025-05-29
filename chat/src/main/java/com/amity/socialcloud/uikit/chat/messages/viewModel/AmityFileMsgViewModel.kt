package com.amity.socialcloud.uikit.chat.messages.viewModel


import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.file.AmityFileRepository
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.file.AmityFile
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class AmityFileMsgViewModel : AmitySelectableMessageViewModel() {

    val fileUrl = ObservableField("")
    var fileUri: Uri? = Uri.EMPTY
    val senderFillColor =
        ObservableField(com.amity.socialcloud.uikit.common.R.color.amityMessageBubble)
    val receiverFillColor =
        ObservableField(com.amity.socialcloud.uikit.common.R.color.amityMessageBubbleInverse)
    val uploading = ObservableBoolean(false)
    val uploadProgress = ObservableField(0)
    val buffering = ObservableBoolean(false)

    init {
        uploadProgress.addOnPropertyChanged {
            if (uploadProgress.get() == 100) {
                uploading.set(false)
            } else {
                uploading.set(true)
            }
        }

        /**
         * Not using now will be used when we'll start downloading Audio Files
         * @author sumitlakra
         * @date 12/01/2020
         */
//        audioUrl.addOnPropertyChanged {
//            if (audioUrl.get() != null) {
//                triggerEvent(EventIdentifier.SET_AUDIO_FILE_PROPERTIES)
//            }
//        }
    }

    fun playButtonClicked() {
        if (!buffering.get()) {
            triggerEvent(AmityEventIdentifier.AUDIO_PLAYER_PLAY_CLICKED)
        }
    }

    fun getImageUploadProgress(message: AmityMessage) {
        val fileData = message.getData() as AmityMessage.Data.FILE
        val localPath = fileData.getFile()?.getFilePath()
        if (!localPath.isNullOrEmpty()) {
            val file = File(localPath)
            if (file.exists() && fileUrl.get() != localPath) {
                fileUrl.set(localPath)
            }
        } else {
            if (message.getState() == AmityMessage.State.SYNCED) {
                if (fileUrl.get() != fileData.getFile()?.getUrl()) {
                    fileUrl.set(fileData.getFile()?.getUrl())
                    uploading.set(false)
                }
            } else {
                if (message.getState() == AmityMessage.State.UPLOADING) {
                    val fileRepository: AmityFileRepository = AmityCoreClient.newFileRepository()
                    addDisposable(fileRepository.getUploadInfo(message.getMessageId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { uploadInfo ->
                            if (!uploadInfo.getFilePath().isNullOrEmpty()
                            ) {
                                fileUrl.set(uploadInfo.getFilePath())
                            }
                            uploadProgress.set(uploadInfo.getProgressPercentage())
                        }.doOnError {

                        }.subscribe()
                    )
                }
            }
        }
    }


}