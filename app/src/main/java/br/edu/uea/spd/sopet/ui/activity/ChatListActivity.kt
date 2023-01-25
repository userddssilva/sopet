package br.edu.uea.spd.sopet.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.adapter.UserChatAdapter
import br.edu.uea.spd.sopet.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatListActivity : AppCompatActivity() {

    private lateinit var recycleView: RecyclerView
    private lateinit var adapterUser: UserChatAdapter
    private lateinit var usersList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat_list)
//        supportActionBar?.hide()
        title = "Emoji Recommender"

        recycleView = findViewById(R.id.rc_users_list)
        recycleView.setHasFixedSize(true)
        recycleView.layoutManager = LinearLayoutManager(applicationContext)

        usersList = arrayListOf()

        getAllUsers()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.edit_profile -> {
            startActivity(Intent(this, ProfileActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun getAllUsers() {
        val fUser = FirebaseAuth.getInstance().currentUser
        val firebaseReference = FirebaseDatabase.getInstance().getReference("Users")

        firebaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                for (ds in snapshot.children) {
                    val mUser = ds.getValue(User::class.java)
                    if (!mUser?.uid.equals(fUser?.uid)) {
                        mUser?.let { usersList.add(it) }
                    }
                    adapterUser = UserChatAdapter(usersList)
                    recycleView.adapter = adapterUser
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}