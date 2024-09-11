package com.example.compassintltask.ui.detail

import android.Manifest
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.compassintltask.databinding.DetailFragmentBinding
import com.example.compassintltask.utils.Constants
import com.example.compassintltask.utils.TakePictureWithUriContract
import com.example.compassintltask.utils.createImageUri
import com.example.compassintltask.utils.getActualFilePath
import com.example.compassintltask.utils.saveCaptureImageAndGetPath
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding
    private val viewModel: DetailViewModel by viewModels()

    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var cameraPermission: ActivityResultLauncher<String>
    private lateinit var pickPicture: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initCall()
        initListener()
        registerActivityListeners()

    }

    private fun initCall() {
        val userId = requireArguments().getString(Constants.BUNDLE_KEY)
        userId?.let {
            viewModel.getUserInfo(userId = userId)
        }
    }

    private fun initListener() {
        binding.imageCardView.setOnClickListener {
            showOptionsDialog()
        }
    }

    private fun initObservers() {
        viewModel.userInfoLiveData.observe(viewLifecycleOwner) { response ->
            response?.let {
                binding.usernameTextView.text = response.username
                binding.passwordTextView.text = response.password
                if (response.profileImagePath.isNullOrEmpty().not()) {
                    Glide.with(requireContext())
                        .load(response.profileImagePath)
                        .into(binding.profileImageView)
                }
            }
        }

    }

    private fun registerActivityListeners() {
        pickPicture = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                viewModel.updateUserProfileImage(
                    userId = viewModel.getCurrentSessionUserId(),
                    url = requireContext().getActualFilePath(it)
                )
            }
        }
        cameraPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    takePicture.launch(requireContext().createImageUri())
                } else {
                    Toast.makeText(
                        requireContext(), "Camera permission is not granted",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

        takePicture = registerForActivityResult(TakePictureWithUriContract()) {
            if (it.first) {
                val actualPath = requireContext().saveCaptureImageAndGetPath(it.second)
                viewModel.updateUserProfileImage(
                    userId = viewModel.getCurrentSessionUserId(),
                    url = actualPath
                )
            }
        }
    }

    private fun showOptionsDialog() {
        val options = arrayOf("Camera", "Gallery")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Option")
        builder.setItems(options) { _, option ->
            when (option) {
                0 -> {
                    // Handle Camera option
                    cameraPermission.launch(Manifest.permission.CAMERA)
                }

                1 -> {
                    // Handle Gallery option
                    pickPicture.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            }
        }
        builder.show()
    }
}

