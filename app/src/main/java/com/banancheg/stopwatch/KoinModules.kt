package com.banancheg.stopwatch

import org.koin.dsl.module

val application = module {
    single<ITimestampProvider> { TimestampProvider() }

    single { ElapsedTimeCalculator(get()) }

    single { StopwatchStateCalculator(timestampProvider = get(), elapsedTimeCalculator = get()) }

    single { TimestampMillisecondsFormatter() }

    factory { StopwatchStateHolder(
        stopwatchStateCalculator = get(),
        elapsedTimeCalculator = get(),
        timestampMillisecondsFormatter = get()
    ) }
}