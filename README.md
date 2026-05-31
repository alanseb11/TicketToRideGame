# Ticket To Ride London – Sprint 3

**Author:** Alan Sebastian
**Student ID:** 33855137

## Project Overview

This project is a Java Swing implementation of **Ticket To Ride London** developed for Sprint 3.

The project extends the previous implementation through three gameplay extensions:

1. **Required Extension – Ferry Routes**
   Routes requiring locomotive cards with substitution rules.

2. **Pick-From-The-List Extension – Save / Load System**
   Allows players to save an active game and restore a previous game state.

3. **Self-Defined Extension – Landmark Routes**
   Special routes that trigger unique gameplay effects and player decisions.

---

## Sprint 3 Features

### Ferry Routes

Ferry routes are identified by locomotive symbols displayed on the board.

To claim a ferry route, players must provide:

* The required number of locomotive (`MULTI`) cards.
* The normal coloured cards needed for the route.

If locomotives are missing, they may be substituted using **3 cards of the same colour per missing locomotive**.

---

### Save / Load System

The game supports saving and loading gameplay state.

Saved information includes:

* Players
* Transport cards
* Destination tickets
* Scores
* Buses remaining
* Claimed routes
* Current turn information

Loaded games restore the previous gameplay state directly from file.

---

### Landmark Routes

Landmark routes are marked using star indicators on the map.

Implemented landmark effects:

* **STEAL_CARD** — steals a random transport card from the opponent.
* **POINTS_OR_CARDS** — allows the player to choose bonus points or additional transport cards.
* **DETECTIVE_CHOICE** — provides an additional interactive gameplay decision.

---

## Platform Requirements

Tested using:

* **Operating System:** Windows
* **IDE:** IntelliJ IDEA
* **Language:** Java
* **Java Version:** Java 17

---

## Running the Executable (.jar)

1. Download and extract the provided executable zip file.
2. Navigate to the executable folder.
3. Run the provided `.jar` file.

If double-clicking does not launch the program:

Open Command Prompt inside the executable directory and run:

```text
java -jar TicketToRideLondon.jar
```

Requirements:

* Java Runtime Environment (JRE) installed.
* Java 17 or later recommended.

---

## Running from Source Code

1. Open the project in **IntelliJ IDEA**.
2. Configure the project SDK to **Java 17**.
3. Open the main entry class:

```text
frontend.Main
```

4. Run the `main()` method.

---

## Creating the Executable from Source

Detailed instructions for creating the executable `.jar` file using IntelliJ are provided in the accompanying PDF:

**CreatingExecutableInstructionsAlanS.pdf**

This document contains:

* Artifact configuration
* JAR creation steps
* Main class selection
* Build artifact process
* Output directory setup

---

## Project Structure

```text
src/
 ├── backend/
 │    ├── City.java
 │    ├── Colour.java
 │    ├── Deck.java
 │    ├── DestinationTicket.java
 │    ├── DestinationTicketCardDeck.java
 │    ├── Game.java
 │    ├── GameSaveManager.java
 │    ├── LandmarkEffect.java
 │    ├── Player.java
 │    ├── Route.java
 │    ├── TransportationDeck.java
 │    └── TransportCard.java
 │
 ├── frontend/
 │    ├── ActionPanel.java
 │    ├── GameController.java
 │    ├── GameDialogService.java
 │    ├── GameFrame.java
 │    ├── Main.java
 │    ├── MapPanel.java
 │    ├── PlayerInfoPanel.java
 │    ├── RouteVisual.java
 │    └── StartScreen.java
 │
 └── resources/
      ├── locomotive_18x12.png
      └── star.png
```

---

## Additional Notes

* The application is implemented as a standalone Java Swing program.
* No external libraries or servers are required.
* Custom graphical assets are used for ferry and landmark visual indicators.
* Save files are generated locally through the save/load system.

---

## Repository Contents

This repository includes:

* Complete Sprint 3 source code
* Updated UML diagram
* Design rationale document
* Supplementary documentation
* Executable creation instructions PDF
* README documentation
# FIT3077 Repository