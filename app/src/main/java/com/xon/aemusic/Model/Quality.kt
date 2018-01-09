package com.xon.aemusic.Model

/**
 * Created by Administrator on 12/16/2017.
 */
class Quality {
    var q128: String = ""
    var q320: String = ""
    var q500: String = ""
    var lossless: String = ""

    constructor(q128: String, q320: String, q500: String, lossless: String) {
        this.q128 = q128
        this.q320 = q320
        this.q500 = q500
        this.lossless = lossless
    }

    constructor()

    fun getHighQuality(): Qualities {
        if (!lossless.isNullOrEmpty()) {
            return Qualities.Lossless
        } else if (!q500.isNullOrEmpty())
            return Qualities.q500
        else if (!q320.isNullOrEmpty())
            return Qualities.q320
        else
            return Qualities.q128
    }

     fun covertQualitiesToText(quality: Qualities): String {
        when (quality) {
            Qualities.q128 -> return "128 Kbps"
            Qualities.q320 -> return "320 Kbps"
            Qualities.q500 -> return "500 Kbps"
            Qualities.Lossless -> return "Lossless"
        }
    }

    fun getSourceQualityCurrent(qualityCurrent : Qualities) : QualitySource {
        when (qualityCurrent) {
            Qualities.q128 -> {
                return QualitySource(Qualities.q128,q128)
            }
            Qualities.q320 -> {
                if (!q320.isNullOrEmpty()) {
                    return QualitySource(Qualities.q320,q320)
                } else {
                    return QualitySource(Qualities.q128,q128)
                }
            }
            Qualities.q500 -> {
                if (!q500.isNullOrEmpty()) {
                    return QualitySource(Qualities.q500,q500)
                } else if (!q320.isNullOrEmpty()) {
                    return QualitySource(Qualities.q320,q320)
                } else {
                    return QualitySource(Qualities.q128,q128)
                }
            }
            Qualities.Lossless -> {
                if (!lossless.isNullOrEmpty()) {
                    return QualitySource(Qualities.Lossless,lossless)
                } else if (!q500.isNullOrEmpty()) {
                    return QualitySource(Qualities.q500,q500)
                } else if (!q320.isNullOrEmpty()) {
                    return QualitySource(Qualities.q320,q320)
                } else {
                    return QualitySource(Qualities.q128,q128)
                }
            }
        }
    }


}