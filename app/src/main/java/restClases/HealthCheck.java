
package restClases;

import java.util.HashMap;
import java.util.Map;

public class HealthCheck {

    private App app;
    private Worker worker;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
