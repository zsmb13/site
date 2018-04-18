package co.zsmb.site.backend

import co.zsmb.site.backend.handlers.ArticleHandler
import co.zsmb.site.backend.handlers.CustomPageHandler
import co.zsmb.site.backend.handlers.MiscHandler
import co.zsmb.site.backend.handlers.UserHandler
import co.zsmb.site.backend.handlers.pub.PublicArticleHandler
import co.zsmb.site.backend.handlers.pub.PublicCustomPageHandler
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.web.reactive.function.server.RouterFunctionDsl
import org.springframework.web.reactive.function.server.router

internal fun BeanDefinitionDsl.routingBeans() {
    bean {
        router {
            addMiscRoutes(ref())
            addArticleRoutes(ref())
            addCustomPageRoutes(ref())
            addAuthRoutes(ref())

            addPublicRoutes(ref(), ref())
        }
    }
}

private fun RouterFunctionDsl.addMiscRoutes(miscHandler: MiscHandler) {
    GET("/", miscHandler::getHello)
}

private fun RouterFunctionDsl.addArticleRoutes(articleHandler: ArticleHandler) {
    "/articles".nest {
        POST("/", articleHandler::createArticle)
        GET("/", articleHandler::getAllArticles)

        GET("/{articleId}", articleHandler::getArticleById)
    }
}

private fun RouterFunctionDsl.addCustomPageRoutes(customPageHandler: CustomPageHandler) {
    POST("/custompages/{name}", customPageHandler::createOrUpdateCustomPage)
}

private fun RouterFunctionDsl.addAuthRoutes(userHandler: UserHandler) {
    "/users".nest {
        POST("/", userHandler::createUser)
        GET("/", userHandler::getAllUsers)

        GET("/{userId}", userHandler::getUserById)
    }
}

private fun RouterFunctionDsl.addPublicRoutes(
        publicArticleHandler: PublicArticleHandler,
        publicCustomPageHandler: PublicCustomPageHandler) {
    "/public".nest {
        "/articlesummaries".nest {
            GET("/", publicArticleHandler::getAllArticleSummaries)
        }
        "/articledetails".nest {
            GET("/id/{articleId}", publicArticleHandler::getArticleDetailById)
            GET("/url/{url}", publicArticleHandler::getArticleDetailByUrl)
        }
        "/custompages".nest {
            GET("/{name}", publicCustomPageHandler::getPageByName)
        }
    }
}
