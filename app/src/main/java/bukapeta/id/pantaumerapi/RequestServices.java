package bukapeta.id.pantaumerapi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestServices {
    @GET(GlobalConstant.data_home)
    Call<ModelDataHome> datahome();

}
