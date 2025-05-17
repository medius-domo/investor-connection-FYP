package com.invesproject.web.ui.theme

import org.jetbrains.compose.web.css.*

object WebColors {
    val Primary = Color("#2196F3")
    val OnPrimary = Color("#FFFFFF")
    val PrimaryContainer = Color("#BBDEFB")
    val OnPrimaryContainer = Color("#1976D2")
    val Secondary = Color("#4CAF50")
    val OnSecondary = Color("#FFFFFF")
    val SecondaryContainer = Color("#C8E6C9")
    val OnSecondaryContainer = Color("#388E3C")
    val Tertiary = Color("#F44336")
    val OnTertiary = Color("#FFFFFF")
    val TertiaryContainer = Color("#FFCDD2")
    val OnTertiaryContainer = Color("#D32F2F")
    val Error = Color("#B00020")
    val OnError = Color("#FFFFFF")
    val ErrorContainer = Color("#FDE7E7")
    val OnErrorContainer = Color("#941916")
    val Background = Color("#FAFAFA")
    val OnBackground = Color("#1C1B1F")
    val Surface = Color("#FEFEFE")
    val OnSurface = Color("#1C1B1F")
}

object WebTypography {
    val HeadlineLarge = StyleSheet.style {
        fontSize(32.px)
        fontWeight("bold")
        lineHeight("40px")
    }

    val HeadlineMedium = StyleSheet.style {
        fontSize(28.px)
        fontWeight("bold")
        lineHeight("36px")
    }

    val TitleLarge = StyleSheet.style {
        fontSize(22.px)
        fontWeight("bold")
        lineHeight("28px")
    }

    val BodyLarge = StyleSheet.style {
        fontSize(16.px)
        lineHeight("24px")
    }

    val BodyMedium = StyleSheet.style {
        fontSize(14.px)
        lineHeight("20px")
    }

    val LabelLarge = StyleSheet.style {
        fontSize(14.px)
        fontWeight("500")
        lineHeight("20px")
    }
}

object WebTheme : StyleSheet() {
    init {
        "body" style {
            margin(0.px)
            padding(0.px)
            backgroundColor(WebColors.Background)
            color(WebColors.OnBackground)
            fontFamily("Roboto", "sans-serif")
        }

        "button" style {
            backgroundColor(WebColors.Primary)
            color(WebColors.OnPrimary)
            border(0.px)
            borderRadius(4.px)
            padding(12.px, 24.px)
            cursor("pointer")
            fontSize(14.px)
            fontWeight("500")
            property("transition", "background-color 0.2s")

            hover {
                backgroundColor(WebColors.PrimaryContainer)
            }
        }

        "input" style {
            backgroundColor(WebColors.Surface)
            color(WebColors.OnSurface)
            border(1.px, LineStyle.Solid, WebColors.OnSurface)
            borderRadius(4.px)
            padding(12.px)
            fontSize(14.px)
            width(100.percent)
            boxSizing("border-box")

            focus {
                borderColor(WebColors.Primary)
                outline("none")
            }
        }
    }
} 