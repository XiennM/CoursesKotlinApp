package com.example.effectivemobile.domain.repository

interface FavouritesRepository {

    fun saveFavourite(saveParam: Int) : Boolean
    fun getFavourites() : List<String>
}