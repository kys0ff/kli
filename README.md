## 👀 KLI — Kotlin Lightweight Interactive CLI DSL

**KLI** is a Kotlin library for easily building powerful, beautiful, and interactive command-line applications.
It features a DSL-driven structure, interactive mode, progress bars, colorful outputs, and much more.

---

## 📜 Table of Contents

- [Installation](#-installation)
- [Quick Start](#%EF%B8%8F-quick-start)
- [Features](#-features)
  - [Basic CLI Setup](#-basic-cli-setup)
  - [Adding Commands](#-adding-commands)
  - [Interactive Mode](#-interactive-mode)
  - [User Input Scenarios](#-user-input-scenarios)
  - [Progress Bar Support](#-progress-bar-support)
  - [Colorful Text Printing](#-colorful-text-printing)
- [Configuration Options](#%EF%B8%8F-configuration-options)
- [Available Progress Types](#-available-progress-types)
- [License](#-license)

---

## 🚀 Installation

**First, add the JitPack repository to your `settings.gradle.kts`:**

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

**Then, add the dependency to your `build.gradle.kts`:**

```kotlin
dependencies {
    implementation("com.github.kys0ff:kli:[latest_version]")
}
```

---

## ⚡️ Quick Start

```kotlin
import off.kys.kli.dsl.kli.kli
import off.kys.kli.io.AnsiColor
import off.kys.kli.io.readInputOrNull
import off.kys.kli.utils.KliScope

fun main(args: Array<String>) = kli(args) {
    configure {
        name = "MyCli"
        version = "0.1.1"
        description = "My first CLI app!"
    }

    greetCommand()
}

fun KliScope.greetCommand() = command("greet") {
    description = "Greets the user"

    // Register args and flags for better --help output
    argument("name", "The name to greet")
    flag("shout", "Shout the greeting")

    action { args ->
        val name = args.getParams().getOrNull(1)
            ?: readInputOrNull("Enter your name", AnsiColor.CYAN) ?: "stranger"

        val greeting = "Hello, $name"
        if (args.getFlag("shout"))
            println(greeting.uppercase())
        else println(greeting)
    }
}
```

Example run:

```
./your-app greet John
# ➔ prints Hello, John

./your-app greet John --shout
# ➔ prints HELLO, JOHN
```
If you skip the name:

```bash
./your-app greet
Enter your name: Alice
Hello, Alice
```

Check available commands:

```bash
./your-app --help
```

---

## 🌟 Features

### 🔹 Basic CLI Setup

```kotlin
configure {
    name = "CoolTool"
    version = "2.5.0"
    description = "Does cool things!"
}
```

---

### 🔹 Adding Commands

```kotlin
command("sum") {
    description = "Adds two numbers."

    // Register args
    argument("x", "First number")
    argument("y", "Second number")

    action { cliArgs ->
        val x = cliArgs["x"]?.toIntOrNull() ?: 0
        val y = cliArgs["y"]?.toIntOrNull() ?: 0
        println("Sum: ${x + y}")
    }
}
```

---

### 🔹 Interactive Mode

When no arguments are passed, CLI enters interactive mode automatically. Missing arguments can be interactively requested:

```kotlin
import off.kys.kli.io.readInput

command("multiply") {
    description = "Multiplies two numbers interactively if missing."

    action { cliArgs ->
        val x = cliArgs["num1"]?.toIntOrNull()
            ?: readInput("Enter first number: ").toIntOrNull() ?: 1

        val y = cliArgs["num2"]?.toIntOrNull()
            ?: readInput("Enter second number: ").toIntOrNull() ?: 1

        println("Result: ${x * y}")
    }
}
```

Example session:

```
Welcome to CoolTool interactive mode!
Type 'help' for available commands, 'exit' to quit
CoolTool> multiply
Enter first number: 6
Enter second number: 7
Result: 42
CoolTool> exit
Goodbye!
```

---

### 🔹 User Input Scenarios

#### Password Input

```kotlin
command("setPassword") {
    description = "Set a secure password."

    action { 
        val password = readPassword("Enter your password")
        if (password.isNullOrBlank()) {
            println("Password not set. Operation cancelled.")
        } else {
            println("Your password has been set!")
            // You can store or hash the password here
        }
    }
}
```

Example run:

```pgsql
./your-app setPassword
Enter your password: ••••••
Your password has been set!
```

#### Confirmation Prompt

```kotlin
command("delete") {
    description = "Delete a file after confirmation."

    argument("file", "The file path to delete")
    flag("force", "Skip confirmation")

    action { args ->
        val filePath = args.getParams().getOrNull(1)
            ?: readInputOrNull("Enter file path to delete") ?: return@action

        val shouldDelete = args.getFlag("force")
            || confirm("Are you sure you want to delete '$filePath'?")

        if (shouldDelete) {
            println("File '$filePath' deleted!") // Replace with actual file delete logic
        } else {
            println("Operation cancelled.")
        }
    }
}
```

#### Select from Options

```kotlin
command("chooseColor") {
    description = "Choose a color from the list."

    argument("color", "Color name (Red, Green, Blue, Yellow)")

    action { args ->
        val colors = listOf("Red", "Green", "Blue", "Yellow")
        val inputColor = args["color"] ?: select(colors)
        println("You selected: $inputColor")
    }
}
```

#### Select Menu with Title

```kotlin
command("chooseFruit") {
    description = "Select your favorite fruit from the menu."

    argument("fruit", "Fruit name (Apple, Banana, Cherry, Date)")

    action { args ->
        val fruits = listOf("Apple", "Banana", "Cherry", "Date")
        val selectedFruit = args["fruit"] ?: selectMenu("Pick your favorite fruit:", fruits)
        println("You chose: $selectedFruit!")
    }
}
```

Example session:

```
CoolTool> chooseFruit
Pick your favorite fruit:
1) Apple
2) Banana
3) Cherry
4) Date
Enter choice (1-4)
CoolTool> 2
You chose: Banana!
CoolTool> exit
Goodbye!
```

---

### 🔹 Progress Bar Support

```kotlin
import off.kys.kli.dsl.progress.progressBar
import off.kys.kli.ui.progress.util.ProgressType
import off.kys.kli.ui.progress.util.SpinnerStyle

progressBar {
    type = ProgressType.SPINNER
    spinnerStyle = SpinnerStyle.PIPE
    width = 40
    prefix = "Working"
    suffix = "%"
    start {
        val total = 100
        for (i in 1..total) {
            update(i)
            Thread.sleep(20)
        }
    }
}
```

---

### 🔹 Colorful Text Printing

```kotlin
import off.kys.kli.utils.extensions.println
import off.kys.kli.utils.extensions.State

println("Success!", State.SUCCESS)
println("Warning!", State.WARNING)
println("An error occurred!", State.ERROR)

import off.kys.kli.io.AnsiColor

println(AnsiColor.colorize("Important!", AnsiColor.BRIGHT_RED))
println("Important!", AnsiColor.RED)
```

---

## ⚙️ Configuration Options

| Property                    | Default                  | Description                                          |
|------------------------------|---------------------------|------------------------------------------------------|
| name                         | null                      | CLI app name shown in interactive mode and help      |
| version                      | null                      | CLI app version                                      |
| description                  | null                      | CLI app description                                 |
| showInteractiveModeMessage   | true                      | Show welcome message in interactive mode             |
| showGoodbyeMessage           | true                      | Show goodbye message when exiting                   |
| showUsageOnError             | true                      | Show help if wrong command used                     |
| showPrompt                   | true                      | Show CLI prompt in interactive mode                 |
| promptColor                  | AnsiColor.BRIGHT_CYAN     | Prompt text color                                   |
| greetingColor                | AnsiColor.BRIGHT_GREEN    | Greeting text color                                 |
| goodbyeColor                 | AnsiColor.BRIGHT_GREEN    | Goodbye text color                                  |

---

## 🧹 Available Progress Types

| ProgressType | Description                           |
|--------------|---------------------------------------|
| BAR          | Standard filled progress bar (=====)  |
| SPINNER      | Animated spinner (configurable style) |
| DOTS         | Growing dots animation (...)          |
| BLOCKS       | Progress bar using blocks (▓░)         |
| PULSE        | Simple left/right pulse arrows (← →)   |

---

## 📄 License

This project is licensed under the MIT License.
Feel free to use it in your personal and commercial projects!
