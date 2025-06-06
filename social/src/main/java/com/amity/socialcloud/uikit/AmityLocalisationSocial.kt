package com.amity.socialcloud.uikit

import android.content.Context
import androidx.annotation.StringRes
import com.amity.socialcloud.uikit.common.AmityLocalisationCommon
import com.amity.socialcloud.uikit.community.R
import com.pars.localisation.LocalizationManager

object AmityLocalisationSocial {
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
        return stringsMap[stringResId] ?.let { LocalizationManager.getString(it) } ?: applicationContext?.getString(stringResId) ?:"Unknown String"
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
        // Community Related
        R.string.amity_recommended_for_you to "social.community.recommended.pageTitle",
        R.string.amity_community_followers to "social.community.followRequestsPopup.followers",
        R.string.amity_todays_trending to "social.community.trending.pageTitle",
        R.string.amity_my_community to "social.community.myCommunities.title",
        R.string.amity_number_of_followers to "social.community.followRequestsPopup.followers",
        R.string.amity_posts to "social.general.unit.content.postPlural",
        R.string.amity_followers to "social.community.followRequestsPopup.followers",
        R.string.amity_mutual_friends to "social.community.basicInfo.memberCount",
        R.string.amity_following to "social.community.followRequestsPopup.following",
        R.string.amity_title_news_feed to "social.post.newsfeed.pageTitle",
        R.string.amity_title_explore to "social.community.socialTabBar.exploreTab",
        R.string.amity_community to "social.community.home.pageTitle",
        R.string.amity_create_community to "social.community.createForm.createTitle",
        R.string.amity_cc_hint to "social.community.createForm.nameHint",
        R.string.amity_about to "social.community.basicInfo.aboutField",
        R.string.amity_enter_description to "social.community.createForm.aboutHint",
        R.string.amity_tv_public to "social.community.createForm.publicTitle",
        R.string.amity_admin_description to "social.community.createForm.adminRuleDesc",
        R.string.amity_public_description to "social.community.createForm.publicDesc",
        R.string.amity_tv_private to "social.community.createForm.privateTitle",
        R.string.amity_private_description to "social.community.createForm.privateDesc",
        R.string.amity_only_admin to "social.community.settings.adminRuleTitle",
        R.string.amity_add_members to "social.community.createForm.addMemberTitle",
        R.string.amity_select_members to "social.community.memberSelect.selectTitle",
        R.string.amity_members to "social.community.basicInfo.memberCount",
        R.string.amity_members_capital to "social.community.basicInfo.memberCount",
        R.string.amity_members_count to "social.community.basicInfo.memberCount",
        
        // Post Related
        R.string.amity_feed_post_time to "social.general.time.justNow",
        R.string.amity_post_compose_hint to "social.post.create.textPlaceholder",
        R.string.amity_post to "social.general.content.generalPost",
        R.string.amity_post_caps to "social.general.content.generalPost",
        R.string.amity_new_post to "social.post.create.submitButton",
        R.string.amity_attachment_size_limit_exceed to "social.general.content.filSizeError",
        R.string.amity_attachment_count_limit_exceed to "social.general.content.maxFileNumberReached",
        R.string.amity_discard_post_title to "social.post.create.discardTitle",
        R.string.amity_discard_post_message to "social.post.create.discardMessage",
        R.string.amity_share to "social.general.content.generalShare",
        R.string.amity_comment to "social.general.content.generalComment",
        R.string.amity_like to "social.general.content.generalLike",
        R.string.amity_liked to "social.general.content.generalLiked",
        R.string.amity_image_count to "social.general.content.generalPhoto",
        R.string.amity_view_all_files to "social.general.content.generalFile",
        R.string.amity_delete_post_title to "social.post.detail.deletePostTitle",
        R.string.amity_delete_post_warning_message to "social.post.detail.deletePostMessage",
        R.string.amity_my_timeline to "social.post.timeline.pageTitle",
        R.string.amity_post_to to "social.post.detail.postToTitle",
        R.string.amity_post_as_community to "social.post.detail.postAsCommunityTitle",
        R.string.amity_post_as_community_message to "social.post.detail.postAsCommunityMessage",
        
