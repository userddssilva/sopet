package br.edu.uea.spd.sopet.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.core.content.ContextCompat
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.ui.fragment.ProfileFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {

    companion object {
        // Permissions Constants
        const val CAMERA_REQUEST_CODE = 100
        const val STORAGE_REQUEST_CODE = 200
        const val IMAGE_PICK_CAMERA_CODE = 300
        const val IMAGE_PICK_GALLERY_CODE = 400
    }

    // Arrays of permissions to be requested
    private lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    // Views from xml
    private lateinit var ivAvatar: ImageView
    private lateinit var ivCover: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var fabEdit: FloatingActionButton

    // General
    private lateinit var progressDialog: ProgressDialog
    private lateinit var imageUri: Uri
    private lateinit var profileOrCoverPhoto: String
    private lateinit var storageReference: StorageReference
    private val storagePath = "Users_Profile_Cover_Imgs/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)

        // Init arrays of permissions
        cameraPermissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        // Init Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Users")
        storageReference = FirebaseStorage.getInstance().reference

        // Init views
        ivAvatar = findViewById(R.id.iv_avatar)
        ivCover = findViewById(R.id.iv_cover)
        tvEmail = findViewById(R.id.tv_email)
        tvName = findViewById(R.id.tv_user_name)
        tvPhone = findViewById(R.id.tv_phone)
        fabEdit = findViewById(R.id.fab_edit)

        // Init progress bar
        progressDialog = ProgressDialog(this@ProfileActivity)


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
                    val cover = "${postSnapshot.child("cover").value}"

                    // Set data
                    tvName.text = name
                    tvEmail.text = email
                    tvPhone.text = phone

                    try {
                        // If image is received then set
                        Picasso.get().load(image).into(ivAvatar)
                    } catch (e: Exception) {
                        // If there is any exception while getting image then set default
                        Picasso.get().load(R.drawable.ic_default_img_while).into(ivAvatar)
                    }
                    try {
                        // If image is received then set
                        Picasso.get().load(cover).into(ivCover)
                    } catch (e: Exception) {
                        // If there is any exception while getting image then set default
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException())
            }
        })

        // fab button click
        fabEdit.setOnClickListener {
            showEditProfileDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK) {
            if (requestCode == ProfileFragment.IMAGE_PICK_GALLERY_CODE) {
                imageUri = data?.data!!
                uploadProfileCoverPhoto(imageUri)
            }
            if (requestCode == ProfileFragment.IMAGE_PICK_CAMERA_CODE) {
                uploadProfileCoverPhoto(imageUri)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadProfileCoverPhoto(uri: Uri) {
        progressDialog.show()
        val filePathAndName = storagePath + profileOrCoverPhoto + "_" + firebaseUser.uid
        val storageReference2nd = storageReference.child(filePathAndName)
        storageReference2nd.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val downloadUri = uriTask.result
                if (uriTask.isSuccessful) {
                    val results = HashMap<String, Any>()
                    results[profileOrCoverPhoto] = downloadUri.toString()

                    databaseReference.child(firebaseUser.uid).updateChildren(results)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this@ProfileActivity, "Image Updated!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(this@ProfileActivity, "Error updating Image", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this@ProfileActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this@ProfileActivity, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showEditProfileDialog() {
        val options = arrayOf("Edit Profile Picture", "Edit Cover Photo", "Edit Name")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choice Action")

        // Set items to dialog
        builder.setItems(options) { _, pos ->
            when (pos) {
                0 -> {
                    progressDialog.setMessage("Updating Profile Picture")
                    profileOrCoverPhoto = "image"
                    showImagePicDialog()
                }
                1 -> {
                    progressDialog.setMessage("Updating Cover Photo")
                    profileOrCoverPhoto = "cover"
                    showImagePicDialog()
                }
                2 -> {
                    progressDialog.setMessage("Updating Name")
                    showPhoneNameUpdateDialog("name")
                }
//                3 -> {
//                    progressDialog.setMessage("Updating Phone")
//                    showPhoneNameUpdateDialog("phone")
//                }
            }
        }
        builder.create().show()
    }

    private fun showPhoneNameUpdateDialog(key: String) {
        val builder = AlertDialog.Builder(this@ProfileActivity)
        builder.setTitle("Update $key")
        val linearLayout = LinearLayout(this@ProfileActivity)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding(10, 10, 10, 10)
        val editText = EditText(this@ProfileActivity)
        editText.hint = "Enter $key"
        linearLayout.addView(editText)
        builder.setView(linearLayout)

        builder.setPositiveButton("Update") { _, _ ->
            val inputField = editText.text.toString()
            if (inputField.isNotEmpty()) {
                progressDialog.show()
                val hashMap = HashMap<String, Any>()
                hashMap[key] = inputField
                databaseReference.child(firebaseUser.uid).updateChildren(hashMap)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this@ProfileActivity, "Updated!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(this@ProfileActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        builder.setNegativeButton("Cancel") { dial, _ ->
            dial.dismiss()
        }

        builder.create().show()
    }

    private fun showImagePicDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this@ProfileActivity)
        builder.setTitle("Pick Image From")

        // Set items to dialog
        builder.setItems(options) { _, pos ->
            when (pos) {
                0 -> {
                    if (!checkCameraPermission()) {
                        requestCameraPermission()
                    } else {
                        pickFromCamera()
                    }
                }
                1 -> {
                    if (!checkStoragePermission()) {
                        requestStoragePermission()
                    } else {
                        pickFromGallery()
                    }
                }
            }
        }
        builder.create().show()
    }

    private fun checkStoragePermission(): Boolean {
        return this.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } == (PackageManager.PERMISSION_GRANTED)
    }

    private fun requestStoragePermission() {
        requestPermissions(
            storagePermissions,
            ProfileFragment.STORAGE_REQUEST_CODE
        )
    }

    private fun checkCameraPermission(): Boolean {
        val result1 = this.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.CAMERA
            )
        } == (PackageManager.PERMISSION_GRANTED)

        val result2 = this.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } == (PackageManager.PERMISSION_GRANTED)

        return result1 && result2
    }

    private fun requestCameraPermission() {
        requestPermissions(
            cameraPermissions,
            ProfileFragment.CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            ProfileFragment.CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && writeStorageAccepted) {
                        pickFromCamera()
                    } else {
                        Toast.makeText(this@ProfileActivity, "Please enable permissions", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            ProfileFragment.STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (writeStorageAccepted) {
                        pickFromGallery()
                    } else {
                        Toast.makeText(this@ProfileActivity, "Please enable permissions", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun pickFromCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description")
        imageUri = this.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )!!
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, ProfileFragment.IMAGE_PICK_CAMERA_CODE)
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, ProfileFragment.IMAGE_PICK_GALLERY_CODE)
    }
}