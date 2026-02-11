package ee.zxz.helloapi.domain;

public class ApiParam {
    private int api_id;
    private String name;
    private int required;
    private String type;
    private String msg;

    public int getApi_id() {
        return api_id;
    }

    public void setApi_id(int api_id) {
        this.api_id = api_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
