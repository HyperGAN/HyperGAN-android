
package hypr.a255bits.com.hypr.Generator;

import com.squareup.moshi.Json;

public class Output {

    @Json(name = "type")
    private String type;
    @Json(name = "width")
    private Integer width;
    @Json(name = "height")
    private Integer height;
    @Json(name = "channels")
    private Integer channels;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getChannels() {
        return channels;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

}
