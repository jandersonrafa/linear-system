package com.linearsystem.controller;


import com.linearsystem.model.Params;
import com.linearsystem.service.LinearSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/gauss")
public class GaussController {

    @Autowired
    private LinearSystemService linearSystemService;

    @GetMapping
    public List<Params> getAllEmployees() {
        return null;
    }

    @PutMapping("/delete/{id}")
    public void delete(@PathVariable("id") String id) {
    }

    @PostMapping("/simulate")
    public void simulate() {
        BigDecimal[][] matriz = new BigDecimal[3][4];
        matriz[0][0] = BigDecimal.valueOf(10.0);
        matriz[0][1] =  BigDecimal.valueOf(2.0);
        matriz[0][2] =  BigDecimal.valueOf(1.0);
        matriz[0][3] =  BigDecimal.valueOf(7.0);
        matriz[1][0] =  BigDecimal.valueOf(1.0);
        matriz[1][1] =  BigDecimal.valueOf(5.0);
        matriz[1][2] =  BigDecimal.valueOf(1.0);
        matriz[1][3] =  BigDecimal.valueOf(-8.0);
        matriz[2][0] =  BigDecimal.valueOf(2.0);
        matriz[2][1] =  BigDecimal.valueOf(3.0);
        matriz[2][2] =  BigDecimal.valueOf(10.0);
        matriz[2][3] =  BigDecimal.valueOf(6.0);
        SseEmitter sseEmitter = null;
//        linearSystemService.calculate(matriz, sseEmitter);
    }

    private static Params params;
    private static String lastCode;

    @PostMapping("/calculate")
    public String calculate(@RequestBody Params paramsUrl) {
        linearSystemService.convergePorMetodoSassenfeld(paramsUrl.getMatriz());
        params = paramsUrl;
        String nextCode = String.valueOf(new Random().nextInt());
        lastCode = nextCode;
        return nextCode;
    }

    @GetMapping("/emitter/{code}")
    public SseEmitter enableNotifier(@PathVariable("code") String code) throws IOException {
        if (code.equals(lastCode)) {
            SseEmitter sseEmitter = new SseEmitter(86400000L);
            linearSystemService.calculate(params, sseEmitter);
            return sseEmitter;
        }
        return null;
    }

}
