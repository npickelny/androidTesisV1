package restClases;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMessage {

    @SerializedName("msg")
    @Expose
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResponseMessage() {
    }

    public ResponseMessage(String msg) {
        this.msg = msg;
    }
}
