package com.example.hunterPreyPredator;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by mateusz on 27.04.17.
 */
@Data
public class ReplacesMapForFile implements Serializable {
    Map<String, String> replaces;
}
