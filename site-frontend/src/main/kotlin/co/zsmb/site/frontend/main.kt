package co.zsmb.site.frontend

import co.zsmb.kagu.core.init.application
import co.zsmb.site.frontend.components.about.AboutComponent
import co.zsmb.site.frontend.components.article.detail.ArticleDetailComponent
import co.zsmb.site.frontend.components.article.list.ArticleListComponent
import co.zsmb.site.frontend.components.article.summary.ArticleSummaryComponent
import co.zsmb.site.frontend.components.loading.LoadingComponent
import co.zsmb.site.frontend.components.notfound.NotFoundComponent
import co.zsmb.site.frontend.components.talks.TalksComponent
import co.zsmb.site.frontend.config.ConfigModule
import co.zsmb.site.frontend.services.network.ApiModule


fun main(args: Array<String>) = application {

    modules {
        +ApiModule
        +ConfigModule
    }

    components {
        +ArticleSummaryComponent
        +LoadingComponent
    }

    routing {
        config {
            noHashMode = true
        }
        defaultState {
            path = "/404"
            handler = NotFoundComponent
        }
        state {
            path = "/about"
            handler = AboutComponent
        }
        state {
            path = "/talks"
            handler = TalksComponent
        }
        state {
            path = "/"
            handler = ArticleListComponent
        }
        state {
            path = "/:articleUrl"
            handler = ArticleDetailComponent
        }
    }

}
