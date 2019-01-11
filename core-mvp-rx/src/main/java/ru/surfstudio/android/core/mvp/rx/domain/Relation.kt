package ru.surfstudio.android.core.mvp.rx.domain

import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface Relation<V, in S : Source, in T : Target> {

    fun getSourceConsumer(source: S): Consumer<V>
    fun getSourceObservable(source: S): Observable<V>

    fun getTargetConsumer(target: T): Consumer<V>
    fun getTargetObservable(target: T): Observable<V>
}

interface Related<S : Source, T : Target> {

    fun source(): S
    fun target(): T

    fun <V> Relation<V, S, T>.getObservable() =
        this.getSourceObservable(source())

     fun <V> Relation<V, S, T>.getConsumer() =
        this.getTargetConsumer(target())
}

interface RelationEntity

interface Source : RelationEntity
interface Target : RelationEntity

interface ActionRelationEntity : RelationEntity
interface ActionSource : Source, ActionRelationEntity
interface ActionTarget : Target, ActionRelationEntity

interface CommandRelationEntity : RelationEntity
interface CommandSource : Source, CommandRelationEntity
interface CommandTarget : Target, CommandRelationEntity

interface StateRelationEntity : RelationEntity
interface StateSource : Source, StateRelationEntity
interface StateTarget : Target, StateRelationEntity

object VIEW : ActionSource, CommandTarget, StateSource, StateTarget
object PRESENTER : ActionTarget, CommandSource, StateSource, StateTarget
