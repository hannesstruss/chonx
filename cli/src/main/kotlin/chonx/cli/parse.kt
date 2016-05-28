package chonx.cli

import chonx.core.Slot
import chonx.statemachine.Command
import chonx.statemachine.Phase

val LockRegex = """^lock (\d(, \d)*)""".toRegex()
val UnlockRegex = """^unlock (\d(, \d)*)""".toRegex()
val PickRegex = """pick (\w+)""".toRegex()

fun parse(phase: Phase, cmd: String): Command {
  if (phase is Phase.CollectPlayers) {
    if (cmd == "begin") {
      return Command.BeginGame()
    } else {
      return Command.AddPlayer(cmd)
    }
  } else if (phase is Phase.GamePhase) {
    LockRegex.find(cmd)?.let {
      val indices = it.groupValues[1].split(",").map { it.trim().toInt() - 1 }
      return Command.LockDice(indices)
    }

    UnlockRegex.find(cmd)?.let {
      val indices = it.groupValues[1].split(",").map { it.trim().toInt() - 1 }
      return Command.UnlockDice(indices)
    }

    PickRegex.find(cmd)?.let {
      val slotId = it.groupValues[1].trim()
      val slot = Slot.byId(slotId)
      if (slot == null) {
        throw ParseException("Can't parse slot ${slotId}")
      }
      return Command.PickSlot(slot)
    }

    return when (cmd) {
      "roll" -> Command.RollDice()
      "accept" -> Command.AcceptDice()
      else -> throw ParseException("Can't parse command ${cmd}")
    }
  }

  throw IllegalStateException("Can't parse command in phase: ${phase.javaClass}")
}

class ParseException(message: String) : IllegalArgumentException(message)
