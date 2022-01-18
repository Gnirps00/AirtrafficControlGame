package air_traffic_control_game

import scala.collection.mutable.Buffer
import scala.collection.mutable.Queue
import scala.util.Random

class Game(playerName: String, val airportName: String, val level: String, val width: Int, val height: Int) {
  /**
   * Create airports and their runways
   */
  
  private val helsinki = new Airport("Helsinki-Vantaa", width, height, "Helsinki-Vantaa" == airportName)
  helsinki.setRunway(new Runway("04L", 3060))
  helsinki.setRunway(new Runway("04R", 3440))
  helsinki.setRunway(new Runway("33", 2901))

  private val narita = new Airport("Tokyo-Narita", width, height, "Tokyo-Narita" == airportName)
  narita.setRunway(new Runway("A", 4000))
  narita.setRunway(new Runway("B", 2500))

  private val heathrow = new Airport("London-Heathrow", width, height, "London-Heathrow" == airportName)
  heathrow.setRunway(new Runway("North", 3902))
  heathrow.setRunway(new Runway("South", 3660))

  private val frankfurt = new Airport("Frankfurt am Main", width, height, "Frankfurt am Main" == airportName)
  frankfurt.setRunway(new Runway("North", 4000))
  frankfurt.setRunway(new Runway("South", 4000))
  frankfurt.setRunway(new Runway("Northwest", 2800))
  frankfurt.setRunway(new Runway("West", 4000))

  private val paris = new Airport("Paris-Charles de Gaulle", width, height, "Paris-Charles de Gaulle" == airportName)
  paris.setRunway(new Runway("09L", 2700))
  paris.setRunway(new Runway("09R", 4200))
  paris.setRunway(new Runway("08L", 4215))
  paris.setRunway(new Runway("08R", 2700))

  private val newYork = new Airport("New York-JFK International Airport", width, height, "New York-JFK International Airport" == airportName)
  newYork.setRunway(new Runway("04L", 3682))
  newYork.setRunway(new Runway("04R", 2560))
  newYork.setRunway(new Runway("13L", 3048))
  newYork.setRunway(new Runway("13R", 4423))

  private val beijing = new Airport("Beijing Capital Airport", width, height, "Beijing Capital Airport" == airportName)
  beijing.setRunway(new Runway("01", 3810))
  beijing.setRunway(new Runway("18L", 3810))
  beijing.setRunway(new Runway("18R", 3445))

  private val airports: Buffer[Airport] = Buffer()

  airports += helsinki
  airports += narita
  airports += heathrow
  airports += frankfurt
  airports += paris
  airports += newYork
  airports += beijing

  /**
   * Create airlines
   */

  private val finnair = Airline("Finnair", "AY")
  private val jal = Airline("Japan Airlines", "JL")
  private val ba = Airline("British Airways", "BA")
  private val lufthansa = Airline("Lufthansa", "LH")
  private val france = Airline("Air France", "AF")
  private val america = Airline("American Airlines", "AA")
  private val china = Airline("China Southern Airlines", "CZ")

  private val airlines = Buffer[Airline]()
  airlines += finnair
  airlines += jal
  airlines += ba
  airlines += lufthansa
  airlines += france
  airlines += america
  airlines += china

  /**
   * Create journeys
   */
  private var allJourneys = Vector[Journey]()

  for (i <- this.airports.indices; j <- this.airports.indices; if i != j) {
    allJourneys = allJourneys ++ Vector(new Journey(1, airports(i), airports(j)), new Journey(2, airports(i), airports(j)))
  }

  private val amount = level match {
    case "Easy"   => allJourneys.size / 2
    case "Normal" => allJourneys.size
    case "Hard"   => allJourneys.size
    case _        => 0
  }

  private val journeys = allJourneys.sortBy(_.order).take(amount)

  private val random = Random
  private val consumption = 20
  
