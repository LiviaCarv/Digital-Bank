package com.project.digitalbank.ui.user_profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.project.digitalbank.R
import com.project.digitalbank.data.model.User
import com.project.digitalbank.databinding.FragmentUserProfileBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.hideKeyboard
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding get() = _binding!!
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private var user: User? = null
    private var imageProfile: String? = null
    private var currentPhotoPath: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar)
        getProfile()
        initListener()
        checkPermissionCamera()
    }

    private fun initListener() {
        binding.btnUpdateInfo.setOnClickListener {
            if (user != null) {
                validateData()
            }
        }
    }

    private fun checkPermissionCamera() {

        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
                openCamera()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    requireContext(),
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        showDialogPermissionDenied(
            permissionlistener,
            Manifest.permission.CAMERA,
            R.string.text_message_camera_permission_denied
        )

    }

    private fun checkPermissionGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            Manifest.permission.READ_MEDIA_IMAGES

        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
                openGallery()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    requireContext(),
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        showDialogPermissionDenied(
            permissionlistener,
            permission,
            R.string.text_message_gallery_permission_denied
        )
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(
                requireContext(),
                "It was not possible to open camera.",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (photoFile != null) {
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                "com.project.digitalbank.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            cameraLauncher.launch(takePictureIntent)
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale("pt", "BR")).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        currentPhotoPath = image.absolutePath
        return image
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath!!)
            binding.imgUserIcon.setImageURI(Uri.fromFile(file))
            imageProfile = file.toURI().toString()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageSelected = result.data!!.data
            imageProfile = imageSelected.toString()
            if (imageSelected != null) {
                binding.imgUserIcon.setImageBitmap(getBitmap(imageSelected))
            }
        }
    }

    private fun getBitmap(pathUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    pathUri
                )
            } else {
                val source =
                    ImageDecoder.createSource(requireActivity().contentResolver, pathUri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // A permissão foi concedida, você pode agora acessar os arquivos de mídia
                openGallery()
            } else {
                // A permissão foi negada, informe ao usuário que o acesso aos arquivos de mídia não é possível
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialogPermissionDenied(
        permissionListener: PermissionListener,
        permission: String,
        message: Int
    ) {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedTitle("Permission Denied.")
            .setDeniedMessage(message)
            .setDeniedCloseButtonText("Deny")
            .setGotoSettingButtonText("Approve")
            .setPermissions(permission)
            .check()
    }

    private fun getProfile() {
        userProfileViewModel.getUserProfile().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    stateView.data?.let {
                        user = it
                        configData()
                    }
                }

                else -> {
                    binding.progressBar.isVisible = false

                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }

        }
    }

    private fun validateData() {
        binding.apply {
            val name = edtTextName.text.toString().trim()
            val phoneNumber = edtTextTelephone.unMaskedText

            if (name.isEmpty()) {
                showBottomSheet(message = getString(R.string.register_provide_name))
            } else if (phoneNumber?.isEmpty() == true) {
                showBottomSheet(message = getString(R.string.register_provide_phone))
            } else if (phoneNumber?.length != 11) {
                showBottomSheet(message = getString(R.string.register_phone_number_invalid))
            } else {
                hideKeyboard()
                user?.name = name
                user?.phone = phoneNumber
                user?.let {
                    confirmProfileChanges()
                }
            }

        }
    }

    private fun saveProfile() {
        user?.let {
            userProfileViewModel.saveProfile(it).observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is StateView.Success -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                    }
                }
            }
        }
    }

    private fun confirmProfileChanges() {
        showBottomSheet(
            message = getString(R.string.text_message_changes),
            titleDialog = R.string.title_dialog_confirm_changes,
            titleButton = R.string.txt_bottom_sheet_ok,
            onClick = {
                saveProfile()
            }
        )
    }

    private fun configData() {
        user?.let {
            binding.apply {
                edtTextName.setText(it.name)
                edtTextTelephone.setText(it.phone)
                edtTextEmail.setText(it.email)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}