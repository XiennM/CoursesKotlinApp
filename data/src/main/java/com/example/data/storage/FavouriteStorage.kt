package com.example.effectivemobile.data.storage

interface FavouriteStorage {

    fun save(saveParam: Int) : Boolean

    fun get(): List<String>
}