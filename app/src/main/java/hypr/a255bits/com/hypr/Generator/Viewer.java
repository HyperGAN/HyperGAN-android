
package hypr.a255bits.com.hypr.Generator;

import java.util.List;
import com.squareup.moshi.Json;

public class Viewer {

    @Json(name = "type")
    private String type;
    @Json(name = "operation")
    private String operation;
    @Json(name = "controls")
    private List<Control> controls = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<Control> getControls() {
        return controls;
    }

    public void setControls(List<Control> controls) {
        this.controls = controls;
    }

}
