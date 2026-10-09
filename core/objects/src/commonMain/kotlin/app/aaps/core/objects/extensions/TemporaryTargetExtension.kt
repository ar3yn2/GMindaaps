package app.gmaaps.core.objects.extensions

import app.gmaaps.core.data.model.TT

fun TT.target(): Double =
    (this.lowTarget + this.highTarget) / 2

