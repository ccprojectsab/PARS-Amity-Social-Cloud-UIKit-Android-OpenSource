package com.amity.socialcloud.uikit.chat.messages.viewHolder

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.databinding.AmityItemFileMessageSenderBinding
import com.amity.socialcloud.uikit.chat.messages.popUp.AmityPopUp
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityFileMsgViewModel
import com.amity.socialcloud.uikit.common.AmityLocalisation
import com.amity.socialcloud.uikit.common.common.AmityFileUtils
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityConstants.FILE_EXTENSION_SEPARATOR
import java.net.URLEncoder


class AmityFileMsgSenderViewHolder(
    itemView: View,
    val itemViewModel: AmityFileMsgViewModel,
    private val context: Context,
) : AmitySelectableMessageViewHolder(itemView, itemViewModel, context) {

    private val binding: AmityItemFileMessageSenderBinding? = DataBindingUtil.bind(itemView)
    private var popUp: AmityPopUp? = null

    init {
        binding?.vmFileMsg = itemViewModel
        addViewModelListeners()
    }

    private fun addViewModelListeners() {
        itemViewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.DISMISS_POPUP -> popUp?.dismiss()
                else -> {
                }
            }
        }
    }

    override fun showPopUp() {

    }

    override fun setMessageData(item: AmityMessage) {
        itemViewModel.getImageUploadProgress(item)
        when (val data = item.getData()) {
            is AmityMessage.Data.FILE -> {
                val fileName = data.getFile()?.getFileName()
                val fileUrl = data.getFile()?.getUrl()
                val fileExtention = data.getFile()?.getFileExtension()
                val fileMIMe = data.getFile()?.getMimeType()
                println("extention $fileExtention")
                println("MIME $fileMIMe")
                val drawableRes = AmityFileUtils.getFileIcon(data.getFile()?.getMimeType() ?: "")
                binding?.ivFileIcon?.setImageDrawable(itemView.context.getDrawable(drawableRes))
                if (fileName != null) {
                    setFileName(fileName)
                }


                /*   fileUrl?.let { url ->
                       Glide.with(binding?.ivFileIcon!!.context)
                           .load(url)
                           .placeholder(R.drawable.amity_ic_image) // Add a default file icon
                           .error(R.drawable.amity_ic_image) // Error icon if loading fails
                           .into(binding?.ivFileIcon!!)
                   }*/
            }

            else -> {

            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun setFileName(originalName: String) {

        val fileExtension = if (originalName.contains(FILE_EXTENSION_SEPARATOR)) {
            originalName.substringAfterLast(FILE_EXTENSION_SEPARATOR)
        } else {
            ""
        }
        val truncatedName = originalName.substringBeforeLast(FILE_EXTENSION_SEPARATOR)
            .take(getMaxCharacterLimit())
        if(fileExtension.isNotEmpty()){
            binding?.tvFileName?.text = "$truncatedName.$fileExtension..."
        }
        else{
            binding?.tvFileName?.text = "$truncatedName..."
        }
        binding?.layoutFile?.setOnClickListener {
            println("button pressed")
            openPdf()
        }
    }

    open fun getMaxCharacterLimit(): Int {
        return itemView.resources.getInteger(com.amity.socialcloud.uikit.common.R.integer.maxLimitFileName)
    }


    fun openPdf() {
        print("open functipm called")
        val pdfUrl =
            "https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(
                itemViewModel.fileUrl.get(),
                "UTF-8"
            )
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
        context.startActivity(intent)

    }

    fun downloadFile(context: Context) {
        val request = DownloadManager.Request(Uri.parse(itemViewModel.fileUrl.get())).apply {
            setTitle(AmityLocalisation.getString(com.amity.socialcloud.uikit.common.R.string.amity_downloading_file))
            setDescription(AmityLocalisation.getString(com.amity.socialcloud.uikit.common.R.string.amity_try_again))
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "")
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}