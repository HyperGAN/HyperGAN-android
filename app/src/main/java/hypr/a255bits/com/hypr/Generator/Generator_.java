
package hypr.a255bits.com.hypr.Generator;

import com.squareup.moshi.Json;

public class Generator_ {

    @Json(name = "viewer")
    private Viewer viewer;
    @Json(name = "input")
    private Input input;
    @Json(name = "output")
    private Output output;

    public Viewer getViewer() {
        return viewer;
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

}
