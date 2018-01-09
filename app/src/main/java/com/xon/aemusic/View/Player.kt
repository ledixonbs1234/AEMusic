package com.xon.aemusic.View


import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.view.menu.MenuBuilder
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.xon.aemusic.Interface.ImpPlayer
import com.xon.aemusic.Interface.ViewPlayer
import com.xon.aemusic.Model.DataSongFullSimple
import com.xon.aemusic.Model.Qualities
import com.xon.aemusic.Model.Quality
import com.xon.aemusic.Presenter.PrePlayer

import com.xon.aemusic.R
import com.xon.aemusic.Resposility.BlurImage
import com.xon.aemusic.Resposility.SingletonPlayMusic
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.wasabeef.picasso.transformations.BlurTransformation


/**
 * A simple [Fragment] subclass.
 */
class Player : Fragment() ,ViewPlayer, View.OnClickListener{


    lateinit var mView : View
    lateinit var mContextAc : Context
    lateinit var mPrePlayer : ImpPlayer
    lateinit var mSong : DataSongFullSimple

    lateinit var mNext : ImageButton
    lateinit var mPrevious : ImageButton
    lateinit var mPlay : ImageButton
    lateinit var mImage : ImageView
    lateinit var mTitle : TextView
    lateinit var mSingleton : SingletonPlayMusic
    lateinit var mLayoutBackground : ImageView
    lateinit var mQuality : TextView
    lateinit var mMenu : PopupMenu
    var mIsPlay : Boolean = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView =inflater!!.inflate(R.layout.fragment_player, container, false)
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPrePlayer = PrePlayer(this)
        mSingleton = SingletonPlayMusic.instance

        mSingleton.mPlayer.setOnCompletionListener(object : MediaPlayer.OnCompletionListener{
            override fun onCompletion(p0: MediaPlayer?) {
                mSingleton.nextPlayer()


            }
        })

        init()

    }

    private fun init() {

        mNext = mView.findViewById<ImageButton>(R.id.nextplayer)
        mNext.setOnClickListener(this)
        mPrevious = mView.findViewById<ImageButton>(R.id.previousplayer)
        mPrevious.setOnClickListener(this)
        mPlay = mView.findViewById<ImageButton>(R.id.playpauseplayer)
        mPlay.setOnClickListener(this)
        mImage = mView.findViewById(R.id.thrumbailplayer)
        mTitle = mView.findViewById(R.id.songplayer)
        mLayoutBackground = mView.findViewById(R.id.layoutplayer)

        var url = arguments.getString("URL","")
        mSong = mPrePlayer.getDataFromView(url)

        //Dua ra man hinh
        mTitle.text = mSong.songName
        Picasso.with(mView.context).load(mSong.thrumbail).into(mImage)
        Picasso.with(mView.context).load(mSong.thrumbail)
                .transform(BlurTransformation(mView.context)).into(mLayoutBackground)

        mQuality = mView.findViewById(R.id.qualityplayer)
        mQuality.setOnClickListener(this)
        mMenu = PopupMenu(mView.context,mQuality)

        mMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(p0: MenuItem?): Boolean {
                if (p0!!.title == "Lossless") {
                    mSingleton.mDefaultQuailities = Qualities.Lossless
                    mSingleton.playMusic(mSingleton.mCurrentMusic)
                    mQuality.text = "Lossless"
                } else if (p0!!.title == "500 Kbps") {
                    mSingleton.mDefaultQuailities = Qualities.q500
                    mSingleton.playMusic(mSingleton.mCurrentMusic)
                    mQuality.text = "500 Kbps"
                } else if (p0!!.title == "320 Kbps") {
                    mSingleton.mDefaultQuailities = Qualities.q320
                    mSingleton.playMusic(mSingleton.mCurrentMusic)
                    mQuality.text = "320 Kbps"
                } else if (p0!!.title == "128 Kbps") {
                    mSingleton.mDefaultQuailities = Qualities.q128
                    mSingleton.playMusic(mSingleton.mCurrentMusic)
                    mQuality.text = "128 Kbps"
                }
                return true
            }
        })

        if (!mSingleton.mCurrentMusicNoSource.linkWebCSN.isNullOrEmpty()) {
            mSingleton.setCurrentMusicFromLinkCSN(mSingleton.mCurrentMusicNoSource)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe({e ->
                        e.q128 = mSong.source.q128
                        mSingleton.mCurrentMusicNoSource.source = e
                        mSingleton.mCurrentMusic = mSingleton.mCurrentMusicNoSource

                        setMenuFromQualities(mSingleton.mCurrentMusic)
                        mSingleton.playMusic(mSingleton.mCurrentMusic)
                        mQuality.text = Quality().covertQualitiesToText(mSingleton.mDefaultQualitiesTrustInstance)

                    })
        }
        else {
            mSingleton.mCurrentMusicNoSource.source.q128 = mSong.source.q128
            mSingleton.mCurrentMusic = mSingleton.mCurrentMusicNoSource

            setMenuFromQualities(mSingleton.mCurrentMusic)
            mSingleton.playMusic(mSingleton.mCurrentMusic)
            mQuality.text = Quality().covertQualitiesToText(mSingleton.mDefaultQualitiesTrustInstance)

        }



        mIsPlay = true
        mPlay.setImageResource(R.drawable.ic_pause_white_50dp)


    }


    fun setMenuFromQualities(song :DataSongFullSimple){
        mMenu.menu.clear()
        if (!song.source.q128.isNullOrEmpty()) {
            mMenu.menu.add(Menu.NONE,1,Menu.NONE,"128 Kbps")
        }
        if (!song.source.q320.isNullOrEmpty()) {
            mMenu.menu.add(Menu.NONE,1,Menu.NONE,"320 Kbps")
        }
        if (!song.source.q500.isNullOrEmpty()) {
            mMenu.menu.add(Menu.NONE,1,Menu.NONE,"500 Kbps")
        }
        if (!song.source.lossless.isNullOrEmpty()) {
            mMenu.menu.add(Menu.NONE,1,Menu.NONE,"Lossless")
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.playpauseplayer -> {
                if (mIsPlay) {
                    mIsPlay = false
                    mSingleton.PausePlayer()
                    mPlay.setImageResource(R.drawable.ic_play_arrow_white_50dp)
                } else {
                    mIsPlay = true
                    mSingleton.continuePlayer()
                    mPlay.setImageResource(R.drawable.ic_pause_white_50dp)
                }
            }
            R.id.previousplayer -> {
                var song = mSingleton.previousPlayer()
                mIsPlay = true
                if (song != null) {
                    mTitle.text = song!!.songName
                }

                mPlay.setImageResource(R.drawable.ic_pause_white_50dp)
            }
            R.id.nextplayer -> {
                var song = mSingleton.nextPlayer()
                mIsPlay = true
                if (song != null) {
                    mTitle.text = song!!.songName
                }
                mPlay.setImageResource(R.drawable.ic_pause_white_50dp)
            }
            R.id.qualityplayer -> {
                mMenu.show()
            }
        }
    }
}// Required empty public constructor
