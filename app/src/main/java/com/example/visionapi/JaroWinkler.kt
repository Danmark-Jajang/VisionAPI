package com.example.visionapi

import kotlin.math.max
import kotlin.math.min

class JaroWinkler(private val threshold: Double = 0.7) {
    fun similarity(first: String, second: String): Double {
        if (first == second) return 1.0
        val (m, t, l, p) = matchStrings(first, second)
        if (m == 0f) return 0.0
        // Jaro similarity = 1/3 * (m/|s1| + m/|s2| + (m-t)/m)
        val sj = ((m / first.length) + (m / second.length) + ((m - t) / m)) / 3.0
        // Winkler similarity = Sj + P * L * (1 – Sj)
        return if (sj > threshold) sj + p * l * (1 - sj) else sj
    }

    private fun matchStrings(first: String, second: String): Match {
        val min = minOf(first, second)
        val max = maxOf(first, second)
        val (matchIndexes, matchFlags, matches) = computeStringMatch(max, min)
        val ms1 = CharArray(matches).apply { fill(min, matchIndexes) }
        val ms2 = CharArray(matches).apply { fill(max, matchFlags) }
        val transpositions = transpositions(ms1, ms2)
        val prefix = commonPrefix(min, max)
        val scaling = min(JW_SCALING_FACTOR, 1.0 / max.length)
        return Match(matches.toFloat(), transpositions, prefix, scaling)
    }

    private fun computeStringMatch(max: String, min: String): StringMatchInfo {
        val range = max(max.length / 2 - 1, 0)
        val matchIndexes = IntArray(min.length) { -1 }
        val matchFlags = BooleanArray(max.length)
        var matches = 0
        for (i in min.indices) {
            val char = min[i]
            var xi = max(i - range, 0)
            val xn = min(i + range + 1, max.length)
            while (xi < xn) {
                if (!matchFlags[xi] && char == max[xi]) {
                    matchIndexes[i] = xi
                    matchFlags[xi] = true
                    matches++
                    break
                }
                xi++
            }
        }
        return StringMatchInfo(matchIndexes, matchFlags, matches)
    }

    private fun CharArray.fill(max: String, flags: BooleanArray) {
        var si = 0
        for (i in max.indices) {
            if (flags[i]) {
                this[si] = max[i]
                si++
            }
        }
    }

    private fun CharArray.fill(min: String, indexes: IntArray) {
        var si = 0
        for (i in min.indices) {
            if (indexes[i] != -1) {
                this[si] = min[i]
                si++
            }
        }
    }

    private fun commonPrefix(min: String, max: String): Int {
        var prefix = 0
        for (mi in min.indices) {
            if (min[mi] != max[mi]) break
            prefix++
        }
        return prefix
    }

    private fun transpositions(ms1: CharArray, ms2: CharArray): Int {
        var transpositions = 0
        for (mi in ms1.indices) {
            if (ms1[mi] != ms2[mi]) {
                transpositions++
            }
        }
        return transpositions / 2
    }

    companion object {

        private const val JW_SCALING_FACTOR = 0.1
    }
}

internal data class StringMatchInfo(
    val matchIndexes: IntArray,
    val matchFlags: BooleanArray,
    val matches: Int
)

internal data class Match(
    val matches: Float,
    val transpositions: Int,
    val prefix: Int,
    val scaling: Double,
)