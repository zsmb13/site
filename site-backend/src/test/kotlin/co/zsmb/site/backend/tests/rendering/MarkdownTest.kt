package co.zsmb.site.backend.tests.rendering

import co.zsmb.site.backend.Markdown
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MarkdownTest {

    @Test
    fun `Render single paragraph`() {
        assertEquals("<p>Hello world</p>\n", Markdown.render("Hello world"))
    }

    @Test
    fun `Render header and paragraph`() {
        val input = """
            # Title
            Hello world
            """.trimIndent()
        assertEquals("<h1>Title</h1>\n<p>Hello world</p>\n", Markdown.render(input))
    }

    @Test
    fun `Multiple render calls`() {
        assertEquals("<p>Hello world</p>\n", Markdown.render("Hello world"))
        assertEquals("<p>Hey there</p>\n", Markdown.render("Hey there"))
        assertEquals("<p>Brown cow</p>\n", Markdown.render("Brown cow"))
    }

}
