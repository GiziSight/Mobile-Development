package com.example.gizisight.ui.fragment.camera

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gizisight.*
import com.example.gizisight.data.Result
import com.example.gizisight.data.remote.response.KandunganItem
import com.example.gizisight.data.remote.response.ManfaatItem
import com.example.gizisight.databinding.FragmentCameraBinding
import com.example.gizisight.ui.HasilPrediksiActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private lateinit var currentPhotoPath: String
    var isWrapped = false // Variable to keep track of the state
    private lateinit var viewModel: CameraViewModel // Replace with your ViewModel class name
    private lateinit var viewModelFactory: ViewModelFactory


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var getFile: File? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[CameraViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pbUpload.visibility = View.GONE

        viewModel =
            ViewModelProvider(requireActivity())[CameraViewModel::class.java]

        binding.btnAmbilGambar.setOnClickListener{
            showBottomSheetDialog()
        }





//        binding.scanResult.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        context?.let { intent.resolveActivity(it.packageManager) }

        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireActivity(),
                "com.example.gizisight",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = exif(currentPhotoPath)
            uploadImage(viewModel)

        }
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireContext())
                getFile = myFile
                uploadImage(viewModel)
            }
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
        val folder = bottomSheetDialog.findViewById<LinearLayout>(R.id.folderLinearLayout)
        val camera = bottomSheetDialog.findViewById<LinearLayout>(R.id.cameraLinearLayout)

        bottomSheetDialog.show()

        folder?.setOnClickListener {
            startGallery()
            bottomSheetDialog.dismiss();
        }

        camera?.setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                startTakePhoto()
                bottomSheetDialog.dismiss();
            }
//            if (allPermissionsGranted()) {
//            } else {
//                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT)
//                    .show()
//            }
        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadImage(
        viewModel: CameraViewModel,
    ) {
        binding.pbUpload.visibility = View.VISIBLE
        if (getFile != null) {
            lifecycleScope.launch {
                val file = reduceFileImage(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestImageFile
                )
                viewModel.predictImage(imageMultipart).observe(viewLifecycleOwner){ result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.pbUpload.visibility = View.VISIBLE
//                            Toast.makeText(
//                                requireActivity(),
//                                "Loading..",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            }
                            is Result.Success -> {
                                val data = result.data

                                binding.pbUpload.visibility = View.GONE
                                binding.apply {
                                    Log.d("FASDzzz", result.data.message)

                                    val moveWithObjectIntent = Intent(requireActivity(), HasilPrediksiActivity::class.java)
                                    moveWithObjectIntent.putExtra("NUTRISI", data)
                                    startActivity(moveWithObjectIntent)

                                }
                            }
                            is Result.Error -> {
                                binding.pbUpload.visibility = View.GONE
                                Toast.makeText(
                                    requireActivity(),
                                    result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(
                requireActivity(),
                "Tidak ada gambar yang diupload",
                Toast.LENGTH_SHORT
            ).show()
        }
    }




    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}