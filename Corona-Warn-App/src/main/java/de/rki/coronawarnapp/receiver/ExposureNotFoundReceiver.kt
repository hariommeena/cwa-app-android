package de.rki.coronawarnapp.receiver

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.nearby.exposurenotification.ExposureNotificationClient
import de.rki.coronawarnapp.exception.ExceptionCategory.INTERNAL
import de.rki.coronawarnapp.exception.NoTokenException
import de.rki.coronawarnapp.exception.WrongReceiverException
import de.rki.coronawarnapp.exception.reporting.report
import de.rki.coronawarnapp.nearby.ExposureStateUpdateWorker
import timber.log.Timber

class ExposureNotFoundReceiver : BroadcastReceiver() {
    companion object {
        private val TAG: String? = ExposureNotFoundReceiver::class.simpleName
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val expectedAction = ExposureNotificationClient.ACTION_EXPOSURE_NOT_FOUND
        try {
            if (expectedAction != action) {
                throw WrongReceiverException(
                    action,
                    expectedAction,
                    IllegalArgumentException("wrong action was received")
                )
            }

            AlertDialog.Builder(context)
                .setMessage("EXPOSURE_NOT_FOUND")
                .show()
            Timber.tag(TAG).e("EXPOSURE_NOT_FOUND")

        } catch (e: WrongReceiverException) {
            e.report(INTERNAL)
        } catch (e: NoTokenException) {
            e.report(INTERNAL)
        }
    }
}