        // Comments Related
        R.string.amity_post_comment_hint to "social.post.detail.commentHint",
        R.string.amity_reply to "social.general.content.generalReply",
        R.string.amity_view_replies to "social.post.detail.viewAllReplies",
        R.string.amity_view_more_replies to "social.post.detail.viewMoreReplies",
        R.string.amity_view_all_replies to "social.post.detail.viewAllReplies",
        R.string.amity_view_all_comments to "social.post.detail.viewAllReplies",
        R.string.amity_edit_reply to "social.post.detail.editReply",
        R.string.amity_delete_reply to "social.post.detail.deleteReply",
        R.string.amity_delete_reply_title to "social.post.detail.deleteReplyTitle",
        R.string.amity_delete_reply_warning_message to "social.post.detail.deleteReplyMessage",
        R.string.amity_add_reply_error_message to "social.general.content.errorMessage",
        R.string.amity_discard_reply_message to "social.post.detail.discardReplyMessage",
        R.string.amity_update_reply_error_message to "social.general.content.errorMessage",
        R.string.amity_reply_to to "social.post.detail.replyTo",
        R.string.amity_reply_deleted_message to "social.post.detail.deletedReplyMessage",
        R.string.amity_discard_reply_title to "social.post.detail.discardReplyTitle",
        
        // Settings Related
        R.string.amity_community_setting to "social.community.basicInfo.settingsTitle",
        R.string.amity_basic_info to "social.community.settings.basicInfoHeader",
        R.string.amity_community_permissions to "social.community.settings.permissionsHeader",
        R.string.amity_notification_settings to "social.community.settings.notificationsTitle",
        R.string.amity_notification to "social.community.notificationSettings.enableToggle",
        R.string.amity_notifications to "social.community.notificationSettings.pageTitle",
        R.string.amity_notification_on to "social.community.notificationSettings.enableToggle",
        R.string.amity_notification_off to "social.community.notificationSettings.enableToggle",
        R.string.amity_allow_notifications to "social.community.notificationSettings.enableToggle",
        R.string.amity_notifications_description to "social.community.notificationSettings.newPostDesc",
        
        // Notifications Related
        R.string.amity_new_posts to "social.community.notificationSettings.newPostTitle",
        R.string.amity_new_posts_description to "social.community.notificationSettings.newPostDesc",
        R.string.amity_reactions to "social.community.notificationSettings.postReactionTitle",
        R.string.amity_reacts_post to "social.community.notificationSettings.postReactionTitle",
        R.string.amity_reacts_post_description to "social.community.notificationSettings.postReactionDesc",
        R.string.amity_reacts_comments to "social.community.notificationSettings.commentReactionTitle",
        R.string.amity_reacts_comments_description to "social.community.notificationSettings.commentReactionDesc",
        R.string.amity_new_comments to "social.community.notificationSettings.newCommentTitle",
        R.string.amity_new_comments_description to "social.community.notificationSettings.newCommentDesc",
        R.string.amity_replies to "social.community.notificationSettings.replyTitle",
        R.string.amity_replies_description to "social.community.notificationSettings.replyDesc",
        
        // General UI/Actions
        R.string.amity_cancel to "social.general.content.generalCancel",
        R.string.amity_add to "social.general.content.generalAdd",
        R.string.amity_selected to "social.general.content.selected",
        R.string.amity_delete to "social.general.content.generalDelete",
        R.string.amity_leave to "social.general.content.generalLeave",
        R.string.amity_close to "social.general.content.generalClose",
        R.string.amity_done to "social.general.content.generalDone",
        R.string.amity_ok to "social.general.content.generalOk",
        R.string.amity_report to "social.general.content.generalReport",
        R.string.amity_report_user to "social.chat.settings.reportUser",
        R.string.amity_un_report_user to "social.chat.settings.unreportUser",
        R.string.amity_undo_report to "social.general.content.generalUndoReport",
        R.string.amity_manage to "social.community.settings.basicInfoHeader",
        R.string.amity_others to "social.general.content.others",
        R.string.amity_settings to "social.general.content.generalSettings",
        
