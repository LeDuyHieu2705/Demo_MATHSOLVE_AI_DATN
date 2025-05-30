package com.hieuld.datn.mathsolved.utils.commons.managers

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SimpleFavouriteManager {

    private const val PREF_NAME = "favourite_messages"
    private const val KEY_FAVOURITES = "favourites_with_timestamp"
    private const val KEY_FAVOURITES_OLD = "favourite_messages"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    data class FavouriteItem(
        val message: String,
        val timestamp: Long
    )

    /**
     * Thêm message vào favourites với timestamp hiện tại
     */
    fun addToFavourite(context: Context, message: String) {
        val currentItems = getFavouriteItems(context).toMutableList()

        // Kiểm tra xem message đã tồn tại chưa
        if (!currentItems.any { it.message == message }) {
            currentItems.add(FavouriteItem(message, System.currentTimeMillis()))
            saveFavouriteItems(context, currentItems)
        }
    }

    /**
     * Xóa message khỏi favourites
     */
    fun removeFromFavourite(context: Context, message: String) {
        val currentItems = getFavouriteItems(context).toMutableList()
        currentItems.removeAll { it.message == message }
        saveFavouriteItems(context, currentItems)
    }

    fun isFavourite(context: Context, message: String): Boolean {
        return getFavouriteItems(context).any { it.message == message }
    }


    /**
     * Lấy danh sách messages kèm timestamp
     */
    fun getFavouriteMessagesWithTimestamp(context: Context): List<Pair<String, Long>> {
        return getFavouriteItems(context).map { Pair(it.message, it.timestamp) }
    }


    /**
     * Xóa tất cả favourites
     */
    fun clearAllFavourites(context: Context) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit()
            .remove(KEY_FAVOURITES)
            .remove(KEY_FAVOURITES_OLD)
            .apply()
    }

    /**
     * Lấy danh sách FavouriteItem từ SharedPreferences
     */
    private fun getFavouriteItems(context: Context): List<FavouriteItem> {
        val sharedPref = getSharedPreferences(context)

        // Thử load từ key mới trước
        val jsonString = sharedPref.getString(KEY_FAVOURITES, null)

        return if (jsonString != null) {
            try {
                val type = object : TypeToken<List<FavouriteItem>>() {}.type
                Gson().fromJson(jsonString, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            // Migrate từ data cũ nếu có
            migrateOldData(context)
        }
    }

    /**
     * Lưu danh sách FavouriteItem vào SharedPreferences
     */
    private fun saveFavouriteItems(context: Context, items: List<FavouriteItem>) {
        val sharedPref = getSharedPreferences(context)
        val jsonString = Gson().toJson(items)

        sharedPref.edit()
            .putString(KEY_FAVOURITES, jsonString)
            .apply()
    }

    /**
     * Migrate data từ format cũ (chỉ message) sang format mới (message + timestamp)
     */
    private fun migrateOldData(context: Context): List<FavouriteItem> {
        val sharedPref = getSharedPreferences(context)
        val oldJsonString = sharedPref.getString(KEY_FAVOURITES_OLD, null)

        return if (oldJsonString != null) {
            try {
                val type = object : TypeToken<List<String>>() {}.type
                val oldMessages: List<String> = Gson().fromJson(oldJsonString, type) ?: emptyList()

                val newItems = oldMessages.map {
                    FavouriteItem(it, System.currentTimeMillis())
                }

                saveFavouriteItems(context, newItems)
                sharedPref.edit().remove(KEY_FAVOURITES_OLD).apply()

                newItems
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }
}