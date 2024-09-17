package com.example.moviesproject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MainCoroutineRule(
    private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestRule {
    val testDispatcher get() = dispatcher
    val testScope get() = TestCoroutineScope(dispatcher)

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                Dispatchers.setMain(dispatcher)
                try {
                    base.evaluate()
                } finally {
                    Dispatchers.resetMain()
                    dispatcher.cleanupTestCoroutines()
                }
            }
        }
    }
}