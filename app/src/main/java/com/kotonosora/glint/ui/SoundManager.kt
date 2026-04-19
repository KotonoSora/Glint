package com.kotonosora.glint.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.kotonosora.glint.R

class SoundManager(context: Context) {
    private val soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        soundMap["tap"] = soundPool.load(context, R.raw.tap, 1)
        soundMap["error"] = soundPool.load(context, R.raw.error, 1)
        soundMap["win"] = soundPool.load(context, R.raw.win, 1)
        soundMap["lose"] = soundPool.load(context, R.raw.lose, 1)
    }

    fun playSound(name: String) {
        val soundId = soundMap[name] ?: return
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }
}
