package com.thrashed.flibuster.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thrashed.flibuster.base.recyclerview.SearchItemDecoration
import com.thrashed.flibuster.base.recyclerview.removeAllItemDecorations
import com.thrashed.flibuster.data.database.entities.toBookItem
import com.thrashed.flibuster.databinding.FragmentHistoryBinding
import com.thrashed.flibuster.ui.search.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val model: HistoryVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchAdapter = SearchAdapter()
        searchAdapter.onItemClick = {
            val action = HistoryFragmentDirections.actionHistoryFragmentToBookFragment(it)
            findNavController().navigate(action)
        }

        binding.rvHistory.adapter = searchAdapter
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.removeAllItemDecorations()
        binding.rvHistory.addItemDecoration(SearchItemDecoration())

        model.books.observe(viewLifecycleOwner) { items ->
            if (!items.isNullOrEmpty()) {
                searchAdapter.updateItems(items.map { it.toBookItem() })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}