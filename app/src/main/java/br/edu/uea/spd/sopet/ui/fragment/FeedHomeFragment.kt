package br.edu.uea.spd.sopet.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.adapter.FeedAdapter
import br.edu.uea.spd.sopet.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class FeedHomeFragment : Fragment() {


    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference: DatabaseReference = firebaseDatabase.getReference("Post")
    private var firebaseUser: FirebaseUser = firebaseAuth.currentUser!!
    private var posts = arrayListOf<HashMap<String, String>>()
    private lateinit var recycleView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_feed_home, container, false)

        recycleView= view.findViewById(R.id.rc_pet_list)
        loadDataFromFirebase()

        return view
    }

    private fun openDetailClassWhenClickItem(pet: Post) {
//        startActivity(Intent(context, PostDetailsActivity::class.java))

    }

    private fun loadDataFromFirebase() {
        // Query
        posts.clear()
        val query = databaseReference.orderByChild("pTime:")
        // My top posts by number of stars
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    // Get data

                    val hashMap = HashMap<String, String>()
                    hashMap["uid"] = postSnapshot.child("uid").value.toString()
                    hashMap["uName"] = postSnapshot.child("uName").value.toString()
                    hashMap["uEmail"] = postSnapshot.child("uEmail").value.toString()
                    hashMap["uDp"] = postSnapshot.child("uDp").value.toString()
                    hashMap["pId"] = postSnapshot.child("pId").value.toString()
                    hashMap["pDescr"] = postSnapshot.child("pDescr").value.toString()
                    hashMap["pImage"] = postSnapshot.child("pImage").value.toString()
                    hashMap["pTime"] = postSnapshot.child("pTime").value.toString()

                    Log.d("Debug firebase =======> ", hashMap.toString())

                    posts.add(hashMap)
//                    val name = "${postSnapshot.child("name").value}"
//                    val email = "${postSnapshot.child("email").value}"
//                    val phone = "${postSnapshot.child("phone").value}"
//                    val image = "${postSnapshot.child("image").value}"
//                    val cover = "${postSnapshot.child("cover").value}"
//
//                    // Set data
//                    tvName.text = name
//                    tvEmail.text = email
//                    tvPhone.text = phone
//
//                    try {
//                        // If image is received then set
//                        Picasso.get().load(image).into(ivAvatar)
//                    } catch (e: Exception) {
//                        // If there is any exception while getting image then set default
//                        Picasso.get().load(R.drawable.ic_default_img_while).into(ivAvatar)
//                    }
//                    try {
//                        // If image is received then set
//                        Picasso.get().load(cover).into(ivCover)
//                    } catch (e: Exception) {
//                        // If there is any exception while getting image then set default
//                    }
                }

                val adapter = FeedAdapter(posts) { openDetailClassWhenClickItem(it) }
                recycleView.layoutManager = LinearLayoutManager(context)
                recycleView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException())
            }


        })
    }
}