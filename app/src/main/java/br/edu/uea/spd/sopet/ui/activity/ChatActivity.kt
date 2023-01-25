package br.edu.uea.spd.sopet.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.adapter.ChatAdapter
import br.edu.uea.spd.sopet.data.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    private val TAG: String? = ChatActivity::class.java.canonicalName
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var ivProfile: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserStatus: TextView
    private lateinit var etMessage: EditText
    private lateinit var ibtnSend: ImageButton

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var hisUid: String
    private lateinit var hisImage: String
    private lateinit var myUid: String

    // Message
    private lateinit var seenListener: ValueEventListener
    private lateinit var userRefForSeen: DatabaseReference

    private lateinit var chatList: ArrayList<Chat>
    private lateinit var adapterChat: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        title = ""

        // View elements
        toolbar = findViewById(R.id.toolbar_chat)
        recyclerView = findViewById(R.id.rc_chat)
        ivProfile = findViewById(R.id.iv_profile)
        tvUserName = findViewById(R.id.tv_user_name)
        tvUserStatus = findViewById(R.id.tv_user_status)
        etMessage = findViewById(R.id.et_message)
        ibtnSend = findViewById(R.id.ibtn_send)

        // firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Users")

        // Users
        hisUid = intent.getStringExtra("userID").toString()
        myUid = firebaseAuth.currentUser!!.uid

        // RecycleView
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager

        loadHisData()

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


        ibtnSend.setOnClickListener {
            val message = etMessage.text.toString()
            if (TextUtils.isEmpty(message)) {
                Toast.makeText(this, "Cannot send the empty message...", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(message)
            }
        }

        // Check edit text change listener
        etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty() == true) {
                    checkTypingStatus("noOne")
                } else {
                    checkTypingStatus(hisUid)
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        readMessages()

        seenMessage()

    }

    override fun onStart() {
        // Set online
        checkOnlineStatus("Online")
        super.onStart()
    }

    override fun onResume() {
        // Set online
        checkOnlineStatus("Online")
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        // Get timestamp
        val timesTamp = System.currentTimeMillis().toString()

        // Set offline with last seen timestamp
        checkOnlineStatus(timesTamp)

        checkTypingStatus("noOne")
        userRefForSeen.removeEventListener(seenListener)
    }

    private fun loadHisData() {
        val query = databaseReference.orderByChild("uid").equalTo(hisUid)
        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {

                    val onlineStatus = ds.child("status").value.toString()
                    val typeStatus = ds.child("type").value.toString()
                    hisImage = ds.child("image").value.toString()
                    tvUserName.text = ds.child("name").value.toString()

                    if (typeStatus == myUid) {
                        tvUserStatus.text = "Typing..."
                    } else {
                        if (onlineStatus == "Online") {
                            tvUserStatus.text = onlineStatus
                        } else {
                            val cal = Calendar.getInstance(Locale.ENGLISH)
                            cal.timeInMillis = (onlineStatus.toLongOrNull() ?: 0)
                            val datetime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString()
                            tvUserStatus.text = "Last seen at: $datetime"
                        }
                    }

                    try {
                        if (hisImage.isNotEmpty()) {
                            Picasso.get().load(hisImage)
                                .placeholder(R.drawable.ic_default_img_while).into(ivProfile)
                        }
                    } catch (e: Exception) {
                        Picasso.get().load(R.drawable.ic_default_img_while).into(ivProfile)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats")
        seenListener = userRefForSeen.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val chat = ds.getValue(Chat::class.java)
                    chat?.let {
                        if (chat.receiver.equals(myUid) && chat.sender.equals(hisUid)) {
                            val hasSeenHashMap = HashMap<String, Any>()
                            hasSeenHashMap["isSeen"] = true
                            ds.ref.updateChildren(hasSeenHashMap)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun readMessages() {
        chatList = arrayListOf()
        val dbRef = FirebaseDatabase.getInstance().getReference("Chats")
        dbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (ds in snapshot.children) {
                    val chat = ds.getValue(Chat::class.java)
                    chat?.let {
                        if (chat.receiver.equals(myUid) && chat.sender.equals(hisUid) ||
                            chat.receiver.equals(hisUid) && chat.sender.equals(myUid)
                        ) {
                            chat.let { chatList.add(it) }
                        }
                        adapterChat = ChatAdapter(chatList, hisImage)
                        recyclerView.adapter = adapterChat
                        adapterChat.notifyDataSetChanged()
                    }
                }

                Log.d(TAG, "The data is changed and now this listener was called")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun sendMessage(message: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference
        val hashMap = HashMap<String, Any>()
        val timeStamp = System.currentTimeMillis().toString()
        hashMap["sender"] = myUid
        hashMap["receiver"] = hisUid
        hashMap["message"] = message
        hashMap["timestamp"] = timeStamp
        hashMap["isSeen"] = false
        databaseReference.child("Chats").push().setValue(hashMap)
        etMessage.setText("")
    }

    private fun checkOnlineStatus(status: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Users").child(myUid)
        val hasMap = HashMap<String, Any>()
        hasMap["status"] = status
        // Update value of online status of current user
        dbRef.updateChildren(hasMap)
    }

    private fun checkTypingStatus(typing: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Users").child(myUid)
        val hasMap = HashMap<String, Any>()
        hasMap["type"] = typing
        // Update value of online status of current user
        dbRef.updateChildren(hasMap)
    }
}