        // Error Messages
        R.string.amity_error_name to "social.community.alert.unableToPerformActionTitle",
        R.string.amity_connectivity_issue to "social.community.alert.unableToPerformActionDescription",
        R.string.amity_add_comment_error_message to "social.general.content.errorMessage",
        R.string.amity_update_comment_error_message to "social.general.content.errorMessage",
        R.string.amity_settings_error to "social.general.content.errorMessage",
        R.string.amity_unable_to_save to "social.general.content.errorMessage",
        R.string.amity_follow_error to "social.general.content.errorMessage",
        R.string.amity_unfollow_error to "social.general.content.errorMessage",
        R.string.amity_request_error to "social.general.content.errorMessage",
        R.string.amity_unable_to_remove to "social.general.content.errorMessage",
        R.string.amity_refresh_error to "social.community.followRequestsPopup.cannotRefreshFeed",
        R.string.amity_error_loading_feed to "social.community.followRequestsPopup.cannotRefreshFeed",
        R.string.amity_something_went_wrong to "social.community.basicInfo.errorText",
        R.string.amity_something_went_wrong_pls_try to "social.community.basicInfo.errorText",
        
        // Success Messages
        R.string.amity_report_sent to "social.chat.settings.reportSent",
        R.string.amity_unreport_sent to "social.chat.settings.unreportUser",
        R.string.amity_report_has_been_sent to "social.general.content.statusMessagesSuccess",
        R.string.amity_community_success to "social.general.content.statusMessagesSuccess",
        
        // Video/Media Related
        R.string.amity_general_photos to "social.general.mediaButtons.photo",
        R.string.amity_general_videos to "social.general.mediaButtons.video",
        R.string.amity_video_stream_title to "social.livestreaming.content.addPostTitle",
        R.string.amity_video_stream_live to "social.livestreaming.content.goLive",
        R.string.amity_video_stream_recorded to "social.livestreaming.content.recordedStatus",
        R.string.amity_video_stream_ended_title to "social.livestreaming.content.streamEnded",
        R.string.amity_video_stream_ended_description to "social.livestreaming.content.playbackSoon",
        
        // Empty States
        R.string.amity_empty_feed to "social.post.newsfeed.emptyTitle",
        R.string.amity_empty_category_community to "social.community.emptyCommunity.description",
        R.string.amity_no_comments_found to "social.post.detail.noComments",
        R.string.amity_no_post to "social.post.newsfeed.noPostsTitle",
        R.string.amity_no_community to "social.community.emptyCommunity.title",
        R.string.amity_no_results to "social.community.basicInfo.noResults",
        R.string.amity_no_pending_posts to "social.post.pending.emptyTitle",
        
        // Additional Strings
        R.string.amity_edit_profile to "social.community.detail.editProfileButton",
        R.string.amity_message to "social.chat.messageBox.inputHint",
        R.string.amity_no_user_found to "social.community.basicInfo.noUserFound",
        R.string.amity_cancel_request to "social.general.content.generalCancel",
        R.string.amity_discard to "social.general.content.generalDiscard",
        R.string.amity_share_to_my_timeline to "social.post.detail.shareTimeline",
        R.string.amity_share_to_group to "social.post.detail.shareGroup",
        R.string.amity_more_options to "social.post.detail.moreOptions",
        R.string.amity_choose_existing to "social.general.content.generalAlbum",
        R.string.amity_take_photo to "social.general.content.generalCamera",
        R.string.amity_find_community to "social.community.action.find",
        R.string.amity_explore_community to "social.community.action.explore",
        
