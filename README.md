# 🐍 Snake and Ladders

A console-based **Snake and Ladders game implemented in Java** using Object-Oriented Programming principles.

The game supports configurable board dimensions, dice count, snakes, ladders, and multiple players. Game configuration is loaded from an `input.txt` file, making the game flexible without requiring changes to the source code.

## 🚀 Features
* File based input
* Configurable number of dice
* Configurable snakes
* Configurable ladders
* Multiple players
* Configurable board size
* Automatic winner detection
* Turn-based gameplay using a queue

---


# 📂 Project Structure

```text
Snake_And_Ladders/
│
├── .idea/
├── .mvn/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/
│   │           └── example/
│   │               ├── App.java
│   │               ├── Board.java
│   │               ├── Dice.java
│   │               ├── Player.java
│   │               ├── SnakesAndLaddersData.java
│   │               └── input.txt
│   │
│   └── test/
│       └── java/
│           └── org/
│               └── example/
│
├── pom.xml
└── README.md
```

The repository currently contains five core Java classes under `src/main/java/org/example`.

---

# 🏗️ Class Design

The application is divided into five main classes:

```text
                    ┌──────────────┐
                    │    App       │
                    │ Entry Point  │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │    Board     │
                    │ Game Engine  │
                    └───┬────┬─────┘
                        │    │
             ┌──────────┘    └──────────┐
             ▼                          ▼
       ┌───────────┐            ┌─────────────────────┐
       │   Dice    │            │ SnakesAndLaddersData│
       │ Dice Logic│            │                     │
       └───────────┘            └─────────────────────┘
             │
             │
             ▼
       ┌───────────┐
       │  Player   │
       │ Player    │
       │ State     │
       └───────────┘
```

---

# 1. `App.java`

### Responsibility

`App` is the **entry point of the application**.

It contains the `main()` method and is responsible for:

1. Reading the game configuration from `input.txt` file
2. Validating the input
3. Creating the snakes and ladders mapping
4. Reading player information
5. Creating the `Game`
6. Starting the game

The application starts from:

```java
public static void main(String[] args)
```

`App` reads the board dimensions, dice count, snakes, ladders, number of players, and player names before creating the `Game` object.

### Important responsibilities

```text
input.txt
    │
    ├── Board dimensions
    ├── Dice count
    ├── Snakes
    ├── Ladders
    ├── Number of players
    └── Player names
            │
            ▼
         App.java
            │
            ▼
         Board.java
            │
            ▼
        board.play()
```

### Why this class exists

`App` acts as the **composition/root layer** of the application.

It doesn't contain the actual game logic. Instead, it prepares the required objects and delegates gameplay to `Game`.

Input File Format:

Line 1: Board Dimensions

Line 2: Dice count

Line 3: No of Snakes (S)

Next S lines: Snakes Mappings( Head -> Tail) i.e Player will move from Snake head -> Snake Tail

Line 4: No of Ladders (L)

Next L lines: Snakes Mappings( bottom -> top) i.e Player will move from Ladder Bottom -> Ladder top

Line 5: No of Players (P)

Next P Lines: Player Names

---

# 2. `Board.java`

### Responsibility

`Game` is the **main game engine**.

It controls the actual gameplay and coordinates:

* Players
* Dice
* Snakes
* Ladders
* Player turns
* Player movement
* Winning condition

The class maintains:

```java
private Integer totalCells;
private Dice dice;
private SnakesAndLaddersData snakesAndLaddersData;
private Queue<Player> playersQueue;
```

The board size is calculated as:

```java
totalCells = boardDimensions * boardDimensions;
```

Players are inserted into a `LinkedList`-based queue to maintain turn order.

---

## Turn Management

The game uses a **Queue** to manage player turns.

```text
Player 1
   ↓
Player 2
   ↓
Player 3
   ↓
Player 1
   ↓
Player 2
   ↓
...
```

The current player is removed using:

```java
playersQueue.poll();
```

After completing the turn, the player is added back:

```java
playersQueue.offer(currentPlayer);
```

This provides a simple **FIFO-based turn management mechanism**.

---

## Player Movement

After rolling the dice:

```text
Current Position
       +
Dice Result
       =
New Position
```

The board then checks whether the new position contains a snake or ladder.

