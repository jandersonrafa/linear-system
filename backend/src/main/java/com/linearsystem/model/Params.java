package com.linearsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Params {
    private Integer maxLoop;
    private BigDecimal computerError;
    private BigDecimal[][] matriz;
    private Integer totalVariables;
    private Integer millisecondsInterval;
}