# LambdaCore

[![Kotlin](https://img.shields.io/badge/kotlin-2.x-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Platform](https://img.shields.io/badge/platform-Folia%20%7C%20Paper-blue.svg)]()
[![License](https://img.shields.io/badge/license-MIT-green.svg)]()

### Minecraft 플러그인 개발을 위한 Kotlin 기반 통합 프레임워크

LambdaCore는 Folia 환경을 포함한 최신 Minecraft 서버에서  
플러그인 개발을 더 빠르고 안전하게 만들기 위해 설계된 프레임워크입니다.

단순한 라이브러리가 아닌,  
**Command / Scheduler / Config / Database를 하나의 구조로 통합**합니다.

---

## Features

- 어노테이션 기반 Command 시스템
- SubCommand depth (`/lc user add`)
- Argument 자동 변환 및 TabComplete
- DI 컨테이너 (Constructor Injection)
- Config 자동 매핑 + reload
- Event 자동 등록
- Folia 전용 Scheduler
- HikariCP 기반 Database
- Async DB + Thread-safe 실행

---

## Example

```kotlin
@LambdaCommand("user")
class UserCommand(
    private val service: UserService
) : LambdaCommandExecutor {

    @SubCommand("add")
    fun add(context: LambdaCommandContext, player: Player) {
        service.add(player)
        context.sender.sendMessage("추가 완료")
    }
}
```
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

