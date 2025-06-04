package com.amity.socialcloud.uikit.chat

import android.content.Context
import androidx.annotation.StringRes
import com.pars.localisation.LocalizationManager

/**
 * AmityLocalisation object class for centralized string management
 * Currently returns hardcoded strings - will be enhanced to support dynamic localization
 */
object AmityLocalisationChat {

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
        R.string.amity_chat to "social.chat.messageBox.chatTitle",
        R.string.amity_title_recent_chat to "social.chat.messageBox.recentTitle",
        R.string.amity_title_directory to "social.chat.messageBox.directoryTitle",
        R.string.amity_no_conversations to "social.chat.messageBox.emptyChatList",
        R.string.amity_type_message to "social.chat.messageBox.textMessagePlaceholder",
        R.string.amity_settings to "social.general.content.generalSettings",
        R.string.amity_edit to "social.general.content.generalEdit",
        R.string.amity_delete to "social.general.content.generalDelete",
        R.string.amity_report to "social.general.content.generalReport",
        R.string.amity_unreport to "social.general.content.generalUndoReport",
        R.string.amity_edit_msg to "social.chat.messageBox.editOption",
        R.string.amity_save to "social.general.content.generalSave",
        R.string.amity_msg_dltd to "social.chat.messageBox.messageDeleted",
        R.string.amity_unknown_msg to "social.chat.errors.sendErrorDesc",
        R.string.amity_msg_edited to "social.chat.messageBox.messageEdited",
        R.string.amity_failed_msg to "social.chat.errors.sendFailed",
        R.string.amity_camera to "social.general.content.generalCamera",
        R.string.amity_album to "social.general.content.generalAlbum",
        R.string.amity_file to "social.general.content.generalFile",
        R.string.amity_location to "social.general.content.generalLocation",
        R.string.amity_hold_to_record to "social.chat.messageBox.recordHint",
        R.string.amity_message_is_too_short to "social.post.create.messageErrorTitle",
        R.string.amity_playback_error to "social.livestreaming.content.unablePlayStreaming",
        R.string.amity_connected to "social.chat.settings.pageTitle",
        R.string.amity_no_internet to "social.general.content.errorMessage",
        R.string.amity_channel_creation_error to "social.chat.errors.chatCreateFailed",
        R.string.amity_channel_creation_loading to "social.general.content.statusMessagesLoading",
        R.string.amity_report_user to "social.general.content.generalReportUser",
        R.string.amity_un_report_user to "social.general.content.generalUnreportUser",
        R.string.amity_settings_chat to "social.chat.settings.pageTitle",
        R.string.amity_settings_chat_title to "social.chat.settings.leaveChat",
        R.string.amity_leave_chat to "social.chat.settings.leaveChat",
        R.string.amity_leave_chat_title to "social.chat.settings.leaveChatTitle",
        R.string.amity_leave_chat_des to "social.chat.settings.leaveChatMessage",

        )
} 