```text
Player
  │
  ▼
Roll Dice
  │
  ▼
Calculate New Position
  │
  ▼
Snake / Ladder?
  │
 ┌┴─────────┐
Yes         No
 │           │
 ▼           ▼
Teleport   Stay
 │
 ▼
Update Player
```

The implementation also repeatedly checks the destination, allowing chained snake/ladder transitions.

---

## Winning Condition

A player wins when their position becomes equal to the total number of cells:

```java
if(newCell == totalCells)
```

The player's position and winning status are then updated.

---

# 3. `Dice.java`

### Responsibility

`Dice` encapsulates all **dice-related logic**.

It maintains:

```java
private Integer diceCount;
private Random random;
```

The dice count is configurable when the object is created.

```java
Dice(Integer diceCount)
```

The main method is:

```java
Integer rollDice(Integer remainingCells)
```

---

## Single Dice Mode

When:

```java
diceCount == 1
```

the game follows special rules for rolling a `6`.

### Rules implemented

* A `6` allows another roll.
* Multiple consecutive `6`s can result in additional movement.
* Three consecutive `6`s cause the player's turn to be lost.
* If a roll exceeds the remaining cells, the movement is rejected.

The implementation explicitly handles the three-consecutive-6 rule.

Example:

```text
Player rolls 6
      ↓
Roll again

Player rolls 6
      ↓
Roll again

Player rolls 6
      ↓
Lose turn
```

---

## Random Number Generation

The dice uses:

```java
Random random = new Random();
```

and generates values between `1` and `6`:

```java
random.nextInt(6) + 1
```

---

# 4. `Player.java`

### Responsibility

`Player` represents a **player participating in the game**.

It maintains three pieces of information:

```java
private String name;
private Integer currentCell;
private boolean winStatus;
```

### Attributes

| Attribute     | Purpose                       |
| ------------- | ----------------------------- |
| `name`        | Player's name                 |
| `currentCell` | Current position on the board |
| `winStatus`   | Whether the player has won    |

The player starts at cell `1` and initially has a `false` winning status.

---

## Important Methods

### `getName()`

Returns the player's name.

```java
getName()
```

### `getCurrentCell()`

Returns the player's current position.

```java
getCurrentCell()
```

### `moveToCell()`

Updates the player's position.

```java
moveToCell(Integer newCell)
```

### `setWinStatus()`

Updates the player's winning state.

```java
setWinStatus(boolean winStatus)
```

### `isWinStatus()`

Checks whether the player has won.

---

## Why `Player` is a Separate Class

Instead of storing player information directly inside `Game`, the player state is encapsulated in its own object.

This follows the OOP principle of **encapsulation**.

```text
Player
 ├── name
 ├── currentCell
 └── winStatus
```

The `Game` is responsible for the game, while `Player` is responsible for maintaining player state.

---

# 5. `SnakesAndLaddersData.java`

### Responsibility

This class manages the **snake and ladder mapping**.

It stores:

```java
Map<Integer, Integer> snakesAndLaddersMap;
```

The map represents:

```text
Starting Cell → Destination Cell
```
Note: 
If starting cell > destination cell, it represents a snake.
else, it represents a ladder.

For example:

```text
62 → 5
33 → 6
49 → 9
```

means:

```text
62
 ↓
5
```

So if a player lands on `62`, they are moved to `5`.

Similarly:

```text
81 → 100
```

represents a ladder.

---

## `checkSnakeOrLadder()`

The main lookup method is:

```java
Integer checkSnakeOrLadder(Integer currCell)
```

It checks whether the current cell exists in the map.

If it does:

```text
current cell
     ↓
lookup in Map
     ↓
destination
```

If no snake or ladder exists, it returns:

```java
-1
```

This allows `Game` to determine whether the player needs to be moved again.

---

# 📄 `input.txt`

The game configuration is externalized into:

```text
src/main/java/org/example/input.txt
```

This allows the board and game configuration to be changed without modifying Java code.

The current input contains:

```text
10
1
9
62 5
33 6
49 9
88 16
41 20
56 53
98 64
93 73
95 75
8
2 37
27 46
10 32
51 68
61 79
65 84
71 91
81 100
2
Gaurav
Sagar
```

The current configuration therefore represents a **10 × 10 board**, one die, 9 snakes, 8 ladders, and 2 players.

---

# 📥 Input Format

The expected format is:

