package se.nomp.instrumentation.otel.providers;

import io.opentelemetry.sdk.autoconfigure.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigurableSamplerProvider;
import io.opentelemetry.sdk.trace.samplers.Sampler;

public class NompSamplerProvider implements ConfigurableSamplerProvider {

    /**
     * Returns a {@link Sampler} that can be registered to OpenTelemetry by providing the property
     * value specified by {@link #getName()}.
     */
    @Override
    public Sampler createSampler(ConfigProperties config) {
        return Sampler.parentBased(new NompParentBasedSampler());
    }

    /**
     * Returns the name of this sampler, which can be specified with the {@code otel.traces.sampler}
     * property to enable it. The name returned should NOT be the same as any other exporter name. If
     * the name does conflict with another exporter name, the resulting behavior is undefined and it
     * is explicitly unspecified which exporter will actually be used.
     */
    public String getName() {
        return "nomp";
    }
}
