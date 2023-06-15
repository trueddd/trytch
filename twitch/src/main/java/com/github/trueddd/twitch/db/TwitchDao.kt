package com.github.trueddd.twitch.db

import androidx.room.*
import com.github.trueddd.twitch.data.Stream
import com.github.trueddd.twitch.data.Tokens
import com.github.trueddd.twitch.data.User
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TwitchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Query("update users set current = 0 where current = 1")
    suspend fun clearCurrentUser()

    @Query("select * from users where current = 1 limit 1")
    fun getUserFlow(): Flow<User?>

    @Query("select * from users where current = 1 limit 1")
    suspend fun getUser(): User?

    @Query("select * from users where displayName = :displayName limit 1")
    suspend fun getUserByName(displayName: String): User?

    @Query("select * from users where id = :userId limit 1")
    fun getUserByIdFlow(userId: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserToken(tokens: Tokens)

    @Update
    suspend fun updateUserToken(tokens: Tokens)

    @Query("select accessToken from tokens limit 1")
    suspend fun getUserToken(): String?

    @Query("delete from tokens")
    suspend fun deleteTokens()

    @Transaction
    suspend fun insertUserInfo(user: User, tokens: Tokens) {
        clearCurrentUser()
        insertUser(user)
        insertUserToken(tokens)
    }

    @Transaction
    suspend fun upsertToken(tokens: Tokens) {
        when (getUserToken()) {
            null -> insertUserToken(tokens)
            else -> updateUserToken(tokens)
        }
    }

    @Insert
    suspend fun insertStreams(streams: List<Stream>)

    @Query("delete from streams")
    suspend fun deleteStreams()

    @Query("select * from streams")
    fun getStreamsFlow(): Flow<List<Stream>>

    @Query("select * from streams where id = :id limit 1")
    suspend fun getStreamById(id: String): Stream?

    @Query("select * from streams where userName = :channel limit 1")
    fun getStreamFlowByChannel(channel: String): Flow<Stream?>

    @Query("select * from streams where userName = :channel limit 1")
    suspend fun getStreamByUserName(channel: String): Stream?

    @Transaction
    suspend fun upsertStreams(streams: List<Stream>) {
        deleteStreams()
        insertStreams(streams)
    }
}
