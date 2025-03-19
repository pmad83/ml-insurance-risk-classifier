package pl.pm.mlinsuranceriskclassifier.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pm.mlinsuranceriskclassifier.entity.Klient;
import pl.pm.mlinsuranceriskclassifier.ml.RiskClassifier;
import pl.pm.mlinsuranceriskclassifier.repository.KlientRepository;

import java.util.HashMap;
import java.util.Map;

/*
 * Klasa KlientController to kontroler REST API obsługujący operacje klasyfikacji ryzyka ubezpieczeniowego.
 */
@RestController
@RequestMapping("/api/klienci")
public class KlientController {

    private static final Logger logger = LoggerFactory.getLogger(KlientController.class);
    private static final Map<Integer, String> RISK_LEVEL_MAP = new HashMap<>();

    static {
        RISK_LEVEL_MAP.put(1, "niskie");
        RISK_LEVEL_MAP.put(2, "średnie");
        RISK_LEVEL_MAP.put(3, "wysokie");
        RISK_LEVEL_MAP.put(4, "bardzo wysokie");
    }
    private final RiskClassifier classifier;
    private final KlientRepository klientRepository;

    @Autowired
    public KlientController(KlientRepository klientRepository) {

        this.klientRepository = klientRepository;
        this.classifier = new RiskClassifier();
    }

    @PostMapping("/klasyfikuj")
    public ResponseEntity<String> klasyfikuj(@RequestBody Klient klient) {
        Integer wynik = classifier.classify(klient);

        if (wynik != null) {
            String wynikOpisowy = "Ryzyko ubezpieczenia (" + wynik + ") jest " + mapRiskLevel(wynik);

            klient.setId(null);
            klient.setRyzyko(wynik);
            klientRepository.save(klient);
            logger.info("Zapisano nowego klienta; ID: {}", klient.getId());

            return ResponseEntity.status(HttpStatus.OK).body(wynikOpisowy);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nie można oszacować ryzyka");
        }
    }

    private String mapRiskLevel(double riskValue) {
        int level = (int) Math.round(riskValue);
        return RISK_LEVEL_MAP.getOrDefault(level, "nieznane");
    }
}
