package com.example.effectivemobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.effectivemobile.databinding.FragmentFavouritesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment(R.layout.fragment_favourites) {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private val vm: FavouritesViewModel by viewModel()

    private val adapter by lazy {
        CourseListAdapter(onBookmarkClick = { vm.onBookmarkClick(it) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerCourses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCourses.adapter = adapter
        binding.recyclerCourses.setHasFixedSize(true)
        binding.recyclerCourses.itemAnimator = null
        binding.recyclerCourses.setItemViewCacheSize(12)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { st ->
                    adapter.submit(st.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
