package org.diskursus

import io.vertx.config.ConfigRetriever
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.config.ConfigRetrieverOptions
import io.vertx.kotlin.config.ConfigStoreOptions
import org.diskursus.module.DaggerApplicationComponent
import org.diskursus.module.VertxModule

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class DiskursusApplication {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val vertx = Vertx.vertx()

            val configRetriever = configRetriever(vertx)
            configRetriever.getConfig({ config ->
                if (config.failed()) {
                    println("Server failed to start when loading config. Cause: ${config.cause()}")
                } else {
                    val configResult = config.result()
                    val app = DaggerApplicationComponent.builder()
                            .vertxModule(VertxModule(vertx, configResult))
                            .build()

                    vertx.deployVerticle(app.mainVerticle(), DeploymentOptions().apply {
                        this.config = configResult
                    })

                    println("Server successfully started...")
                }
            })

        }

        private fun configRetriever(vertx: Vertx): ConfigRetriever {
            val propsConfig = ConfigStoreOptions(
                    type = "file",
                    format = "properties",
                    config = json {
                        obj("path" to "application-properties.properties")
                    }
            )

            return ConfigRetriever.create(vertx, ConfigRetrieverOptions(
                    stores = listOf(propsConfig)
            ))
        }
    }
}

