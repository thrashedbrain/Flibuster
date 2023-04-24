package com.thrashed.flibuster.ui.book

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.thrashed.flibuster.R
import com.thrashed.flibuster.data.database.entities.BookEntity
import com.thrashed.flibuster.data.models.search.SearchLinkType
import com.thrashed.flibuster.databinding.FragmentBookBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class BookFragment : Fragment() {

    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!

    private val model: BookVM by viewModels()

    private val args: BookFragmentArgs? by navArgs()
    private val item by lazy { args?.arg }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        model.setBottomNavVisibility(false)
        requireActivity().window.statusBarColor = requireContext().getColor(R.color.colorBlue10)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ivBookBack.setOnClickListener { findNavController().popBackStack() }

        model.getBook(item).observe(viewLifecycleOwner) { book ->
            val state = book?.isFavorite ?: false
            binding.ivBookBookmark.setImageDrawable(getBookmarkDrawable(state))

            binding.ivBookBookmark.setOnClickListener {
                if (state) {
                    model.deleteFavoriteBook(item)
                } else {
                    model.addFavoriteBook(item)
                }
            }

            if (book?.isDownloaded == true) {
                binding.clBookLoader.isVisible = false
            }

            if (model.checkDownloadedFile(book)) {
                binding.mbBookDownload.setText("Скачано")
                binding.mbBookDownload.icon =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_open)
            }

            binding.mbBookDownload.setOnClickListener {

                if (model.checkDownloadedFile(book)) {
                    openBook(book)
                } else {
                    binding.clBookLoader.isVisible = true
                    model.downloadBook(item ?: return@setOnClickListener)
                }
            }
        }


        item?.link?.firstOrNull { it.rel == SearchLinkType.TITLE.value }?.let {
            binding.sivBook.load("http://flib.flibusta.is${it.href}")
        } ?: kotlin.run {
            binding.sivBook.load(R.drawable.img_default)
        }
        binding.tvBookTitle.text = item?.title?.trimStart()
        item?.author?.let { searchAuthorItems ->
            binding.tvBookAuthor.text = searchAuthorItems.joinToString { it.name.toString() }
        }

        binding.tvBookDescription.text =
            Html.fromHtml(item?.content?.content, Html.FROM_HTML_MODE_LEGACY)

        binding.tvBookYear.text = item?.year
        binding.tvBookLang.text = item?.language
        binding.tvBookFormat.text = item?.format
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        model.setBottomNavVisibility(true)
        requireActivity().window.statusBarColor = requireContext().getColor(R.color.white)
    }

    private fun getBookmarkDrawable(state: Boolean): Drawable? {
        val id = if (state) R.drawable.ic_bookmark_added else R.drawable.ic_bookmark
        return AppCompatResources.getDrawable(requireContext(), id)
    }

    private fun openBook(bookEntity: BookEntity?) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(
            requireContext(), "${requireContext().packageName}.provider",
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "${bookEntity?.fileUri}"
            )
        )

        intent.setDataAndType(
            uri,
            "application/${bookEntity?.fileUri?.split('.')?.last()}"
        )
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        requireContext().startActivity(Intent.createChooser(intent, "Открыть книгу"))
    }
}