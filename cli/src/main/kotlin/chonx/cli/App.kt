package chonx.cli

import chonx.core.IllegalMoveException
import chonx.core.SlotAlreadyFilledException
import chonx.statemachine.Command
import chonx.statemachine.Phase
import chonx.statemachine.StateMachine

fun main(args: Array<String>) {
  var state = StateMachine.new()

  state = state
      .handle(Command.AddPlayer("Hannes"))
      .handle(Command.AddPlayer("Dieter"))
      .handle(Command.BeginGame())

  while (true) {
    println("")
    val phase = state.phase
    if (phase is Phase.GamePhase) {
      println("${phase.javaClass.simpleName} - Current: ${phase.game.currentPlayer.name} (${phase.game.score(phase.game.currentPlayer)})")

      if (phase is Phase.MovePhase) {
        val msg = phase.moveInProgress.dice()
            .mapIndexed { index, die ->
              if (phase.moveInProgress.isLocked(index)) {
                "(${die})"
              } else {
                die.toString()
              }
            }.joinToString(" ")
        println(msg)
      }
    }
    print("> ")
    val input = readLine()

    if (input == null) {
      break
    }

    try {
      val cmd = parse(input.trim())
      state = state.handle(cmd)
    } catch (e: ParseException) {
      println(e.message)
      continue
    } catch (e: IllegalMoveException) {
      println(e.message)
      continue
    } catch (e: SlotAlreadyFilledException) {
      println(e.message)
      continue
    }
  }
}

