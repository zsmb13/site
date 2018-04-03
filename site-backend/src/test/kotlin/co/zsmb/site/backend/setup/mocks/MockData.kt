package co.zsmb.site.backend.setup.mocks

import co.zsmb.site.backend.Article
import co.zsmb.site.backend.security.User

object MockData {

    const val ID = "afoin33th83t3gge"

    const val NON_EXISTENT_ID = "asnfjhn3khr8732bg3in97enfb32"

    val ARTICLES = listOf(
            Article(title = "First", content = "First content", id = "abc123"),
            Article(title = "Second", content = "Second content", id = "def456"),
            Article(title = "Third", content = "Third content", id = "ghi789")
    )

    val USERS = listOf(
            User("jenny", "12345", true, arrayOf("USER"), "c57685s768ac"),
            User("kelly", "fluffy", true, arrayOf("USER"), "56116c5sac"),
            User("thomas", "0000", true, arrayOf("ADMIN"), "cab6dc1d8dbe")
    )

}