  // The airport which is in the centre of the field (map)
  val airport: Airport = this.airports.find(_.name == airportName).get
  private val runways: Vector[Runway] = this.airport.runways
  private val player: Player = new Player(playerName)
  val newScore: Score = new Score(player, level)
  private val wind: Vector3D = {
    val (x, y) = level match {
      case "Easy"   => (0, 0)
      case "Normal" => (50, 50)
      case "Hard"   => (100, 150)
      case _        => (0, 0)
    }

    new Vector3D(x, y, 0)
  }
  // Counts turn
  private var count = 0
  // The frequency of coming new aircraft to the field
  private val freq = level match {
    case "Easy" => 25
    case "Normal" => 20
    case "Hard" => 15
  }
  // Field(map) where are the aircrafts
  val field = new Field(airport, wind, height / 2, width / 2)

  // Possible initial positions and speeds, and code for new aircraft
  private val poss = Vector[Vector3D](new Vector3D(0, 0, 5000), new Vector3D(0, height, 7000), new Vector3D(width, 0, 8000), new Vector3D(width, height, 10000))
  private val speeds = Vector[Vector3D](new Vector3D(random.nextDouble() * 10, random.nextDouble() * 10, 0), new Vector3D(random.nextDouble() * 10, random.nextDouble() * -10, 0), new Vector3D(random.nextDouble() * -10, random.nextDouble() * 10, 0), new Vector3D(random.nextDouble() * -10, random.nextDouble() * -10, 10000))
  private val codes = Vector[(Int, Int)]((300, 266), (330, 236))

  /**
   * Create aircrafts
   */
  private var allAircrafts = Buffer[Aircraft]()
  for (j <- journeys) {
    val isCentre = j.origin.name == this.airportName
    val index = random.nextInt(4)
    val index2 = random.nextInt(2)
    val toBeAdded = (j.origin, j.order) match {
      case (this.helsinki, 1) =>
        new Aircraft(finnair, 350, j, 10000, this.consumption, 400, if (isCentre) j.origin.pos else poss(index), speeds(index), !isCentre)
      case (this.narita, 1) =>
        new Aircraft(jal, 350, j, 10000, this.consumption, 400, if (isCentre) j.origin.pos else poss(index), speeds(index), !isCentre)
      case (this.heathrow, 1) =>
        new Aircraft(ba, 350, j, 10000, this.consumption, 400, if (isCentre) j.origin.pos else poss(index), speeds(index), !isCentre)
      case (this.frankfurt, 1) =>
        new Aircraft(lufthansa, 350, j, 10000, this.consumption, 400, if (isCentre) j.origin.pos else poss(index), speeds(index), !isCentre)
      case (this.paris, 1) =>
        new Aircraft(france, 350, j, 10000, this.consumption, 400, if (isCentre) j.origin.pos else poss(index), speeds(index), !isCentre)
      case (this.newYork, 1) =>
        new Aircraft(america, 350, j, 10000, this.consumption, 400, if (isCentre) j.origin.pos else poss(index), speeds(index), !isCentre)
      case (this.beijing, 1) =>
        new Aircraft(china, 350, j, 10000, this.consumption, 400, if (isCentre) j.origin.pos else poss(index), speeds(index), !isCentre)
      case (_, 2) =>
        new Aircraft(airlines(random.nextInt(7)), codes(index2)._1, j, 10000, this.consumption, codes(index2)._2, if (isCentre) j.origin.pos else poss(index), speeds(index), !isCentre)
      case (_, _) =>
        new Aircraft(airlines(random.nextInt(7)), codes(index2)._1, j, 10000, this.consumption, codes(index2)._2, if (isCentre) j.origin.pos else poss(index), speeds(index), !isCentre)
    }
    allAircrafts = allAircrafts += toBeAdded
  }
  
  // All the aircrafts appearing in the game
  private val aircrafts = allAircrafts.filter(a => a.destination == this.airport || a.journey.origin == this.airport)
  // Aircrafts that are out of the area(field)
  val goneAircrafts = Buffer[Aircraft]()
  var isCrash = false

