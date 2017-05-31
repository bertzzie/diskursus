package org.diskursus.module

import dagger.Component
import io.vertx.core.json.JsonObject
import org.diskursus.verticle.MainVerticle
import javax.inject.Named
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
@Singleton
@Component(modules = arrayOf(VertxModule::class, MongoModule::class))
interface ApplicationComponent {
    fun mainVerticle(): MainVerticle

    @Named("dataInitializer") fun initializer(): () -> Unit
}