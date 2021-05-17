package se.nomp.instrumentation.otel.providers;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NompParentBasedSampler implements Sampler {

    private final static Pattern CAPTURE_REL_URL = Pattern.compile("^[^#]*?://.*?(/.*)$");

    private final Sampler highTrafficSampler = Sampler.parentBased(Sampler.traceIdRatioBased(0.0001));
    private final Sampler defaultSampler = Sampler.parentBased(Sampler.traceIdRatioBased(0.005));

    @Override
    public SamplingResult shouldSample(Context parentContext, String traceId, String name, SpanKind spanKind, Attributes attributes, List<LinkData> parentLinks) {
        if (spanKind == SpanKind.SERVER) {
            String urlStr = attributes.get(AttributeKey.stringKey("http.url"));
            if (urlStr != null) {
                Matcher matcher = CAPTURE_REL_URL.matcher(urlStr);
                String relUrl = matcher.matches() ? matcher.group(1) : null;
                if (relUrl != null) {
                    if (relUrl.startsWith("/ws/")) {
                        return highTrafficSampler.shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);
                    }
                }
            }
        }
        return defaultSampler.shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);
    }

    @Override
    public String getDescription() {
        return "NompParentBasedSampler";
    }
}
