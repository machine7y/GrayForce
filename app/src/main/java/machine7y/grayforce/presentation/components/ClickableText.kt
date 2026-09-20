package machine7y.grayforce.presentation.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private const val CLICKABLE_TAG = "CLICKABLE"

@Composable
fun ClickableText(
    text: String,
    clickableTextList: List<String>,
    onTextClicked: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    val scope = rememberCoroutineScope()
    var textLayoutResult by remember {
        mutableStateOf<TextLayoutResult?>(null)
    }
    val annotatedText = buildAnnotatedString {
        append(text)

        clickableTextList.forEach { clickableText ->
            var startIndex = text.indexOf(clickableText)

            while (startIndex != -1) {
                addStringAnnotation(
                    tag = CLICKABLE_TAG,
                    annotation = clickableText,
                    start = startIndex,
                    end = startIndex + clickableText.length,
                )

                addStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                    ),
                    start = startIndex,
                    end = startIndex + clickableText.length,
                )

                startIndex = text.indexOf(
                    string = clickableText,
                    startIndex = startIndex + clickableText.length,
                )
            }
        }
    }

    Text(
        text = annotatedText,
        fontSize = 16.sp,
        textAlign = textAlign,
        onTextLayout = { textLayoutResult = it },
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { position ->
                    val offset = textLayoutResult
                        ?.getOffsetForPosition(position)
                        ?: return@detectTapGestures

                    annotatedText.getStringAnnotations(
                        tag = CLICKABLE_TAG,
                        start = offset,
                        end = offset,
                    )
                        .firstOrNull()
                        ?.let { annotation ->
                            scope.launch { onTextClicked(annotation.item) }
                        }
                }
            }
            .padding(8.dp),
    )
}
