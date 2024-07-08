package com.amity.socialcloud.uikit.community.compose.post.detail

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class AmityPostDetailPageViewModel : AmityBaseViewModel() {

    fun getCurrentUser(): Flowable<AmityUser> {
        return AmityCoreClient.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getPost(postId: String): Flow<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .getPost(postId)
            .asFlow()
            .catch {}
    }

    fun addReaction(postId: String) {
        AmityCoreClient.newReactionRepository()
            .addReaction(AmityReactionReference.POST(postId), AmityConstants.POST_REACTION)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun removeReaction(postId: String) {
        AmityCoreClient.newReactionRepository()
            .removeReaction(AmityReactionReference.POST(postId), AmityConstants.POST_REACTION)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun isCommunityModerator(post: AmityPost?): Boolean {
        val roles =
            (post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCreatorMember()?.getRoles()
        return roles?.any {
            it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE
        } ?: false
    }
}