package co.zsmb.site.backend

import co.zsmb.site.backend.handlers.*
import co.zsmb.site.backend.handlers.pub.PublicArticleHandler
import co.zsmb.site.backend.handlers.pub.PublicCustomPageHandler
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctionDsl
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

internal fun BeanDefinitionDsl.routingBeans() {
    bean {
        router {
            addMiscRoutes(ref())
            addArticleRoutes(ref())
            addCustomPageRoutes(ref())
            addAuthRoutes(ref())
            addAnalyticsRoutes(ref())

            addPublicRoutes(ref(), ref())
        }.addAnalyticsFilter(ref())
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
            PUT("/", articleHandler::updateArticle)
            DELETE("/", articleHandler::removeArticleById)
        }
    }
}

private fun RouterFunctionDsl.addCustomPageRoutes(customPageHandler: CustomPageHandler) {
    GET("/custompages", customPageHandler::getAllCustomPages)
    POST("/custompages/{name}", customPageHandler::createOrUpdateCustomPage)
}

private fun RouterFunctionDsl.addAuthRoutes(userHandler: UserHandler) {
    "/users".nest {
        POST("/", userHandler::createUser)
        GET("/", userHandler::getAllUsers)

        "/{userId}".nest {
            GET("/", userHandler::getUserById)
            PUT("/", userHandler::updateUserById)
            DELETE("/", userHandler::removeUserById)
        }
    }
}

private fun RouterFunctionDsl.addAnalyticsRoutes(analyticsHandler: AnalyticsHandler) {
    "/analytics".nest {
        GET("/", analyticsHandler::getAllEvents)
        DELETE("/", analyticsHandler::removeAllEvents)

        GET("/grouped", analyticsHandler::getAllEventsGrouped)
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

private fun RouterFunction<ServerResponse>.addAnalyticsFilter(analyticsFilter: AnalyticsFilter) = filter(analyticsFilter)
