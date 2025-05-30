package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.image.rememberZoomState
import com.amity.socialcloud.uikit.common.ui.image.zoomable
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.RenderAltTextConfigSheet
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextMedia
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostVideoPlayerHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityPostMediaPreviewDialog(
    modifier: Modifier = Modifier,
    childPosts: List<AmityPost.Data> = emptyList(),
    isVideoPost: Boolean,
    selectedFileId: String,
    isPostCreator: Boolean = false,
    onDismiss: () -> Unit,
) {
    val imageMap = remember { mutableMapOf<String, AmityImage>() }
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(15_000)
            .setSeekForwardIncrementMs(15_000)
            .setPauseAtEndOfMediaItems(true)
            .build()
    }
    var isAudioMuted by remember { mutableStateOf(false) }

    var verticalDragAmount by remember {
        mutableFloatStateOf(0f)
    }

    val pagerState = rememberPagerState {
        childPosts.size
    }

    val videos by remember(childPosts.size) {
        prepareVideoUrl(childPosts)
    }.subscribeAsState(initial = emptyList())

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var openMenu by remember { mutableStateOf(false) }
    val onMenuClick: (Int) -> Unit = { index ->
        childPosts.getOrNull(index)?.let { data ->
            if (data is AmityPost.Data.IMAGE) {
                val image = data.getImage()
                image?.let {
                    image.getFileId().let { fileId ->
                        imageMap[fileId]?.let {
                            openMenu = true
                            viewModel.setAltTextMedia(AltTextMedia.Image(it))
                        }
                    }
                }
            }
        }

    }


    LaunchedEffect(selectedFileId) {
        pagerState.scrollToPage(childPosts.indexOfFirst {
            when (it) {
                is AmityPost.Data.IMAGE -> it.getPostId() == selectedFileId
                is AmityPost.Data.VIDEO -> it.getPostId() == selectedFileId
                else -> false
            }
        })
    }

    LaunchedEffect(exoPlayer) {
        AmityPostVideoPlayerHelper.setup(exoPlayer)
    }

    LaunchedEffect(videos) {
        AmityPostVideoPlayerHelper.add(videos)
    }

    LaunchedEffect(pagerState.currentPage) {
        delay(100)
        AmityPostVideoPlayerHelper.playMediaItem(pagerState.currentPage)
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        AmityBaseComponent(
            componentId = "post_media_preview",
            needScaffold = true,
        ) {
            HorizontalPager(
                state = pagerState,
                key = { it },
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                if (verticalDragAmount > 0) {
                                    onDismiss()
                                }
                                verticalDragAmount = 0f
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            verticalDragAmount += dragAmount
                        }
                    }
            ) { index ->
                when (val data = childPosts[index]) {
                    is AmityPost.Data.IMAGE -> {
                        val image = data.getImage()
                        image?.let {
                            if (imageMap[image.getFileId()] == null) {
                                imageMap[image.getFileId()] = image
                            }
                        }
                        val imageUrl = image?.getUrl(AmityImage.Size.MEDIUM)

                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (imageUrl != null) {
                                AsyncImage(
                                    model = ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(imageUrl)
                                        .crossfade(true)
                                        .networkCachePolicy(CachePolicy.ENABLED)
                                        .diskCachePolicy(CachePolicy.ENABLED)
                                        .memoryCachePolicy(CachePolicy.ENABLED)
                                        .build(),
                                    contentDescription = "Image Post",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .zoomable(rememberZoomState()),
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(480.dp)
                                        .background(AmityTheme.colors.baseShade4),
                                )
                            }
                        }
                        if (openMenu) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    openMenu = false
                                },
                                sheetState = rememberModalBottomSheetState(
                                    skipPartiallyExpanded = true,
                                ),
                                containerColor = AmityTheme.colors.background,
                                contentWindowInsets = { WindowInsets.waterfall },
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = modifier
                                        .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
                                ) {
                                    AmityBottomSheetActionItem(
                                        icon = R.drawable.amity_ic_edit_profile,
                                        text = "Edit alt text",
                                        modifier = modifier.testTag("bottom_sheet_edit_alt_text_button"),
                                    ) {
                                        image?.getFileId()?.let { fileId ->
                                            imageMap[fileId]?.let {
                                                viewModel.showAltTextConfigSheet()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is AmityPost.Data.VIDEO -> {
                        AmityPostMediaVideoPlayer(
                            exoPlayer = exoPlayer,
                            isVisible = pagerState.currentPage == index,
                        )
                    }

                    else -> {}
                }
            }

            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(Color.Black.copy(alpha = 0.5f)),
            ) {
                val (closeBtn, muteBtn, counter, menuBtn) = createRefs()

                AmityMenuButton(
                    size = 32.dp,
                    iconPadding = 8.dp,
                    modifier = Modifier
                        .zIndex(Float.MAX_VALUE).constrainAs(closeBtn) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    },
                ) {
                    onDismiss()
                }

                if (isVideoPost) {
                    Image(
                        painter = painterResource(
                            id = if (isAudioMuted) R.drawable.amity_ic_story_audio_mute
                            else R.drawable.amity_ic_story_audio_unmute
                        ),
                        contentDescription = "Video Audio",
                        modifier = Modifier
                            .size(32.dp)
                            .constrainAs(muteBtn) {
                                top.linkTo(parent.top, margin = 64.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                            }
                            .clickableWithoutRipple {
                                isAudioMuted = !isAudioMuted
                                exoPlayer.volume = if (isAudioMuted) 0f else 1f
                            },
                    )
                }

                Text(
                    text = "${pagerState.currentPage + 1} / ${childPosts.size}",
                    style = AmityTheme.typography.titleLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    ),
                    modifier = modifier
                        .semantics {
                            contentDescription = "Photo ${pagerState.currentPage + 1} of ${childPosts.size}"
                        }
                        .constrainAs(counter) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(closeBtn.bottom)
                    }
                )

                if (isPostCreator) {
                    AmityMenuButton(
                        icon = R.drawable.amity_ic_more_horiz,
                        size = 32.dp,
                        iconPadding = 2.dp,
                        modifier = Modifier.constrainAs(menuBtn) {
                            top.linkTo(parent.top, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        },
                    ) {
                        onMenuClick(pagerState.currentPage)
                    }
                }
            }
            RenderAltTextConfigSheet(
                forcedEditMode = true,
                onSucess = { image ->
                    imageMap[image.getFileId()] = image
                    AmityUIKitSnackbar.publishSnackbarMessage(
                        message = context.getString(R.string.amity_image_alt_text_updated_message),
                    )
                    },
                onDismiss = {
                    openMenu = false
                })
            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                    AmityPostVideoPlayerHelper.clear()
                }
            }

            BackHandler {
                onDismiss()
            }
        }
    }
}

fun prepareVideoUrl(childPosts: List<AmityPost.Data>): Single<List<AmityVideo>> {
    return Single.zip(childPosts
        .filterIsInstance<AmityPost.Data.VIDEO>()
        .map { it.getVideo() }
    ) { videoList ->
        videoList
            .map { video -> video as AmityVideo }
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}