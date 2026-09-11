package android.speech.tts;

/**
 * android.speech.tts.UtteranceProgressListener（Java 版——平台类型，让 app 覆写时用 String 或 String? 都能匹配）。
 * Nova 注：回调参数用平台 String! 语义，避免 Kotlin String?/String 覆写不匹配。
 */
public abstract class UtteranceProgressListener {
    public abstract void onStart(String utteranceId);
    public abstract void onDone(String utteranceId);

    @Deprecated
    public abstract void onError(String utteranceId);

    public void onError(String utteranceId, int errorCode) {
        onError(utteranceId);
    }

    public void onStop(String utteranceId, boolean interrupted) {}
    public void onBeginSynthesis(String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) {}
    public void onAudioAvailable(String utteranceId, byte[] audio) {}
    public void onRangeStart(String utteranceId, int start, int end, int frame) {}
    public void onRangeSpeechRequest(String utteranceId, int start, int end, int frame) {}
}
