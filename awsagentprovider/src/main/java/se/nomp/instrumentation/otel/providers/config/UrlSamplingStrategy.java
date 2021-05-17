package se.nomp.instrumentation.otel.providers.config;

public class UrlSamplingStrategy {
    String url;
    ProbabilisticSamplingStrategy probabilisticSampling;

    public String getUrl() {
        return url;
    }

    public ProbabilisticSamplingStrategy getProbabilisticSampling() {
        return probabilisticSampling;
    }

}
