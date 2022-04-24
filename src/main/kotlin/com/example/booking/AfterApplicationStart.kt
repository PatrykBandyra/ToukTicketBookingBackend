package com.example.booking

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.util.*

/**
 *  This class is responsible for populating database when Application is ready.
 *  Supports only Linux and Windows.
 */
@Service
class AfterApplicationStart {

    @EventListener(ApplicationReadyEvent::class)
    fun populateDatabase() {
        when (val osName = getOS()) {
            OS.WINDOWS -> {
                val scriptName = "initDB.bat"
                println("Detected OS: $osName")
                println("Populating database...")
                Runtime.getRuntime()
                    .exec(System.getProperty("user.dir") + "/src/main/kotlin/com/example/booking/$scriptName")
            }
            OS.LINUX -> {
                val scriptName = "initDB.sh"
                println("Detected OS: $osName")
                println("Populating database...")
                Runtime.getRuntime()
                    .exec(System.getProperty("user.dir") + "/src/main/kotlin/com/example/booking/$scriptName")
            }
            else -> {
                println("OS not detected. Database will not be populated.")
            }
        }
    }

    enum class OS {
        WINDOWS, LINUX
    }

    fun getOS(): OS? {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        return when {
            os.contains("win") -> {
                OS.WINDOWS
            }
            os.contains("nix") || os.contains("nux") || os.contains("aix") || os.contains("ubu") -> {
                OS.LINUX
            }
            else -> null
        }
    }
}