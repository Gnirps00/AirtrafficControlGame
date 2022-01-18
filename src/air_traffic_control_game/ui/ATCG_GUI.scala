package air_traffic_control_game.ui

import air_traffic_control_game._
import scala.swing._
import scala.swing.event._
import javax.swing.UIManager
import javax.swing.ImageIcon
import java.net.URL
import scala.swing.Alignment._
import javax.imageio._
import java.io.File
import javax.swing.JOptionPane
import java.awt.event.ActionListener
import java.awt.Font

object ATCG_GUI extends SimpleSwingApplication {

  def top = new MainFrame {

    title = "Air Traffic controller"
    resizable = false
    
    // Asks user his/her username and stores it to this variable
    val userName = JOptionPane.showInputDialog("Put your name here")
    // Options for possible airports
    val airportOptions = Array("Helsinki-Vantaa", "Tokyo-Narita", "London-Heathrow", "Frankfurt am Main", 
        "Paris-Charles de Gaulle", "New York-JFK International Airport", "Beijing Capital Airport")
    // Asks user to choose the airport from options above
    val airportName = Dialog.showInput(null, "Choose the airport:", "Choosing the airport", Dialog.Message.Question, null, airportOptions, airportOptions.head)
    // Diffrent possible levels(difficulties)
    val levels = Array("Easy", "Normal", "Hard")
    // Asks user to choose the level from options above
    val level = Dialog.showInput(null, "Choose the level of the game:", "Choosing the level", Dialog.Message.Question, null, levels, levels.head)

    // Stores pictures matching chosen airport
    val (picture, runwayPic) = airportName match {
      case Some("Helsinki-Vantaa") => ("Helsinki-Vantaa.png", "Vantaa 500x500 for shun.png")
      case Some("Tokyo-Narita") => ("Tokyo-Narita.png", "Tokyo-Narita runway.png")
      case Some("London-Heathrow") => ("London-Heathrow.png", "London-Heathrow runway.png")
      case Some("Frankfurt am Main") => ("Frankfurt am Main.png", "Frankfurt am Main runway.jpg")
      case Some("Paris-Charles de Gaulle") => ("Paris-Charles de Gaulle.png", "Paris-Charles de Gaulle runway.png")
      case Some("New York-JFK International Airport") => ("New York-JFK International Airport.png", "New York-JFK International Airport runway.png")
      case Some("Beijing Capital Airport") => ("Beijing Capital Airport.png", "Beijing Capital Airport runway.jpg")
      case _ => ("", "")
    }

    val mapPic = ImageIO.read(new File(picture))
    val aircraftPic = ImageIO.read(new File("aircraft.png"))

    // Gets the width and height of map and stores them
    val (width, height) = (mapPic.getWidth, mapPic.getHeight)

    // Creates the game using variables created above
    val game = new Game(userName, airportName.get, level.get, width, height)
    
    // Font of textfield, where the commands will be typed in
    val font1 = new Font("SansSerif", Font.BOLD, 20)
    val input = new TextField("Type commands here", 50) {
      font = font1
      //    preferredSize = new Dimension(100, 100)
    }
    this.listenTo(input.keys)

    // The label where the map and aircrafts will be drawn
    val pic1 = new Label {
      icon = new ImageIcon(picture)

      override def paintComponent(g: Graphics2D) = {
        g.drawImage(mapPic, null, 0, 0)
        for (p <- game.field.aircrafts) {
          g.drawImage(aircraftPic, null, p.pos.x.toInt, p.pos.y.toInt)
        }
      }
      //    preferredSize = new Dimension(1000, 500)
    }
    
    // The picture of runways
    val pic2 = new Label {
      icon = new ImageIcon(runwayPic)
      //      preferredSize = new Dimension(100, 100)
    }
    
    // The area where the informations of aircrafts will be shown
    val aircraftInfo = new TextArea(50, 10) {
      editable = false
      wordWrap = true
      lineWrap = true
      //    preferredSize = new Dimension(500, 1000)
    }
    
    // The area where the information of typed command will be shown
    val commandInfo = new TextArea(50, 10) {
      editable = false
      wordWrap = true
      lineWrap = true
    }
    
    // Combines infos above
    val infos = new BoxPanel(Orientation.Vertical) {
      contents += aircraftInfo
      contents += commandInfo
    }
    val topFigure = new BoxPanel(Orientation.Horizontal) {
      contents += pic1
      contents += pic2
    }
    val bottomFigure = new BoxPanel(Orientation.Horizontal) {
      contents += input
      contents += infos
    }
    val everything = new BoxPanel(Orientation.Vertical) {
      contents += topFigure
      contents += bottomFigure
    }

    val wholeLayout = new BoxPanel(Orientation.Vertical)
    wholeLayout.contents += everything

    contents = wholeLayout

    // When the player types in the command this GUI will give the command to the class Command
    this.reactions += {
      case keyEvent: KeyPressed =>
        if (keyEvent.source == this.input && keyEvent.key == Key.Enter && !this.game.isOver) {
          val command = this.input.text.trim()
          if (command.nonEmpty) {
            val c = new Command(command)
            this.input.text = ""
            this.commandInfo.text = c.execute(game)
          }
        }
    }
    
    // The informations will be shown if the game is not over. If the game isn't over the method step in the class Game will be used.
    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) = {
        if (!game.isOver) {
          game.step()
          input.enabled = !game.isOver
          
          val rw = game.airport.toString()
          val ar = game.field.arrivals.map(_.toString()).mkString("\n")
          val dp = game.field.departures.map(_.toString()).mkString("\n")
          val gone = game.goneAircrafts.map(_.toString()).mkString("\n")
          val tx = rw + "\nArrivals:\n" + ar + "\nDepartures:\n" + dp + "\nGone:\n" + gone

          pic1.repaint()
          aircraftInfo.text = tx
        } else {
          val title = if (game.isCrash) "GAME OVER!!!" else "CLEAR!!!"
          val wholeText = title + "\n" + game.newScore.toString()
          commandInfo.text = wholeText
        }
      }
    }

    val timer = new javax.swing.Timer(1000, listener)

    timer.start()

  }

}