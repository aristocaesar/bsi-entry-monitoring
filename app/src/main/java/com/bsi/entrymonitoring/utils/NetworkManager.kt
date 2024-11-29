package com.bsi.entrymonitoring.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings

class NetworkManager {
    companion object {
        /**
         * Checks if the network is available.
         *
         * @param context The application context.
         * @return True if the network is available, otherwise false.
         */
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        /**
         * Show an AlertDialog with a message to the user.
         *
         * @param context The application context.
         */
        fun showNetworkErrorDialog(context: Context) {
            AlertDialogManager.showErrorDialog(
                context,
                "Network Error",
                "No internet connection detected. Please check your network and try again.",
                positiveButtonText = "Close App",
                negativeButtonText = "Go to Wi-Fi settings",
                onPositiveClick = {
                    (context as? android.app.Activity)?.finish()
                },
                onNegativeClick = {
                    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    context.startActivity(intent)
                }
            )
        }
    }
}