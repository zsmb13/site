package co.zsmb.site.backend.setup.mocks

import co.zsmb.site.backend.data.Article
import co.zsmb.site.backend.data.CustomPage
import co.zsmb.site.backend.security.User
import java.util.*

object MockData {

    const val ID = "afoin33th83t3gge"

    const val NON_EXISTENT_ID = "asnfjhn3khr8732bg3in97enfb32"
    const val NON_EXISTENT_NAME = "sgfgdf98h1fdy0h90df1h"

    val ARTICLES = listOf(
            Article(title = "First",
                    summary = "This is the first article summary",
                    url = "first-article",
                    content = "This is the first article content which is longer than the summary",
                    id = "abc123",
                    publishDate = Date(123L),
                    lastModificationDate = Date(253L)),
            Article(title = "Second",
                    summary = "This is the second article summary",
                    url = "second-article",
                    content = "This is the second article content which is longer than the summary",
                    id = "as46516f",
                    publishDate = Date(25L),
                    lastModificationDate = Date(100L)),
            Article(title = "Third",
                    summary = "This is the third article summary",
                    url = "third-article",
                    content = "This is the third article content which is longer than the summary",
                    id = "16ngfre",
                    publishDate = Date(3562L),
                    lastModificationDate = Date(25125L)),
            Article(title = "Future article",
                    summary = "whatever",
                    url = "future-article",
                    content = "some content",
                    id = "future",
                    publishDate = Date(32528044800000L),
                    lastModificationDate = Date(32528044800000L))
    )

    val USERS = listOf(
            User("jenny", "12345", true, arrayOf("USER"), "c57685s768ac"),
            User("kelly", "fluffy", true, arrayOf("USER"), "56116c5sac"),
            User("thomas", "0000", true, arrayOf("ADMIN"), "cab6dc1d8dbe")
    )

    val CUSTOM_PAGES = listOf(
            CustomPage("about", "Content of about page", "asdsa2952"),
            CustomPage("talks", "Content of talks page", "198fdas32"),
            CustomPage("projects", "Content of projects page", "hr8e9h2d8")
    )

}
