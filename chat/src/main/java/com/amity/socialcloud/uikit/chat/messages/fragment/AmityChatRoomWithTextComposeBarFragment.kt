package com.amity.socialcloud.uikit.chat.messages.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityFragmentChatWithTextComposeBarBinding
import com.amity.socialcloud.uikit.chat.messages.adapter.AmityMessagePagingAdapter
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityChatRoomEssentialViewModel
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityMessageListViewModel
import com.amity.socialcloud.uikit.chat.settings.AmityChatSettingsActivity
import com.amity.socialcloud.uikit.common.base.AmityPickerFragment
import com.amity.socialcloud.uikit.common.common.setShape
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.components.AmityAudioRecorderListener
import com.amity.socialcloud.uikit.common.components.AmityMessageListListener
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityAndroidUtil
import com.amity.socialcloud.uikit.common.utils.AmityBackPressUtil
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream


class AmityChatRoomWithTextComposeBarFragment() : AmityPickerFragment(),
    AmityAudioRecorderListener, AmityMessageListListener {

    private lateinit var essentialViewModel: AmityChatRoomEssentialViewModel
    private val messageListViewModel: AmityMessageListViewModel by viewModels()

    private lateinit var mAdapter: AmityMessagePagingAdapter
    private lateinit var binding: AmityFragmentChatWithTextComposeBarBinding
    private var msgSent = false
    private var viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener? = null
    private var messageListDisposable: Disposable? = null
    private var currentCount = 0
    private var isImagePermissionGranted = false
    private var isReachBottom = true
    var ROUTE_NAME = "ROUTE_NAME"

    private val pickMultipleImagesPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                isImagePermissionGranted = true
                pickMultipleImages()
            } else {
                isImagePermissionGranted = false
                view?.showSnackBar("Permission denied", Snackbar.LENGTH_SHORT)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        essentialViewModel =
            ViewModelProvider(requireActivity()).get(AmityChatRoomEssentialViewModel::class.java)
        messageListViewModel.channelID = essentialViewModel.channelId
        viewHolderListener = essentialViewModel.customViewHolder
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_chat_with_text_compose_bar,
            container,
            false
        )
        binding.viewModel = messageListViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getChannelType()
        initToolBar()
        initRecyclerView()
        setupComposebar()
        observeViewModelEvents()
        initMessageLoader()
