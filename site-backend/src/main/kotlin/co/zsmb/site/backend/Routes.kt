package co.zsmb.site.backend

import co.zsmb.site.backend.handlers.ArticleHandler
import co.zsmb.site.backend.handlers.MiscHandler
import co.zsmb.site.backend.handlers.UserHandler
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.web.reactive.function.server.RouterFunctionDsl
import org.springframework.web.reactive.function.server.router

internal fun BeanDefinitionDsl.routingBeans() {
    bean {
        router {
            addMiscRoutes(ref())
            addArticleRoutes(ref())
            addAuthRoutes(ref())
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

        "/{articleId}".nest {
            GET("/", articleHandler::getArticleById)
        }
    }
    "/articlesummaries".nest {
        GET("/", articleHandler::getAllArticleSummaries)
    }
}

private fun RouterFunctionDsl.addAuthRoutes(userHandler: UserHandler) {
    "/users".nest {
        POST("/", userHandler::createUser)
        GET("/", userHandler::getAllUsers)

        "/{userId}".nest {
            GET("/", userHandler::getUserById)
        }
    }
}
