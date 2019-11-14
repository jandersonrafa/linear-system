package com.linearsystem.service;

import com.linearsystem.model.Params;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
public class LinearSystemServiceImpl implements LinearSystemService {

    @Override
    public Double[] calculate(Params params, SseEmitter sseEmitter) {
        int matrizSize = params.getMatriz().length;

        boolean converge = converge(params.getMatriz());
        if (converge) {

            Double[][] pivotamento = pivotamento(params.getMatriz(), matrizSize);
            Double[] resultadosEtapas = metodoGaus(pivotamento, matrizSize, sseEmitter, params.getMaxLoop(), params.getRate());
            printMatriz(resultadosEtapas);
            return resultadosEtapas;
        } else {
            throw new RuntimeException("Não converge");
        }
    }

    private void printMatriz(Double[] resultadosEtapas) {
        for (int line = 0; line < resultadosEtapas.length; line++) {
            System.out.println("X" + line + 1 + ": " + resultadosEtapas[line] + "|");
        }
    }

    private Double[] metodoGaus(Double[][] pivotamento, int matrizSize, SseEmitter sseEmitter, Integer maxLoop, Long rate) {
        Double[][] matrizEtapa = new Double[matrizSize][matrizSize + 1];
        Double[] resultadosEtapas = new Double[matrizSize];
        for (int column = 0; column < matrizSize; column++) {
            resultadosEtapas[column] = 0.0;
        }
        recursiveGauss(pivotamento, matrizSize, resultadosEtapas, 0, sseEmitter, maxLoop, rate);
        return resultadosEtapas;
    }

    private void sendEmitter(SseEmitter sseEmitter, Object resultadosEtapas) {
        try {
            sseEmitter.send(resultadosEtapas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recursiveGauss(Double[][] pivotamento, int matrizSize, Double[] resultadoEtapaAnterior, int interacoes, SseEmitter sseEmitter, Integer maxLoop, Long rate) {
        ++interacoes;
//        Double[] resultadosEtapaAtual = new Double[pivotamento.length];
        if (interacoes >= maxLoop) {
            sendEmitter(sseEmitter, resultadoEtapaAnterior);
            return;
        } else {
            String message = "";
            for (int line = 0; line < matrizSize; line++) {
                Double columnOne = pivotamento[line][1];
                Double resultLine = columnOne;
                for (int column = 2; column <= matrizSize; column++) {
                    Double[] resultadoEtapaAnteriorRemoveLinhaAtual = new Double[resultadoEtapaAnterior.length - 1];
                    int countLine = 0;
                    for (int lineResultadoEtapaAnterior = 0; lineResultadoEtapaAnterior < resultadoEtapaAnterior.length; lineResultadoEtapaAnterior++) {
                        if (lineResultadoEtapaAnterior != line) {
                            resultadoEtapaAnteriorRemoveLinhaAtual[countLine++] = resultadoEtapaAnterior[lineResultadoEtapaAnterior];
                        }
                    }
                    Double columnMultiplyResultEtapaAnterior = pivotamento[line][column] * resultadoEtapaAnteriorRemoveLinhaAtual[column - 2];
                    resultLine += columnMultiplyResultEtapaAnterior;
                }
                message+=resultLine+"|";
                resultadoEtapaAnterior[line] = resultLine;
            }
            sendEmitter(sseEmitter, message);
            recursiveGauss(pivotamento, matrizSize, resultadoEtapaAnterior, interacoes, sseEmitter, maxLoop, rate);
        }

    }

    private Double[][] pivotamento(Double[][] matriz, Integer matrizSize) {
        Double[][] novaMatriz = new Double[matrizSize][matrizSize + 1];


        for (int line = 0; line < matrizSize; line++) {
            Double valueDiagonal = matriz[line][line];
            int position = 0;
            novaMatriz[line][position++] = Double.valueOf(String.valueOf(line));
            Double resultEquation = matriz[line][matrizSize];
            novaMatriz[line][position++] = resultEquation / valueDiagonal;
            for (int column = 0; column < matrizSize; column++) {
                Double valueLineColumn = matriz[line][column];
                if (line != column) {
                    novaMatriz[line][position++] = (valueLineColumn * -1.0) / valueDiagonal;
                    ;
                }
            }
        }
        return novaMatriz;
    }

    private boolean converge(Double[][] matriz) {
        int matrizSize = matriz.length;
        Double[] beta = new Double[matrizSize];
        for (int line = 0; line < matrizSize; line++) {
            Double sumColums = 0.0;
            Double valueDiagonal = 0.0;
            Integer positionBeta = 0;
            for (int column = 0; column < matrizSize; column++) {
                Double valueLineColumn = matriz[line][column];
                if (line != column) {
                    Double valuePositionBeforeBeta = getPositionBeta(beta, positionBeta);
                    sumColums += (valuePositionBeforeBeta != null ? valueLineColumn * valuePositionBeforeBeta : valueLineColumn);
                } else {
                    valueDiagonal = valueLineColumn;
                }
            }
            beta[line] = valueDiagonal > 0.0 ? sumColums / valueDiagonal : 10000.0;
            if (beta[line] > 1.0) {
                return false;
            }
        }
        return true;
    }

    private Double getPositionBeta(Double[] beta, Integer positionBeta) {
        Double valueBeforeBeta = beta[positionBeta];
        positionBeta = positionBeta + 1;
        return valueBeforeBeta;
    }
//    public List<int[]> generate(int n, int r) {
//        List<int[]> combinations = new ArrayList<>();
//        helper(combinations, new int[r], 0, n - 1, 0);
//        return combinations;
//    }

//    private void helper(List<int[]> combinations, int data[], int start, int end, int index) {
//        if (index == data.length) {
//            int[] combination = data.clone();
//            combinations.add(combination);
//        } else {
//            int max = Math.min(end, end + 1 - data.length + index);
//            for (int i = start; i <= max; i++) {
//                data[index] = i;
//                helper(combinations, data, i + 1, end, index + 1);
//            }
//        }
//    }
}
