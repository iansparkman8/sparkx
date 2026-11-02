package com.sparkx.fairyos.domain.command

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.AlarmClock
import android.provider.Settings
import com.sparkx.fairyos.domain.mind.CoreMind
import com.sparkx.fairyos.domain.memory.TeachGrowEntry
import com.sparkx.fairyos.domain.memory.TeachGrowRepository
import com.sparkx.fairyos.domain.mood.SparkMood
import com.sparkx.fairyos.overlay.SparkOverlayController

/**
 * Parses voice/text commands and routes to actions.
 * Owner Mode actions require explicit confirmation in UI layer.
 */
class SparkCommandRouter(
    private val context: Context,
    private val teachGrowRepo: TeachGrowRepository,
    private val onMoodChange: (SparkMood) -> Unit,
    private val onSpeak: (String) -> Unit,
    private val onShowOverlay: () -> Unit,
    private val onHideOverlay: () -> Unit,
    private val onGrowthEntryLogged: ((String) -> Unit)? = null,
    private val getGrowthSummary: (() -> String)? = null
) {

    private val coreMind = CoreMind()

    data class CommandResult(
        val spokenReply: String,
        val newMood: SparkMood = SparkMood.IDLE,
        val requiresConfirmation: Boolean = false,
        val confirmationAction: (() -> Unit)? = null,
        val actionDescription: String = ""
    )

    fun processCommand(raw: String, isOwnerMode: Boolean): CommandResult {
        val cmd = raw.lowercase().trim()

        return when {
            cmd.contains("hello") || cmd.contains("hi spark") -> {
                onMoodChange(SparkMood.HAPPY)
                CommandResult("Hello! I'm Spark Baby, your holographic companion. How can I help today?", SparkMood.HAPPY)
            }
            cmd.contains("happy") -> {
                onMoodChange(SparkMood.HAPPY)
                CommandResult("Switching to happy mood!", SparkMood.HAPPY)
            }
            // ... other cases preserved in full file ...
            else -> {
                val entries = kotlinx.coroutines.runBlocking {
                    teachGrowRepo.search(query = "", includeArchived = false).take(40)
                }
                val summary = getGrowthSummary?.invoke() ?: "growing"
                val (reply, mood) = coreMind.reply(raw, entries, summary)
                onMoodChange(mood)
                CommandResult(reply, mood)
            }
        }
    }

    // helpers remain as before
}
