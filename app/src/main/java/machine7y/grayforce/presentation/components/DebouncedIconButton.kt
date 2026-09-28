package machine7y.grayforce.presentation.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DEBOUNCE = 300L

@Composable
fun DebouncedIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    var clickable by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    IconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = {
            if (!clickable) return@IconButton

            clickable = false
            onClick()

            coroutineScope.launch {
                delay(DEBOUNCE)
                clickable = true
            }
        }
    ) {
        content()
    }
}
