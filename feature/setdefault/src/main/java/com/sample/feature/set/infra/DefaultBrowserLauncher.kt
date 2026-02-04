package com.sample.feature.set.infra

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.sample.feature.set.repository.DefaultBrowserConfig

class DefaultBrowserLauncher(
    private val activity: ComponentActivity,
    private val onFastReturn: () -> Unit
) {

    private var launchTime = 0L

    private val roleLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            handleReturn()
        }

    fun launch() {
        launchTime = SystemClock.elapsedRealtime()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val roleManager =
                    activity.getSystemService(RoleManager::class.java)

                if (roleManager.isRoleAvailable(RoleManager.ROLE_BROWSER) &&
                    !roleManager.isRoleHeld(RoleManager.ROLE_BROWSER)
                ) {
                    val intent =
                        roleManager.createRequestRoleIntent(
                            RoleManager.ROLE_BROWSER
                        )
                    roleLauncher.launch(intent)
                    return
                }
            }
        } catch (e: Exception) {
            // ignore
        }

        openSettings()
    }

    private fun handleReturn() {
        val duration =
            SystemClock.elapsedRealtime() - launchTime

        val isDefault =
            DefaultBrowserChecker.isDefaultBrowser(activity)

        if (duration < DefaultBrowserConfig.ROLE_RETURN_FAST_THRESHOLD &&
            !isDefault
        ) {
            onFastReturn()
        }
    }

    private fun openSettings() {
        activity.startActivity(
            Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
        )
    }
}
