🤖 KLI — Kotlin Lightweight Interactive CLI DSL
===============================================

**KLI** is a Kotlin library to build powerful, beautiful, and interactive command-line applications easily.  
It features a DSL-driven structure, interactive mode, progress bars, colorful outputs, and much more.

* * *

📜 Table of Contents
--------------------

*   [Installation](#installation)
*   [Quick Start](#quick-start)
*   [Features](#features)

*   [Basic CLI Setup](#basic-cli-setup)
*   [Adding Commands](#adding-commands)
*   [Interactive Mode](#interactive-mode)
*   [Progress Bar Support](#progress-bar-support)
*   [Colorful Text Printing](#colorful-text-printing)

*   [Configuration](#configuration-options)
*   [Available Progress Types](#available-progress-types)
*   [License](#license)

* * *

[🚀 Installation](#installation)
---------------

**Add to your `libs.versions.toml`:**

```toml
[libraries]
kli = { group = "off.kys", name = "kli", version = "1.0.0" }
```
And in your `build.gradle.kts`:
```kotlin
dependencies {
  implementation(libs.kli)
}
```
        

* * *

⚡ Quick Start
-------------

```kotlin
    import off.kys.kli.dsl.kli
    
    fun main(args: Array) = kli(args) {
        configure {
            name = "MyCli"
            version = "0.1.0"
            description = "My first CLI app!"
        }
    
        command("greet") {
            description = "Greets a user."
    
            action { cliArgs ->
                val name = cliArgs.getParams().getOrNull(1) ?: "World"
                println("Hello, $name!")
            }
        }
    }
```

Example run:

```
    ./your-app greet John
    # ➔ prints Hello, John!
``` 

* * *

🌟 Features
-----------

### 🔹 Basic CLI Setup

```kotlin
    configure {
        name = "CoolTool"
        version = "2.5.0"
        description = "Does cool things!"
    }
```

* * *

### 🔹 Adding Commands

```kotlin
    command("sum") {
        description = "Adds two numbers."
    
        action { cliArgs ->
            val x = cliArgs.getParams().getOrNull(1)?.toIntOrNull() ?: 0
            val y = cliArgs.getParams().getOrNull(2)?.toIntOrNull() ?: 0
            println("Sum: ${x + y}")
        }
    }
```

* * *

### 🔹 Interactive Mode

When no arguments are passed, CLI enters interactive mode automatically:  
You can enhance interactive commands to prompt the user when missing arguments:

```kotlin
    import off.kys.kli.io.InputReader
    
    command("multiply") {
        description = "Multiplies two numbers interactively if missing."
    
        action { cliArgs ->
            val x = cliArgs["num1"]?.toIntOrNull()
                ?: InputReader.readInput("Enter first number: ").toIntOrNull()
                ?: 1
    
            val y = cliArgs["num2"]?.toIntOrNull()
                ?: InputReader.readInput("Enter second number: ").toIntOrNull()
                ?: 1
    
            println("Result: ${x * y}")
        }
    }
```

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

* * *

### 🔹 User Input Scenarios

Here are interactive input scenarios using `InputReader`:

#### **Password Input**

```kotlin
    command("setPassword") {
        description = "Set a secure password."
    
        action {
            val password = InputReader.readPassword("Enter your password:")
            println("Your password has been set!")
        }
    }
```

#### **Confirmation Prompt**

```kotlin
    command("delete") {
        description = "Delete a file after confirmation."
    
        action {
            if (InputReader.confirm("Are you sure you want to delete this file?")) {
                println("File deleted!")
            } else {
                println("Operation cancelled.")
            }
        }
    }
```

#### **Select from Options (Simple)**

```kotlin
    command("chooseColor") {
        description = "Choose a color from the list."
    
        action {
            val colors = listOf("Red", "Green", "Blue", "Yellow")
            val selectedColor = InputReader.select(colors)
            println("You selected: $selectedColor")
        }
    }
```

#### **Select Menu with Title**

    
    command("chooseFruit") {
        description = "Select your favorite fruit from the menu."
    
        action {
            val fruits = listOf("Apple", "Banana", "Cherry", "Date")
            val selectedFruit = InputReader.selectMenu("Pick your favorite fruit:", fruits)
            println("You chose: $selectedFruit!")
        }
    }
        

**Example interactive session for selecting fruit:**

```
    Welcome to CoolTool interactive mode!
    Type 'help' for available commands, 'exit' to quit
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

* * *

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

* * *

### 🔹 Colorful Text Printing

```kotlin
    import off.kys.kli.utils.extensions.println
    import off.kys.kli.utils.extensions.State
    
    println("Success!", State.SUCCESS)
    println("Warning!", State.WARNING)
    println("An error occurred!", State.ERROR)
        

    
    import off.kys.kli.io.AnsiColor
    
    println(AnsiColor.colorize("Important!", AnsiColor.BRIGHT_RED))
```

* * *

⚙️ Configuration Options
------------------------

| Property                  | Default                  | Description                                          |
|----------------------------|---------------------------|------------------------------------------------------|
| name                     | null                   | CLI app name shown in interactive mode and help      |
| version                  | null                   | CLI app version                                      |
| description              | null                   | CLI app description                                 |
| showInteractiveModeMessage | true                 | Show welcome message in interactive mode             |
| showGoodbyeMessage       | true                   | Show goodbye message when exiting                   |
| showUsageOnError         | true                   | Show help if wrong command used                     |
| showPrompt               | true                   | Show CLI prompt in interactive mode                 |
| promptColor              | AnsiColor.BRIGHT_CYAN   | Prompt text color                                   |
| greetingColor            | AnsiColor.BRIGHT_GREEN  | Greeting text color                                 |
| goodbyeColor             | AnsiColor.BRIGHT_GREEN  | Goodbye text color                                  |


* * *

🧩 Available Progress Types
---------------------------

| ProgressType | Description                         |
|--------------|-------------------------------------|
| BAR        | Standard filled progress bar (=====) |
| SPINNER    | Animated spinner (configurable style) |
| DOTS       | Growing dots animation (...)       |
| BLOCKS     | Progress bar using blocks (▓░)     |
| PULSE      | Simple left/right pulse arrows (← →) |

* * *

📄 License
----------

This project is licensed under the MIT License.  
Feel free to use it in your personal and commercial projects!
