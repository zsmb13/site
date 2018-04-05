package co.zsmb.site.backend.data

import co.zsmb.site.common.ArticleSummary

fun Article.toSummary() = ArticleSummary(
        title = title,
        summary = summary,
        publishDate = publishDate?.time,
        id = id
)
