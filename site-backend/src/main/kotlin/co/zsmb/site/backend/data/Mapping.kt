package co.zsmb.site.backend.data

import co.zsmb.site.common.ArticleDetail
import co.zsmb.site.common.ArticleSummary

fun Article.toSummary() = ArticleSummary(
        title = title,
        summary = summary,
        publishDate = publishDate?.time,
        id = id
)

fun Article.toDetail() = ArticleDetail(
        title = title,
        content = content,
        publishDate = publishDate?.time,
        lastModificationDate = lastModificationDate?.time,
        id = id
)
