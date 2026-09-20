package machine7y.grayforce.presentation.utils

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard

private const val LABEL = "GrayForce"

suspend fun Clipboard.copyToClipboard(text: String) {
    setClipEntry(
        ClipEntry(
            ClipData.newPlainText(
                /* label = */ LABEL,
                /* text = */ text,
            )
        )
    )
}
