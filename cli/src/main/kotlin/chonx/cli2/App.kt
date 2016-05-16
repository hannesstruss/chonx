package chonx.cli2

fun main(args: Array<String>) {
  var state = StateMachine.new()

  state = state
      .handle(Command.AddPlayer("Hannes"))
      .handle(Command.AddPlayer("Dieter"))
      .handle(Command.BeginGame())


}

