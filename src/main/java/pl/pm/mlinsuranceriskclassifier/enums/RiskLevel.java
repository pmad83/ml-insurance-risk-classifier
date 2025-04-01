package pl.pm.mlinsuranceriskclassifier.enums;

import java.util.HashMap;
import java.util.Map;

/*
 * Klasa RiskLevel zawiera stałe reprezentujące poziomy ryzyka ubezpieczeniowego.
 */
public class RiskLevel {
    private static final Map<Integer, String> RISK_LEVEL_MAP = new HashMap<>();

    static {
        RISK_LEVEL_MAP.put(1, "niskie");
        RISK_LEVEL_MAP.put(2, "średnie");
        RISK_LEVEL_MAP.put(3, "wysokie");
        RISK_LEVEL_MAP.put(4, "bardzo wysokie");
    }

    public static String mapRiskLevel(double riskValue) {
        int level = (int) Math.round(riskValue);
        return RISK_LEVEL_MAP.getOrDefault(level, "nieznane");
    }
}
