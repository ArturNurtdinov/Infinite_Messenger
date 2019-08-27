package com.topaz.easymessenger.views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.topaz.easymessenger.R
import com.topaz.easymessenger.contracts.LatestMessagesContract
import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.presenters.LatestMessagesPresenter
import com.topaz.easymessenger.adapters.LatestMessagesItem
import com.topaz.easymessenger.utils.Constants
import com.topaz.easymessenger.views.NewMessageActivity.Companion.USER_KEY
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*

class LatestMessagesActivity : AppCompatActivity(), LatestMessagesContract.View {
    companion object {
        var currentUser: User? = null
    }

    private val presenter = LatestMessagesPresenter(this)
    private val adapter = GroupAdapter<ViewHolder>()
    private val latestMessagesMap = LinkedHashMap<String, ChatMessage>()
    private var notificationManager : NotificationManager? = null
    private var notificationID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra(USER_KEY, (item as LatestMessagesItem).userPartner)
            startActivity(intent)
        }
        latest_messages_recycler.adapter = adapter
        latest_messages_recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    Constants.CHANNEL_ID,
                    Constants.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        presenter.verifyIsLogged()
    }

    override fun onLatestChanged(chatMessage: ChatMessage, key: String) {
        latestMessagesMap[key] = chatMessage
        adapter.clear()
        latestMessagesMap.toSortedMap(compareBy { -latestMessagesMap[it]?.timestamp!! })
            .values.forEach {
            adapter.add(LatestMessagesItem(it))
        }
        presenter.setNotificationWithFetchingUser(chatMessage)
    }

    override fun onLatestAdded(chatMessage: ChatMessage, key: String) {
        latestMessagesMap[key] = chatMessage
        adapter.clear()
        latestMessagesMap.toSortedMap(compareBy { -latestMessagesMap[it]?.timestamp!! })
            .values.forEach {
            adapter.add(LatestMessagesItem(it))
        }
        if (chatMessage.timestamp == System.currentTimeMillis()) {
            presenter.setNotificationWithFetchingUser(chatMessage)
        }
    }

    override fun createNotification(chatMessage: ChatMessage, user: User) {
        if (chatMessage.fromId != currentUser?.uid) {
            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra(USER_KEY, user)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setContentTitle(user.username)
                .setWhen(System.currentTimeMillis())
                .setContentText(chatMessage.message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
            notificationManager?.notify(++notificationID, notification)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_sign_out -> {
                presenter.signOut()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun isLogged() {
        presenter.fetchCurrentUser()
        presenter.setListenerForLatest()
    }

    override fun initializeUser(user: User?) {
        currentUser = user
    }

    override fun isNotLogged() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onSignOut() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
