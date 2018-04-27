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
        assertEquals("<h1>Title</h1>\n<p>Hello world</p>", Markdown.render(input).trim())
    }

    @Test
    fun `Multiple render calls`() {
        assertEquals("<p>Hello world</p>", Markdown.render("Hello world").trim())
        assertEquals("<p>Hey there</p>", Markdown.render("Hey there").trim())
        assertEquals("<p>Brown cow</p>", Markdown.render("Brown cow").trim())
    }

    @Test
    fun `Table rendering`() {
        val input = """
            | Col A | Col B | Col C |
            | ----- | ----- | ----- |
            | foo   | bar   | qwert |
            """.trimIndent()

        val expectedOutput = """
            <table class="table table-bordered table-sm">
            <thead>
            <tr><th>Col A</th><th>Col B</th><th>Col C</th></tr>
            </thead>
            <tbody>
            <tr><td>foo</td><td>bar</td><td>qwert</td></tr>
            </tbody>
            </table>
            """.trimIndent()

        assertEquals(expectedOutput, Markdown.render(input).trim())
    }

    @Test
    fun `Strikethrough rendering`() {
        assertEquals("<p><del>text</del></p>", Markdown.render("~~text~~").trim())
    }

}
