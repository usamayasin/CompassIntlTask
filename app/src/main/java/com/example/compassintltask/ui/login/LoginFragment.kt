package com.example.compassintltask.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.compassintltask.R
import com.example.compassintltask.data.model.UIState
import com.example.compassintltask.data.remote.model.LoginRequestBody
import com.example.compassintltask.databinding.LoginFragmentBinding
import com.example.compassintltask.utils.Constants
import com.example.compassintltask.utils.gone
import com.example.compassintltask.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetup()
    }

    private fun initSetup() {
        if (viewModel.checkIfSessionExist()) {
            val bundle = bundleOf(Constants.BUNDLE_KEY to viewModel.getCurrentSessionUserId())
            findNavController().navigate(R.id.toDetailFragment, bundle)
        } else {
            initObservers()
            initListener()
        }
    }

    private fun initListener() {
        binding.loginButton.setOnClickListener {
            viewModel.handleLogin(
                LoginRequestBody(
                    username = binding.usernameEditText.text.toString().trim(),
                    password = binding.passwordEditText.text.toString().trim()
                )
            )
        }
    }

    private fun initObservers() {
        viewModel.loginLiveData.observe(viewLifecycleOwner) { response ->
            response?.let {
                val bundle = bundleOf(Constants.BUNDLE_KEY to response.userid)
                findNavController().navigate(R.id.toDetailFragment, bundle)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                when (uiState) {
                    is UIState.LoadingState -> {
                        binding.progressbar.visible()
                    }

                    is UIState.ContentState, UIState.InitialState -> {
                        binding.progressbar.gone()
                    }

                    is UIState.ErrorState -> {
                        binding.progressbar.gone()
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.loginLiveData.removeObservers(this)
    }
}