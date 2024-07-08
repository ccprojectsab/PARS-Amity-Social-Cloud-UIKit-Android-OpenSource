package com.amity.socialcloud.uikit.community.compose.search.global

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityFilter
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import java.util.concurrent.TimeUnit

class AmitySocialGlobalSearchPageViewModel : AmityBaseViewModel() {

    private val _keyword by lazy {
        MutableStateFlow("")
    }
    val keyword get() = _keyword

    fun setKeyword(keyword: String) {
        _keyword.value = keyword
    }

    private val _searchType by lazy {
        MutableStateFlow(AmityGlobalSearchType.COMMUNITY)
    }
    val searchType get() = _searchType

    fun setSearchType(searchType: AmityGlobalSearchType) {
        _searchType.value = searchType
    }

    private val _communityListState by lazy {
        MutableStateFlow<CommunityListState>(CommunityListState.SUCCESS)
    }

    val communityListState get() = _communityListState

    fun setCommunityListState(state: CommunityListState) {
        _communityListState.value = state
    }

    private val _userListState by lazy {
        MutableStateFlow<UserListState>(UserListState.SUCCESS)
    }

    val userListState get() = _userListState

    fun setUserListState(state: UserListState) {
        _userListState.value = state
    }

    fun searchCommunities(): Flow<PagingData<AmityCommunity>> {
        return AmitySocialClient.newCommunityRepository()
            .searchCommunities(_keyword.value)
            .run {
                if (_searchType.value == AmityGlobalSearchType.MY_COMMUNITY) {
                    filter(AmityCommunityFilter.MEMBER)
                } else {
                    this
                }
            }
            .build()
            .query()
            .asFlow()
            .cachedIn(viewModelScope)
            .catch {}
    }

    fun searchUsers(): Flow<PagingData<AmityUser>> {
        return AmityCoreClient.newUserRepository()
            .searchUsers(_keyword.value)
            .build()
            .query()
            .debounce(300, TimeUnit.MILLISECONDS)
            .asFlow()
            .cachedIn(viewModelScope)
            .catch {}
    }

    sealed class CommunityListState {
        object LOADING : CommunityListState()
        object EMPTY : CommunityListState()
        object SUCCESS : CommunityListState()

        companion object {
            fun from(
                loadState: CombinedLoadStates,
                itemCount: Int,
            ): CommunityListState {
                return if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                    LOADING
                } else if (
                    loadState.refresh is LoadState.NotLoading &&
                    loadState.append is LoadState.NotLoading &&
                    itemCount == 0
                ) {
                    EMPTY
                } else {
                    SUCCESS
                }
            }
        }
    }

    sealed class UserListState {
        object LOADING : UserListState()
        object EMPTY : UserListState()
        object SUCCESS : UserListState()

        companion object {
            fun from(
                loadState: CombinedLoadStates,
                itemCount: Int,
            ): UserListState {
                return if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                    LOADING
                } else if (
                    loadState.refresh is LoadState.NotLoading &&
                    loadState.append is LoadState.NotLoading &&
                    itemCount == 0
                ) {
                    EMPTY
                } else {
                    SUCCESS
                }
            }
        }
    }
}

enum class AmityGlobalSearchType {
    COMMUNITY,
    MY_COMMUNITY,
    USER;
}