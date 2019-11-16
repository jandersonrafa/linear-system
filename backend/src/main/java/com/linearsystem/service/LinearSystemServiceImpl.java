package com.linearsystem.service;

import com.linearsystem.model.Params;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LinearSystemServiceImpl implements LinearSystemService {

    @Override
    @Async
    public BigDecimal[] calculate(Params params, SseEmitter sseEmitter) {
        try {
            int matrizSize = params.getMatriz().length;
            BigDecimal[][] pivotamento = pivotamento(params.getMatriz(), matrizSize);
            BigDecimal total = params.getMatriz()[0][matrizSize];
            BigDecimal[] resultadosEtapas = metodoGaus(pivotamento, matrizSize, sseEmitter, params, total);
            sseEmitter.complete();
            return resultadosEtapas;
        } catch (Exception ex) {
            sseEmitter.completeWithError(ex);
            return null;
        }
    }

    private void printMatriz(BigDecimal[] resultadosEtapas) {
        for (int line = 0; line < resultadosEtapas.length; line++) {
            System.out.println("X" + line + 1 + ": " + resultadosEtapas[line] + "|");
        }
    }

    private BigDecimal[] metodoGaus(BigDecimal[][] pivotamento, int matrizSize, SseEmitter sseEmitter, Params params, BigDecimal total) {
        BigDecimal[] resultadosEtapas = new BigDecimal[matrizSize];
        for (int column = 0; column < matrizSize; column++) {
            resultadosEtapas[column] = BigDecimal.ZERO;
        }
        recursiveGauss(pivotamento, matrizSize, resultadosEtapas, 0, sseEmitter, params, total);
        return resultadosEtapas;
    }

    private void sendEmitter(SseEmitter sseEmitter, Object resultadosEtapas, Params params) {
        try {
            final Integer millisecondsInterval = params.getMillisecondsInterval();
            sseEmitter.send(resultadosEtapas);
            if (millisecondsInterval != null && millisecondsInterval > 0) {
                Thread.sleep(millisecondsInterval);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void recursiveGauss(BigDecimal[][] pivotamento, int matrizSize, BigDecimal[] resultadoEtapaAnterior, int interacoes, SseEmitter sseEmitter, Params params, BigDecimal total) {
        ++interacoes;
//        BigDecimal[] resultadosEtapaAtual = new BigDecimal[pivotamento.length];
        BigDecimal error = calculateError(resultadoEtapaAnterior, params.getMatriz(), total);
        sendEmitter(sseEmitter, createMessage(resultadoEtapaAnterior, interacoes, error), params);
        if (interacoes >= params.getMaxLoop() || error.compareTo(params.getComputerError()) != 1) {
            return;
        } else {
            for (int line = 0; line < matrizSize; line++) {
                BigDecimal columnOne = pivotamento[line][1];
                BigDecimal resultLine = columnOne;
                for (int column = 2; column <= matrizSize; column++) {
                    BigDecimal[] resultadoEtapaAnteriorRemoveLinhaAtual = new BigDecimal[resultadoEtapaAnterior.length - 1];
                    int countLine = 0;
                    for (int lineResultadoEtapaAnterior = 0; lineResultadoEtapaAnterior < resultadoEtapaAnterior.length; lineResultadoEtapaAnterior++) {
                        if (lineResultadoEtapaAnterior != line) {
                            resultadoEtapaAnteriorRemoveLinhaAtual[countLine++] = resultadoEtapaAnterior[lineResultadoEtapaAnterior];
                        }
                    }
                    BigDecimal columnMultiplyResultEtapaAnterior = pivotamento[line][column].multiply(resultadoEtapaAnteriorRemoveLinhaAtual[column - 2]);
                    resultLine = resultLine.add(columnMultiplyResultEtapaAnterior);
                }
                resultadoEtapaAnterior[line] = resultLine;
            }
            recursiveGauss(pivotamento, matrizSize, resultadoEtapaAnterior, interacoes, sseEmitter, params, total);
        }

    }

    private BigDecimal calculateError(BigDecimal[] resultadoEtapaAnterior, BigDecimal[][] matrizOriginal, BigDecimal total) {
        BigDecimal totalCalculated = BigDecimal.ZERO;
        for (int i = 0; i < resultadoEtapaAnterior.length; i++) {
            totalCalculated = totalCalculated.add(matrizOriginal[0][i].multiply(resultadoEtapaAnterior[i]));
        }
        return total.abs().subtract(totalCalculated.abs()).abs();
    }

    private Object createMessage(BigDecimal[] resultadoEtapaAnterior, Integer position, BigDecimal totalCalculated) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(position + "-");
        for (int i = 0; i < resultadoEtapaAnterior.length; i++) {
            stringBuilder.append("X" + (i + 1) + ": ");
            stringBuilder.append(resultadoEtapaAnterior[i].setScale(8, RoundingMode.DOWN));
            stringBuilder.append("|   ");
        }
        stringBuilder.append("|   Erro: " + totalCalculated.setScale(8, RoundingMode.DOWN));
        return stringBuilder.toString();
    }

    private BigDecimal[][] pivotamento(BigDecimal[][] matriz, Integer matrizSize) {
        BigDecimal[][] novaMatriz = new BigDecimal[matrizSize][matrizSize + 1];


        for (int line = 0; line < matrizSize; line++) {
            BigDecimal valueDiagonal = matriz[line][line];
            int position = 0;
            novaMatriz[line][position++] = BigDecimal.valueOf(line);
            BigDecimal resultEquation = matriz[line][matrizSize];
            novaMatriz[line][position++] = resultEquation.divide(valueDiagonal, 30, RoundingMode.DOWN);
            for (int column = 0; column < matrizSize; column++) {
                BigDecimal valueLineColumn = matriz[line][column];
                if (line != column) {
                    novaMatriz[line][position++] = (valueLineColumn.multiply(BigDecimal.valueOf(-1.0))).divide(valueDiagonal, 30, RoundingMode.DOWN);
                    ;
                }
            }
        }
        return novaMatriz;
    }

    @Override
    public boolean converge(BigDecimal[][] matriz) {
        int matrizSize = matriz.length;
        BigDecimal[] beta = new BigDecimal[matrizSize];
        for (int line = 0; line < matrizSize; line++) {
            BigDecimal sumColums = BigDecimal.ZERO;
            BigDecimal valueDiagonal = BigDecimal.ZERO;
            Integer positionBeta = 0;
            for (int column = 0; column < matrizSize; column++) {
                BigDecimal valueLineColumn = matriz[line][column].abs();
                if (line != column) {
                    BigDecimal valuePositionBeforeBeta = getPositionBeta(beta, positionBeta);
                    sumColums = sumColums.add(valuePositionBeforeBeta != null ? valueLineColumn.multiply(valuePositionBeforeBeta) : valueLineColumn);
                } else {
                    valueDiagonal = valueLineColumn;
                }
            }
            beta[line] = valueDiagonal.compareTo(BigDecimal.ZERO) == 1 ? sumColums.divide(valueDiagonal, 30, RoundingMode.DOWN) : new BigDecimal(10000.0);
            if (beta[line].compareTo(BigDecimal.ONE) == 1) {
                throw new RuntimeException("Não converge");
            }
        }
        return true;
    }

    private BigDecimal getPositionBeta(BigDecimal[] beta, Integer positionBeta) {
        BigDecimal valueBeforeBeta = beta[positionBeta];
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
