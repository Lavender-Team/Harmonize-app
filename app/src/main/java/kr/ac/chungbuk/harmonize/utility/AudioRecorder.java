package kr.ac.chungbuk.harmonize.utility;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

import java.io.IOException;

import lombok.Getter;

public class AudioRecorder {

    public interface RecordingCallback {
        void onRecordingStarted();
        void onVoiceDetacted();
        void onRecordingStopped();
    }

    private MediaRecorder mediaRecorder;
    private Handler handler = new Handler();
    private boolean isVoiceDetected = false;

    // 음성 감지 임계값
    private static final int VOICE_THRESHOLD = 2000;

    @Getter // 파일 액세스용 경로
    private String filePath;
    @Getter
    private boolean isRecording;
    @Getter
    private boolean isCompleted;

    private RecordingCallback callback; // 녹음 완료 후 실행

    // 녹음을 시작하고 목소리를 감지하면 10초 타이머를 시작
    public void startRecording(RecordingCallback callback) {
        isRecording = true;
        this.callback = callback;

        // 저장할 파일 경로 설정
        String fileName = "recording_" + System.currentTimeMillis() + ".m4a";
        filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/" + fileName;

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(filePath);

        callback.onRecordingStarted();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

            // 목소리를 감지하는 작업을 주기적으로 실행
            handler.postDelayed(this::detectVoice, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 목소리 감지
    private void detectVoice() {
        if (mediaRecorder == null) return;

        // 현재 음량(dB) 가져오기
        int amplitude = mediaRecorder.getMaxAmplitude();

        // 사용자의 목소리가 감지되었는지 확인
        if (amplitude > VOICE_THRESHOLD) {
            callback.onVoiceDetacted();

            if (!isVoiceDetected) {
                isVoiceDetected = true;
                // 목소리를 감지한 순간부터 20초 후 자동으로 녹음 중지
                handler.postDelayed(this::stopRecording, 20000);
                isCompleted = true;
            }
        } else {
            // 감지가 안 된 경우, 주기적으로 다시 감지
            handler.postDelayed(this::detectVoice, 100);
        }
    }

    // 녹음을 중지하는 메서드
    public void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isVoiceDetected = false; // 초기화
            isRecording = false;

            if (callback != null) {
                callback.onRecordingStopped();
            }
        }
    }
}
