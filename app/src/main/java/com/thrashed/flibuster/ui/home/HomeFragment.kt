package com.thrashed.flibuster.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thrashed.flibuster.base.recyclerview.HistoryItemDecoration
import com.thrashed.flibuster.base.recyclerview.SearchItemDecoration
import com.thrashed.flibuster.base.recyclerview.removeAllItemDecorations
import com.thrashed.flibuster.databinding.FragmentHomeBinding
import com.thrashed.flibuster.ui.search.SearchPagingAdapter
import com.thrashed.flibuster.ui.search.SearchStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val model: HomeVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.initHistory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initHistory()
        initRecommendations()

        model.upCommand.observe(viewLifecycleOwner) {
            if (it) {
                binding.rvHomeRecommendations.smoothScrollToPosition(0).run {
                    binding.ablHome.setExpanded(true, true)
                }
            }
        }
    }

    private fun initHistory() {
        val historyAdapter = HomeHistoryAdapter()
        historyAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToBookFragment(it)
            findNavController().navigate(action)
        }

        binding.rvHomeHistory.adapter = historyAdapter
        binding.rvHomeHistory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeHistory.removeAllItemDecorations()
        binding.rvHomeHistory.addItemDecoration(HistoryItemDecoration())

        model.homeHistory.observe(viewLifecycleOwner) {
            historyAdapter.updateItems(it)
        }
    }

    private fun initRecommendations() {
        val searchAdapter = SearchPagingAdapter()
        val resultAdapter = searchAdapter.withLoadStateFooter(SearchStateAdapter())
        searchAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToBookFragment(it)
            findNavController().navigate(action)
        }

        binding.rvHomeRecommendations.removeAllItemDecorations()
        binding.rvHomeRecommendations.addItemDecoration(SearchItemDecoration())

        binding.rvHomeRecommendations.adapter = resultAdapter
        binding.rvHomeRecommendations.layoutManager = LinearLayoutManager(requireContext())

        model.booksFlow.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                searchAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}