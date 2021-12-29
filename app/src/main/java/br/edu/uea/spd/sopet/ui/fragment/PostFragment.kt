package br.edu.uea.spd.sopet.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.edu.uea.spd.sopet.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


@Suppress("PrivatePropertyName")
class PostFragment : Fragment() {

    // local constants
    private val IMAGE_HEIGHT = 660
    private val TAG = PostFragment.toString()

    // firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser

    // view items
    private lateinit var mEtPostDescription: EditText
    private lateinit var mIvPostImage: ImageView
    private lateinit var mBtnPost: MaterialButton
    private lateinit var mIBtnCamera: ImageButton
    private lateinit var mIBtnGallery: ImageButton
    private lateinit var mMTvUserName: MaterialTextView
    private lateinit var mCIvProfilePic: CircleImageView
    private lateinit var bottomNavigation: BottomNavigationView

    // storage
    private lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    // user info
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var uid: String
    private lateinit var imageDp: String

    // progress bar
    private lateinit var mProgressDialog: ProgressDialog

    // image picked will be samed in this uri
    private var imageUri: Uri? = null

    companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val STORAGE_REQUEST_CODE = 200
        const val IMAGE_PICK_CAMERA_CODE = 300
        const val IMAGE_PICK_GALLERY_CODE = 400
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        // init permissions array
        cameraPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        storagePermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        // init firebase instances
        firebaseInit()

        mProgressDialog = ProgressDialog(context)
        bottomNavigation = requireActivity().findViewById(R.id.bottom_navigation)

        mEtPostDescription = view.findViewById(R.id.post_et_description)
        mIvPostImage = view.findViewById(R.id.post_iv_photo)
        mIBtnCamera = view.findViewById(R.id.post_ibtn_camera)
        mIBtnGallery = view.findViewById(R.id.post_ibtn_gallery)
        mBtnPost = view.findViewById(R.id.post_btn_post)
        mMTvUserName = view.findViewById(R.id.post_mtv_user_name)
        mCIvProfilePic = view.findViewById(R.id.post_img_user)


        // get image from camera
        mIBtnCamera.setOnClickListener {
            if (!checkCameraPermission()) {
                requestCameraPermission()
            } else {
                getImageFromCamera()
            }
        }

        // get image from gallery
        mIBtnGallery.setOnClickListener {
            if (!checkStoragePermission()!!) {
                requestStoragePermission()
            } else {
                getImageFromGallery()
            }
        }

        // Upload button click listener
        mBtnPost.setOnClickListener {
            val description = mEtPostDescription.text.toString()

            if (description.isEmpty()) {
                Toast.makeText(context, "Insert a post description...", Toast.LENGTH_SHORT).show()
            } else {

                if (imageUri == null) {
                    // post without image
                    uploadData(description, "noImage")

                } else {
                    // post with image
                    uploadData(description, imageUri.toString())
                }
            }
        }

