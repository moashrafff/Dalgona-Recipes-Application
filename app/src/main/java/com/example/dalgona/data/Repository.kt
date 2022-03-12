package com.example.dalgona.data

import com.example.dalgona.data.source.LocalDataSource
import com.example.dalgona.data.source.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject


@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
    ) {

    val remote = remoteDataSource
    val local = localDataSource

}