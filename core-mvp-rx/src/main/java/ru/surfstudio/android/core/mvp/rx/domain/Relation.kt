package ru.surfstudio.android.core.mvp.rx.domain

import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface Relation<V, in S : RelationEntity, in T : RelationEntity> {

    fun getSourceConsumer(source: S): Consumer<V>
    fun getSourceObservable(source: S): Observable<V>

    fun getTargetConsumer(target: T): Consumer<V>
    fun getTargetObservable(target: T): Observable<V>
}

interface Related<S> where S: RelationEntity {

    fun relationEntity(): S

    fun <V> Relation<V, *, S>.getObservable() =
        this.getTargetObservable(relationEntity())

    fun <V> Relation<V, S, *>.getConsumer() =
            this.getSourceConsumer(relationEntity())
}

interface RelationEntity

interface ActionSource : RelationEntity
interface ActionTarget : RelationEntity

interface CommandSource : RelationEntity
interface CommandTarget : RelationEntity

interface StateSource : RelationEntity
interface StateTarget : RelationEntity

object VIEW : ActionSource, CommandTarget, StateSource, StateTarget
object PRESENTER : ActionTarget, CommandSource, StateSource, StateTarget
