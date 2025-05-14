package com.airalo.example.command

/**
 * Marker interface for command receivers.
 */
sealed interface CommandReceiver

/**
* Contract which defines a command that can be executed by a [CommandReceiver].
*/
fun interface Command<T : CommandReceiver> {
    /**
     * Executes a command on a given receiver.
     *
     * @param receiver A [CommandReceiver] on which to execute a command.
     */
    operator fun invoke(receiver: T)
}

/**
 * Contract for a command executor.
 * @param T The type of [CommandReceiver] that the command operates on.
 */
fun interface CommandExecutor<T: CommandReceiver> {
    /**
     * Executes a given command.
     *
     * @param command A [Command] to executed.
     */
    operator fun invoke(command: Command<in T>)
}

