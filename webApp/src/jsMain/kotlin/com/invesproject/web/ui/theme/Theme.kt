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
    val OnSurfaceVariant = Color("#49454F")
}

object WebTypography : StyleSheet() {
    val HeadlineLarge by style {
        fontSize(32.px)
        fontWeight(700)
        lineHeight("40px")
    }

    val HeadlineMedium by style {
        fontSize(28.px)
        fontWeight(700)
        lineHeight("36px")
    }

    val TitleLarge by style {
        fontSize(22.px)
        fontWeight(700)
        lineHeight("28px")
    }

    val BodyLarge by style {
        fontSize(16.px)
        lineHeight("24px")
    }

    val BodyMedium by style {
        fontSize(14.px)
        lineHeight("20px")
    }

    val LabelLarge by style {
        fontSize(14.px)
        fontWeight(500)
        lineHeight("20px")
    }
}

object InvesProjectTheme : StyleSheet() {
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
            fontWeight(500)
            property("transition", "background-color 0.2s")

            hover(self) style {
                backgroundColor(WebColors.PrimaryContainer)
            }
        }

        "input" style {
            backgroundColor(WebColors.Surface)
            color(WebColors.OnSurface)
            border {
                width = 1.px
                style = LineStyle.Solid
                color = WebColors.OnSurface
            }
            borderRadius(4.px)
            padding(12.px)
            fontSize(14.px)
            width(100.percent)
            property("box-sizing", "border-box")

            focus(self) style {
                border {
                    color = WebColors.Primary
                }
                outline("none")
            }
        }

        "textarea" style {
            backgroundColor(WebColors.Surface)
            color(WebColors.OnSurface)
            border {
                width = 1.px
                style = LineStyle.Solid
                color = WebColors.OnSurface
            }
            borderRadius(4.px)
            padding(12.px)
            fontSize(14.px)
            width(100.percent)
            property("box-sizing", "border-box")
            resize("vertical")
            minHeight(100.px)

            focus(self) style {
                border {
                    color = WebColors.Primary
                }
                outline("none")
            }
        }
    }
} 