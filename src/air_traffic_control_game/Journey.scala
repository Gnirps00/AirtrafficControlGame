package air_traffic_control_game

/**
 * Represents a journey from origin airport to destination airport
 * @param order: order of the same kind of journey 
 * e.g. if the journey is first one which is from the specific origin to the specific destination, the order is 1
 * @param origin: where the journey starts
 * @param destination: where the journey ends
 */
class Journey(val order: Int, val origin: Airport, val destination: Airport) {
  private var succ: Option[Boolean] = None
  
  // If the journey is not completed returns None, if succeeded Some(true) and if failed Some(false)
  def succeeded: Option[Boolean] = this.succ
  
  def success() = this.succ = Some(true)
  
  def failure() = this.succ = Some(false)
  
  override def toString() = 
    this.order + ". flight from " + this.origin.toString() + " to " + this.destination.toString()
}