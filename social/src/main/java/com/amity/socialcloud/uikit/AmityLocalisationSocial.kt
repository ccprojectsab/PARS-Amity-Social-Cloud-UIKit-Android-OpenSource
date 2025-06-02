package com.amity.socialcloud.uikit

import android.content.Context
import androidx.annotation.StringRes
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
        // Community Related
        R.string.amity_recommended_for_you to "social.community.content.recommendedForYou",
        R.string.amity_community_followers to "social.community.content.communityFollowers",
        R.string.amity_todays_trending to "social.community.content.todaysTrending",
        R.string.amity_number_of_followers to "social.community.content.numberOfFollowers",
        R.string.amity_posts to "social.community.content.posts",
        R.string.amity_followers to "social.community.content.followers",
        R.string.amity_mutual_friends to "social.community.content.mutualFriends",
        R.string.amity_following to "social.community.content.following",
        R.string.amity_title_news_feed to "social.community.content.newsFeed",
        R.string.amity_title_explore to "social.community.content.explore",
        R.string.amity_community to "social.community.content.community",
        R.string.amity_create_community to "social.community.create.title",
        R.string.amity_cc_hint to "social.community.create.nameHint",
        R.string.amity_about to "social.community.content.about",
        R.string.amity_enter_description to "social.community.content.enterDescription",
        R.string.amity_tv_public to "social.community.settings.public",
        R.string.amity_admin_description to "social.community.settings.adminDescription",
        R.string.amity_public_description to "social.community.settings.publicDescription",
        R.string.amity_tv_private to "social.community.settings.private",
        R.string.amity_private_description to "social.community.settings.privateDescription",
        R.string.amity_only_admin to "social.community.settings.onlyAdmin",
        R.string.amity_add_members to "social.community.members.add",
        R.string.amity_select_members to "social.community.members.select",
        R.string.amity_members to "social.community.content.members",
        R.string.amity_members_capital to "social.community.content.membersCapital",
        R.string.amity_members_count to "social.community.content.membersCount",
        
        // Post Related
        R.string.amity_feed_post_time to "social.post.content.time",
        R.string.amity_post_compose_hint to "social.post.create.hint",
        R.string.amity_post to "social.post.create.post",
        R.string.amity_post_caps to "social.post.action.postCaps",
        R.string.amity_new_post to "social.post.create.newPost",
        R.string.amity_attachment_size_limit_exceed to "social.post.create.attachmentSizeLimitExceed",
        R.string.amity_attachment_count_limit_exceed to "social.post.create.attachmentCountLimitExceed",
        R.string.amity_discard_post_title to "social.post.create.discardTitle",
        R.string.amity_discard_post_message to "social.post.create.discardMessage",
        R.string.amity_share to "social.post.content.share",
        R.string.amity_comment to "social.post.content.comment",
        R.string.amity_like to "social.post.content.like",
        R.string.amity_liked to "social.post.content.liked",
        R.string.amity_image_count to "social.media.content.imageCount",
        R.string.amity_view_all_files to "social.media.action.viewAllFiles",
        R.string.amity_delete_post_title to "social.post.delete.title",
        R.string.amity_delete_post_warning_message to "social.post.delete.warning",
        R.string.amity_my_timeline to "social.post.content.myTimeline",
        R.string.amity_post_to to "social.post.create.postTo",
        R.string.amity_post_as_community to "social.post.create.postAsCommunity",
        R.string.amity_post_as_community_message to "social.post.create.postAsCommunityMessage",
        
        // Comments Related
        R.string.amity_post_comment_hint to "social.post.comment.hint",
        R.string.amity_reply to "social.post.comment.reply",
        R.string.amity_view_replies to "social.reply.action.viewReplies",
        R.string.amity_view_more_replies to "social.reply.action.viewMoreReplies",
        R.string.amity_view_all_replies to "social.reply.action.viewAllReplies",
        R.string.amity_view_all_comments to "social.post.comment.viewAllComments",
        R.string.amity_edit_reply to "social.post.comment.editReply",
        R.string.amity_delete_reply to "social.post.comment.deleteReply",
        R.string.amity_delete_reply_title to "social.post.comment.deleteReplyTitle",
        R.string.amity_delete_reply_warning_message to "social.post.comment.deleteReplyWarning",
        R.string.amity_add_reply_error_message to "social.post.comment.addReplyError",
        R.string.amity_discard_reply_message to "social.post.comment.discardReplyMessage",
        R.string.amity_update_reply_error_message to "social.reply.error.update",
        R.string.amity_reply_to to "social.post.comment.replyTo",
        R.string.amity_reply_deleted_message to "social.reply.content.deleted",
        R.string.amity_discard_reply_title to "social.reply.discard.title",
        
        // Settings Related
        R.string.amity_community_setting to "social.community.settings.title",
        R.string.amity_basic_info to "social.community.settings.basicInfo",
        R.string.amity_community_permissions to "social.community.settings.permissions",
        R.string.amity_notification_settings to "social.community.settings.notifications",
        R.string.amity_notification to "social.community.settings.notification",
        R.string.amity_notifications to "social.community.settings.notifications",
        R.string.amity_notification_on to "social.notification.statusOn",
        R.string.amity_notification_off to "social.notification.statusOff",
        R.string.amity_allow_notifications to "social.community.settings.allowNotifications",
        R.string.amity_notifications_description to "social.notification.description",
        
        // Notifications Related
        R.string.amity_new_posts to "social.notification.newPosts",
        R.string.amity_new_posts_description to "social.notification.newPostsDescription",
        R.string.amity_reactions to "social.notification.reactions",
        R.string.amity_reacts_post to "social.notification.reactsPost",
        R.string.amity_reacts_post_description to "social.notification.reactsPostDescription",
        R.string.amity_reacts_comments to "social.notification.reactsComments",
        R.string.amity_reacts_comments_description to "social.notification.reactsCommentsDescription",
        R.string.amity_new_comments to "social.notification.newComments",
        R.string.amity_new_comments_description to "social.notification.newCommentsDescription",
        R.string.amity_replies to "social.notification.replies",
        R.string.amity_replies_description to "social.notification.repliesDescription",
        
        // Poll Related
        R.string.amity_poll_question to "social.poll.create.question",
        R.string.amity_poll_question_hint to "social.poll.create.questionHint",
        R.string.amity_poll_question_error to "social.poll.create.questionError",
        R.string.amity_poll_answer to "social.poll.create.answer",
        R.string.amity_poll_answer_hint to "social.poll.create.answerHint",
        R.string.amity_poll_add_answer to "social.poll.create.addAnswer",
        R.string.amity_poll_answer_error_empty to "social.poll.create.answerErrorEmpty",
        R.string.amity_poll_answer_error_exceed_limit to "social.poll.create.answerErrorExceedLimit",
        R.string.amity_poll_multiple_answers to "social.poll.create.multipleAnswers",
        R.string.amity_poll_multiple_answers_hint to "social.poll.create.multipleAnswersHint",
        R.string.amity_poll_closed_in to "social.poll.create.closedIn",
        R.string.amity_poll_closed_in_error to "social.poll.create.closedInError",
        R.string.amity_poll_closed_in_hint to "social.poll.create.closedInHint",
        R.string.amity_poll_closed_in_dialog to "social.poll.create.closedInDialog",
        R.string.amity_poll_status_closed to "social.poll.content.statusClosed",
        R.string.amity_poll_submit_vote to "social.poll.content.submitVote",
        R.string.amity_poll_expand_voted to "social.poll.content.expandVoted",
        R.string.amity_delete_poll_title to "social.poll.delete.title",
        R.string.amity_delete_poll_message to "social.poll.delete.message",
        R.string.amity_close_poll_title to "social.poll.close.title",
        R.string.amity_close_poll_message to "social.poll.close.message",
        R.string.amity_delete_poll to "social.poll.delete.action",
        R.string.amity_close_poll to "social.poll.close.action",
        
        // General UI/Actions
        R.string.amity_cancel to "social.general.action.cancel",
        R.string.amity_add to "social.general.action.add",
        R.string.amity_selected to "social.general.content.selected",
        R.string.amity_delete to "social.general.action.delete",
        R.string.amity_leave to "social.general.action.leave",
        R.string.amity_close to "social.general.action.close",
       /* R.string.amity_edit to "social.general.action.edit",
        R.string.amity_save to "social.general.action.save",*/
        R.string.amity_done to "social.general.action.done",
        R.string.amity_ok to "social.general.action.ok",
        R.string.amity_report to "social.general.action.report",
        R.string.amity_report_user to "social.general.action.reportUser",
        R.string.amity_un_report_user to "social.general.action.unreportUser",
        R.string.amity_undo_report to "social.general.action.undoReport",
        R.string.amity_manage to "social.general.action.manage",
        R.string.amity_others to "social.general.content.others",
        R.string.amity_settings to "social.general.content.settings",
        
        // Error Messages
        R.string.amity_error_name to "social.error.nameTaken",
        R.string.amity_connectivity_issue to "social.error.connectivityIssue",
        R.string.amity_add_comment_error_message to "social.error.addCommentError",
        R.string.amity_update_comment_error_message to "social.error.updateCommentError",
        R.string.amity_settings_error to "social.error.settingsError",
        R.string.amity_unable_to_save to "social.error.unableToSave",
        R.string.amity_follow_error to "social.error.followError",
        R.string.amity_unfollow_error to "social.error.unfollowError",
        R.string.amity_request_error to "social.error.requestError",
        R.string.amity_unable_to_remove to "social.error.unableToRemove",
        R.string.amity_refresh_error to "social.error.refreshError",
        R.string.amity_error_loading_feed to "social.error.loadingFeed",
        
        // Success Messages
        R.string.amity_report_sent to "social.success.reportSent",
        R.string.amity_unreport_sent to "social.success.unreportSent",
        R.string.amity_report_has_been_sent to "social.success.reportHasBeenSent",
        R.string.amity_community_success to "social.success.communityCreated",
        
        // Video/Media Related
        R.string.amity_general_photos to "social.media.photos",
        R.string.amity_general_videos to "social.media.videos",
        R.string.amity_video_stream_title to "social.media.streamTitle",
        R.string.amity_video_stream_live to "social.media.streamLive",
        R.string.amity_video_stream_recorded to "social.media.streamRecorded",
        R.string.amity_video_stream_ended_title to "social.media.streamEndedTitle",
        R.string.amity_video_stream_ended_description to "social.media.streamEndedDescription",
        
        // Empty States
        R.string.amity_empty_feed to "social.empty.feed",
        R.string.amity_empty_category_community to "social.empty.categoryCommunity",
        R.string.amity_no_comments_found to "social.empty.noComments",
        R.string.amity_no_post to "social.empty.noPosts",
        R.string.amity_no_community to "social.empty.noCommunity",
        R.string.amity_no_results to "social.empty.noResults",
        R.string.amity_no_pending_posts to "social.empty.noPendingPosts",
        
        // Additional Strings
      /*  R.string.amity_general_search to "social.general.action.search",
        R.string.amity_read_more to "social.general.action.readMore",
        R.string.amity_gallery to "social.media.gallery",
        R.string.amity_search_results to "social.general.content.searchResults",
        R.string.amity_choose_image to "social.media.chooseImage",
        R.string.amity_saved to "social.general.content.saved",
        R.string.amity_delete_msg to "social.general.content.deleteMessage",
        R.string.amity_dlt_dlg_body to "social.general.content.deleteMessageBody",
        R.string.amity_failed_dlg_body to "social.error.messageNotSent",
        R.string.amity_day to "social.general.time.day",
        R.string.amity_hour to "social.general.time.hour",
        R.string.amity_min to "social.general.time.minute",
        R.string.amity_just_now to "social.general.time.justNow",
        R.string.amity_save_caps to "social.general.action.saveCaps",
        R.string.amity_anonymous to "social.general.content.anonymous",*/
        R.string.amity_edit_profile to "social.profile.action.edit",
        R.string.amity_message to "social.general.content.message",
       /* R.string.amity_display_name to "social.profile.content.displayName",
        R.string.amity_report_msg to "social.general.content.reportMessage",
        R.string.amity_max_image_selected to "social.media.error.maxImageLimit",
        R.string.amity_unable_to_delete to "social.error.unableToDeleteFile",
        R.string.amity_try_again to "social.general.action.tryAgain",
        R.string.amity_image_preview_title to "social.media.content.imagePreviewTitle",
        R.string.amity_release_to_send to "social.general.action.releaseToSend",
        R.string.amity_time to "social.general.time.format",
        R.string.amity_remove_from_community to "social.community.action.removeMember",
        R.string.amity_no_permission_title to "social.error.noPermissionTitle",
        R.string.amity_no_permission_message to "social.error.noPermissionMessage",*/
        R.string.amity_downloading_file to "social.media.content.downloadingFile",
       /* R.string.amity_download_complete to "social.media.content.downloadComplete",
        R.string.amity_following_count to "social.profile.content.followingCount",
        R.string.amity_remove_follower to "social.profile.action.removeFollower",
        R.string.amity_remove_follower_msg to "social.profile.content.removeFollowerMessage",*/
        R.string.amity_no_user_found to "social.error.noUserFound",
       // R.string.amity_yesterday to "social.general.time.yesterday",
        R.string.amity_cancel_request to "social.general.action.cancelRequest",
        R.string.amity_hello_blank_fragment to "social.general.content.blankFragment",
        R.string.amity_discard to "social.general.action.discard",
        R.string.amity_share_to_my_timeline to "social.post.action.shareToTimeline",
        R.string.amity_share_to_group to "social.post.action.shareToGroup",
        R.string.amity_more_options to "social.post.action.moreOptions",
        R.string.amity_choose_existing to "social.media.action.chooseExisting",
        R.string.amity_take_photo to "social.media.action.takePhoto",
        R.string.amity_find_community to "social.community.action.find",
        R.string.amity_explore_community to "social.community.action.explore",
        
        // Additional Community Strings
        R.string.amity_community_name to "social.community.content.name",
        R.string.amity_category to "social.community.category.title",
        R.string.amity_category_required_field to "social.community.category.required",
        R.string.amity_please_select_category to "social.community.category.selectPrompt",
        R.string.amity_categories to "social.community.category.list",
        R.string.amity_cc_leave to "social.community.create.leavePrompt",
        R.string.amity_cc_dialog_msg to "social.community.create.leaveWarning",
        R.string.amity_successfully_created_community to "social.community.create.success",
        R.string.amity_moderator to "social.community.role.moderator",
        
        // Comment Related
        R.string.amity_delete_comment to "social.comment.action.delete",
        R.string.amity_edit_comment to "social.comment.action.edit",
        R.string.amity_delete_comment_title to "social.comment.delete.title",
        R.string.amity_delete_comment_warning_message to "social.comment.delete.warning",
        R.string.amity_discard_comment_title to "social.comment.discard.title",
        R.string.amity_discard_comment_message to "social.comment.discard.warning",
        
        // Post Related
        R.string.amity_edited to "social.post.content.edited",
        R.string.amity_create_post_max_image_selected_warning to "social.post.media.maxSelectionWarning",
        
        // Allow Notifications
        R.string.amity_allow_notifications to "social.notification.allow",
        R.string.amity_notifications_description to "social.notification.description",
        
        // Additional Post Related
        R.string.amity_post_review to "social.post.review.title",
        R.string.amity_join to "social.community.action.join",
        R.string.amity_join_community_message to "social.community.content.joinMessage",
        R.string.amity_timeline to "social.general.content.timeline",
        R.string.amity_no_post to "social.post.content.noPosts",
        R.string.amity_comment_deleted_message to "social.comment.content.deleted",
        R.string.amity_select_category to "social.community.category.select",
        R.string.amity_start_post to "social.post.action.startFirst",
        R.string.amity_my_community to "social.community.content.myCommunity",
        R.string.amity_see_all to "social.general.action.seeAll",
        
        // Upload Related
        R.string.amity_upload_incomplete to "social.upload.status.incomplete",
        R.string.amity_image_upload_failed_message to "social.upload.error.imagesFailed",
        R.string.amity_attachment_upload_failed_message to "social.upload.error.attachmentsFailed",
        R.string.amity_bullet to "social.general.symbol.bullet",
        R.string.amity_members_count to "social.community.content.membersCount",
        R.string.amity_preparing to "social.general.status.preparing",
        
        // Community Settings
        R.string.amity_close_community to "social.community.action.close",
        R.string.amity_close_community_description to "social.community.content.closeDescription",
        R.string.amity_leave_community to "social.community.action.leave",
        R.string.amity_moderator_leave_community_msg to "social.community.content.moderatorLeaveMessage",
        R.string.amity_moderator_leave_community_error_msg to "social.community.error.moderatorLeave",
        R.string.amity_last_moderator_leave_community_msg to "social.community.content.lastModeratorLeaveMessage",
        R.string.amity_close_community_msg to "social.community.content.closeMessage",
        R.string.amity_leave_community_msg to "social.community.content.leaveMessage",
        R.string.amity_leave_community_error_title to "social.community.error.leaveTitle",
        R.string.amity_close_community_error_title to "social.community.error.closeTitle",
        
        // Mention Related
        R.string.amity_mention_error_title to "social.mention.error.title",
        R.string.amity_mention_error_msg to "social.mention.error.message",
        
        // Character Limits
        R.string.amity_post_characters_limit_error_title to "social.post.error.characterLimitTitle",
        R.string.amity_comment_characters_limit_error_title to "social.comment.error.characterLimitTitle",
        R.string.amity_characters_limit_error_msg to "social.general.error.characterLimit",
        R.string.amity_characters_limit_dynamic_error_msg to "social.general.error.characterLimitDynamic",
        
        // Post Review
        R.string.amity_unable_turn_off_post_review_title to "social.post.review.error.turnOffTitle",
        R.string.amity_unable_turn_on_post_review_title to "social.post.review.error.turnOnTitle",
        R.string.amity_something_went_wrong_pls_try to "social.general.error.tryAgain",
        R.string.amity_turn_off_post_review to "social.post.review.action.turnOff",
        R.string.amity_turn_off_post_review_msg to "social.post.review.content.turnOffMessage",
        R.string.amity_turn_off to "social.general.action.turnOff",
        
        // Moderator Related
        R.string.amity_moderators to "social.community.role.moderators",
        R.string.amity_moderator_msg to "social.community.error.moderatorPermission",
        
        // File Related
        R.string.amity_file_max_limit_exceed_title to "social.file.error.maxLimitTitle",
        R.string.amity_file_max_limit_exceed_message to "social.file.error.maxLimitMessage",
        R.string.amity_duplicate_files to "social.file.error.duplicate",
        
        // Profile Related
        R.string.amity_upload_failed_profile_picture to "social.profile.error.uploadPicture",
        R.string.amity_edit_profile_update_failed to "social.profile.error.update",
        
        // User Actions
        R.string.amity_promote_moderator to "social.community.action.promoteModerator",
        R.string.amity_remove_moderator to "social.community.action.removeModerator",
        R.string.amity_remove to "social.general.action.remove",
        R.string.amity_remove_user_msg to "social.community.content.removeUserMessage",
        
        // Post Content
        R.string.amity_unrecognised_post_type to "social.post.error.unrecognizedType",
        R.string.amity_something_went_wrong to "social.general.error.generic",
        R.string.amity_replying_to to "social.comment.content.replyingTo",
        
        // Community Permissions
        R.string.amity_approve_member_post to "social.community.permission.approvePosts",
        R.string.amity_approve_member_post_desc to "social.community.permission.approvePostsDescription",
        
        // Comments
        R.string.amity_comments to "social.comment.content.comments",
        R.string.amity_add_comment to "social.comment.action.add",
        R.string.amity_general to "social.general.content.general",
        R.string.amity_add_blocked_words_comment_error_message to "social.comment.error.blockedWords",
        
        // Plurals
        R.plurals.amity_feed_number_of_likes to "social.post.content.numberOfLikes",
        R.plurals.amity_feed_number_of_comments to "social.post.content.numberOfComments",
        R.plurals.amity_community_banner_number_of_pending_posts to "social.community.content.pendingPostsCount",
        
        // User Management
        R.string.amity_unfollow_user to "social.user.action.unfollow",
        R.string.amity_unfollow to "social.user.action.unfollowSimple",
        R.string.amity_share_profile to "social.user.action.shareProfile",
        R.string.amity_communities to "social.community.content.communities",
        R.string.amity_accounts to "social.user.content.accounts",
        R.string.amity_follow_requests to "social.user.content.followRequests",
        R.string.amity_private_account to "social.user.content.privateAccount",
        R.string.amity_follow_user to "social.user.action.followToSee",
        R.string.amity_pending_requests to "social.user.content.pendingRequests",
        R.string.amity_requests_review to "social.user.content.requestsReview",
        R.string.amity_accept to "social.general.action.accept",
        R.string.amity_decline to "social.general.action.decline",
        R.string.amity_request_error to "social.user.error.requestNotAvailable",
        R.string.amity_request_withdrawn to "social.user.content.requestWithdrawn",
        R.string.amity_remove_user to "social.user.action.remove",
        R.string.amity_removed to "social.general.status.removed",
        R.string.amity_unfollow_description to "social.user.content.unfollowDescription",
        
        // Feed Related
        R.string.amity_refresh_error to "social.feed.error.refresh",
        R.string.amity_error_loading_feed to "social.feed.error.loading",
        
        // Pending Posts
        R.string.amity_pending_posts to "social.post.review.pendingPosts",
        R.string.amity_pending_posts_description to "social.post.review.pendingDescription",
        R.string.amity_pending_posts_banner_member_message to "social.post.review.pendingBannerMessage",
        R.string.amity_delete_pending_post_warning_message to "social.post.review.deleteWarning",
        R.string.amity_create_post_pending_post_title_dialog to "social.post.review.submitTitle",
        R.string.amity_create_post_pending_post_message_dialog to "social.post.review.submitMessage",
        R.string.amity_post_approve_error_dialog_title to "social.post.review.error.notAvailable",
        
        // Reactions
        R.string.amity_title_no_reactions to "social.reaction.error.title",
        R.string.amity_content_no_reactions to "social.reaction.error.content"
    )
}