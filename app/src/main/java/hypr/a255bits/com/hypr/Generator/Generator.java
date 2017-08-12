
package hypr.a255bits.com.hypr.Generator;

import com.squareup.moshi.Json;

public class Generator {

    @Json(name = "name")
    private String name;
    @Json(name = "file_size_in_bytes")
    private Integer fileSizeInBytes;
    @Json(name = "model_url")
    private String modelUrl;
    @Json(name = "generator")
    private Generator_ generator;
    @Json(name = "price_in_cents")
    private Integer priceInCents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFileSizeInBytes() {
        return fileSizeInBytes;
    }

    public void setFileSizeInBytes(Integer fileSizeInBytes) {
        this.fileSizeInBytes = fileSizeInBytes;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    public Generator_ getGenerator() {
        return generator;
    }

    public void setGenerator(Generator_ generator) {
        this.generator = generator;
    }

    public Integer getPriceInCents() {
        return priceInCents;
    }

    public void setPriceInCents(Integer priceInCents) {
        this.priceInCents = priceInCents;
    }

}