        // Additional Community Strings
        R.string.amity_community_name to "social.community.createForm.nameField",
        R.string.amity_category to "social.community.category.pageTitle",
        R.string.amity_category_required_field to "social.community.createForm.categoryField",
        R.string.amity_please_select_category to "social.community.createForm.categoryHint",
        R.string.amity_categories to "social.community.category.list",
        R.string.amity_cc_leave to "social.community.createForm.createTitle",
        R.string.amity_cc_dialog_msg to "social.community.createForm.alertDesc",
        R.string.amity_successfully_created_community to "social.community.create.successMessage",
        R.string.amity_moderator to "social.community.members.moderatorLabel",
        
        // Comment Related
        R.string.amity_delete_comment to "social.comment.delete.button",
        R.string.amity_edit_comment to "social.comment.edit.button",
        R.string.amity_delete_comment_title to "social.comment.delete.title",
        R.string.amity_delete_comment_warning_message to "social.comment.delete.message",
        R.string.amity_discard_comment_title to "social.comment.discard.title",
        R.string.amity_discard_comment_message to "social.comment.discard.message",
        R.string.amity_comment_deleted_message to "social.post.detail.deletedCommentMessage",
        R.string.amity_comments to "social.general.unit.content.commentSingular",
        R.string.amity_add_comment to "social.comment.create.button",
        R.string.amity_add_blocked_words_comment_error_message to "social.comment.create.blockedWordsError",
        R.string.amity_replying_to to "social.comment.reply.replyingTo",
        
        // Post Related
        R.string.amity_edited to "social.post.edit.indicator",
        R.string.amity_create_post_max_image_selected_warning to "social.post.create.maxImagesError",
        R.string.amity_timeline to "social.post.timeline.pageTitle",
        R.string.amity_no_post to "social.post.newsfeed.noPostsTitle",
        R.string.amity_select_category to "social.community.category.selectTitle",
        R.string.amity_start_post to "social.post.create.startPrompt",
        R.string.amity_see_all to "social.general.action.viewAll",
        R.string.amity_unrecognised_post_type to "social.post.error.unknownType",
        
        // Notifications
        R.string.amity_allow_notifications to "social.notification.settings.enable",
        R.string.amity_notifications_description to "social.notification.settings.description",
        
        // Upload Related
        R.string.amity_upload_incomplete to "social.upload.status.incomplete",
        R.string.amity_image_upload_failed_message to "social.upload.error.imageFailure",
        R.string.amity_attachment_upload_failed_message to "social.upload.error.attachmentFailure",
        R.string.amity_bullet to "social.general.symbol.bullet",
        R.string.amity_preparing to "social.general.status.preparing",
        R.string.amity_duplicate_files to "social.upload.error.duplicateFile",
        
        // Profile Related
        R.string.amity_upload_failed_profile_picture to "social.profile.avatar.uploadError",
        R.string.amity_edit_profile_update_failed to "social.profile.edit.updateError",
        
        // Mention Related
        R.string.amity_mention_error_title to "social.post.mention.errorTitle",
        R.string.amity_mention_error_msg to "social.post.mention.postErrorDesc",
        
        // Character Limits
        R.string.amity_post_characters_limit_error_title to "social.post.create.characterLimitTitle",
        R.string.amity_comment_characters_limit_error_title to "social.comment.create.characterLimitTitle",
        R.string.amity_characters_limit_error_msg to "social.general.error.characterLimit",
        R.string.amity_characters_limit_dynamic_error_msg to "social.general.error.characterLimitFormat",
        
        // Post Review
        R.string.amity_unable_turn_off_post_review_title to "social.community.settings.postReviewOffError",
        R.string.amity_unable_turn_on_post_review_title to "social.community.settings.postReviewOnError",
        R.string.amity_turn_off_post_review to "social.community.settings.postReviewOffTitle",
        R.string.amity_turn_off_post_review_msg to "social.community.settings.postReviewOffDesc",
        
