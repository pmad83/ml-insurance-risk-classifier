package pl.pm.mlinsuranceriskclassifier.ml;

import java.io.*;
import java.nio.file.Paths;

import com.oracle.labs.mlrg.olcut.provenance.ProvenanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tribuo.*;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.dtree.CARTClassificationTrainer;
import org.tribuo.classification.dtree.impurity.GiniIndex;
import org.tribuo.classification.evaluation.LabelEvaluation;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.data.csv.CSVIterator;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.evaluation.TrainTestSplitter;
import org.tribuo.impl.ArrayExample;
import pl.pm.mlinsuranceriskclassifier.entity.Customer;

/*
 * Klasa RiskClassifier służy do klasyfikacji ryzyka ubezpieczeniowego.
 */
public class RiskClassifier {
    private static final Logger logger = LoggerFactory.getLogger(RiskClassifier.class);
    private static final String[] FEATURE_NAMES = new String[] {"age", "accidents_qty", "insurance_sum", "claims_history"};
    private static final String TRAINING_DATA = "src/main/resources/data/training_data.csv";

    private Model<Label> model;
    private Trainer<Label> trainer;
    private MutableDataset<Label> trainSet;
    private MutableDataset<Label> testSet;

    public RiskClassifier() {
        createTrainer();
        try {
            createDatasets();
            trainAndEvaluate();
        } catch (IOException e) {
            logger.error("Błąd podczas próby wczytania danych uczenia: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Integer classify(Customer customer) {
        Label unknownOutput = new LabelFactory().getUnknownOutput();

        Example<Label> inputData = new ArrayExample<>(unknownOutput, FEATURE_NAMES, getFeatureValues(customer));

        Prediction<Label> prediction = model.predict(inputData);
        Label predictedLabel = prediction.getOutput();
        String label = predictedLabel.getLabel();
        logger.info("Wynik klasyfikacji = {}", label);

        try {
            return (int) Double.parseDouble(label);
        } catch (NumberFormatException e) {
            logger.error("Niepoprawny format liczby: {}", e.getMessage());
            return null;
        }
    }

    private double[] getFeatureValues(Customer customer) {
        logger.info("klient.getAge() = {}, klient.getAccidentsQty() = {}, klient.getInsuranceSum() = {}, klient.getClaimsHistory() = {}",
                customer.getAge(), customer.getAccidentsQty(), customer.getInsuranceSum(), customer.getClaimsHistory());
        return new double[] {customer.getAge(), customer.getAccidentsQty(), customer.getInsuranceSum(), customer.getClaimsHistory()};
    }

    private void createTrainer() {
        logger.info("Tworzenie trenera....");
        trainer = new CARTClassificationTrainer(
                5,               // maxDepth: Maksymalna głębokość drzewa decyzyjnego. Określa liczbę poziomów w drzewie. Im wyższa wartość, tym drzewo jest bardziej skomplikowane.
                5.0F,            // minChildWeight: Minimalna liczba próbek wymagana do podziału węzła. Kontroluje minimalną wielkość danych, które są potrzebne do dalszego podziału.
                0.0F,            // minImpurityDecrease: Minimalny spadek nieczystości, który musi wystąpić, aby podjąć decyzję o podziale. Używane do zapobiegania zbyt małym podziałom.
                1.0F,            // fractionFeaturesInSplit: Frakcja cech, które będą brane pod uwagę podczas podejmowania decyzji o podziale węzła. Umożliwia losowe próbkowanie cech.
                true,            // useRandomSplitPoints: Flaga określająca, czy mają być używane losowe punkty podziału w węzłach. Pomaga to we wprowadzeniu losowości do modelu.
                new GiniIndex(), // impurity: Miara nieczystości używana do oceny jakości podziału w węzłach. Gini Index ocenia czystość klasyfikacji w danym węźle.
                12345L           // seed: Ziarno dla generatora liczb losowych, zapewniające powtarzalność wyników dla tego samego zestawu danych.
        );
    }

    private void createDatasets() throws IOException {
        logger.info("Tworzenie zbiorów danych...");

        LabelFactory labelFactory = new LabelFactory();
        CSVLoader<Label> csvLoader = new CSVLoader<>(';', CSVIterator.QUOTE, labelFactory);
        DataSource<Label> dataSource = csvLoader.loadDataSource(Paths.get(TRAINING_DATA), "risk_level");

        TrainTestSplitter<Label> dataSplitter = new TrainTestSplitter<>(dataSource,0.7,1L);

        trainSet = new MutableDataset<>(dataSplitter.getTrain());
        logger.info(String.format("Rozmiar zbioru treningowego = %d, liczba cech = %d", trainSet.size(), trainSet.getFeatureMap().size()));

        testSet = new MutableDataset<>(dataSplitter.getTest());
        logger.info(String.format("Rozmiar zbioru testowego = %d, liczba cech = %d", testSet.size(), testSet.getFeatureMap().size()));
    }

    private void trainAndEvaluate() {
        logger.info("Trenowanie modelu...");

        model = trainer.train(trainSet);
        evaluate(model, "zbiór treningowy", trainSet);

        logger.info("Testowanie modelu...");
        evaluate(model, "zbiór testowy", testSet);

        logger.debug("Pochodzenie zbioru danych:");
        logger.debug(ProvenanceUtil.formattedProvenanceString(model.getProvenance().getDatasetProvenance()));
        logger.debug("Pochodzenie trenera:");
        logger.debug(ProvenanceUtil.formattedProvenanceString(model.getProvenance().getTrainerProvenance()));

        logger.debug("Mapy identyfikatorów cech modelu\n{}", model.getFeatureIDMap().toReadableString());
    }

    private void evaluate(Model<Label> model, String datasetName, Dataset<Label> dataset) {
        LabelEvaluation evaluation = new LabelEvaluator().evaluate(model, dataset);
        logger.info("Ocena modelu ({})\n{} ", datasetName, evaluation.toString());
    }
}
