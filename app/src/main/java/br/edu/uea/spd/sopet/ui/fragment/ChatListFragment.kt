package br.edu.uea.spd.sopet.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class ChatListFragment : Fragment() {

    private lateinit var recycleView: RecyclerView
    private lateinit var adapterUser: UserChatAdapter
    private lateinit var usersList: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_chat_list, container, false)

        recycleView = view.findViewById(R.id.rc_users_list)
        recycleView.setHasFixedSize(true)
        recycleView.layoutManager = LinearLayoutManager(context)

        usersList = arrayListOf()

        getAllUsers()

        return view
    }

    private fun getAllUsers() {
        val fUser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().getReference("Users")

        ref.addValueEventListener (object : ValueEventListener {
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