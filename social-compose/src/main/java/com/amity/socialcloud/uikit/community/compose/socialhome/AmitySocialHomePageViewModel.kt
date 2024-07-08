package com.amity.socialcloud.uikit.community.compose.socialhome

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.community.query.AmityCommunitySortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityFilter
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityAdInjector
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AmitySocialHomePageViewModel : AmityBaseViewModel() {

    private val _postListState by lazy {
        MutableStateFlow<PostListState>(PostListState.EMPTY)
    }
    val postListState get() = _postListState

    private val _communityListState by lazy {
        MutableStateFlow<CommunityListState>(CommunityListState.EMPTY)
    }
    val communityListState get() = _communityListState

    private val _isGlobalFeedRefreshing by lazy {
        MutableStateFlow(false)
    }

    val isGlobalFeedRefreshing get() = _isGlobalFeedRefreshing

    fun setPostListState(state: PostListState) {
        _postListState.value = state
    }

    fun setCommunityListState(state: CommunityListState) {
        _communityListState.value = state
    }

    fun setGlobalFeedRefreshing() {
        viewModelScope.launch {
            _isGlobalFeedRefreshing.value = true
            delay(1500)
            _isGlobalFeedRefreshing.value = false
        }
    }

    fun getMyCommunities(): Flow<PagingData<AmityCommunity>> {
        return AmitySocialClient.newCommunityRepository()
            .getCommunities()
            .filter(AmityCommunityFilter.MEMBER)
            .sortBy(AmityCommunitySortOption.DISPLAY_NAME)
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    fun getGlobalFeed(): Flow<PagingData<AmityListItem>> {
        val injector = AmityAdInjector<AmityPost>(
            placement = AmityAdPlacement.FEED,
            communityId = null,
        )

        return AmitySocialClient.newFeedRepository()
            .getGlobalFeed()
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .throttleLatest(2000, TimeUnit.MILLISECONDS)
            .map { injector.inject(it) }
            .asFlow()
            .catch {}
    }

    sealed class PostListState {
        object LOADING : PostListState()
        object SUCCESS : PostListState()
        object EMPTY : PostListState()
        object ERROR : PostListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): PostListState {
                return if (loadState is LoadState.Loading) {
                    LOADING
                } else if (loadState is LoadState.NotLoading && itemCount == 0 && loadState.endOfPaginationReached) {
                    EMPTY
                } else if (loadState is LoadState.Error && itemCount == 0) {
                    ERROR
                } else {
                    SUCCESS
                }
            }
        }
    }

    sealed class CommunityListState {
        object LOADING : CommunityListState()
        object SUCCESS : CommunityListState()
        object EMPTY : CommunityListState()
        object ERROR : CommunityListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): CommunityListState {
                return if (loadState is LoadState.Loading) {
                    LOADING
                } else if (loadState is LoadState.NotLoading && itemCount == 0 && loadState.endOfPaginationReached) {
                    EMPTY
                } else if (loadState is LoadState.Error && itemCount == 0) {
                    ERROR
                } else {
                    SUCCESS
                }
            }
        }
    }
}