# Ticket to Ride London

**Author:** Alan Sebastian  
**Language:** Java  
**UI Framework:** Java Swing  
**Java Version:** Java 17  

## Project Overview

This project is a desktop implementation of **Ticket to Ride London**, developed in Java using Swing.

The application allows two players to compete by collecting transport cards, claiming routes between London landmarks, completing destination tickets, and earning points.

The project was developed over multiple sprints, beginning with the core gameplay system and progressively adding graphical improvements, save/load functionality, ferry routes, landmark routes, testing, documentation, and software design refinements.

The final version demonstrates object-oriented programming, event-driven GUI development, modular software architecture, persistence, automated testing, and iterative development using Agile and Scrum practices.

---

## Core Gameplay

The game supports the main Ticket to Ride gameplay flow:

1. Players enter their names and select their colours.
2. Each player receives transport cards and destination tickets.
3. Players take turns performing actions.
4. Players may:
   - Draw transport cards
   - Draw destination tickets
   - Claim routes
   - Save the current game
   - Load a previously saved game
5. Players earn points by claiming routes and completing destination tickets.
6. The game tracks player scores, cards, routes, buses, and turn information.

---

## Main Features

### Player Setup

The game begins with a start screen where two players can:

- Enter their names
- Select player colours
- Start a new game
- Load a saved game

Player information is displayed throughout the game, including:

- Current score
- Remaining buses
- Transport cards
- Destination tickets
- Current player turn

---

### Transport Card System

Players collect transport cards used to claim routes.

The game includes:

- Multiple card colours
- Locomotive (`MULTI`) cards
- Card drawing from the transport deck
- Card validation when claiming routes
- Support for locomotive substitution rules

---

### Destination Tickets

Destination tickets provide players with objectives requiring them to connect two London locations.

The system supports:

- Initial destination ticket selection
- Drawing additional destination tickets
- Storing tickets per player
- Restoring tickets when loading a saved game
- Tracking ticket ownership during gameplay

---

### Route Claiming

Players can select and claim routes displayed on the London map.

Route claiming includes:

- Route colour requirements
- Route length requirements
- Player card validation
- Bus availability validation
- Score calculation
- Visual route ownership updates
- Prevention of invalid or duplicate claims

Once claimed, a route is visually updated using the claiming player's colour.

---

### Interactive London Map

The game board is rendered using Java Swing.

The map includes:

- London landmarks and locations
- Routes between cities
- Multi-segment route graphics
- Route highlighting
- Player ownership colours
- Locomotive indicators for ferry routes
- Star indicators for landmark routes

The map communicates with the game controller to handle route selection and route claiming.

---

## Extended Gameplay Features

### Ferry Routes

Ferry routes require players to use locomotive cards when claiming certain routes.

Each ferry route specifies a required number of locomotive cards.

To claim a ferry route, the player must provide:

- The required locomotive cards
- The remaining coloured cards needed for the route

If a player does not have enough locomotives, the implementation supports substitution using:

- Three cards of the same colour for each missing locomotive

The route validates whether the player has a valid card combination before allowing the claim.

Ferry routes are identified visually using locomotive icons.

---

### Save and Load System

The application includes a file-based save and load system.

A saved game stores:

- Player names
- Player colours
- Player scores
- Remaining buses
- Transport card counts
- Destination tickets
- Claimed routes
- Route ownership
- Current player turn

When a game is loaded, the application restores the complete gameplay state.

Loaded games skip the normal new-game setup and continue from the saved position.

Save and load functionality is managed by the `GameSaveManager` class.

---

### Landmark Routes

Landmark routes are special routes that trigger additional gameplay effects when claimed.

Landmark routes are displayed using star icons on the map.

The system uses the `LandmarkEffect` enum to represent different effects.

Implemented effects include:

#### `STEAL_CARD`

Allows the current player to steal a transport card from the opposing player.

#### `POINTS_OR_CARDS`

Allows the player to choose between:

