package co.zsmb.site.frontend.util

import org.w3c.dom.HTMLElement

private external class NativeHLJS {
    fun highlightBlock(block: HTMLElement)
}

private external val hljs: NativeHLJS

object HLJS {

    fun highlightBlock(block: HTMLElement) {
        block.visitChildrenThat(
                predicate = {
                    it.nodeName.toLowerCase() == "code" && it.parentNode?.nodeName?.toLowerCase() == "pre"
                },
                action = { code ->
                    hljs.highlightBlock(code as HTMLElement)
                }
        )
    }

}
