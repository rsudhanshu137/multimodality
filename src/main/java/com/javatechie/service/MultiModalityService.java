package com.javatechie.service;

import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

@Service
public class MultiModalityService {


    private final OpenAiAudioTranscriptionModel audioTranscriptionModel;

    private final OpenAiAudioSpeechModel aiAudioSpeechModel;

    private final OpenAiImageModel openAiImageModel;

    public MultiModalityService(OpenAiAudioTranscriptionModel audioTranscriptionModel, OpenAiAudioSpeechModel aiAudioSpeechModel, OpenAiImageModel openAiImageModel) {
        this.audioTranscriptionModel = audioTranscriptionModel;
        this.aiAudioSpeechModel = aiAudioSpeechModel;
        this.openAiImageModel = openAiImageModel;
    }

    // convert voice/audio/speech to text
    public String convertAudioToText(Resource audioFile){
        return audioTranscriptionModel.transcribe(audioFile);
    }

    // convert text to audio
    public String convertTextToAudio(String text) throws IOException {
        OpenAiAudioSpeechOptions options=OpenAiAudioSpeechOptions
                .builder()
                .voice(OpenAiAudioSpeechOptions.Voice.SHIMMER)
                .build();

        byte[] audioContentBytes= aiAudioSpeechModel.call(new TextToSpeechPrompt(text,options))
                .getResult()
                .getOutput();
        Path audioPath=Path.of("ai_geneated_audio.mp3");
        Files.write(audioPath,audioContentBytes);
        return "Audio generated and saved at path " + audioPath.toAbsolutePath();

    }

    // convert Text to image
    public byte[] generateImageFromText(String input){

        // gpt-image-1 family: no style/response_format params, quality is low|medium|high|auto
        OpenAiImageOptions options=OpenAiImageOptions
                .builder()
                .quality("high")
                .build();

        var image = openAiImageModel.call(new ImagePrompt(input,options))
                .getResult()
                .getOutput();

        // gpt-image-1 models only return base64; dall-e-2/3 return a url instead
        return Base64.getDecoder().decode(image.getB64Json());
    }


}
