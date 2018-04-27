package co.zsmb.site.backend

import org.commonmark.Extension
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TableBlock
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.AttributeProvider
import org.commonmark.renderer.html.HtmlRenderer

internal object Markdown {

    private val extensions: List<Extension> = listOf(TablesExtension.create(), StrikethroughExtension.create())

    private val parser = Parser
            .builder()
            .extensions(extensions)
            .build()
    private val htmlRenderer = HtmlRenderer
            .builder()
            .extensions(extensions)
            .attributeProviderFactory {
                AttributeProvider { node, _, attributes ->
                    when (node) {
                        is TableBlock -> attributes["class"] = "table table-bordered table-sm"
                    }
                }
            }
            .build()

    fun render(markdownString: String): String {
        val node = parser.parse(markdownString)
        return htmlRenderer.render(node)
    }

}
