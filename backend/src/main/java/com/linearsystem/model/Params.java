package com.linearsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Params {
    private Integer maxLoop;
    private Long rate;
    private Double[][] matriz;
}