package com.example.effectivemobile

import androidx.recyclerview.widget.DiffUtil
import com.example.effectivemobile.domain.models.Course
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class CourseListAdapter(onBookmarkClick: (Course) -> Unit) :
    AsyncListDifferDelegationAdapter<Course>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(o: Course, n: Course) = o.id == n.id
            override fun areContentsTheSame(o: Course, n: Course) = o == n
        }
    }


    init {
        delegatesManager
            .addDelegate(courseDelegate(onBookmarkClick))
    }

    fun submit(items: List<Course>) = differ.submitList(items)
}