package com.linearsystem.controller;


import com.linearsystem.model.Params;
import com.linearsystem.service.LinearSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
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
        Double[][] matriz = new Double[3][4];
        matriz[0][0] = 10.0;
        matriz[0][1] = 2.0;
        matriz[0][2] = 1.0;
        matriz[0][3] = 7.0;
        matriz[1][0] = 1.0;
        matriz[1][1] = 5.0;
        matriz[1][2] = 1.0;
        matriz[1][3] = -8.0;
        matriz[2][0] = 2.0;
        matriz[2][1] = 3.0;
        matriz[2][2] = 10.0;
        matriz[2][3] = 6.0;
        SseEmitter sseEmitter = null;
        linearSystemService.calculate(matriz, sseEmitter);
    }

    private static Params params;
    private static String lastCode;

    @PostMapping("/calculate")
    public String calculate(@RequestBody Params paramsUrl) {
        params = paramsUrl;
        String nextCode = String.valueOf(new Random().nextInt());
        lastCode = nextCode;
        return nextCode;
    }

    @GetMapping("/emitter/{code}")
    public SseEmitter enableNotifier(@PathVariable("code") String code) throws IOException {
        if (code.equals(lastCode)) {
            SseEmitter sseEmitter = new SseEmitter(86400000L);
            Double[] calculate = linearSystemService.calculate(params, sseEmitter);
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .data(calculate)
                    .id("aqui")
                    .name("sse event - mvc");
            sseEmitter.send(calculate);
            return sseEmitter;
        }
        return null;
    }

}
