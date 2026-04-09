package com.challenge.moises.core.network.data.repositories

import com.challenge.moises.core.network.ITunesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItunesRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: ITunesApi
    private lateinit var repository: ItunesRepositoryImpl

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
        repository = ItunesRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getSongDetails returns songs when api succeeds`() = runTest {
        // Given
        val json = readResourceFile("itunes_response.json")
        mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(200))

        // When
        val result = repository.getSongDetails("123").first()

        // Then
        assertEquals(1, result.size)
        assertEquals("123", result[0].id)
        assertEquals("Track", result[0].title)
        assertEquals("Artist", result[0].artistName)
    }

    @Test
    fun `getAlbumSongs returns songs when api succeeds`() = runTest {
        // Given
        val json = readResourceFile("itunes_album_response.json")
        mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(200))

        // When
        val result = repository.getAlbumSongs("456").first()

        // Then
        assertEquals(1, result.size)
        assertEquals("456", result[0].id)
    }

    @Test(expected = Exception::class)
    fun `getSongDetails throws exception when api fails`() = runTest {
        // Given
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        // When
        repository.getSongDetails("123").first()
    }

    @Test
    fun `getSongDetails returns empty list when api returns no results`() = runTest {
        // Given
        val json = readResourceFile("itunes_empty_response.json")
        mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(200))

        // When
        val result = repository.getSongDetails("unknown").first()

        // Then
        assert(result.isEmpty())
    }

    private fun readResourceFile(fileName: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        return inputStream?.bufferedReader()?.use { it.readText() } ?: ""
    }
}
