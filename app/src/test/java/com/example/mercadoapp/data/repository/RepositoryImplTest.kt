package com.example.mercadoapp.data.repository

import com.example.mercadoapp.RetrofitHelper
import com.example.mercadoapp.data.api.MeliApi
import com.example.mercadoapp.domain.models.ProductList
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class RepositoryImplTest {

    private lateinit var repositoryImpl: Repository
    private lateinit var meliApi: MeliApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        meliApi = RetrofitHelper.meliApiInstance(mockWebServer.url("/").toString())
        repositoryImpl = RepositoryImpl(meliApi)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `api must return a response productList object with http 200`() = runTest {
        val productList = ProductList(arrayListOf())

        val expectedResponse = MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(productList))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repositoryImpl.getProducts("Teste")
        assertThat(actualResponse.body(), equalTo(productList))
        assertThat(actualResponse.code(), equalTo(200))
    }

}