package com.challenge.moises.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.reflect.Type

@OptIn(ExperimentalCoroutinesApi::class)
class BasePreferencesManagerTest {

    private val dataStore = mockk<DataStore<Preferences>>()
    private val gson = Gson()
    
    private val manager = object : BasePreferencesManager(dataStore, gson) {
        fun <T> testReadObject(key: Preferences.Key<String>, type: Type, defaultValue: T) =
            readObject(key, type, defaultValue)

    }

    private val testKey = stringPreferencesKey("test_key")

    @Test
    fun `readObject returns defaultValue when key is missing`() = runTest {
        // Given
        val preferences = mockk<Preferences>()
        every { preferences[testKey] } returns null
        every { dataStore.data } returns flowOf(preferences)

        // When
        val result = manager.testReadObject(testKey, String::class.java, "default").first()

        // Then
        assertEquals("default", result)
    }

    @Test
    fun `readObject returns parsed object when key exists`() = runTest {
        // Given
        val preferences = mockk<Preferences>()
        every { preferences[testKey] } returns "\"stored_value\""
        every { dataStore.data } returns flowOf(preferences)

        // When
        val result = manager.testReadObject(testKey, String::class.java, "default").first()

        // Then
        assertEquals("stored_value", result)
    }

    @Test
    fun `readObject returns defaultValue when json is invalid`() = runTest {
        // Given

        val preferences = mockk<Preferences>()
        every { preferences[testKey] } returns "{ invalid }"
        every { dataStore.data } returns flowOf(preferences)

        // When
        val result = manager.testReadObject(testKey, FakeObject::class.java, FakeObject("default")).first()

        // Then
        assertEquals("default", result.name)
    }
}


data class FakeObject(val name: String)