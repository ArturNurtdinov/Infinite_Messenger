package com.infinitevoid.easymessenger.views


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
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.contracts.LatestMessagesContract
import com.infinitevoid.easymessenger.data.ChatMessage
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.presenters.LatestMessagesPresenter
import com.infinitevoid.easymessenger.adapters.LatestMessagesItem
import com.infinitevoid.easymessenger.utils.Constants
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_messages_row.view.*

class LatestMessagesActivity : AppCompatActivity(), LatestMessagesContract.View {
    companion object {
        var currentUser: User? = null
    }

    private var notificationManager: NotificationManager? = null
    private var channel: NotificationChannel? = null
    private val presenter = LatestMessagesPresenter(this)
    private val adapter = GroupAdapter<ViewHolder>()
    private val latestMessagesMap = LinkedHashMap<String, ChatMessage>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra(Constants.USER_KEY, (item as LatestMessagesItem).userPartner)
            intent.putExtra(Constants.MESSAGE_KEY, item.chatMessage)
            view.read_mark.visibility = View.GONE
            if ((item.chatMessage.read == "false") && (item.chatMessage.fromId != currentUser?.uid)) {
                presenter.setMessageRead(item.chatMessage)
            }
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
            channel = NotificationChannel(
                Constants.CHANNEL_ID,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(channel ?: return)
        }
        presenter.verifyIsLogged()
    }

    override fun isLogged() {
        presenter.fetchCurrentUser()
        presenter.setListenerForLatest()
        if (latestMessagesMap.isEmpty()) {
            progress_bar.visibility = View.GONE
        }
    }

    override fun onLatestChanged(chatMessage: ChatMessage, key: String) {
        latestMessagesMap[key] = chatMessage
        adapter.clear()
        presenter.setPersonalNotificationListener(chatMessage)
        latestMessagesMap.toSortedMap(compareBy { -latestMessagesMap[it]?.timestamp!! })
            .values.forEach {
            adapter.add(LatestMessagesItem(it))
        }
    }

    override fun onLatestAdded(chatMessage: ChatMessage, key: String) {
        latestMessagesMap[key] = chatMessage
        adapter.clear()
        progress_bar.visibility = View.GONE
        presenter.setPersonalNotificationListener(chatMessage)
        latestMessagesMap.toSortedMap(compareBy { -latestMessagesMap[it]?.timestamp!! })
            .values.forEach {
            adapter.add(LatestMessagesItem(it))
        }
    }

    private var id = 0
    override fun setNotification(chatMessage: ChatMessage, user: User) {
        val intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_NO_CREATE)
        if ((chatMessage.read == "false") && (chatMessage.toId == currentUser?.uid)) {
            val notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(user.username)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setContentText(chatMessage.message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
            notificationManager?.notify(++id, notification)
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

            R.id.profile_settings -> {
                val intent = Intent(this, ProfileSettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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
