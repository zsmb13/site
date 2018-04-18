package co.zsmb.site.frontend.util

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.get

fun Node.removeChildren() {
    while (hasChildNodes()) {
        removeChild(firstChild!!)
    }
}

inline fun NodeList.forEach(function: (Node) -> Unit) {
    for (i in 0 until this.length) {
        function(this[i]!!)
    }
}

fun Node.visitChildrenThat(predicate: (Node) -> Boolean, action: (Node) -> Unit) {
    this.childNodes.forEach {
        if (predicate(it)) {
            action(it)
        }
        it.visitChildrenThat(predicate, action)
    }
}

fun Element.appendMarkdown(markdown: String?) {
    markdown ?: return
    this.append(*JQ.parseHTML(markdown))
}
