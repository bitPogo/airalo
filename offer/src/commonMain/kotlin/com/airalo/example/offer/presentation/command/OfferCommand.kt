package com.airalo.example.offer.presentation.command

import com.airalo.example.command.Command
import com.airalo.example.command.CommandExecutor
import com.airalo.example.command.CommandReceiver

/**
 * Marker interface for an offer command receivers.
 */
sealed interface OfferCommandReceiver

fun interface OfferCommandExecutor<T> : CommandExecutor<T> where T: OfferCommandReceiver, T: CommandReceiver {
    override operator fun invoke(command: Command<in T>)
}

