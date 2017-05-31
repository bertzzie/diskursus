package org.diskursus.module

import dagger.Module
import dagger.Provides
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
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
}