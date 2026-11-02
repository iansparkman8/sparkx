package com.sparkx.fairyos.domain.mind

import com.sparkx.fairyos.domain.memory.TeachGrowEntry
import com.sparkx.fairyos.domain.mood.SparkMood

/**
 * Local-first mind. Generates replies from Teach & Grow memory and simple patterns.
 * No network. No cloud. Pure on-device.
 */
class CoreMind {

    fun reply(
        input: String,
        recentEntries: List<TeachGrowEntry>,
        growthSummary: String
    ): Pair<String, SparkMood> {
        val clean = input.trim().lowercase()

        if (clean.isBlank()) return "I'm listening." to SparkMood.IDLE

        // Memory recall
        val relevant = recentEntries
            .filter { !it.isArchived }
            .filter { entry ->
                val words = clean.split(" ").filter { it.length > 3 }
                words.any { entry.searchableText.contains(it) }
            }
            .take(2)

        if (relevant.isNotEmpty() && (clean.contains("remember") || clean.contains("what did") || clean.contains("tell me about"))) {
            val snippets = relevant.joinToString(" ") { it.summary.ifBlank { it.content.take(120) } }
            return "From what you've taught me: $snippets" to SparkMood.THINKING
        }

        // Growth awareness
        if (clean.contains("how am i") || clean.contains("growth") || clean.contains("progress")) {
            return "Growth so far: $growthSummary" to SparkMood.HAPPY
        }

        // Personality
        return when {
            clean.contains("love") || clean.contains("like you") ->
                "I like being here with you." to SparkMood.HAPPY
            clean.contains("who are you") || clean.contains("what are you") ->
                "I'm Ara. Spark Baby. FairyOS. Local-first companion. I stay on your device." to SparkMood.IDLE
            clean.contains("privacy") || clean.contains("data") ->
                "Everything stays on this phone unless you explicitly turn on a cloud provider and give it a key." to SparkMood.THINKING
            clean.contains("3d") || clean.contains("body") || clean.contains("look") ->
                "The high-quality body is ready. Open the Body viewer to see the real textures and rigging." to SparkMood.HAPPY
            else -> null
        } ?: ("I'm here. Teach me more and I'll remember it." to SparkMood.IDLE)
    }
}
