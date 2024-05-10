package com.example.mercadoapp.data.repository

import com.example.mercadoapp.RetrofitHelper
import com.example.mercadoapp.data.api.MeliApi
import com.example.mercadoapp.domain.models.Product
import com.example.mercadoapp.domain.models.ProductDetails
import com.example.mercadoapp.domain.models.ProductListResponse
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

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


    //getProducts() tests
    @Test
    fun `api must return a response ProductList object with http 200`() = runTest {
        val productListResponse = ProductListResponse(
            arrayListOf(
                Product("MLB1212", "TV", 2000.0, ""),
                Product("MLB2020", "TV", 2000.0, "")
            )
        )

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(productListResponse))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repositoryImpl.getProducts("Teste")
        assertThat(actualResponse.body(), equalTo(productListResponse))
        assertThat(actualResponse.code(), equalTo(HttpURLConnection.HTTP_OK))
    }

    @Test
    fun `for a empty search api must return an body with empty array with http 200`() = runTest {
        val productListResponse = ProductListResponse(arrayListOf())

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(productListResponse))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repositoryImpl.getProducts("asgasgasghsdfh")
        assertThat(actualResponse.body(), equalTo(productListResponse))
        assertThat(actualResponse.body()?.results, equalTo(productListResponse.results))
        assertThat(actualResponse.code(), equalTo(HttpURLConnection.HTTP_OK))
    }

    @Test
    fun `for getProducts server error, api must return with http code 5xx`() = runTest {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repositoryImpl.getProducts("Teste")
        assertThat(actualResponse.code(), equalTo(HttpURLConnection.HTTP_INTERNAL_ERROR))
    }


    //getProductById() tests
    @Test
    fun `for product id, api must return a response ProductDetails object with http 200`() = runTest {
        val productDetailsResponse = ProductDetails("MLB1010", "TV", 2000.00, arrayListOf(), arrayListOf())

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(productDetailsResponse))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repositoryImpl.getProductById("MLB1010")
        assertThat(actualResponse.body(), equalTo(productDetailsResponse))
        assertThat(actualResponse.body()?.id, equalTo(productDetailsResponse.id))
        assertThat(actualResponse.code(), equalTo(HttpURLConnection.HTTP_OK))
    }

    @Test
    fun `for a not found Product id api must return an body with empty array with http 200`() = runTest {
        val productDetailsResponse = ProductDetails()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(productDetailsResponse))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repositoryImpl.getProductById("")
        assertThat(actualResponse.body(), equalTo(productDetailsResponse))
        assertThat(actualResponse.body()?.id, equalTo(productDetailsResponse.id))
        assertThat(actualResponse.code(), equalTo(HttpURLConnection.HTTP_OK))
    }

    @Test
    fun `for getProductById server error, api must return with http code 5xx`() = runTest {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repositoryImpl.getProductById("Teste")
        assertThat(actualResponse.code(), equalTo(HttpURLConnection.HTTP_INTERNAL_ERROR))
    }
}