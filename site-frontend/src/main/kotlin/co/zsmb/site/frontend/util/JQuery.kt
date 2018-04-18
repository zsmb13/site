package co.zsmb.site.frontend.util

import org.w3c.dom.Document
import kotlin.browser.document

private external interface JQueryStatic {
    fun parseHTML(data: String, context: Document?): Array<Any>
}

private external var jQuery: JQueryStatic = definedExternally

object JQ {
    fun parseHTML(data: String): Array<Any> = jQuery.parseHTML(data, document)
}
