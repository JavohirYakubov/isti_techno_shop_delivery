package uz.ferganagroup.arzonibizdadelivery.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import uz.ferganagroup.arzonibizdadelivery.R
import uz.ferganagroup.arzonibizdadelivery.screen.main.MainActivity
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs


class AppFirebaseMessagingService : FirebaseMessagingService(){

    private var count = 0

    override fun onNewToken(token: String) {
        Log.d("tag-debug : " , token)
        Prefs.setFCM(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            Log.d("tag-debug : body " , remoteMessage?.notification?.body.toString())
            Log.d("tag-debug : title " , remoteMessage?.notification?.title.toString())
            Log.d("tag-debug : " , remoteMessage?.from.toString())
            Log.d("tag-debug : " , remoteMessage?.data.toString() ?:"")

            sendNotification(
                remoteMessage?.notification?.title ?:"",
                remoteMessage?.notification?.body ?:"",
                remoteMessage.data.get("type")?:""
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun sendNotification(
        title: String?,
        text: String?,
        type: String?
    ) {

        Log.d("tag-debug : " , "$title >>> $text >>> $type")

        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var intent = Intent(this, MainActivity::class.java)


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("type", type)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = "ArzoniBizda"
        val builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.logo))
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#3568D8"))
                .setSound(defaultSoundUri)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .setContentIntent(pendingIntent)

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "ArzoniBizda channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(count, builder.build())
        count++
    }


}