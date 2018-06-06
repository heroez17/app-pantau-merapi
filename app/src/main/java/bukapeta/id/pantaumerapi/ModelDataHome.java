package bukapeta.id.pantaumerapi;

import com.google.gson.annotations.SerializedName;

public class ModelDataHome {
    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    @SerializedName("radius")
    private int radius;


    public ModelDataHome(String status, String info, int radius) {
        this.status = status;
        this.info = info;
        this.radius = radius;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
