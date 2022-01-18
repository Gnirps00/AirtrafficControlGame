package air_traffic_control_game

class Command(input: String) {
  
  private val commandText = input.trim().toLowerCase()
  private val verb = commandText.takeWhile(_ != ' ')
  private val modifiers = commandText.drop(verb.length()).trim()
  private val code = modifiers.takeWhile(_ != ' ')
  private val others = modifiers.drop(code.length()).trim()
  private val RHA = others.takeWhile(_ != '_')
  private val speed = others.drop(RHA.length()).trim()
  
  // Calls methods from class game, depending on input
  def execute(game: Game) = this.verb match {
    case "land" => game.land(code, RHA)
    case "take-off" => game.takeOff(code, RHA)
    case "stand-by" => game.standBy(code, RHA)
    case "changeheight" => 
      val height = if(RHA.forall(_.isDigit)) RHA.toDouble else -1
      game.changeHeight(code, height)
    case "changeaands" => 
      val newAngle = if(RHA.forall(_.isDigit)) RHA.toDouble else -1
      val newSpeed = if(speed.forall(_.isDigit)) speed.toDouble else -1
      game.changeAandS(code, newAngle, newSpeed)
    case other => "Unknown command: " + other
  }
  
  override def toString = this.verb + " (modifiers: " + this.modifiers + ")"
  
  
}