  // Order the aircraft,which has given code to given runway's name, to land, if there exist ones
  def land(code: String, runwayName: String): String = {
    val plane = this.field.aircrafts.find(_.code.toLowerCase() == code)
    
    if(plane.isEmpty){
      "An aircraft " + code + " not found."
    } else if(!this.airport.runwayFound(runwayName)){
      "A runway " + runwayName + " not found."
    } else {
      this.newScore.succeededL()
      plane.get.land(runwayName)
    }
  }

  // Order the aircraft, which has given code from given runway's name, to take-off, if there exist ones
  def takeOff(code: String, runwayName: String): String = {
    val plane = this.field.aircrafts.find(_.code.toLowerCase() == code)
    
    if(plane.isEmpty){
      "An aircraft " + code + " not found."
    } else if(!this.airport.runwayFound(runwayName)){
      "A runway " + runwayName + " not found."
    } else {
      this.newScore.succeededT()
      plane.get.takeOff(runwayName)
    }
  }
  
  // Order the aircraft, which has given code to given runways name, to stand-by, if there exist ones
  def standBy(code: String, runwayName: String): String = {
    val plane = this.field.aircrafts.find(_.code.toLowerCase() == code)
    
    if(plane.isEmpty){
      "An aircraft " + code + " not found."
    } else {
      plane.get.standBy(runwayName)
    }
  }
  
  // Order the aircraft, which has given code to given runways name, to change its height, if there exist ones
  def changeHeight(code: String, height: Double): String = {
    val plane = this.field.aircrafts.find(_.code.toLowerCase() == code)
    
    if(plane.isEmpty){
      "An aircraft " + code + " not found."
    } else if(height < 0 || height > 15000){
      "Input height is not valid."
    } else {
      plane.get.changeHeight(height)
    }
  }
  
  // Order the aircraft, which has given code to given runways name, to change its angle and speed, if there exist ones
  def changeAandS(code: String, newAngle: Double, newSpeed: Double) = {
    val plane = this.field.aircrafts.find(_.code.toLowerCase() == code)
    
    if(plane.isEmpty){
      "An aircraft " + code + " not found."
    } else if(newAngle < 0 && newSpeed <= 0){
      "Input angle and speed are not valid."
    } else if(newAngle < 0 && newSpeed > 0){
      "Input angle is not valid."
    } else if(newAngle >= 0 && newSpeed <= 0){
      "Input speed is not valid."
    } else {
      plane.get.changeAandS(newAngle, newSpeed)
    }
  }
  
  private def crash() = this.newScore.crashed()

  // The game is over if there is any crashed aircraft or every landing and take-offs succeeded
  def isOver = isCrash || (count > 0 && field.aircrafts.isEmpty && aircrafts.isEmpty)

  // Moves all the aircrafts in the field and increase turn by one
  def step() = {
    if (count % freq == 0 && !aircrafts.isEmpty) {
      val added = aircrafts(random.nextInt(aircrafts.length))
      aircrafts -= added
      field.addAircraft(added)
    }
    this.field.aircrafts.foreach(_.move(wind))
    
    val gone = this.field.aircrafts.filterNot(a => a.isInside(width, height))
    val failedAndGone = gone.filterNot(_.journey.succeeded.exists(b => b))
    val arrived = this.field.aircrafts.filter(a => a.destination == this.airport && a.journey.succeeded.exists(b => b))
    goneAircrafts ++= gone
    goneAircrafts ++= arrived
    goneAircrafts.foreach(this.field.removeAircraft(_))
    failedAndGone.foreach(_.crash())
    if(!failedAndGone.forall(!_.isCrashed) || !field.aircrafts.forall(!_.isCrashed)){
      this.crash()
      this.isCrash = true
    }
    
    count += 1
  }

}