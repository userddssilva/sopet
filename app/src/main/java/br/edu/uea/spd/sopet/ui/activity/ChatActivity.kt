package br.edu.uea.spd.sopet.ui.activity

import android.os.Bundle
import android.text.TextUtils
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

class ChatActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var ivProfile: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserStatus: TextView
    private lateinit var etMessage: EditText
    private lateinit var ibtnSend: ImageButton

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userUidReceiver: String
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var hisUid: DatabaseReference

    private lateinit var hisImage: String
    private lateinit var myUid: String

    // Message
    private lateinit var seenListener: ValueEventListener
    private lateinit var userRefForSeen: DatabaseReference

    private lateinit var chatList: ArrayList<Chat>
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar = findViewById(R.id.toolbar_chat)
//        setSupportActionBar(toolbar)
        title = ""
        recyclerView = findViewById(R.id.rc_chat)
        ivProfile = findViewById(R.id.iv_profile)
        tvUserName = findViewById(R.id.tv_user_name)
        tvUserStatus = findViewById(R.id.iv_user_status)
        etMessage = findViewById(R.id.et_message)
        ibtnSend = findViewById(R.id.ibtn_send)

        firebaseAuth = FirebaseAuth.getInstance()

        userUidReceiver = intent.getStringExtra("userID").toString()

        firebaseDatabase = FirebaseDatabase.getInstance()
        hisUid = firebaseDatabase.getReference("Users")

        myUid = firebaseAuth.currentUser!!.uid

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager
        
        
        val query = hisUid.orderByChild("uid").equalTo(userUidReceiver)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val name = ds.child("name").value.toString()
                    hisImage = ds.child("image").value.toString()
                    tvUserName.text = name

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

        ibtnSend.setOnClickListener {
            val message = etMessage.text.toString()
            if (TextUtils.isEmpty(message)) {
                Toast.makeText(this, "Cannot send the empty message...", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(message)
            }
        }

        readMessages()

        seenMessage()

    }

    private fun seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats")
        seenListener = userRefForSeen.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val chat = ds.getValue(Chat::class.java)
                    if (chat?.receiver.equals(myUid) && chat?.sender!!.equals(hisUid)) {
                        val hasSeenHashMap = HashMap<String, Any>()
                        hasSeenHashMap["isSeen"] = true
                        ds.ref.updateChildren(hasSeenHashMap)
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
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (ds in snapshot.children) {
                    val chat = ds.getValue(Chat::class.java)
                    if ( chat?.receiver.equals(myUid) && chat?.sender!!.equals(hisUid) ||
                            chat?.receiver!!.equals(hisUid) && chat.sender.equals(myUid)) {
                        chatList.add(chat)
                        adapter = ChatAdapter(chatList, hisImage)
                        adapter.notifyDataSetChanged()
                        recyclerView.adapter = adapter
                    }
                }
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
        hashMap["receiver"] = userUidReceiver
        hashMap["message"] = message
        hashMap["timestamp"] = timeStamp
        hashMap["isSeen"] = false

        databaseReference.child("Chats").push().setValue(hashMap)

        etMessage.setText("")
    }

    override fun onPause() {
        super.onPause()
        userRefForSeen.removeEventListener(seenListener)
    }

}