package com.invesproject.web.ui.components

import androidx.compose.runtime.*
import com.invesproject.web.ui.theme.WebColors
import com.invesproject.web.ui.theme.WebTypography
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: StyleScope.() -> Unit = {}
) {
    Button(
        attrs = {
            onClick { onClick() }
            style {
                backgroundColor(WebColors.Primary)
                color(WebColors.OnPrimary)
                border(0.px)
                borderRadius(4.px)
                padding(12.px, 24.px)
                cursor("pointer")
                fontSize(14.px)
                fontWeight("500")
                modifier()
            }
        }
    ) {
        Text(text)
    }
}

@Composable
fun OutlinedInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    type: String = "text",
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(4.px)
            }
        }
    ) {
        Label(
            attrs = {
                style {
                    color(if (isError) WebColors.Error else WebColors.OnSurface)
                    fontSize(12.px)
                }
            }
        ) {
            Text(label)
        }
        Input(
            attrs = {
                type(type)
                value(value)
                onInput { event -> onValueChange(event.value) }
                style {
                    width(100.percent)
                    padding(12.px)
                    borderRadius(4.px)
                    border(1.px, LineStyle.Solid, if (isError) WebColors.Error else WebColors.OnSurface)
                    fontSize(14.px)
                    outline("none")
                    property("&:focus", "border-color: ${WebColors.Primary}")
                }
            }
        )
        if (errorMessage != null) {
            Span(
                attrs = {
                    style {
                        color(WebColors.Error)
                        fontSize(12.px)
                    }
                }
            ) {
                Text(errorMessage)
            }
        }
    }
}

@Composable
fun LoadingSpinner() {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                justifyContent(JustifyContent.Center)
                alignItems(AlignItems.Center)
                padding(24.px)
            }
        }
    ) {
        Div(
            attrs = {
                style {
                    width(24.px)
                    height(24.px)
                    border(2.px, LineStyle.Solid, WebColors.Primary)
                    property("border-top-color", "transparent")
                    borderRadius(50.percent)
                    property("animation", "spin 1s linear infinite")
                }
            }
        )
    }
    Style("""
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    """.trimIndent())
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                alignItems(AlignItems.Center)
                gap(16.px)
                padding(24.px)
            }
        }
    ) {
        Text(
            attrs = {
                style {
                    color(WebColors.Error)
                }
            }
        ) {
            Text(message)
        }
        PrimaryButton(
            text = "Retry",
            onClick = onRetry
        )
    }
}

@Composable
fun Card(
    modifier: StyleScope.() -> Unit = {},
    content: @Composable () -> Unit
) {
    Div(
        attrs = {
            style {
                backgroundColor(WebColors.Surface)
                borderRadius(8.px)
                padding(16.px)
                property("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)")
                modifier()
            }
        }
    ) {
        content()
    }
} 