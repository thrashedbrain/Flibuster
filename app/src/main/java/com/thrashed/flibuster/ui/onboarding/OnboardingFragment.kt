package com.thrashed.flibuster.ui.onboarding

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.thrashed.flibuster.R
import com.thrashed.flibuster.data.models.ui.OnboardActionType
import com.thrashed.flibuster.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private val model: OnboardingVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.initOnboarding()
    }

    override fun onResume() {
        super.onResume()
        model.showOnboarding(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        model.showOnboarding(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = OnboardingAdapter()

        val requestMultiplePermissions = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isSuccess = permissions.entries.firstOrNull { !it.value }
            if (isSuccess == null) {
                model.setOnboardingState()
                findNavController().setGraph(R.navigation.main_graph)
            }
        }

        adapter.onItemClick = { onboardActionType, position ->
            if (onboardActionType == OnboardActionType.NONE) {
                if (position < adapter.itemCount - 1) {
                    binding.rvOnboarding.smoothScrollToPosition(position + 1)
                }
            } else {
                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }

        binding.rvOnboarding.adapter = adapter
        binding.rvOnboarding.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvOnboarding)

        model.onboardingItems.observe(viewLifecycleOwner) {
            adapter.updateItems(it)
        }
    }

}