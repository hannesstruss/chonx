package chonx.cli

import chonx.core.IllegalMoveException
import chonx.core.SlotAlreadyFilledException
import chonx.statemachine.Phase
import chonx.statemachine.StateMachine

fun main(args: Array<String>) {
  var state = StateMachine.new()

  while (true) {
    println("")
    val phase = state.phase
    if (phase is Phase.CollectPlayers) {
      println("""Players: ${phase.preGame.players.map { it.name }.joinToString(", ")}""")
    } else if (phase is Phase.GamePhase) {
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
    } else if (phase is Phase.Ended) {
      println("Done!")
      System.exit(0)
    }

    print("> ")
    val input = readLine()

    if (input == null) {
      break
    }

    try {
      val cmd = parse(phase, input.trim())
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

