package com.thrashed.flibuster.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thrashed.flibuster.R
import com.thrashed.flibuster.base.hideSoftInput
import com.thrashed.flibuster.base.recyclerview.SearchItemDecoration
import com.thrashed.flibuster.base.recyclerview.removeAllItemDecorations
import com.thrashed.flibuster.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val model: SearchVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSearch()
        initSuggestions()

        model.upCommand.observe(viewLifecycleOwner) {
            if (it) binding.rvSearch.smoothScrollToPosition(0)
        }

        lifecycleScope.launchWhenResumed {
            binding.rvSearchSuggest.isVisible = false
        }

        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            binding.rvSearchSuggest.isVisible = hasFocus
        }
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isBlank()) {
                binding.rvSearchSuggest.isVisible = false
            }
            model.setSuggestionQuery(text.toString())
        }
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                model.setSearch(binding.etSearch.text.toString())
                binding.rvSearchSuggest.isVisible = false
                hideSoftInput()
                true
            } else {
                false
            }
        }
        model.state.observe(viewLifecycleOwner) {
            if (it == SearchVM.SearchState.BOOK) {
                binding.tilSearch.setStartIconDrawable(R.drawable.ic_book)
                binding.tilSearch.hint = "Поиск по книгам"
            } else {
                binding.tilSearch.setStartIconDrawable(R.drawable.ic_person)
                binding.tilSearch.hint = "Поиск по авторам"
            }
        }
        binding.tilSearch.setStartIconOnClickListener {
            model.setSearchState()
        }
    }

    private fun initSuggestions() {
        val adapter = SearchSuggestionsAdapter()
        adapter.onItemClick = {
            model.setSearch(it)
            binding.rvSearchSuggest.isVisible = false
            hideSoftInput()
        }
        binding.rvSearchSuggest.adapter = adapter
        binding.rvSearchSuggest.layoutManager = LinearLayoutManager(requireContext())
        model.suggestions.observe(viewLifecycleOwner) {
            adapter.updateItems(it)
        }
    }

    private fun initSearch() {
        val adapter = SearchPagingAdapter()
        val resultAdapter = adapter.withLoadStateFooter(SearchStateAdapter())
        adapter.onItemClick = {
            val action = SearchFragmentDirections.actionSearchFragmentToBookFragment(it)
            findNavController().navigate(action)
        }

        binding.rvSearch.removeAllItemDecorations()
        binding.rvSearch.addItemDecoration(SearchItemDecoration())

        binding.rvSearch.adapter = resultAdapter
        binding.rvSearch.layoutManager = LinearLayoutManager(requireContext())

        model.booksFlow.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}