        // Community Permissions
        R.string.amity_approve_member_post to "social.community.permission.postApproval",
        R.string.amity_approve_member_post_desc to "social.community.permission.postApprovalDesc",
        
        // Plurals
        R.string.amity_feed_number_of_likes_single to "social.general.unit.content.likeSingular",
        R.string.amity_feed_number_of_likes_plural to "social.general.unit.content.likePlural",
        R.string.amity_feed_number_of_comments_single to "social.general.unit.content.commentSingular",
        R.string.amity_feed_number_of_comments_plural to "social.general.unit.content.commentPlural",
        //R.string.amity_community_banner_number_of_pending_posts to "social.post.pending.pageTitle",
        
        // User Management
        R.string.amity_unfollow_user to "social.community.followRequestsPopup.declineButton",
        R.string.amity_unfollow to "social.general.content.generalUnreportUser",
        R.string.amity_share_profile to "social.general.content.generalShare",
        R.string.amity_communities to "social.community.basicInfo.communityList",
        R.string.amity_accounts to "social.community.basicInfo.accountList",
        R.string.amity_follow_requests to "social.community.followRequestsPopup.title",
        R.string.amity_private_account to "social.post.userFeed.privateTitle",
        R.string.amity_follow_user to "social.post.userFeed.privateSubtitle",
        R.string.amity_pending_requests to "social.community.followRequestsPopup.title",
        R.string.amity_requests_review to "social.community.followRequestsPopup.title",
        R.string.amity_accept to "social.community.followRequestsPopup.acceptButton",
        R.string.amity_decline to "social.community.followRequestsPopup.declineButton",
        R.string.amity_request_error to "social.community.followRequestsPopup.unavailableFollowRequest",
        R.string.amity_request_withdrawn to "social.community.followRequestsPopup.unavailableFollowRequest",
        R.string.amity_remove_user to "social.community.members.removeOption",
        R.string.amity_removed to "social.community.members.removeDesc",
        R.string.amity_unfollow_description to "social.community.followRequestsPopup.unavailableFollowRequest",
        
        // Feed Related
        R.string.amity_refresh_error to "social.community.followRequestsPopup.cannotRefreshFeed",
        R.string.amity_error_loading_feed to "social.community.followRequestsPopup.cannotRefreshFeed",
        
        // Pending Posts
        R.string.amity_pending_posts to "social.post.pending.pageTitle",
        R.string.amity_pending_posts_description to "social.post.pending.emptyTitle",
        R.string.amity_pending_posts_banner_member_message to "social.post.pending.memberDesc",
        R.string.amity_delete_pending_post_warning_message to "social.post.pending.deleteDesc",
        R.string.amity_create_post_pending_post_title_dialog to "social.post.pending.deleteTitle",
        R.string.amity_create_post_pending_post_message_dialog to "social.post.pending.deleteErrorDesc",
        R.string.amity_post_approve_error_dialog_title to "social.post.pending.notAvailable",
        
        // Reactions
        R.string.amity_title_no_reactions to "social.post.reaction.emptyTitle",
        R.string.amity_content_no_reactions to "social.post.reaction.emptySubtitle",
        
        // Community Settings
        R.string.amity_leave_community to "social.community.settings.leaveTitle",
        R.string.amity_close_community to "social.community.settings.closeTitle",
        R.string.amity_close_community_description to "social.community.settings.closeDesc",
        R.string.amity_moderator_leave_community_msg to "social.community.settings.leaveModeratorDesc",
        R.string.amity_last_moderator_leave_community_msg to "social.community.settings.leaveLastModeratorDesc",
        R.string.amity_close_community_msg to "social.community.settings.closeAlertDesc",
        R.string.amity_leave_community_msg to "social.community.settings.leaveAlertDesc",
        R.string.amity_leave_community_error_title to "social.community.settings.leaveErrorTitle",
        R.string.amity_close_community_error_title to "social.community.settings.closeErrorTitle",
        
