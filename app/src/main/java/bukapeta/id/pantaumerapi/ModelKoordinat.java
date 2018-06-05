package bukapeta.id.pantaumerapi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelKoordinat {

    @SerializedName("id")
    private int id;

    @SerializedName("namapos")
    private String namapos;

    @SerializedName("koordinat")
    private List<Double> koordinat;

    public ModelKoordinat(int id, String namapos, List<Double> koordinat) {
        this.id = id;
        this.namapos = namapos;
        this.koordinat = koordinat;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamapos() {
        return namapos;
    }

    public void setNamapos(String namapos) {
        this.namapos = namapos;
    }

    public List<Double> getKoordinat() {
        return koordinat;
    }

    public void setKoordinat(List<Double> koordinat) {
        this.koordinat = koordinat;
    }
}
