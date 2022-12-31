package co.pvphub.multilib

import net.md_5.bungee.api.ChatColor
import java.util.regex.Matcher
import java.util.regex.Pattern

private val pattern: Pattern = Pattern.compile("&#[a-fA-F0-9]{6}")
fun String.color(): String {
    var string = this
    var match: Matcher = pattern.matcher(string)
    while (match.find()) {
        val color: String = string.substring(match.start() + 1, match.end())
        val color1: String = string.substring(match.start(), match.end())
        string = string.replace(color1, "${ChatColor.of(color)}")
        match = pattern.matcher(string)
    }
    return ChatColor.translateAlternateColorCodes('&', string)
}