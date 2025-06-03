package com.amity.socialcloud.uikit.community.mycommunity.activity

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.mycommunity.fragment.AmityMyCommunityFragment
import com.amity.socialcloud.uikit.AmityLocalisationSocial

class AmityMyCommunityActivity : AmityBaseToolbarFragmentContainerActivity() {
    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, com.amity.socialcloud.uikit.common.R.drawable.amity_ic_arrow_back)
        )

        getToolBar()?.setCenterString(AmityLocalisationSocial.getString(R.string.amity_my_community))
    }

    override fun getContentFragment(): Fragment {
        return AmityMyCommunityFragment.newInstance().build()
    }
}