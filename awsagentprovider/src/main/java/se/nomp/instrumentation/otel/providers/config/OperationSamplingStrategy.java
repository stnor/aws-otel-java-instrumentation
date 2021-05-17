package se.nomp.instrumentation.otel.providers.config;

public class OperationSamplingStrategy {
    String operation;
    ProbabilisticSamplingStrategy probabilisticSampling;

    public String getOperation() {
        return operation;
    }

    public ProbabilisticSamplingStrategy getProbabilisticSampling() {
        return probabilisticSampling;
    }

}
