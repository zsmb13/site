package co.zsmb.site.frontend.util

import org.w3c.dom.HTMLElement

private external class NativeHLJS {
    fun highlightBlock(block: HTMLElement)
}

private external val hljs: NativeHLJS

private external fun KotlinPlayground(block: HTMLElement)

object CodeUtil {

    fun highlightKotlinCodeBlocks(block: HTMLElement) {
        block.visitChildrenThat(
                predicate = {
                    it.nodeName.toLowerCase() == "code" && it.parentNode?.nodeName?.toLowerCase() == "pre"
                },
                action = { code ->
                    code as HTMLElement
                    val textContent = code.textContent

                    if (code.className.contains("kotlin") && textContent != null
                            && textContent.contains("fun main(args: Array<String>)")) {
                        KotlinPlayground(code)
                        (code.parentNode as HTMLElement).style.paddingTop = "10px"
                    } else {
                        hljs.highlightBlock(code)
                    }
                }
        )
    }

}
