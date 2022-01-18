package air_traffic_control_game

/**
 * Represents a runway
 * @param name of the runway
 * @param length of the runway
 */
class Runway(val name: String, val length: Int) {
  private var using: Boolean = false
  
  // Shows whether this runway is using currently
  def isUsing: Boolean = this.using
  
  def use() = this.using = true
  
  def remove() = this.using = false
  
  def ==(other: Runway): Boolean = {
    this.name == other.name && this.length == other.length
  }
  
  override def toString() = this.name + ": " + (if(isUsing) "is used." else "not used.")
  
}