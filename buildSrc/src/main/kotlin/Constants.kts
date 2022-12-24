object Versions {
    const val kotlinVersion = "1.7.10"
    const val kotlinxCoroutines = "1.6.4"
    const val koin = "3.2.1"
    const val apollo = "3.6.2"
    const val multiplatformPaging = "0.5.0"

    const val compose = "1.3.0-rc01"
    const val composeCompiler = "1.3.2"
    const val navCompose = "2.6.0-alpha03"
    const val pagingCompose = "1.0.0-alpha16"
    const val accompanist = "0.26.2-beta"
    const val coilCompose = "2.2.2"
    const val amplify = "1.37.6"
    const val amplifyKotlin = "0.21.6"
    const val timber = "5.0.1"

    const val junit = "4.13"
}


object AndroidSdk {
    const val min = 21
    const val compile = 33
    const val target = compile
}

object Deps {
    const val apolloRuntime = "com.apollographql.apollo3:apollo-runtime:${Versions.apollo}"
    const val apolloNormalizedCache =
        "com.apollographql.apollo3:apollo-normalized-cache:${Versions.apollo}"
    const val multiplatformPaging =
        "io.github.kuuuurt:multiplatform-paging:${Versions.multiplatformPaging}"
    const val amplifyAuth = "com.amplifyframework:aws-auth-cognito:${Versions.amplify}"
    const val amplifyCore = "com.amplifyframework:core-kotlin:${Versions.amplifyKotlin}"

    object Kotlinx {
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"
    }
}

object Compose {
    const val compiler = "androidx.compose.compiler:compiler:${Versions.composeCompiler}"
    const val ui = "androidx.compose.ui:ui:${Versions.compose}"
    const val runtime = "androidx.compose.runtime:runtime:${Versions.compose}"
    const val activity = "androidx.activity:activity-compose:${Versions.compose}"
    const val uiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
    const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val foundationLayout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
    const val material = "androidx.compose.material:material:${Versions.compose}"
    const val materialIconsExtended =
        "androidx.compose.material:material-icons-extended:${Versions.compose}"
    const val navigation = "androidx.navigation:navigation-compose:${Versions.navCompose}"
    const val paging = "androidx.paging:paging-compose:${Versions.pagingCompose}"
    const val coilCompose = "io.coil-kt:coil-compose:${Versions.coilCompose}"
}

object Koin {
    val core = "io.insert-koin:koin-core:${Versions.koin}"
    val test = "io.insert-koin:koin-test:${Versions.koin}"
    val testJUnit4 = "io.insert-koin:koin-test-junit4:${Versions.koin}"
    val android = "io.insert-koin:koin-android:${Versions.koin}"
    val compose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
}