- Receiving bonus points
- Drawing additional transport cards

#### `EXTRA_DRAW`

Provides the player with an additional card draw.

Landmark effects are handled after a valid route claim.

---

## Software Architecture

The project follows a modular object-oriented structure separated into backend and frontend packages.

### Backend

The backend contains the game model and gameplay rules.

Responsibilities include:

- Player state
- Cards and decks
- Destination tickets
- Routes
- Scores
- Route validation
- Ferry route logic
- Landmark effects
- Save and load functionality

### Frontend

The frontend contains the Java Swing interface and user interaction logic.

Responsibilities include:

- Start screen
- Main game window
- Map rendering
- Action buttons
- Player information panels
- Dialogs
- Route selection
- Status and error messages

---

## Design Patterns and Principles

The project applies several software engineering concepts.

### Object-Oriented Programming

The implementation uses:

- Encapsulation
- Inheritance where appropriate
- Polymorphism
- Interfaces
- Composition
- Enumerations

### SOLID Principles

The design was improved to separate responsibilities between classes.

Examples include:

- `GameSaveManager` handles game persistence
- `GameDialogService` handles user dialogs
- `MapPanel` handles map rendering
- `Route` handles route-specific validation
- `GameController` coordinates gameplay and UI updates

### Separation of Concerns

The game separates:

- Business logic
- User interface logic
- Persistence
- Visual rendering
- Dialog handling

### Enum-Based Design

The `LandmarkEffect` enum replaces string-based effect values, improving type safety and maintainability.

---

## Key Classes

### `GameController`

Coordinates gameplay actions and communication between the user interface and backend model.

Responsibilities include:

- Drawing cards
- Claiming routes
- Ending turns
- Activating landmark effects
- Updating player information
- Updating status messages
- Coordinating save/load operations

### `Player`

Stores player-specific game state, including:

- Name
- Colour
- Score
- Remaining buses
- Transport cards
- Destination tickets
- Claimed routes

### `Route`

Represents a connection between two London locations.

A route stores:

- Start city
- End city
- Length
- Colour
- Claimed state
- Owning player
- Ferry requirements
- Landmark effect

The class also performs route validation.

### `MapPanel`

Draws the London map and route visuals.

Responsibilities include:

- Rendering routes
- Displaying route ownership
- Handling route selection
- Drawing ferry locomotive icons
- Drawing landmark star icons
- Locating routes between cities

### `GameSaveManager`

Serialises and restores game state using local save files.

### `GameDialogService`

Handles gameplay dialogs, including:

- Destination ticket selection
- Error messages
- Landmark choices
- Gameplay notifications

### `RouteVisual`

Stores the visual representation of a route, including its route object and graphical segments.

---

## Project Development

The project was developed across three sprints.

### Sprint 1 — Core Game Foundation

Sprint 1 focused on building the base game architecture.

Major work included:

- Core backend classes
- Player model
- Transport cards and decks
- Destination tickets
- Route representation
- Initial game controller
- Basic Java Swing interface
- London map layout
- Initial route claiming
- Player setup
- Turn management

---

### Sprint 2 — Gameplay and Interface Improvements

Sprint 2 expanded the core implementation.

Major work included:

- Improved route selection
- Player information panels
- Action controls
- Destination ticket interactions
- Improved map rendering
- Status messages
- Route ownership display
- Backend testing
- Updated class design
- UML documentation
- Improved communication between frontend and backend

---

### Sprint 3 — Extensions and Refactoring

Sprint 3 introduced additional gameplay extensions and software design improvements.

Major additions included:

- Ferry routes
- Save/load system
- Landmark routes
- Locomotive route indicators
- Landmark star indicators
- Game state restoration
- Destination ticket persistence
- Claimed route persistence
- Executable JAR creation
- Refactoring of controller responsibilities
- `GameDialogService`
- `GameSaveManager`
- `LandmarkEffect` enum
- Updated UML diagrams
- Design rationale
- Anti-pattern analysis and refactoring

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
