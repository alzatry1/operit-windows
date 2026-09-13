package com.ai.assistance.operit.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ai.assistance.operit.data.preferences.ActivePromptManager
import com.ai.assistance.operit.data.preferences.CharacterCardManager
import com.ai.assistance.operit.data.preferences.ThemePreferenceSnapshot
import com.ai.assistance.operit.data.preferences.ThemePreferenceValues

val LocalThemePreferenceSnapshot =
    compositionLocalOf<ThemePreferenceSnapshot> {
        // 桌面端 provider 生效前的安全默认快照（原来是 error("not provided")，导致组合一读就崩）。——Nova 注
        ThemePreferenceSnapshot(
            source = "character_card",
            sourceId = CharacterCardManager.DEFAULT_CHARACTER_CARD_ID,
            values = ThemePreferenceValues.defaultVisual(),
        )
    }

@Composable
fun rememberActiveThemePreferenceSnapshot(): ThemePreferenceSnapshot {
    val context = LocalContext.current
    val activePromptManager = remember(context) { ActivePromptManager.getInstance(context) }
    val themeSnapshot by activePromptManager.activeThemePreferenceSnapshotFlow.collectAsState(
        initial =
            ThemePreferenceSnapshot(
                source = "character_card",
                sourceId = CharacterCardManager.DEFAULT_CHARACTER_CARD_ID,
                values = ThemePreferenceValues.defaultVisual(),
            ),
    )
    return themeSnapshot
}
