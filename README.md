# LambdaCore

[![Kotlin](https://img.shields.io/badge/kotlin-2.x-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Platform](https://img.shields.io/badge/platform-Folia%20%7C%20Paper-blue.svg)]()
[![License](https://img.shields.io/badge/license-MIT-green.svg)]()

### Spigot / Paper / Folia 환경을 더 생산적이고 직관적으로 만들어주는 프레임워크

---

## Features

* [DI (Dependency Injection)](#di)
* [Command Framework](#command-framework)
* [Scheduler (Folia)](#scheduler)
* [Config System](#config-system)
* [Database (HikariCP)](#database)
* [Async + Thread Safe](#async--thread-safe)

---

## DI

LambdaCore는 생성자 기반 의존성 주입을 제공합니다.

```kotlin
@Service
class ExampleService {
    fun message() = "LambdaCore"
}

@LambdaCommand("test")
class TestCommand(
    private val service: ExampleService
) : LambdaCommandExecutor {

    override fun execute(context: LambdaCommandContext): Boolean {
        context.sender.sendMessage(service.message())
        return true
    }
}
