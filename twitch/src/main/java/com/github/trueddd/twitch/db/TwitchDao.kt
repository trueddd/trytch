package com.github.trueddd.twitch.db

import androidx.room.*
import com.github.trueddd.twitch.data.Tokens
import com.github.trueddd.twitch.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface TwitchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("delete from users")
    suspend fun deleteUser()

    @Query("select * from users limit 1")
    fun getUserFlow(): Flow<User?>

    @Query("select * from users limit 1")
    suspend fun getUser(): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserToken(tokens: Tokens)

    @Update
    suspend fun updateUserToken(tokens: Tokens)

    @Query("select accessToken from tokens limit 1")
    suspend fun getUserToken(): String?

    @Query("delete from tokens")
    suspend fun deleteTokens()

    // fixme: Foreign key constraint fail
    @Transaction
    suspend fun insertUserInfo(user: User, tokens: Tokens) {
        deleteTokens()
        deleteUser()
        insertUser(user)
        upsertToken(tokens)
    }

    @Transaction
    suspend fun upsertToken(tokens: Tokens) {
        when (getUserToken()) {
            null -> insertUserToken(tokens)
            else -> updateUserToken(tokens)
        }
    }
}
