package org.diskursus.ext

import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import kotlin.reflect.KClass

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
fun logger(clz: KClass<*>): Logger {
    return LoggerFactory.getLogger(clz.qualifiedName)
}