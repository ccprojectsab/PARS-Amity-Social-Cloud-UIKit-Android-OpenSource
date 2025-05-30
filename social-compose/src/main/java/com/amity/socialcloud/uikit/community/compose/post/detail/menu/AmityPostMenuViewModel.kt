package com.amity.socialcloud.uikit.community.compose.post.detail.menu

import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.ekoapp.ekosdk.internal.api.socket.request.FlagContentRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.printStackTrace

class AmityPostMenuViewModel : AmityBaseViewModel() {

    private val _sheetUIState by lazy {
        MutableStateFlow<AmityPostMenuSheetUIState>(AmityPostMenuSheetUIState.CloseSheet)
    }
    val sheetUIState get() = _sheetUIState

    private val _dialogUIState by lazy {
        MutableStateFlow<AmityPostMenuDialogUIState>(AmityPostMenuDialogUIState.CloseDialog)
    }
    val dialogUIState get() = _dialogUIState

    private val _hasDeleteCommunityPostPermission = MutableStateFlow<Boolean>(false)
    val hasDeleteCommunityPostPermission get() = _hasDeleteCommunityPostPermission.asStateFlow()

    private val _hasDeleteUserFeedPostPermission = MutableStateFlow<Boolean>(false)
    val hasDeleteUserFeedPostPermission get() = _hasDeleteUserFeedPostPermission.asStateFlow()

    fun updateSheetUIState(uiState: AmityPostMenuSheetUIState) {
        viewModelScope.launch {
            _sheetUIState.value = uiState
        }
    }

    fun updateDialogUIState(uiState: AmityPostMenuDialogUIState) {
        viewModelScope.launch {
            _dialogUIState.value = uiState
        }
    }

    fun deletePost(
        postId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newPostRepository()
            .softDeletePost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun flagPost(
        postId: String,
        reason: AmityContentFlagReason,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newPostRepository()
            .flagPost(postId, reason)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun unflagPost(
        postId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newPostRepository()
            .unflagPost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun checkDeleteCommunityPostPermission(communityId: String) {
        addDisposable(
            AmityCoreClient
                .hasPermission(AmityPermission.DELETE_COMMUNITY_POST)
                .atCommunity(communityId)
                .check()
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        _hasDeleteCommunityPostPermission.update { result }
                    },
                    { error ->
                        error.printStackTrace()
                        _hasDeleteCommunityPostPermission.update { false }
                    }
                )
        )
    }

    fun checkDeleteUserFeedPostPermission() {
        addDisposable(
            AmityCoreClient.hasPermission(AmityPermission.DELETE_USER_FEED_POST)
                .atGlobal()
                .check()
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { result ->
                    _hasDeleteUserFeedPostPermission.update {
                        result
                    }
                }
                .doOnError {
                    _hasDeleteUserFeedPostPermission.update {
                        false
                    }
                }
                .subscribe()
        )
    }

    fun isNotMember(post: AmityPost?): Boolean {
        val isNotMember =
            !((post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()?.isJoined()
                ?: true)
        return isNotMember
    }
}

sealed class AmityPostMenuSheetUIState(val postId: String) {

    data class OpenSheet(val id: String) : AmityPostMenuSheetUIState(id)

    data class OpenReportSheet(val id: String) : AmityPostMenuSheetUIState(id)

    data class OpenReportOtherReasonSheet(val id: String) : AmityPostMenuSheetUIState(id)

    data class OpenErrorSheet(val id: String) : AmityPostMenuSheetUIState(id)

    object CloseSheet : AmityPostMenuSheetUIState("")
}

sealed class AmityPostMenuDialogUIState {

    data class OpenConfirmDeleteDialog(val postId: String) : AmityPostMenuDialogUIState()

    data class OpenConfirmEditDialog(val postId: String) : AmityPostMenuDialogUIState()

    data class OpenConfirmClosePollDialog(val pollId: String) : AmityPostMenuDialogUIState()

    object CloseDialog : AmityPostMenuDialogUIState()
}