package org.diskursus.ext

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.ext.sync.Sync


/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
inline fun <T: Any> sync(crossinline operation: (Handler<AsyncResult<T>>) -> Unit): T {
    return Sync.awaitResult{h -> operation(h)}
}