        return view
    }

    private fun closeFragmentAndOpenFeed() {
        val feedHomeFragment = FeedHomeFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_post, feedHomeFragment)
            .addToBackStack(null)
            .commit()
        bottomNavigation.selectedItemId = R.id.page_1
    }

    private fun firebaseInit() {
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        user?.let {
            email = it.email.toString()
            uid = it.uid
        }
        firebaseUser = firebaseAuth.currentUser!!

        // get some info of current user include in post
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val query = databaseReference.orderByChild("email").equalTo(email)
        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    name = ds.child("name").value.toString()
                    email = ds.child("email").value.toString()
                    imageDp = ds.child("image").value.toString()

                    // set data user
                    mMTvUserName.text = name

                    //  load images of item publication
                    try {
                        // If image is received then set
                        Picasso.get().load(imageDp).into(mCIvProfilePic)
                    } catch (e: Exception) {
                        // If there is any exception hide image
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun uploadData(description: String, uri: String) {
        mProgressDialog.setMessage("Publishing post...")
        mProgressDialog.show()

        // for post-time, post-id, post-publish-time
        val timeStamp = System.currentTimeMillis()
        val filePathAndName = "Post/post_$timeStamp"

        if (uri != "noImage") {
            // post with image
            // first post image, if success post all content
            val ref = FirebaseStorage.getInstance().reference.child(filePathAndName)
            ref.putFile(Uri.parse(uri))
                .addOnSuccessListener {
                    val uriTask = it.storage.downloadUrl
                    while (!uriTask.isSuccessful);

                    if (uriTask.isSuccessful) {
                        val downloadUri = uriTask.result.toString()
                        savePostOnFirebase(timeStamp, description, downloadUri)
                    }
                }
                .addOnFailureListener { error ->
                    mProgressDialog.dismiss()
                    Toast.makeText(
                        context,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            // post without image
            savePostOnFirebase(timeStamp, description)
        }
    }

    private fun savePostOnFirebase(timeStamp: Long, description: String, downloadUri: String = "") {
        val hashMap = HashMap<Any, String>()
        hashMap["uid"] = uid
        hashMap["uName"] = name
        hashMap["uEmail"] = email
        hashMap["uDp"] = imageDp
        hashMap["pId"] = timeStamp.toString()
        hashMap["pDescr"] = description
        hashMap["pImage"] = downloadUri
        hashMap["pTime"] = timeStamp.toString()

        val internalRef = FirebaseDatabase.getInstance().getReference("Post")
        internalRef.child(timeStamp.toString()).setValue(hashMap)
            .addOnSuccessListener {
                mProgressDialog.dismiss()
                Toast.makeText(context, "Post published", Toast.LENGTH_SHORT).show()
                mEtPostDescription.setText("")
                mIvPostImage.setImageURI(null)
                imageUri = null
                closeFragmentAndOpenFeed()
            }
            .addOnFailureListener { error ->
                mProgressDialog.dismiss()
                Toast.makeText(context, error.message.toString(), Toast.LENGTH_SHORT).show()
                closeFragmentAndOpenFeed()
            }
    }

//    private fun showImagePickDialog() {
//        val options = arrayOf("Camera", "Gallery")
//        val builder = context?.let { AlertDialog.Builder(it) }
//
//        builder?.setTitle("Choose Image from")
//        builder?.setItems(options) { _, which ->
//            when (which) {
//                0 -> {
//                    if (!checkCameraPermission()) {
//                        requestCameraPermission()
//                    } else {
//                        getImageFromCamera()
//                    }
//                }
//                1 -> {
//                    if (!checkStoragePermission()!!) {
//                        requestStoragePermission()
//                    } else {
//                        getImageFromGallery()
//                    }
//                }
//            }
//        }
//        builder?.create()?.show()
//    }

    private fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE)
    }

    private fun getImageFromCamera() {
        // intent to pick image from camera
        val cv = ContentValues()
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick")
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr")

        imageUri = activity
            ?.contentResolver
            ?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE)
    }

    private fun requestCameraPermission() {
        // request camera permission
        requestPermissions(
            cameraPermissions,
            CAMERA_REQUEST_CODE
        )
    }

    private fun requestStoragePermission() {
        // request storage permission
        requestPermissions(
            storagePermissions,
            STORAGE_REQUEST_CODE
        )
    }

    private fun checkStoragePermission(): Boolean? {
        val resultStoragePermission = context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == (PackageManager.PERMISSION_GRANTED)
        }
        return resultStoragePermission
    }

    private fun checkCameraPermission(): Boolean {
        // check if camera permission is enabled or not
        // return true if enable
        // return false if not enabled
        val resultCameraPermission = context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }
        val resultStoragePermission = context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
        return resultCameraPermission == true && resultStoragePermission == true
    }

    override fun onRequestPermissionsResult(
        // handle permission result
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // this method is called when user press allow or deny from permission request dialog
        // here we will handle permission cases (allowed and denied)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted) {
                        Log.d(TAG, "Debug ===> Calling show pick from camera")
                        // both permission are granted
                        getImageFromCamera()

                    } else {
                        // camera or gallery or both permission were denied
                        Toast.makeText(
                            context,
                            "Camera & Storage both permission are necessary",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted) {
                        // storage permission are granted
                        getImageFromGallery()

                    } else {
                        // camera or gallery or both permission were denied
                        Toast.makeText(
                            context,
                            "Storage both permission are necessary",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // this method will be called after picking image from camera or gallery
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                imageUri = data?.data
            }

            val imageResizedBitmap = resizeImagePost()
            mIvPostImage.visibility = View.VISIBLE
            mIvPostImage.setImageBitmap(imageResizedBitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun resizeImagePost(): Bitmap? {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        // set to imageview
        val yourBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
        return Bitmap.createScaledBitmap(yourBitmap, width, IMAGE_HEIGHT, true)
    }
}