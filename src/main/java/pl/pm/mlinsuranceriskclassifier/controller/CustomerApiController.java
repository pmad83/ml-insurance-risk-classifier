package pl.pm.mlinsuranceriskclassifier.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pm.mlinsuranceriskclassifier.entity.Customer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;
import pl.pm.mlinsuranceriskclassifier.enums.RiskLevel;
import pl.pm.mlinsuranceriskclassifier.ml.RiskClassifier;
import pl.pm.mlinsuranceriskclassifier.repository.CustomerRepository;

/*
 * Klasa CustomerApiController to kontroler REST API obsługujący klasyfikację ryzyka.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-04-01T20:51:40.687523300+02:00[Europe/Berlin]", comments = "Generator version: 7.12.0")
@Controller
@RequestMapping("${openapi.customerRiskClassifier.base-path:}")
public class CustomerApiController implements CustomerApi {

    private final NativeWebRequest request;
    private static final Logger logger = LoggerFactory.getLogger(CustomerApiController.class);
    private final RiskClassifier classifier;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerApiController(NativeWebRequest request, CustomerRepository customerRepository) {

        this.request = request;
        this.customerRepository = customerRepository;
        this.classifier = new RiskClassifier();
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<String> classifyCustomer(Customer customer) {
        Integer wynik = classifier.classify(customer);

        if (wynik != null) {
            String wynikOpisowy = "Ryzyko ubezpieczenia (" + wynik + ") jest " + RiskLevel.mapRiskLevel(wynik);

            customer.setId(null);
            customer.setRiskLevel(wynik);
            customerRepository.save(customer);
            logger.info("Zapisano nowego klienta; ID: {}", customer.getId());

            return ResponseEntity.status(HttpStatus.OK).body(wynikOpisowy);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nie można oszacować ryzyka");
        }
    }
}
