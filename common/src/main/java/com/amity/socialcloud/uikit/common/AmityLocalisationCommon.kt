package com.amity.socialcloud.uikit.common

import android.content.Context
import androidx.annotation.StringRes
import com.amity.socialcloud.uikit.common.R
import com.pars.localisation.LocalizationManager

object AmityLocalisationCommon
{
    private var applicationContext: Context? = null

    fun initialize(context: Context) {
        applicationContext = context
    }

    /**
     * Get string by resource ID
     * @param stringResId String resource ID
     * @return Hardcoded string value
     */
    fun getString(@StringRes stringResId: Int): String {
        return stringsMap[stringResId] ?.let { LocalizationManager.getString(it) }?: applicationContext?.getString(stringResId) ?: "Unknown String"
    }

    /**
     * Get formatted string by resource ID with arguments
     * @param stringResId String resource ID
     * @param formatArgs Variable arguments for string formatting
     * @return Formatted hardcoded string value
     */
    fun getString(@StringRes stringResId: Int, vararg formatArgs: Any?): String {
        return stringsMap[stringResId] ?.let { LocalizationManager.getString(it) }?: applicationContext?.getString(stringResId, formatArgs) ?: "Unknown String"
    }

    private val stringsMap: Map<Int, String> = mapOf(
        // General UI/Actions
        R.string.amity_general_search to "app.common.faq.content.searchPlaceholder",
        R.string.amity_read_more to "app.common.buttons.ok",
        R.string.amity_feed to "app.home.content.newsTips",
        R.string.amity_gallery to "app.common.content.generalGallery",
        R.string.amity_follow to "app.common.content.generalFollow",
        R.string.amity_followers to "social.community.followRequestsPopup.followers",
        R.string.amity_search to "app.common.content.generalSearch",
        R.string.amity_search_results to "app.common.content.generalSearch",
        
        // UI Elements
        R.string.amity_left_icon to "app.common.content.generalImage_gallery",
        R.string.amity_center_icon to "app.common.content.generalImage_gallery",
        R.string.amity_right_icon to "app.common.content.generalImage_gallery",
        
        // Media Related
        R.string.amity_choose_image to "app.userProfile.chooseImage.title",


        // Post Actions
        R.string.amity_edit_post to "social.general.content.generalEdit",
        R.string.amity_delete_post to "social.general.content.generalDelete",
        
        // General Actions
        R.string.amity_save to "app.common.buttons.save",
        R.string.amity_saved to "social.general.content.generalSaved",
        R.string.amity_delete_msg to "social.general.content.generalDelete",
        R.string.amity_dlt_dlg_body to "app.common.errors.errorInvalidCredentials",
        R.string.amity_failed_dlg_body to "app.common.errors.errorInvalidCredentials",
        R.string.amity_cancel to "app.common.buttons.cancel",
        
        // Time Related
        R.string.amity_day to "social.general.unit.content.daySingular",
        R.string.amity_hour to "social.general.unit.content.hourSingular",
        R.string.amity_min to "social.general.unit.content.minuteSingular",
        R.string.amity_just_now to "social.general.time.justNow",
        
        // UI Actions
        R.string.amity_save_caps to "app.common.buttons.save",
        R.string.amity_anonymous to "social.general.content.generalAnonymous",
        R.string.amity_edit_profile to "app.userProfile.editProfile.title",
        R.string.amity_message to "social.chat.messageBox.inputHint",
        R.string.amity_display_name to "app.userProfile.createProfile.firstName",
        
        // Status Messages
        R.string.amity_report_msg to "social.general.content.generalReport",
        R.string.amity_max_image_selected to "social.post.create.attachmentCountLimitExceed",
        R.string.amity_unable_to_delete to "social.general.content.errorMessage",
        R.string.amity_try_again to "social.general.content.errorMessage",
        
        // Media Preview
        R.string.amity_image_preview_title to "app.common.content.generalImage_gallery",
        R.string.amity_release_to_send to "social.chat.messageBox.recordHint",
        
        // Time Format
        R.string.amity_time to "social.general.time.justNow",
        
        // Community Actions
        R.string.amity_remove_from_community to "social.community.members.removeOption",
        
        // Error Messages
        R.string.amity_no_permission_title to "app.common.errors.errorInvalidCredentials",
        R.string.amity_no_permission_message to "app.common.errors.errorInvalidCredentials",
        
        // File Operations
        R.string.amity_downloading_file to "social.general.content.generalFile",
        R.string.amity_download_complete to "social.general.content.generalFile",
        
        // Profile Related
        R.string.amity_following_count to "social.community.followRequestsPopup.following",
        R.string.amity_remove_follower to "social.user.action.remove",
        R.string.amity_remove_follower_msg to "social.user.content.unfollowDescription",
        
        // General UI States
        R.string.amity_no_user_found to "social.community.basicInfo.noUserFound",
        R.string.amity_yesterday to "social.general.time.yesterday",
        R.string.amity_done to "app.common.buttons.done",
        R.string.amity_ok to "app.common.buttons.ok",
        
        // Member Management
        R.string.amity_select_members to "social.community.memberSelect.selectTitle",
        R.string.amity_add to "social.general.content.generalAdd",
        R.string.amity_selected to "social.general.content.selected",
        R.string.amity_following_count to "social.community.following",
        // Plurals for Time
        R.plurals.amity_number_of_years to "social.general.time.years",
        R.plurals.amity_number_of_months to "social.general.time.months",
        R.plurals.amity_number_of_weeks to "social.general.time.weeks",
        R.plurals.amity_number_of_days to "social.general.time.days",
        R.plurals.amity_number_of_hours to "social.general.time.hours",
        R.plurals.amity_number_of_mins to "social.general.time.minutes"

    )
}