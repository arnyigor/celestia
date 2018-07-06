package com.arny.celestiatools.models


class OptionalNull<M>(val value: M?) {
    fun get(): M? {
        return value
    }
}
