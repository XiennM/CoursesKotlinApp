package com.example.effectivemobile.data.repository

import android.content.Context
import com.example.effectivemobile.data.storage.FavouriteStorage
import com.example.effectivemobile.domain.repository.FavouritesRepository



class FavouritesRepositoryImpl(private val favouriteStorage : FavouriteStorage) : FavouritesRepository{


    override fun saveFavourite(saveParam: Int) : Boolean {
        return favouriteStorage.save(saveParam)
    }

    override fun getFavourites() : List<String> {
        return favouriteStorage.get()
    }
}