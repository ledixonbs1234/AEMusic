package com.xon.aemusic.Resposility

import android.media.MediaPlayer
import android.util.Log
import com.xon.aemusic.Model.DataSongFullSimple
import com.xon.aemusic.Model.DataSongManager
import com.xon.aemusic.Model.Qualities
import com.xon.aemusic.Model.Quality
import com.xon.aemusic.View.Player
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

/**
 * Created by Administrator on 12/20/2017.
 */
class SingletonPlayMusic
private constructor() {

    private object Holder {
        val INSTANCE = SingletonPlayMusic()
    }

    companion object {
        val instance: SingletonPlayMusic by lazy { Holder.INSTANCE }
    }

    //****************************

    lateinit var mPlayer : MediaPlayer
    lateinit var mCurrentMusic : DataSongFullSimple
    lateinit var mCurrentMusicNoSource : DataSongFullSimple
    var mDefaultQualitiesTrustInstance = Qualities.q128
    lateinit var mMusics : ArrayList<DataSongFullSimple>
    var mDefaultQuailities : Qualities = Qualities.q128
    var mPlaybackPosition = 0

    init {
        mPlayer = MediaPlayer()

        mMusics = arrayListOf()
    }

    fun playMusic(music: DataSongFullSimple) {
        mCurrentMusic = music
        var qualityCurrent = music.source.getSourceQualityCurrent(mDefaultQuailities)
        var source = qualityCurrent.source
        mDefaultQualitiesTrustInstance = qualityCurrent.quality

        if (!source.isNullOrEmpty()) {
            resetPlayer()
            mPlayer.setDataSource(source)
            mPlayer.prepare()
            mPlayer.start()
        }
    }


    private fun ConvertoString(stream: InputStream): String {
        var result = ""
        val isReader = InputStreamReader(stream)
        val bReader = BufferedReader(isReader)

        try {

            result = bReader.readText()
            bReader.close()
            isReader.close()

        } catch (ex: Exception) {
            Log.e("Tag", "Error checking data" + ex.printStackTrace())
        }
        return result
    }

    fun setCurrentMusicFromLinkCSN(songNoSource: DataSongFullSimple) : Observable<Quality>{
        var observableQuality =  Observable.create<DataSongFullSimple>({ e->e.onNext(songNoSource)
        })
                .observeOn(Schedulers.io())
                .flatMap({ e ->
                    val urlCSN = e.linkWebCSN
                    var url = URL(urlCSN)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.readTimeout = 10000
                    connection.connectTimeout = 10000
                    connection.requestMethod = "GET"
                    connection.instanceFollowRedirects
                    connection.connect()
                    var quality = Quality()
                    var responseCode = connection.responseCode
                    if (responseCode == 200) {
                        var stream  = connection.inputStream
                        var source = ConvertoString(stream)


                        var nFill = Pattern.compile("downloadlink2(\\W|\\w)+?center").matcher(source)
                        if (nFill.find()) {
                            var n = Pattern.compile("href=\"((\\W|\\w)+?)\"").matcher(nFill.group(0))
                            //Link dang
                            //http://chiasenhac.vn/download2.php?v1=1843&v2=1&v3=1YJ2AUA-2XMG2ebG&v4=128&v5=Da%20Lo%20Yeu%20Em%20Nhieu%20-%20JustaTee [128kbps_MP3].mp3

                            while (n.find()) {

                                when (n.group(1).substring(n.group(1).indexOf('['), n.group(1).indexOf('[') + 4)) {
                                    "[320" -> {
                                        quality.q320 = n.group(1)
                                    }
                                    "[500" -> {
                                        quality.q500 = n.group(1)
                                    }
                                    "[Los" -> {
                                        quality.lossless = n.group(1)
                                    }
                                }
                            }

                        }

                }
                    return@flatMap Observable.just(quality)
                })
        return observableQuality
    }

    fun setPosition(position: Int) {
        mPlayer.seekTo(position)
        mPlayer.start()
    }

    fun getPosition() : Int{
        return mPlayer.currentPosition
    }

    fun continuePlayer() {
        mPlayer.seekTo(mPlaybackPosition)
        mPlayer.start()
    }

    fun resetPlayer(){
        mPlayer.reset()
    }

    fun nextPlayer() : DataSongFullSimple?{
        if (mMusics.size != 0 && mMusics.indexOf(mCurrentMusic) < mMusics.size) {
            mCurrentMusic = mMusics.get(mMusics.indexOf(mCurrentMusic) + 1)

            playMusic(mCurrentMusic)
            return mCurrentMusic
        }
        return null
    }

    fun previousPlayer() : DataSongFullSimple?{
        if (mMusics.size != 0 && mMusics.indexOf(mCurrentMusic) >= 1) {
            mCurrentMusic = mMusics.get(mMusics.indexOf(mCurrentMusic) - 1)
            playMusic(mCurrentMusic)
            return mCurrentMusic
        }
        return null
    }

    fun PausePlayer(){
        if (mPlayer.isPlaying) {
            mPlaybackPosition = mPlayer.currentPosition
            mPlayer.pause()
        } else {
            continuePlayer()
        }
    }






    }