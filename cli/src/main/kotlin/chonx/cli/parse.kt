package chonx.cli

import chonx.core.Player
import chonx.core.Slot
import chonx.statemachine.Command

val AddPlayerRegex = """add (\w+)""".toRegex()
val LockRegex = """lock (\d(, \d)*)""".toRegex()
val UnlockRegex = """unlock (\d(, \d)*)""".toRegex()
val PickRegex = """pick (\w+)""".toRegex()

fun parse(cmd: String, playerName: String): Command {
  AddPlayerRegex.find(cmd)?.let {
    return Command.AddPlayer(it.groupValues[1])
  }

  UnlockRegex.find(cmd)?.let {
    val indices = it.groupValues[1].split(",").map { it.trim().toInt() - 1 }
    return Command.UnlockDice(indices)
  }

  LockRegex.find(cmd)?.let {
    val indices = it.groupValues[1].split(",").map { it.trim().toInt() - 1 }
    return Command.LockDice(indices)
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
    "addme" -> Command.AddPlayer(playerName)
    "begin" -> Command.BeginGame()
    "roll" -> Command.RollDice()
    "accept" -> Command.AcceptDice()
    else -> throw ParseException("Can't parse command ${cmd}")
  }
}

class ParseException(message: String) : IllegalArgumentException(message)
