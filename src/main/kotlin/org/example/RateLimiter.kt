package org.example

import io.github.resilience4j.kotlin.ratelimiter.RateLimiterConfig
import io.github.resilience4j.kotlin.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.kotlin.ratelimiter.executeSuspendFunction
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.util.*
import java.time.Duration

class RateLimiter(private val config: Config) {

    companion object Plugin : HttpClientPlugin<Config, RateLimiter> {
        private val rateLimiterRegistry: RateLimiterRegistry = RateLimiterRegistry { }
        override val key: AttributeKey<RateLimiter> = AttributeKey("RateLimiter")

        override fun prepare(block: Config.() -> Unit): RateLimiter =
            RateLimiter(Config().apply(block))

        override fun install(plugin: RateLimiter, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                val rateLimiter = rateLimiterRegistry.rateLimiter(plugin.config.name, RateLimiterConfig {
                    limitForPeriod(plugin.config.limitForPeriod)
                    limitRefreshPeriod(plugin.config.limitRefreshPeriod)
                    timeoutDuration(plugin.config.timeoutDuration)
                })

                rateLimiter.executeSuspendFunction { proceed() }
            }
        }
    }

    class Config {
        var limitRefreshPeriod: Duration = Duration.ofSeconds(1)
        var limitForPeriod = 1
        var name = "default"
        var timeoutDuration: Duration = Duration.ofDays(1)
    }
}

fun HttpClientConfig<*>.rateLimiter(block: RateLimiter.Config.() -> Unit) {
    install(RateLimiter) {
        block()
    }
}