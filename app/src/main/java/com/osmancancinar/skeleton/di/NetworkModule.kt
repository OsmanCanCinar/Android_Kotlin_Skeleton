package com.osmancancinar.skeleton.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.osmancancinar.skeleton.R
import com.osmancancinar.skeleton.business.data.network.NetworkDataSource
import com.osmancancinar.skeleton.business.data.network.NetworkDataSourceImpl
import com.osmancancinar.skeleton.business.domain.models.Model
import com.osmancancinar.skeleton.business.domain.util.mappers.EntityMapper
import com.osmancancinar.skeleton.framework.data_source.network.service.APIService
import com.osmancancinar.skeleton.framework.data_source.network.service.APIServiceImpl
import com.osmancancinar.skeleton.framework.data_source.network.mappers.NetworkMapper
import com.osmancancinar.skeleton.framework.data_source.network.model.NetworkEntity
import com.osmancancinar.skeleton.framework.data_source.network.retrofit.API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dependency Injection for Network Module.
 * Provides Network Mapper, API, API Service, Network Data Source and other network related classes.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkMapper(): EntityMapper<NetworkEntity, Model> {
        return NetworkMapper()
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/OsmanCanCinar/")
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideAPI(retrofit: Retrofit.Builder): API {
        return retrofit.build()
            .create(API::class.java)
    }

    @Singleton
    @Provides
    fun provideAPIService(api: API): APIService {
        return APIServiceImpl(api)
    }

    @Singleton
    @Provides
    fun provideNetworkDataSource(
        APIService: APIService,
        networkMapper: NetworkMapper
    ): NetworkDataSource {
        return NetworkDataSourceImpl(APIService, networkMapper)
    }

    @Singleton
    @Provides
    fun provideGlide(@ApplicationContext context: Context): RequestManager =
        Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
        )
}