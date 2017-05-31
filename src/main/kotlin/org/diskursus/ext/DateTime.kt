package org.diskursus.ext

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
fun DateTime.toFullDateTimeString(): String {
    val formatter = DateTimeFormat.forPattern("dd MMMM yyyy HH:mm:ss (ZZZZ)")

    return this.toString(formatter)
}