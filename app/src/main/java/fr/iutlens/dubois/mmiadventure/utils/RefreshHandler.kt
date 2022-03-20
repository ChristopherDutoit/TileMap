package fr.iutlens.dubois.mmiadventure.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

/**
 * Created by dubois on 27/12/2017.
 */
class RefreshHandler(animator: TimerAction) : Handler(Looper.getMainLooper()) {
    private val weak: WeakReference<TimerAction> = WeakReference<TimerAction>(animator)
    override fun handleMessage(msg: Message) {
        weak.get()?.apply { update() }
    }

    fun scheduleRefresh(delayMillis: Long) {
        this.removeMessages(0)
        sendMessageDelayed(obtainMessage(0), delayMillis)
    }

    fun isRunning(): Boolean {
        return this.hasMessages(0)
    }

}