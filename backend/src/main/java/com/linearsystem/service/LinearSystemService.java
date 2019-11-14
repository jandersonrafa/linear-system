package com.linearsystem.service;

import com.linearsystem.model.Params;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface LinearSystemService {

    public Double[] calculate(Params matriz, SseEmitter sseEmitter);
}
