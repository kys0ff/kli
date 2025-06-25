# 👀 KLI — Kotlin Lightweight Interactive CLI DSL

**KLI** is a developer-friendly Kotlin library for building powerful, beautiful, and interactive command-line applications with minimal effort. It features a DSL-driven structure, interactive mode, progress bars, colorful outputs, and much more.

---

## 📜 Table of Contents

- [Installation](#installation)
- [Quick Start](#quick-start)
- [Features](#features)
- [Configuration Options](#configuration-options)
- [Feature Table](#feature-table)
- [Global Options Table](#global-options-table)
- [Extending and Customization](#extending-and-customization)
- [License](#license)

---

## 🚀 Installation

Add KLI to your Kotlin project using Gradle:
```kotlin
dependencies {
    implementation("off.kys:kli:<latest-version>")
}
```
Or with Maven:
```xml
<dependency>
    <groupId>off.kys.kli</groupId>
    <artifactId>kli</artifactId>
    <version>latest-version</version>
</dependency>
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
        version = "0.1.3"
        description = "My first CLI app!"
    }

    greetCommand()
}

fun KliScope.greetCommand() = command("greet") {
    description = "Greets the user"
    argument("name", "The name to greet")
    flag("shout", "Shout the greeting")

    action {
        val name = param() ?: readInputOrNull("Enter your name", AnsiColor.CYAN) ?: "stranger"
        val greeting = "Hello, $name"
        if (getFlag("shout"))
            println(greeting.uppercase())
        else println(greeting)
    }
}
```
Example run:
```
$ mycli greet kys0ff
Hello, kys0ff
```

---

## 🌟 Features

| Feature                         | Description                                                                                  |
|----------------------------------|----------------------------------------------------------------------------------------------|
| Easy CLI DSL                     | Declarative style for configuring commands and flags                                         |
| Interactive Mode                 | Built-in REPL-like prompt for live user interaction                                         |
| Arguments & Flags                | Simple registration for arguments and flags per command                                     |
| Progress Bars & Spinners         | Built-in progress and spinner utilities                                                     |
| Colorful Output                  | Built-in ANSI color support and extension functions                                         |
| Global/Per-command Error Handler | Custom crash handling at global or per-command level                                        |
| Command Help & Version           | Built-in `--help`, `-h`, and `--version` flags                                              |
| Input Utilities                  | Read user input interactively or via arguments                                              |

---

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
    argument("x", "First number")
    argument("y", "Second number")
    action {
        val x = get("x")?.toIntOrNull() ?: 0
        val y = get("y")?.toIntOrNull() ?: 0
        println("Sum: ${x + y}")
    }
}
```

---

### 🔹 Interactive Mode

Enable interactive mode (REPL-style):
```kotlin
configure {
    interactiveMode = InteractiveMode(enabled = true)
}
```
- Type `help` to list commands, `exit` to quit.
- Each command and flag is available interactively.

---

### 🔹 User Input Scenarios

Prompt for input if argument is not supplied:
```kotlin
action {
    val name = param() ?: readInputOrNull("Enter your name", AnsiColor.CYAN) ?: "stranger"
    println("Hello, $name")
}
```

---

### 🔹 Progress Bar Support

```kotlin
progressBar("Working...") {
    for (i in 1..100) {
        step()
        Thread.sleep(10)
    }
}
```
- Progress types: bar, spinner, etc.

---

### 🔹 Colorful Text Printing

```kotlin
println("This is ".bold() + "bold".italic().color(AnsiColor.BRIGHT_YELLOW))
```
Or use extension functions for colorization and styles.

---

## ⚙️ Configuration Options

| Option                  | Type              | Default                    | Purpose                                 |
|-------------------------|-------------------|----------------------------|-----------------------------------------|
| `name`                  | String            | `"null"`                   | CLI application display name            |
| `version`               | String            | `"null"`                   | CLI version string                      |
| `description`           | String            | `"null"`                   | CLI one-line description                |
| `interactiveMode`       | InteractiveMode   | see below                  | Interactive mode settings               |
| `showUsageOnError`      | Boolean           | `true`                     | Show usage automatically on error       |
| `showPrompt`            | Boolean           | `true`                     | Show prompt in interactive mode         |
| `colors`                | ApplicationColors | predefined (customizable)  | Output color palette                    |

**InteractiveMode options:**
- `enabled`: Enable/disable REPL
- `greet`: Greeting message and color
- `goodBye`: Farewell message and color

---

## 🧩 Feature Table

| Area                | Supported Features                                         |
|---------------------|-----------------------------------------------------------|
| Command Registration| Named commands, arguments, flags, help text               |
| Execution Modes     | Arguments mode, Interactive (REPL) mode                   |
| User Input          | Positional args, interactive prompts, defaults            |
| Output              | Colors, bold/italic, tables, progress bars                |
| Error Handling      | Custom global/per-command crash handler                    |
| Help & Version      | `help` command, `--help`/`-h`/`--version` flags           |

---

## 🛠️ Global Options Table

| Option/Flag   | Description                        |
|---------------|------------------------------------|
| `--help, -h`  | Show help message                  |
| `--version`   | Show version info                  |

---

## 🧩 Extending and Customization

- **Register Commands:** Use `command(name)` and supply a description, arguments, flags, and an `action { }` block.
- **Global Config:** Use `configure { ... }` to set metadata and output styles.
- **Crash Handling:** Assign `onCrash = { e -> ... }` to handle exceptions globally.
- **Progress & Spinners:** Use `progressBar("msg") { ... }` and spinner DSLs.
- **Color Output:** Use extension functions like `.bold()`, `.italic()`, `.color(AnsiColor.BRIGHT_GREEN)` on strings.

---

## 📝 License

[MIT](./LICENSE)

---
