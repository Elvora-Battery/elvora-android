package com.unsoed.elvora.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.UserShippingModel
import com.unsoed.elvora.data.UserVerify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                token = preferences[TOKEN_KEY] ?: "",
                fullName = preferences[FULL_NAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
            )
        }
    }
    fun getTierUser(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PREMIUM_KEY] ?: false
        }
    }

    fun getUserShipping(): Flow<UserShippingModel> {
        return dataStore.data.map { preferences ->
            UserShippingModel(
                name = preferences[NAME_SHIPPING_KEY] ?: "",
                telephoneNumber = preferences[NUMBER_SHIPPING_KEY] ?: "",
                street = preferences[STREET_SHIPPING_KEY] ?: "",
                village = preferences[VILLAGE_SHIPPING_KEY] ?: "",
                address = preferences[ADDRESS_SHIPPING_KEY] ?: ""
            )
        }
    }

    fun getUserVerify(): Flow<UserVerify> {
        return dataStore.data.map { preferences ->
            UserVerify(
                name = preferences[KTP_NAME_KEY] ?: "",
                nik = preferences[KTP_NIK_KEY] ?: "",
                date = preferences[KTP_DATE_KEY] ?: "",
            )
        }
    }

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = user.token
            preferences[EMAIL_KEY] = user.email
            preferences[FULL_NAME_KEY] = user.fullName
        }
    }

    suspend fun saveUserShipping(user: UserShippingModel) {
        dataStore.edit { preferences ->
            preferences[NAME_SHIPPING_KEY] = user.name
            preferences[NUMBER_SHIPPING_KEY] = user.telephoneNumber
            preferences[STREET_SHIPPING_KEY] = user.street
            preferences[VILLAGE_SHIPPING_KEY] = user.village
            preferences[ADDRESS_SHIPPING_KEY] = user.address
        }
    }

    suspend fun saveUserVerify(user: UserVerify) {
        dataStore.edit { preferences ->
            preferences[KTP_NAME_KEY] = user.name
            preferences[KTP_NIK_KEY] = user.nik
            preferences[KTP_DATE_KEY] = user.date
        }
    }

    suspend fun saveUserPremium(isPremium: Boolean) {
        dataStore.edit { preferences ->
            preferences[PREMIUM_KEY] = isPremium
        }
    }


    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(EMAIL_KEY)
            preferences.remove(FULL_NAME_KEY)
        }
    }

    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email_key")
        private val FULL_NAME_KEY = stringPreferencesKey("fullname_key")
        private val TOKEN_KEY = stringPreferencesKey("token_key")
        private val PREMIUM_KEY = booleanPreferencesKey("premium_key")

        private val NAME_SHIPPING_KEY = stringPreferencesKey("name_shipping_key")
        private val NUMBER_SHIPPING_KEY = stringPreferencesKey("number_shipping_key")
        private val STREET_SHIPPING_KEY = stringPreferencesKey("street_shipping_key")
        private val VILLAGE_SHIPPING_KEY = stringPreferencesKey("village_shipping_key")
        private val ADDRESS_SHIPPING_KEY = stringPreferencesKey("address_shipping_key")

        private val KTP_NAME_KEY = stringPreferencesKey("ktp_name_key")
        private val KTP_NIK_KEY = stringPreferencesKey("ktp_nik_key")
        private val KTP_DATE_KEY = stringPreferencesKey("ktp_date_key")

        @Volatile
        private var INSTANCE: UserPreferences? = null

        @JvmStatic
        fun getInstanceDataStore(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}