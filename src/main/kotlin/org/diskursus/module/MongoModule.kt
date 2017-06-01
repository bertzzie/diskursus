package org.diskursus.module

import dagger.Module
import dagger.Provides
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.diskursus.DiskursusConfiguration
import org.diskursus.model.User
import org.diskursus.repository.UserRepository
import org.diskursus.repository.impl.UserRepositoryImpl
import javax.inject.Named
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
@Module
class MongoModule(val vertx: Vertx, val config: JsonObject) {

    @Provides
    @Singleton
    fun provideMongoClient(): MongoClient {
        return MongoClient.createShared(vertx, config)
    }

    @Provides
    @Singleton
    @Named("dataInitializer")
    fun provideInitialData(client: MongoClient): () -> Unit {
        val operation: () -> Unit = {
            val userDoc = config.getString(DiskursusConfiguration.UserDocName, "user_doc")

            client.count(userDoc, json { obj() }, { res ->
                if (!res.succeeded()) {
                    throw res.cause()
                }

                val userCount = res.result()
                if (userCount == 0L) {
                    val default = User.getDefaultUser()
                    client.insert(userDoc, default.toJson(), { iRes ->
                        if (!iRes.succeeded()) {
                            throw iRes.cause()
                        }
                    })
                }
            })
        }

        return operation
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl(provideMongoClient())
    }
}