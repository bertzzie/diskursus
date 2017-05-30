package org.diskursus.module

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import dagger.Module
import dagger.Provides
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
@Module
class VertxModule(val vertx: Vertx, val config: JsonObject) {
    init {
        Json.mapper.apply {
            registerKotlinModule()
            registerModule(ParameterNamesModule())
            registerModule(JavaTimeModule())
        }

        Json.prettyMapper.apply{
            registerKotlinModule()
            registerModule(ParameterNamesModule())
            registerModule(JavaTimeModule())
        }
    }

    @Provides
    fun provideRouter(): Router {
        return Router.router(vertx)
    }

    @Provides
    @Singleton
    fun provideVertx(): Vertx {
        return vertx
    }

    @Provides
    @Singleton
    fun provideObjectMapper(): ObjectMapper {
        return Json.mapper
    }
}