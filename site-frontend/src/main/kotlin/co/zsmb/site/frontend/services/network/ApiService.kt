package co.zsmb.site.frontend.services.network

import co.zsmb.site.common.ArticleDetail
import co.zsmb.site.common.ArticleSummary
import co.zsmb.site.common.CustomPage

interface ApiService {

    fun getArticleSummaries(callback: (List<ArticleSummary>) -> Unit)

    fun getArticleDetail(articleUrl: String, onSuccess: (ArticleDetail) -> Unit, onError: () -> Unit)

    fun getCustomPage(name: String, onSuccess: (CustomPage) -> Unit)

}