package com.amity.socialcloud.uikit.chat.settings

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.apiRequest.DeleteChannelTask
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.common.AmityLocalisation
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.google.android.material.divider.MaterialDivider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale

class AmityChatSettingsActivity : AppCompatActivity() {

    private lateinit var channelId: String
    private lateinit var userId: String

    private lateinit var tvReport: TextView
    private lateinit var tvReportDivider: MaterialDivider

    companion object {
        private const val INTENT_CHANNEL_ID = "channelID"

        private const val INTENT_USER_ID = "userID"
        fun newIntent(context: Context, channelId: String, userId: String): Intent {
            return Intent(context, AmityChatSettingsActivity::class.java).apply {
                putExtra(INTENT_CHANNEL_ID, channelId)
                putExtra(INTENT_USER_ID, userId)

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amity_chat_settings)
        tvReport = findViewById(R.id.tvReport)
        tvReportDivider = findViewById(R.id.tvDivider1)
        channelId = intent.getStringExtra(INTENT_CHANNEL_ID) ?: ""
        userId = intent.getStringExtra(INTENT_USER_ID) ?: ""
        initToolbar()
        setResult(RESULT_CANCELED)
        Log.d("Mytag", "user id is - $userId")
        if (userId.isNotBlank()) {
            val user = try {
                getUser(userId).blockingFirst()
            } catch (e: Exception) {
                Log.e("MYTag", "onCreate: ${e.message}", e)
                null
            }
            if (user != null) {
                tvReport.visibility = View.VISIBLE
                tvReportDivider.visibility = View.VISIBLE
                setReportUserText(user)
                val leaveChannel = findViewById<TextView>(R.id.tvLeave)
                leaveChannel.setOnClickListener {
                    leaveChannelDialog()
                }
                tvReport.setOnClickListener {
                    Log.d("MyTag", "onDeleteChannelError: buttonPressed ")
                    reportUserPermanent(user)
                }
            }
        } else {
            tvReport.visibility = View.GONE
            tvReportDivider.visibility = View.GONE
        }

    }

    private fun getUser(userId: String): Flowable<AmityUser> {
        val userRepository = AmityCoreClient.newUserRepository()
        return userRepository.getUser(userId)
    }

    private fun initToolbar() {
        val backButton = findViewById<ImageView>(R.id.ivBack)
        val title = findViewById<TextView>(R.id.tvName)
        title.text = AmityLocalisation.getString(R.string.amity_settings_chat)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun leaveChannel() {
        /* val btn = findViewById<TextView>(R.id.tvLeave)
         btn.setOnClickListener {*/
        val deleteChannelTask = DeleteChannelTask(
            object : DeleteChannelTask.DeleteChannelListener {

                override fun onDeleteChannelSuccess(result: String) {
                    Log.d("MyTag", "onDeleteChannelError: $result ")
                    setResult(RESULT_OK)
                    finish()

                }

                override fun onDeleteChannelError(error: String) {
                    Log.d("MyTag", "onDeleteChannelError: $error ")
                }
            },
        )

        deleteChannelTask.execute(channelId)

        //   }
    }

    private fun leaveChannelDialog() {
        AmityAlertDialogUtil.showDialog(this,
            "${AmityLocalisation.getString(R.string.amity_leave_chat)}?",
            AmityLocalisation.getString(R.string.amity_leave_chat_des),
            AmityLocalisation.getString(R.string.amity_leave_chat_title),
            AmityLocalisation.getString(R.string.amity_cancel_unfollow).uppercase(),
            DialogInterface.OnClickListener { dialog, which ->
                AmityAlertDialogUtil.checkConfirmDialog(
                    isPositive = which,
                    confirmed = {
                        leaveChannel()
                    },
                    cancel = { dialog.cancel() })
            })
    }

    private fun reportUserPermanent(user: AmityUser) {
        if (user.isFlaggedByMe()) {
            unReportUser(user)
                .doOnComplete {
                    tvReport.text = AmityLocalisation.getString(R.string.amity_report_user)
                }.subscribe()
        } else {
            reportUser(user)
                .doOnComplete {
                    tvReport.text = AmityLocalisation.getString(R.string.amity_un_report_user)
                }
                .subscribe()
        }
    }

    private fun setReportUserText(user: AmityUser) {
        if (user.isFlaggedByMe()) {
            tvReport.setText(R.string.amity_un_report_user)
        } else {
            tvReport.setText(R.string.amity_report_user)
        }
    }

    fun reportUser(user: AmityUser): Completable {
        return user.report().flag()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun unReportUser(user: AmityUser): Completable {
        return user.report().unflag()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}