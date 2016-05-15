package chonx.cli

import chonx.cli.phases.Phase
import chonx.cli.phases.PhaseResult
import chonx.cli.phases.PhaseResult.NextPhase
import chonx.cli.phases.PhaseResult.Quit
import chonx.cli.phases.StartupPhase

class CliGame {
  var phase: Phase = StartupPhase()

  fun run() {
    mainloop@ while (true) {
      val preResult = phase.preCmd()

      when (handle(preResult)) {
        LoopCommand.Continue -> continue@mainloop
        LoopCommand.Break -> break@mainloop
      }

      print("> ")
      val cmd = readLine()

      if (cmd == null || cmd == "quit") {
        printExit()
        break
      }

      when(handle(phase.handleCmd(cmd))) {
        LoopCommand.Break -> break@mainloop
      }
    }
  }

  private fun handle(preResult: PhaseResult): LoopCommand = when (preResult) {
    is NextPhase -> {
      phase = preResult.phase
      LoopCommand.Continue
    }
    is Quit -> {
      printExit()
      LoopCommand.Break
    }
    else -> LoopCommand.None
  }

  private fun printExit() {
    println("")
    println("Doswidanya!")
    println("C H O N X (c) 2016 Hannes Struss")
  }

  enum class LoopCommand {
    None, Continue, Break
  }
}
