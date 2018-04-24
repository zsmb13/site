package co.zsmb.site.backend

import org.commonmark.Extension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

internal object Markdown {

    private val extensions: List<Extension> = listOf(TablesExtension.create())

    private val parser = Parser
            .builder()
            .extensions(extensions)
            .build()
    private val htmlRenderer = HtmlRenderer
            .builder()
            .extensions(extensions)
            .build()

    fun render(markdownString: String): String {
        val node = parser.parse(markdownString)
        return htmlRenderer.render(node)
    }

}
