package pl.pm.mlinsuranceriskclassifier.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pm.mlinsuranceriskclassifier.entity.Klient;
import pl.pm.mlinsuranceriskclassifier.ml.RiskClassifier;

/*
 * Klasa KlientController to kontroler REST API obsługujący operacje klasyfikacji ryzyka ubezpieczeniowego.
 */
@RestController
@RequestMapping("/api/klienci")
public class KlientController {

    private final RiskClassifier classifier;

    public KlientController() {

        this.classifier = new RiskClassifier();
    }

    @PostMapping("/klasyfikuj")
    public ResponseEntity<String> klasyfikuj(@RequestBody Klient klient) {
        String wynik = classifier.classify(klient);
        return ResponseEntity.status(HttpStatus.OK).body(wynik);
    }
}
