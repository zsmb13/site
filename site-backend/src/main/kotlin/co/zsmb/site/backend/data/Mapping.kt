package co.zsmb.site.backend.data

import co.zsmb.site.backend.Markdown
import co.zsmb.site.common.ArticleDetail
import co.zsmb.site.common.ArticleSummary

private fun String.renderMarkdown() = Markdown.render(this)

fun Article.toSummary() = ArticleSummary(
        title = title,
        summary = summary?.renderMarkdown(),
        publishDate = publishDate?.time,
        id = id
)

fun Article.toDetail() = ArticleDetail(
        title = title,
        content = content?.renderMarkdown(),
        publishDate = publishDate?.time,
        lastModificationDate = lastModificationDate?.time,
        id = id
)
