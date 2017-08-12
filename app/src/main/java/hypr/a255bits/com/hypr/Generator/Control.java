
package hypr.a255bits.com.hypr.Generator;

import java.util.List;
import com.squareup.moshi.Json;

public class Control {

    @Json(name = "type")
    private String type;
    @Json(name = "direction")
    private List<Double> direction = null;
    @Json(name = "name")
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getDirection() {
        return direction;
    }

    public void setDirection(List<Double> direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
