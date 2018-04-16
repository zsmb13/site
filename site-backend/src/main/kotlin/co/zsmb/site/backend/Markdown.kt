package co.zsmb.site.backend

import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

internal object Markdown {

    private val parser: Parser = Parser.builder().build()
    private val htmlRenderer: HtmlRenderer = HtmlRenderer.builder().build()

    fun render(markdownString: String): String {
        val node = parser.parse(markdownString)
        return htmlRenderer.render(node)
    }

}