```text
BOARD_DIMENSION
DICE_COUNT

NUMBER_OF_SNAKES
SNAKE_HEAD SNAKE_TAIL
SNAKE_HEAD SNAKE_TAIL
...

NUMBER_OF_LADDERS
LADDER_BOTTOM LADDER_TOP
LADDER_BOTTOM LADDER_TOP
...

NUMBER_OF_PLAYERS
PLAYER_NAME
PLAYER_NAME
...
```

### Example

```text
10
1
9
62 5
33 6
49 9
88 16
41 20
56 53
98 64
93 73
95 75
8
2 37
27 46
10 32
51 68
61 79
65 84
71 91
81 100
2
Gaurav
Sagar
```

---

# 🎮 Game Flow

The overall execution flow is:

```text
                ┌───────────────┐
                │    App.java   │
                └───────┬───────┘
                        │
                        ▼
                 Read input.txt
                        │
                        ▼
              Create configuration
                        │
                        ▼
                ┌───────────────┐
                │   Board.java  │
                └───────┬───────┘
                        │
          ┌─────────────┼──────────────┐
          │             │              │
          ▼             ▼              ▼
       Player         Dice     SnakesAndLaddersData
          │             │              │
          └─────────────┼──────────────┘
                        │
                        ▼
                   Play Game
                        │
                        ▼
                  Roll Dice
                        │
                        ▼
                Move Player
                        │
                        ▼
             Snake / Ladder Check
                        │
                        ▼
                 Update Position
                        │
                        ▼
                  Winner Check
                        │
                  ┌─────┴─────┐
                  │           │
                 No          Yes
                  │           │
                  ▼           ▼
              Next Turn     Game End
```

---

# 🧠 Object-Oriented Design

The project demonstrates several important OOP concepts.

### 1. Encapsulation

Each class owns a specific piece of state and behavior.

```text
Player
→ Player state

Dice
→ Dice behavior

SnakesAndLaddersData
→ Board mapping

Board
→ Game behavior

App
→ Application initialization
```

### 2. Single Responsibility

Each class has a relatively focused responsibility:

| Class                  | Responsibility                      |
| ---------------------- | ----------------------------------- |
| `App`                  | Application startup & input parsing |
| `Game`                | Game orchestration                  |
| `Dice`                 | Dice rolling                        |
| `Player`               | Player state                        |
| `SnakesAndLaddersData` | Snake/ladder mapping                |

### 3. Composition

`Game` contains the objects it needs to run the game:

```java
private Dice dice;
private SnakesAndLaddersData snakesAndLaddersData;
private Queue<Player> playersQueue;
```

This is a good example of **composition** in an object-oriented design.

### 4. Queue-Based Turn Management

The `Queue<Player>` provides a clean way to implement round-robin player turns.

```java
playersQueue.poll();
...
playersQueue.offer(currentPlayer);
```

---

# ▶️ How to Run

## Prerequisites

Make sure you have:

* Java 21+
* Maven 3.x

The project is configured with Maven compiler release `21`.

## Clone the Repository

```bash
git clone https://github.com/ravikiranp04/Snake_And_Ladders.git
cd Snake_And_Ladders
```

## Build the Project

```bash
mvn clean install
```

## Run

Run the `App` class:

```text
src/main/java/org/example/App.java
```

The application reads:

```text
src/main/java/org/example/input.txt
```

and starts the game.

---

# 📊 Example Game Flow

Suppose a player is currently at:

```text
Cell = 56
```

and rolls:

```text
Dice = 6
```

The player moves to:

```text
56 + 6 = 62
```

The game then checks `SnakesAndLaddersData`.

Since:

```text
62 → 5
```

is configured as a snake:

```text
56
 ↓
62
 ↓
5
```

The player's final position becomes:

```text
5
```

The same mechanism works for ladders.

For example:

```text
81 → 100
```

results in:

```text
81
 ↓
100
```

and the player wins after reaching the final cell.

---

# 📚 Learning Objectives

This project is useful for understanding:

* Object-Oriented Programming
* Java Collections
* `Queue`
* `Map`
* Random number generation
* File handling
* Input validation
* Exception handling
* Logging
* Maven project structure
* Unit testing with JUnit
* Game-state management
* Composition between objects

---

# 👨‍💻 Author

**Ravi Kiran**

GitHub: [@ravikiranp04](https://github.com/ravikiranp04)

---
`

The project keeps the game engine separate from player state, dice behavior, and snake/ladder configuration, making it a useful example of designing a small Java application using object-oriented principles.
