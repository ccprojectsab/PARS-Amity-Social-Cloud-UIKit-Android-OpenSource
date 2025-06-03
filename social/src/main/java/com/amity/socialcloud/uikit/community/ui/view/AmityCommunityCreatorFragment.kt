package com.amity.socialcloud.uikit.community.ui.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.amity.socialcloud.uikit.AmityLocalisationSocial
import com.amity.socialcloud.uikit.common.contract.AmityPickMemberContract
import com.amity.socialcloud.uikit.common.memberpicker.adapter.AmityAddedMembersAdapter
import com.amity.socialcloud.uikit.common.memberpicker.listener.AmityAddedMemberClickListener
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.R

import com.amity.socialcloud.uikit.community.utils.AmityAddedMemberItemDecoration


class AmityCommunityCreatorFragment : AmityCommunityCreateBaseFragment(),
    AmityAddedMemberClickListener {

    private lateinit var addedMembersAdapter: AmityAddedMembersAdapter
    private val selectMembers = registerForActivityResult(AmityPickMemberContract()) { list ->
        viewModel.selectedMembersList.clear()
        viewModel.selectedMembersList.addAll(list ?: arrayListOf())
        setAddMemberVisibility()
        setCount()
        addedMembersAdapter.submitList(viewModel.selectedMembersList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.amityImageText.text = AmityLocalisationSocial.getString(R.string.amity_upload_image)
        binding.tvCommName.text = AmityLocalisationSocial.getString(R.string.amity_community_name)
        binding.amityImageText.text = AmityLocalisationSocial.getString(R.string.amity_upload_image)
        binding.tvCommName.text = AmityLocalisationSocial.getString(R.string.amity_community_name)
        binding.ccName.hint = AmityLocalisationSocial.getString(R.string.amity_cc_hint)
        binding.tvAbout.text = AmityLocalisationSocial.getString(R.string.amity_about)
        binding.etDescription.hint = AmityLocalisationSocial.getString(R.string.amity_enter_description)
        binding.tvCategory.text = AmityLocalisationSocial.getString(R.string.amity_category_required_field)
        binding.category.hint = AmityLocalisationSocial.getString(R.string.amity_please_select_category)
        binding.tvAdmin.text = AmityLocalisationSocial.getString(R.string.amity_only_admin)
        binding.tvAdminDescription.text = AmityLocalisationSocial.getString(R.string.amity_admin_description)
        binding.tvPublic.text = AmityLocalisationSocial.getString(R.string.amity_tv_public)
        binding.tvPublicDescription.text = AmityLocalisationSocial.getString(R.string.amity_public_description)
        binding.tvPrivate.text = AmityLocalisationSocial.getString(R.string.amity_tv_private)
        binding.tvPrivateDescription.text = AmityLocalisationSocial.getString(R.string.amity_private_description)
        binding.tvAddMembers.text = AmityLocalisationSocial.getString(R.string.amity_add_members)
        binding.btnCreateCommunity.text = AmityLocalisationSocial.getString(R.string.amity_create_community)
        binding.btnEditCommunity.text = AmityLocalisationSocial.getString(com.amity.socialcloud.uikit.common.R.string.amity_save)
        addMembers()
        setAddedMemberRecyclerView()
    }

    private fun addMembers() {
        getBindingVariable().ivAdd.setOnClickListener {
            goToAddMembersActivity()
        }
    }

    private fun setAddedMemberRecyclerView() {
        addedMembersAdapter = AmityAddedMembersAdapter(this)
        getBindingVariable().rvAddedMembers.layoutManager = GridLayoutManager(requireContext(), 2)
        getBindingVariable().rvAddedMembers.adapter = addedMembersAdapter
        getBindingVariable().rvAddedMembers.addItemDecoration(
            AmityAddedMemberItemDecoration(
                resources.getDimensionPixelSize(com.amity.socialcloud.uikit.common.R.dimen.amity_padding_xs),
                resources.getDimensionPixelSize(com.amity.socialcloud.uikit.common.R.dimen.amity_padding_s)
            )
        )
        addedMembersAdapter.submitList(viewModel.selectedMembersList)
    }

    override fun onAddButtonClicked() {
        goToAddMembersActivity()
    }

    override fun onMemberCountClicked() {
        goToAddMembersActivity()
    }

    override fun onMemberRemoved(itemAmity: AmitySelectMemberItem) {
        val lastItem = viewModel.selectedMembersList[viewModel.selectedMembersList.lastIndex]
        if (lastItem.name == "ADD") {
            viewModel.selectedMembersList.remove(lastItem)
        }
        viewModel.selectedMembersList.remove(itemAmity)
        setAddMemberVisibility()
        setCount()
        addedMembersAdapter.submitList(viewModel.selectedMembersList)
    }

    private fun setCount() {
        if (viewModel.selectedMembersList.size < 8) {
            if (viewModel.selectedMembersList.isNotEmpty()) {
                viewModel.selectedMembersList.add(AmitySelectMemberItem("", "", "ADD"))
            }
        } else {
            viewModel.selectedMembersList[7].subTitle =
                "${viewModel.selectedMembersList.size - 8}"
        }
    }

    private fun goToAddMembersActivity() {

        selectMembers.launch(viewModel.selectedMembersList)
    }

    private fun setAddMemberVisibility() {
        viewModel.addMemberVisible.set(viewModel.selectedMembersList.isEmpty())
    }

    class Builder internal constructor() {
        fun build(): AmityCommunityCreatorFragment {
            return AmityCommunityCreatorFragment()
        }
    }

    companion object {

        fun newInstance(): Builder {
            return Builder()
        }
    }
}