//        observeRefreshStatus()
//        observeConnectionStatus()
    }

    private fun setupComposebar() {
        binding.apply {
            etMessage.setShape(
                null,
                null,
                null,
                null,
                com.amity.socialcloud.uikit.common.R.color.amityColorBase,
                com.amity.socialcloud.uikit.common.R.color.amityColorBase,
                AmityColorShade.SHADE4
            )
            recordBackground.setShape(
                null,
                null,
                null,
                null,
                com.amity.socialcloud.uikit.common.R.color.amityColorBase,
                com.amity.socialcloud.uikit.common.R.color.amityColorBase,
                AmityColorShade.SHADE4
            )
            etMessage.setOnClickListener {
                messageListViewModel.showComposeBar.set(false)
            }
            etMessage.setOnFocusChangeListener { _, _ ->
                messageListViewModel.showComposeBar.set(false)
            }
        }
    }

    private fun initMessageLoader() {
//        messageListViewModel.messageLoader.load()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnComplete { hideLoadingView() }
//            .untilLifecycleEnd(this)
//            .subscribe()
    }

    /*
    private fun observeRefreshStatus() {
        messageListViewModel.observeRefreshStatus { resetMessageLoader() }
            .doOnError { }
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun observeConnectionStatus() {
        messageListViewModel.observeConnectionStatus(
            onDisconnected = { presentDisconnectedView() },
            onReconnected = { presentReconnectedView() })
            .doOnError { }
            .untilLifecycleEnd(this)
            .subscribe()
    }
     */

    private fun presentDisconnectedView() {
        if (essentialViewModel.enableConnectionBar) {
            binding.connectionView.visibility = View.VISIBLE
            binding.connectionTexview.setText(R.string.amity_no_internet)
            binding.connectionTexview.setBackgroundColor(resources.getColor(com.amity.socialcloud.uikit.common.R.color.amityColorGrey))
        }
    }

    private fun presentReconnectedView() {
        if (essentialViewModel.enableConnectionBar) {
            binding.connectionView.visibility = View.GONE
        }
    }

    private fun presentChatRefreshLoadingView() {
        binding.loadingView.setBackgroundColor(resources.getColor(com.amity.socialcloud.uikit.common.R.color.amityTranslucentBackground))
        binding.loadingView.visibility = View.VISIBLE
    }

    private fun resetMessageLoader() {
//        messageListViewModel.channelID = essentialViewModel.channelId
//        messageListViewModel.messageLoader.load()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { presentChatRefreshLoadingView() }
//            .doOnComplete { hideLoadingView() }
//            .untilLifecycleEnd(this)
//            .subscribe()
    }

    override fun onResume() {
        super.onResume()
        setUpBackPress()
    }

    override fun onMessageClicked(position: Int) {
        binding.rvChatList.scrollToPosition(position)
    }

    private fun observeScrollingState(layoutManager: LinearLayoutManager) {
        binding.rvChatList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                mAdapter.firstCompletelyVisibleItem = firstVisibleItem
                val isAlmostReachedTop = firstVisibleItem <= PAGINATION_PRELOAD_THRESHOLD
                val isScrollingUp = dy < 0
                if (isAlmostReachedTop && isScrollingUp) {
//                    if (messageListViewModel.messageLoader.hasMore()) {
//                        Log.e("MML", "Load more")
//                        messageListViewModel.messageLoader.load()
//                            .subscribeOn(Schedulers.io())
//                            .subscribe()
//                    } else {
//                        Log.e("MML", "Last page reached")
//                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    isReachBottom = true
                    mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
                } else if (isReachBottom) {
                    isReachBottom = false
                }
            }
        })

        latestMessageObserver.apply { mAdapter.registerAdapterDataObserver(this) }
    }

    private fun setUpBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (messageListViewModel.showComposeBar.get()) {
                    messageListViewModel.showComposeBar.set(false)
                } else if (AmityBackPressUtil.isLastActivityAndBackStackEmpty(
                        context!!,
                        parentFragmentManager
                    )
                ) {
                    val intent = Intent("com.medcura.ACTION_OPEN_CUSTOM_HOME").apply {
                        putExtra("ROUTE_NAME", "Chat")
                    }
                    startActivity(intent)
                } else {
                    requireActivity().finish()
                }
            }
        })
    }
    /*
        private fun isLastActivity(): Boolean {
            val activityManager = requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val tasks = activityManager.appTasks
            if (tasks.isNotEmpty()) {
                val taskInfo = tasks[0].taskInfo
                if (taskInfo.numActivities == 1) {
                    return true
                }
            }
            return false
        }
        private fun isBackStackEmpty(): Boolean {
            val fragmentManager = parentFragmentManager
            return fragmentManager.backStackEntryCount == 0
        }
        private fun isLastActivityAndBackStackEmpty(): Boolean {
            return isLastActivity() && isBackStackEmpty()
        }
    */


    private fun getChannelType() {
        disposable.add(messageListViewModel.getChannelType().take(1).subscribe { ekoChannel ->
            if (ekoChannel.getChannelType() == AmityChannel.Type.STANDARD) {
                binding.chatToolBar.ivAvatar.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        com.amity.socialcloud.uikit.common.R.drawable.amity_ic_group
                    )
                )
            } else {
                binding.chatToolBar.ivAvatar.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        com.amity.socialcloud.uikit.common.R.drawable.amity_ic_user
                    )
                )
            }

            if (ekoChannel.getChannelType() == AmityChannel.Type.CONVERSATION) {
                val metadata: JsonObject? = ekoChannel.getMetadata()
                val userId = AmityCoreClient.getUserId()
                Log.d("Mytag", "getChannelType:${ekoChannel.getMetadata()} ")
                val otherUserId = metadata?.get(AmityConstants.CHAT_METADATA_USER_IDS)
                    ?.asJsonArray
                    ?.find { it.asString != userId }
                    ?.asString
                essentialViewModel.otherUserId = otherUserId ?: ""
                Log.d("Mytag", "getChannelType: other user id - $otherUserId ")
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val userDetails =
                                messageListViewModel.getDisplayNameUser(otherUserId.toString())
                                    .blockingFirst()
                            withContext(Dispatchers.Main) {
                                userDetails.getDisplayName()?.let { userName ->
                                    messageListViewModel.title.set(userName)
                                }
                                userDetails.getAvatar()?.let { image ->
                                    messageListViewModel.avatarUrl.set(image.getUrl())
                                }
                            }
                        } catch (e: Exception) {
                            // Handle the exception here
                            e.printStackTrace() // You can log the exception or perform other error-handling tasks
                        }
                    }
                }

            } else {
                ekoChannel.membership().getMembers().build().query().doOnNext { pagingMember ->
                    pagingMember.filter { it.getUserId() != AmityCoreClient.getUserId() }
                }.subscribe()
                val metadata: JsonObject? = ekoChannel.getMetadata()
                Log.d("Mytag", "getChannelType:${ekoChannel.getMetadata()} ")
                val userId = AmityCoreClient.getUserId()
                val otherUserId = metadata?.get(AmityConstants.CHAT_METADATA_USER_IDS)
                    ?.asJsonArray
                    ?.find { it.asString != userId }
                    ?.asString
                if (otherUserId != null) {
                    essentialViewModel.otherUserId = otherUserId
                }
                Log.d("Mytag", "getChannelType: other user id - $otherUserId ")
                messageListViewModel.title.set(ekoChannel.getDisplayName())
                messageListViewModel.avatarUrl
                    .set(ekoChannel.getAvatar()?.getUrl(AmityImage.Size.SMALL))
            }
        })
    }

    private val chatSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                activity?.finish()
            }
        }

    private fun initToolBar() {
        binding.chatToolBar.apply {
            if (essentialViewModel.enableChatToolbar) {
                (activity as AppCompatActivity).supportActionBar?.displayOptions =
                    ActionBar.DISPLAY_SHOW_CUSTOM
                (activity as AppCompatActivity).setSupportActionBar(root as Toolbar)

                ivBack.setOnClickListener {
                    if (AmityBackPressUtil.isLastActivityAndBackStackEmpty(
                            requireContext(),
                            parentFragmentManager
                        )
                    ) {
                        val intent = Intent("com.medcura.ACTION_OPEN_CUSTOM_HOME").apply {
                            putExtra("ROUTE_NAME", "Chat")
                        }
                        startActivity(intent)
                        activity?.finish()
                    } else {
                        activity?.finish()
                    }
                }
                val sharedPref = context?.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                val isLiveStreamPermitted =
                    sharedPref?.getBoolean("PREF_USER_CAN_LIVESTREAM", false) ?: false
                if (isLiveStreamPermitted) {
                    ivsetting.visibility = View.VISIBLE
                    ivsetting.setOnClickListener {
                        Log.d("MYtag", "initToolBar: pressed")

                        val chatListIntent =
                            AmityChatSettingsActivity.newIntent(
                                requireContext(),
                                essentialViewModel.channelId,
                                essentialViewModel.otherUserId
                            )
                        chatSettingsLauncher.launch(chatListIntent)
                    }
                    root.visibility = View.VISIBLE
                } else {
                    root.visibility = View.GONE
                }
            }
        }
    }

    private fun initRecyclerView() {
        mAdapter =
            AmityMessagePagingAdapter(
                messageListViewModel,
                viewHolderListener,
                this,
                activity?.baseContext!!
            )
        val layoutManager = LinearLayoutManager(activity).apply {
            this.reverseLayout = true
        }
        binding.rvChatList.apply {
            this.layoutManager = layoutManager
            this.adapter = mAdapter
            this.addItemDecoration(
                AmityRecyclerViewItemDecoration(
                    0,
                    0,
                    resources.getDimensionPixelSize(com.amity.socialcloud.uikit.common.R.dimen.amity_padding_xs)
                )
            )
            this.itemAnimator = null
            val percentage = 30F / 100
            val background = ColorUtils.setAlphaComponent(
                AmityColorPaletteUtil.getColor(
                    ContextCompat.getColor(
                        requireContext(),
                        com.amity.socialcloud.uikit.common.R.color.amityColorBase
                    ), AmityColorShade.SHADE4
                ), (percentage * 255).toInt()
            )
            this.setBackgroundColor(background)
        }
        observeScrollingState(layoutManager)
        observeMessages()
    }

    private fun observeMessages() {
        messageListDisposable = messageListViewModel.getAllMessages().subscribe { messageList ->
            mAdapter.submitData(lifecycle, messageList)
            messageListViewModel.isScrollable.set(binding.rvChatList.computeVerticalScrollRange() > binding.rvChatList.height)
        }
        messageListViewModel.startReading()
    }

    private val latestMessageObserver by lazy {
        object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (isReachBottom) {
                    scrollToLastPosition()
                }
            }
        }
    }

    private fun hideLoadingView() {
        binding.loadingView.visibility = View.GONE
    }

    private fun scrollToLastPosition() {
        binding.rvChatList.scrollToPosition(0)
        isReachBottom = true
    }

    private fun observeViewModelEvents() {
        messageListViewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.CAMERA_CLICKED -> takePicture()
                AmityEventIdentifier.PICK_FILE -> pickFile()
                AmityEventIdentifier.PICK_IMAGE -> pickMultipleImages()
                AmityEventIdentifier.MSG_SEND_ERROR -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        val snackBar =
                            Snackbar.make(
                                binding.rvChatList,
                                R.string.amity_failed_msg,
                                Snackbar.LENGTH_SHORT
                            )
                        snackBar.show()
                    }
                }

                AmityEventIdentifier.MSG_SEND_SUCCESS -> scrollToLastPosition()
                AmityEventIdentifier.TOGGLE_CHAT_COMPOSE_BAR -> toggleSoftKeyboard()
                AmityEventIdentifier.SHOW_AUDIO_RECORD_UI -> showAudioRecordUi()
                else -> {

                }
            }
        }
    }

    private fun showAudioRecordUi() {
        AmityAndroidUtil.hideKeyboard(binding.layoutParent)
        messageListViewModel.showComposeBar.set(false)
    }

    private fun toggleSoftKeyboard() {
        messageListViewModel.isVoiceMsgUi.set(false)
        if (AmityAndroidUtil.isSoftKeyboardOpen(binding.layoutParent)) {
            AmityAndroidUtil.hideKeyboard(binding.layoutParent)
            Handler(Looper.getMainLooper()).postDelayed({
                messageListViewModel.showComposeBar.set(true)
            }, 300)
        } else {
            if (messageListViewModel.showComposeBar.get()) {
                messageListViewModel.showComposeBar.set(false)
                binding.etMessage.requestFocus()
                AmityAndroidUtil.showKeyboard(binding.etMessage)
            } else {
                messageListViewModel.showComposeBar.set(true)
            }
        }

        if (messageListViewModel.keyboardHeight.get() == 0) {
            val height = AmityAndroidUtil.getKeyboardHeight(binding.layoutParent)
            if (height != null && height > 0) {
                messageListViewModel.keyboardHeight.set(height)
            }
        }
    }

    private fun pickMultipleImages() {
        if (isImagePermissionGranted) {
            currentCount = 0
            if (currentCount == AmityConstants.MAX_SELECTION_COUNT) {
                view?.showSnackBar(getString(com.amity.socialcloud.uikit.common.R.string.amity_max_image_selected))
            } else {
                Matisse.from(this)
                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                    .countable(true)
                    .maxSelectable(AmityConstants.MAX_SELECTION_COUNT - currentCount)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .imageEngine(GlideEngine())
                    .theme(com.amity.socialcloud.uikit.common.R.style.AmityImagePickerTheme)
                    .forResult(AmityConstants.PICK_IMAGES)
            }
        } else {
            pickMultipleImagesPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onFilePicked(data: Uri?) {
        view?.showSnackBar("$data", Snackbar.LENGTH_SHORT)
        if (data != null) {
            //  val file = Uri.fromFile(data.toFile())
            val file = getFileFromUri(data)
            if (file != null) {
                disposable.add(
                    messageListViewModel.sendFileMessage(file.toUri()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).doOnComplete {
                            msgSent = true
                        }.doOnError { msgSent = false }.subscribe()
                )
            }
        }

    }

     fun getFileFromUri(uri: Uri): File? {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val fileName = getFileName(uri)
        val file = File(requireContext().cacheDir, fileName)

        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        return file
    }

    // Extracts filename from URI
    private fun getFileName(uri: Uri): String {
        var name = "temp_file"
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex("_display_name")
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
        return name
    }


    override fun onImagePicked(data: Uri?) {

    }

    override fun onPhotoClicked(file: File?) {
        if (file != null) {
            val photoUri = Uri.fromFile(file)
            disposable.add(messageListViewModel.sendImageMessage(photoUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    msgSent = true
                }.doOnError {
                    msgSent = false
                }.subscribe()
            )
            if (messageListViewModel.showComposeBar.get()) {
                messageListViewModel.showComposeBar.set(false)
            }
        }
    }

    override fun onFileRecorded(audioFile: File?, time: Long) {
        messageListViewModel.isRecording.set(false)
        if (audioFile != null) {
            val audioFileUri = Uri.fromFile(audioFile)
            disposable.add(messageListViewModel.sendAudioMessage(audioFileUri, time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    msgSent = true
                }.doOnError {
                    msgSent = false
                }.subscribe()
            )
        }
    }


    override fun showMessage() {
        val layout: View = layoutInflater.inflate(
            R.layout.amity_view_audio_msg_error,
            activity?.findViewById(R.id.errorMessageContainer)
        )
        val textView = layout.findViewById<TextView>(R.id.tvMessage)
        textView.setShape(
            null,
            null,
            null,
            null,
            com.amity.socialcloud.uikit.common.R.color.amityColorBase,
            null,
            null
        )
        layout.showSnackBar("", Snackbar.LENGTH_SHORT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == AmityConstants.PICK_IMAGES) {
            if (requestCode == AmityConstants.PICK_IMAGES) {
                data?.let {
                    val imageUriList = Matisse.obtainResult(it)
                    for (uri in imageUriList) {
                        disposable.add(messageListViewModel.sendImageMessage(uri)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete {
                                msgSent = true
                            }.doOnError {
                                msgSent = false
                            }.subscribe()
                        )
                    }
                }
                if (messageListViewModel.showComposeBar.get()) {
                    messageListViewModel.showComposeBar.set(false)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        messageListViewModel.isRecording.set(false)
        mAdapter.pauseAndResetPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        messageListViewModel.stopReading()
        mAdapter.releaseMediaPlayer()
        try {
            latestMessageObserver.apply { mAdapter.unregisterAdapterDataObserver(this) }
        } catch (e: IllegalStateException) {
            Timber.e(e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        messageListViewModel.onAmityEventReceived.removeAllhandlers()
        if (messageListDisposable?.isDisposed == false) {
            messageListDisposable?.dispose()
        }
    }

    class Builder internal constructor(private val channelId: String) {

        private var enableChatToolbar = true
        private var enableConnectionBar = true
        private var customViewHolder: AmityMessagePagingAdapter.CustomViewHolderListener? = null

        fun enableChatToolbar(enable: Boolean): Builder {
            this.enableChatToolbar = enable
            return this
        }

        fun enableConnectionBar(enable: Boolean): Builder {
            this.enableConnectionBar = enable
            return this
        }

        fun customViewHolder(customViewHolder: AmityMessagePagingAdapter.CustomViewHolderListener): Builder {
            this.customViewHolder = customViewHolder
            return this
        }

        fun build(activity: AppCompatActivity): AmityChatRoomWithTextComposeBarFragment {
            val essentialViewModel =
                ViewModelProvider(activity).get(AmityChatRoomEssentialViewModel::class.java)
            essentialViewModel.channelId = channelId
            essentialViewModel.enableChatToolbar = enableChatToolbar
            essentialViewModel.enableConnectionBar = enableConnectionBar
            essentialViewModel.customViewHolder = customViewHolder
            return AmityChatRoomWithTextComposeBarFragment()
        }
    }


    companion object {
        internal fun newInstance(channelId: String): Builder {
            return Builder(channelId)
        }
    }
}

private const val PAGINATION_PRELOAD_THRESHOLD = 1
