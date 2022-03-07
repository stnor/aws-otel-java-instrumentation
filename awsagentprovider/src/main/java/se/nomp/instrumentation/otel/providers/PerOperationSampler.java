package se.nomp.instrumentation.otel.providers;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import se.nomp.instrumentation.otel.providers.config.UrlSamplingStrategy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link PerOperationSampler} samples spans per operation.
 */
class PerOperationSampler implements Sampler {

    private final Sampler defaultSampler;
    private final Map<String, Sampler> perOperationSampler;

    PerOperationSampler(
            Sampler defaultSampler, List<UrlSamplingStrategy> perOperationSampling) {
        this.defaultSampler = defaultSampler;
        this.perOperationSampler = new LinkedHashMap<>(perOperationSampling.size());
        for (UrlSamplingStrategy opSamplingStrategy : perOperationSampling) {
            this.perOperationSampler.put(
                    opSamplingStrategy.getUrl(),
                    Sampler.traceIdRatioBased(
                            opSamplingStrategy.getProbabilisticSampling().getSamplingRate()));
        }
    }

    @Override
    public SamplingResult shouldSample(
            Context parentContext,
            String traceId,
            String name,
            SpanKind spanKind,
            Attributes attributes,
            List<LinkData> parentLinks) {
        Sampler sampler = this.perOperationSampler.get(name);
        if (sampler == null) {
            sampler = this.defaultSampler;
        }
        return sampler.shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);
    }

    @Override
    public String getDescription() {
        return String.format(
                "PerOperationSampler{default=%s, perOperation=%s}",
                this.defaultSampler, this.perOperationSampler);
    }

    @Override
    public String toString() {
        return getDescription();
    }
}