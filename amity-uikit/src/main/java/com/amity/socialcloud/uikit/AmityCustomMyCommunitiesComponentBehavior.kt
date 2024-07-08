package com.amity.socialcloud.uikit

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityMyCommunitiesComponentBehavior
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity

class AmityCustomMyCommunitiesComponentBehavior : AmityMyCommunitiesComponentBehavior() {
    override fun goToCommunityProfilePage(context: Context, community: AmityCommunity) {
        val intent = AmityCommunityPageActivity.newIntent(
            context = context,
            community = community,
        )
        context.startActivity(intent)
    }
}