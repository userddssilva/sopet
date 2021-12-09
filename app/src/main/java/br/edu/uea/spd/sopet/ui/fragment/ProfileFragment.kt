package br.edu.uea.spd.sopet.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.edu.uea.spd.sopet.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.lang.Exception


class ProfileFragment : Fragment() {

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    // Views from xml
    private lateinit var ivAvatar: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Init Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Users")

        // Init views
        ivAvatar = view.findViewById(R.id.iv_avatar)
        tvEmail = view.findViewById(R.id.tv_email)
        tvName = view.findViewById(R.id.tv_user_name)
        tvPhone = view.findViewById(R.id.tv_phone)

        // Query
        val query = databaseReference.orderByChild("email").equalTo(firebaseUser.email)
        // My top posts by number of stars
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    // Get data
                    val name = "${postSnapshot.child("name").value}"
                    val email = "${postSnapshot.child("email").value}"
                    val phone = "${postSnapshot.child("phone").value}"
                    val image = "${postSnapshot.child("image").value}"

                    // Set data
                    tvName.text = name
                    tvEmail.text = email
                    tvPhone.text = phone
                    try {
                        // If image is received then set
                        Picasso.get().load(image).into(ivAvatar)
                    } catch (e: Exception) {
                        // If there is any exception while getting image then set default
                        Picasso.get().load(R.drawable.ic_baseline_add_a_photo_24).into(ivAvatar)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException())
            }
        })
        return view
    }
}