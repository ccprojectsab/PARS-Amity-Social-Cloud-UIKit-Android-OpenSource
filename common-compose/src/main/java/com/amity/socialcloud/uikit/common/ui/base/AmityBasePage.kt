package com.amity.socialcloud.uikit.common.ui.base

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.elements.AmityProgressSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityProgressSnackbarVisuals
import com.amity.socialcloud.uikit.common.ui.elements.AmitySnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmitySnackbarVisuals
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.rememberAmityComposeScopeProvider
import com.amity.socialcloud.uikit.common.ui.theme.AmityComposeTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getKeyboardHeight


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AmityBasePage(
    pageId: String,
    content: @Composable AmityComposePageScope.() -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val comp = rememberAmityComposeScopeProvider(
        pageId = pageId,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
    )
    val keyboardHeight by getKeyboardHeight()

    AmityComposeTheme(pageScope = comp) {
        Scaffold(
            containerColor = AmityTheme.colors.background,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .padding(bottom = keyboardHeight + 64.dp)
                        .padding(horizontal = 16.dp),
                ) {
                    when (it.visuals) {
                        is AmitySnackbarVisuals -> {
                            AmitySnackbar(data = it.visuals as AmitySnackbarVisuals)
                        }

                        is AmityProgressSnackbarVisuals -> {
                            AmityProgressSnackbar(data = it.visuals as AmityProgressSnackbarVisuals)
                        }
                    }
                }
            },
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            }
        ) {
            if (!comp.isExcluded()) {
                content(comp)
            }
        }
    }
}