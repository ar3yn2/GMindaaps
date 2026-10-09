package app.gmaaps.plugins.automation.elements

import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil

class InputDateTime(private val rh: TextResolver, private val dateUtil: DateUtil, var value: Long = dateUtil.now())

