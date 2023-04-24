package com.thrashed.flibuster.ui.bookmark

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
import com.thrashed.flibuster.databinding.FragmentBookmarkBinding
import com.thrashed.flibuster.ui.search.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val model: BookmarkVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchAdapter = SearchAdapter()
        searchAdapter.onItemClick = {
            val action = BookmarkFragmentDirections.actionBookmarkFragmentToBookFragment(it)
            findNavController().navigate(action)
        }

        binding.rvBookmark.adapter = searchAdapter
        binding.rvBookmark.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookmark.removeAllItemDecorations()
        binding.rvBookmark.addItemDecoration(SearchItemDecoration())

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