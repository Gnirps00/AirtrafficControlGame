package air_traffic_control_game

/**
 * The class where the gathered point is stored
 */
class Score(val player: Player, val level: String) {
  // Multiplayer of the point depending on the level
  val multiplayer: Int = {
    if(level == "Easy") 1 else if(level == "Normal") 2 else 5
  }
  private var point: Int = 0
  
  def points = this.point
  
  // This method is used when succeeded the take-off
  def succeededT() = this.point += 1
  
  // This method is used when succeeded the landing
  def succeededL() = this.point += this.multiplayer * 2
  
  // This method is used when an aircraft crashes
  def crashed() = this.point -= this.multiplayer * 10
  
  override def toString() = "Player " + this.player.name + "'s score:\nLevel: " + this.level + "\nPoints: " + this.points
}