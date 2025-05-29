package com.amity.socialcloud.uikit.chat.apiRequest

import android.os.AsyncTask
import android.provider.Settings
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DeleteChannelTask(
    private val listener: DeleteChannelListener,
) : AsyncTask<String, Void, String>() {

    interface DeleteChannelListener {
        fun onDeleteChannelSuccess(result: String)
        fun onDeleteChannelError(error: String)
    }

    override fun doInBackground(vararg params: String?): String {
        val channelId = params[0]

        // Choose base URL based on the environment

        val urlString = "${com.amity.socialcloud.uikit.chat.BuildConfig.BASE_URL}api/v1/employee/deleteChannel"
        var result = ""

        try {
            val url = URL(urlString)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("device-id", Settings.Secure.ANDROID_ID)
            // Enable input/output streams for POST request
            connection.doOutput = true
            connection.doInput = true

            // Create JSON payload
            val jsonInputString = "{\"channelId\": \"$channelId\"}"

            // Write JSON payload to the connection output stream
            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.writeBytes(jsonInputString)
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            result = if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                reader.readText()
            } else {
                "Error: $responseCode"
            }
        } catch (e: Exception) {
            result = "Error: ${e.message}"
        }

        return result
    }

    override fun onPostExecute(result: String?) {
        if (result != null) {
            listener.onDeleteChannelSuccess(result)
        } else {
            listener.onDeleteChannelError("Unknown error occurred.")
        }
    }
}