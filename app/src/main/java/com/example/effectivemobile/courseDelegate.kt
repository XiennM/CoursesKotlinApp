package com.example.effectivemobile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.effectivemobile.databinding.ItemCourseBinding
import com.example.effectivemobile.domain.models.Course
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import eightbitlab.com.blurview.RenderEffectBlur

@SuppressLint("NewApi")
fun courseDelegate(onBookmarkClick: (Course) -> Unit) =
    adapterDelegateViewBinding<Course, Course, ItemCourseBinding>(
        viewBinding = { inflater: LayoutInflater, parent: ViewGroup ->
            ItemCourseBinding.inflate(inflater, parent, false)
        }
    ) {
        bind {
            binding.txtCourseName.text = item.title
            binding.txtDescription.text = item.description
            binding.txtPrice.text = "₽ ${item.price}"
            binding.imgCourse.setImageResource(R.drawable.cat)
            binding.txtRateChip.text = item.rate
            binding.txtDateChip.text = item.publishDate
            binding.iconBookmark.isSelected = item.hasLike

            binding.iconBookmark.setOnClickListener { onBookmarkClick(item) }

            if (binding.badgeRate.tag != true) {
                val blurRoot = binding.root
                val windowBg = blurRoot.background
                val radius = 18f

                binding.badgeRate.setupWith(blurRoot, RenderEffectBlur())
                    .setFrameClearDrawable(windowBg)
                    .setBlurRadius(radius)


                binding.badgeRate.tag = true
            }

            if (binding.badgeDate.tag != true) {
                val blurRoot = binding.root
                val windowBg = blurRoot.background
                val radius = 18f

                binding.badgeDate.setupWith(blurRoot, RenderEffectBlur())
                    .setFrameClearDrawable(windowBg)
                    .setBlurRadius(radius)


                binding.badgeDate.tag = true
            }

            if (binding.badgeBookmark.tag != true) {
                val blurRoot = binding.root
                val windowBg = blurRoot.background
                val radius = 18f

                binding.badgeBookmark.setupWith(blurRoot, RenderEffectBlur())
                    .setFrameClearDrawable(windowBg)
                    .setBlurRadius(radius)


                binding.badgeBookmark.tag = true
            }
        }
    }