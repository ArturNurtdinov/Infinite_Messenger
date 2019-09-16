package com.infinitevoid.easymessenger.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.contracts.ChatLogContract
import com.infinitevoid.easymessenger.adapters.ChatFromItem
import com.infinitevoid.easymessenger.adapters.ChatToItem
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.data.ChatMessage
import com.infinitevoid.easymessenger.presenters.ChatLogPresenter
import com.infinitevoid.easymessenger.utils.Constants
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity(), ChatLogContract.View {
    private var chatPartner: User? = null
    private var latestMessage: ChatMessage? = null
    private val adapter = GroupAdapter<ViewHolder>()
    private val presenter = ChatLogPresenter(this)
    private var selectedImageUri: Uri? = null
    private var needToScroll: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        chatPartner = intent.getParcelableExtra(Constants.USER_KEY)
        latestMessage = intent.getParcelableExtra(Constants.MESSAGE_KEY)
        supportActionBar?.title = chatPartner?.username

        chat_log_recycler.adapter = adapter

        presenter.setListenerForMessages(chatPartner?.uid ?: return)

        send.setOnClickListener {
            if (((selectedImageUri?.toString() == "") || (selectedImageUri == null)) && (chat_log.text.toString() == "")) {
                Toast.makeText(
                    this,
                    getString(R.string.type_smth_or_choose_an_image_to_send),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val message = ChatMessage("", chat_log.text.toString(), "", "", -1, "", "")
                latestMessage = message
                presenter.sendMessage(
                    chat_log.text.toString(),
                    (chatPartner ?: return@setOnClickListener).uid,
                    selectedImageUri
                )
                loading_mark.visibility = View.VISIBLE
                chat_log.text.clear()
                choose_mark.visibility = View.GONE
                selectedImageUri = null
            }
        }

        choose_file.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        chat_log_recycler.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (chat_log.hasFocus()) {
                chat_log_recycler.postDelayed({
                    if (bottom < oldBottom) {
                        chat_log_recycler.scrollBy(0, oldBottom - bottom)
                    }
                }, 100)
            }
        }

        val layoutManager = chat_log_recycler.layoutManager as LinearLayoutManager
        needToScroll = true
        chat_log_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                needToScroll =
                    (layoutManager.childCount + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.itemCount

            }
        })
    }

    override fun showMessageFrom(message: ChatMessage) {
        adapter.add(ChatFromItem(message, chatPartner ?: return) { openFullscreen(it) })
        if (needToScroll ?: return) {
            chat_log_recycler.scrollToPosition(adapter.itemCount - 1)
        }
    }

    override fun showMessageTo(message: ChatMessage) {
        val currentUser = LatestMessagesActivity.currentUser ?: return
        adapter.add(ChatToItem(message, currentUser) { openFullscreen(it) })
        loading_mark.visibility = View.GONE
        chat_log_recycler.scrollToPosition(adapter.itemCount - 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            choose_mark.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun openFullscreen(url: String) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra("IMAGE_KEY", url)
        startActivity(intent)
    }
}
