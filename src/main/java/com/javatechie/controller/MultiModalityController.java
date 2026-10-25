package com.javatechie.controller;

import com.javatechie.service.MultiModalityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/multimodality")
public class MultiModalityController {

    private final MultiModalityService multiModalityService;

    @Value("classpath:ttsmaker-file.mp3")
    private Resource audioFile;

    public MultiModalityController(MultiModalityService multiModalityService) {
        this.multiModalityService = multiModalityService;
    }

    @GetMapping("/audio-to-text")
    public String convertAudioToText(){
        return multiModalityService.convertAudioToText(audioFile);
    }

    @GetMapping("/text-to-audio")
    public String convertTextToAudio(@RequestParam  String text) throws IOException {
        return multiModalityService.convertTextToAudio(text);
    }

    @GetMapping(value = "/text-to-image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateTextFromImage(@RequestParam  String prompt) throws IOException {
        return multiModalityService.generateImageFromText(prompt);
    }


}