        // Community Creation
        R.string.amity_community_name to "social.community.createForm.nameField",
        R.string.amity_cc_hint to "social.community.createForm.nameHint",
        R.string.amity_category to "social.community.category.pageTitle",
        R.string.amity_category_required_field to "social.community.createForm.categoryField",
        R.string.amity_please_select_category to "social.community.createForm.categoryHint",
        
        // Community Detail
        R.string.amity_join to "social.community.detail.joinButton",
        R.string.amity_join_community_message to "social.post.detail.joinMessage",
        
        // Livestreaming
        R.string.amity_video_stream_title to "social.livestreaming.content.addPostTitle",
        R.string.amity_video_stream_live to "social.livestreaming.content.goLive",
        R.string.amity_video_stream_recorded to "social.livestreaming.content.recordedStatus",
        R.string.amity_video_stream_ended_title to "social.livestreaming.content.streamEnded",
        R.string.amity_video_stream_ended_description to "social.livestreaming.content.playbackSoon",
        
        // Community Members
        R.string.amity_promote_moderator to "social.community.members.promoteToMod",
        R.string.amity_remove_moderator to "social.community.members.removeMod",
        R.string.amity_remove to "social.community.members.removeMember",
        R.string.amity_remove_user_msg to "social.community.members.removeDesc",

        //New
        R.string.amity_upload_image to "social.general.content.generalUploadImage",
        R.string.amity_community_name to "social.community.createForm.nameField",
        R.string.amity_cc_hint to "social.community.createForm.nameHint",
        R.string.amity_about to "social.community.createForm.aboutField",
        R.string.amity_enter_description to "social.community.createForm.aboutHint",
        R.string.amity_category_required_field to "social.community.createForm.categoryField",
        R.string.amity_please_select_category to "social.community.createForm.categoryHint",
        R.string.amity_only_admin to "social.community.createForm.postReview.title",
        R.string.amity_admin_description to "social.community.createForm.publicDesc",
        R.string.amity_tv_public to "social.community.createForm.publicTitle",
        R.string.amity_public_description to "social.community.createForm.publicDesc",
        R.string.amity_tv_private to "social.community.createForm.privateTitle",
        R.string.amity_private_description to "social.community.createForm.privateDesc",
        R.string.amity_add_members to "social.community.createForm.addMemberTitle",
        R.string.amity_create_community to "social.community.createForm.submitButton",
        R.string.amity_edit_profile to "social.community.settings.editProfileTitle",
        R.string.amity_message to "social.profile.message",
        R.string.amity_cancel_request to "social.profile.cancelRequest",
        R.string.amity_pending_requests to "social.profile.pendingRequests",
        R.string.amity_requests_review to "social.profile.requestsReview",
        R.string.amity_posts to "social.community.notificationSettings.postLabel",
        R.string.amity_followers to "social.community.followRequestsPopup.followers",
        R.string.amity_following to "social.profile.following" ,
        R.string.amity_categories to "social.community.category.pageTitle",
        R.string.amity_gallery_no_videos to "social.post.media.noVideos",
        R.string.amity_gallery_no_photos to "social.post.media.noPhotos",
        com.amity.socialcloud.uikit.common.R.string.amity_edit_post to "social.post.create.editButton",
        com.amity.socialcloud.uikit.common.R.string.amity_delete_post to "social.post.create.deleteButton",
        com.amity.socialcloud.uikit.common.R.string.amity_search to "social.general.content.generalSearch",
        com.amity.socialcloud.uikit.common.R.string.amity_following_count to "social.community.followRequestsPopup.following",
        R.string.amity_gallery_title to "social.general.content.generalGallery",
        R.string.amity_home to "social.community.socialTabBar.homeTab",
        R.string.amity_chatt to "social.community.socialTabBar.chatTab",
        R.string.amity_explore to "social.community.socialTabBar.exploreTab",
        R.string.amity_community to "social.community.socialTabBar.communityTab",
        R.string.amity_video_stream_unavailable_description to "social.livestreaming.content.unavailable"
    )
}