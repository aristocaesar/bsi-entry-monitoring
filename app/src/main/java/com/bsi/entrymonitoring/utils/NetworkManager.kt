package com.bsi.entrymonitoring.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

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
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Network Error")
                .setMessage("No internet connection detected. Please check your network and try again.")
                .setCancelable(false)
                .setPositiveButton("Close App") { _, _ ->
                    (context as? android.app.Activity)?.finish()
                }

            val dialog = builder.create()
            dialog.show()
        }
    }
}