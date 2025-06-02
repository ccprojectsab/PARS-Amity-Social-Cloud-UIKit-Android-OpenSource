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
        return stringsMap[stringResId]?.let { LocalizationManager.getString(it) } ?: applicationContext?.getString(stringResId) ?: "Unknown String"
    }

    /**
     * Get formatted string by resource ID with arguments
     * @param stringResId String resource ID
     * @param formatArgs Variable arguments for string formatting
     * @return Formatted hardcoded string value
     */
    fun getString(@StringRes stringResId: Int, vararg formatArgs: Any?): String {
        return stringsMap[stringResId]?.let { LocalizationManager.getString(it, formatArgs) } ?: applicationContext?.getString(stringResId, formatArgs) ?: "Unknown String"
    }

    private val stringsMap: Map<Int, String> = mapOf(
        // General UI/Actions
        R.string.amity_general_search to "common.general.action.search",
        R.string.amity_read_more to "common.general.action.readMore",
        R.string.amity_feed to "common.general.content.feed",
        R.string.amity_gallery to "common.media.gallery",
        R.string.amity_follow to "common.general.action.follow",
        R.string.amity_followers to "common.general.content.followers",
        R.string.amity_search to "common.general.action.search",
        R.string.amity_search_results to "common.general.content.searchResults",
        
        // UI Elements
        R.string.amity_left_icon to "common.ui.leftIcon",
        R.string.amity_center_icon to "common.ui.centerIcon",
        R.string.amity_right_icon to "common.ui.rightIcon",
        
        // Media Related
        R.string.amity_choose_image to "common.media.chooseImage",
        
        // Post Actions
        R.string.amity_edit_post to "common.post.action.edit",
        R.string.amity_delete_post to "common.post.action.delete",
        
        // General Actions
        R.string.amity_save to "common.general.action.save",
        R.string.amity_saved to "common.general.content.saved",
        R.string.amity_delete_msg to "common.general.content.deleteMessage",
        R.string.amity_dlt_dlg_body to "common.general.content.deleteMessageBody",
        R.string.amity_failed_dlg_body to "common.error.messageNotSent",
        R.string.amity_cancel to "common.general.action.cancel",
        
        // Time Related
        R.string.amity_day to "common.general.time.day",
        R.string.amity_hour to "common.general.time.hour",
        R.string.amity_min to "common.general.time.minute",
        R.string.amity_just_now to "common.general.time.justNow",
        
        // UI Actions
        R.string.amity_save_caps to "common.general.action.saveCaps",
        R.string.amity_anonymous to "common.general.content.anonymous",
        R.string.amity_edit_profile to "common.profile.action.edit",
        R.string.amity_message to "common.general.content.message",
        R.string.amity_display_name to "common.profile.content.displayName",
        
        // Status Messages
        R.string.amity_report_msg to "common.general.content.reportMessage",
        R.string.amity_max_image_selected to "common.media.error.maxImageLimit",
        R.string.amity_unable_to_delete to "common.error.unableToDelete",
        R.string.amity_try_again to "common.general.action.tryAgain",
        
        // Media Preview
        R.string.amity_image_preview_title to "common.media.content.imagePreviewTitle",
        R.string.amity_release_to_send to "common.general.action.releaseToSend",
        
        // Time Format
        R.string.amity_time to "common.general.time.format",
        
        // Community Actions
        R.string.amity_remove_from_community to "common.community.action.removeMember",
        
        // Error Messages
        R.string.amity_no_permission_title to "common.error.noPermissionTitle",
        R.string.amity_no_permission_message to "common.error.noPermissionMessage",
        
        // File Operations
        R.string.amity_downloading_file to "common.media.content.downloadingFile",
        R.string.amity_download_complete to "common.media.content.downloadComplete",
        
        // Profile Related
        R.string.amity_following_count to "common.profile.content.followingCount",
        R.string.amity_remove_follower to "common.profile.action.removeFollower",
        R.string.amity_remove_follower_msg to "common.profile.content.removeFollowerMessage",
        
        // General UI States
        R.string.amity_no_user_found to "common.error.noUserFound",
        R.string.amity_yesterday to "common.general.time.yesterday",
        R.string.amity_done to "common.general.action.done",
        R.string.amity_ok to "common.general.action.ok",
        
        // Member Management
        R.string.amity_select_members to "common.member.action.select",
        R.string.amity_add to "common.general.action.add",
        R.string.amity_selected to "common.general.content.selected",

        // Plurals for Time
        R.plurals.amity_number_of_years to "common.general.time.years",
        R.plurals.amity_number_of_months to "common.general.time.months",
        R.plurals.amity_number_of_weeks to "common.general.time.weeks",
        R.plurals.amity_number_of_days to "common.general.time.days",
        R.plurals.amity_number_of_hours to "common.general.time.hours",
        R.plurals.amity_number_of_mins to "common.general.time.minutes"
    )
}