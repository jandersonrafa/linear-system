package com.linearsystem.service;

import com.linearsystem.model.Params;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;

public interface LinearSystemService {

    public BigDecimal[] calculate(Params matriz, SseEmitter sseEmitter);

    public boolean converge(BigDecimal[][] matriz);
}
