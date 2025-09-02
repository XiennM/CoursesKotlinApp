package com.example.effectivemobile.data.storage

import android.content.Context

private const val SHARED_PREFS_NAME = "shared_prefs_name"
private const val KEY_NAME = "ID"

class SharedPrefFavouriteStorage(context: Context) : FavouriteStorage{

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun save(saveParam: Int): Boolean {
        sharedPreferences.edit().putInt(KEY_NAME, saveParam).apply()

        return true
    }

    override fun get(): List<String> {
        //TODO
        //нужно все достать и вернуть

        return emptyList()
    }
}