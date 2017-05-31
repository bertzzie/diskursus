package org.diskursus.module

import dagger.Component
import org.diskursus.verticle.MainVerticle
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
}