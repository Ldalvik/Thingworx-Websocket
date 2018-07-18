package websocket.server;

import fi.iki.elonen.NanoHTTPD;

import java.util.List;
import java.util.Map;

public class Requests {
    private String[] data;
    private String function;
    int id;

    public Requests(String message){
        this.data = message.split(":")[1].split("&");
        this.function = message.split(":")[0];
        this.id = Integer.parseInt(message.substring(message.lastIndexOf('=')+1));
    }

    public boolean isUpdate(){
        return function.equals("updateText");
    }

    public boolean isAddText(){
        return function.equals("addText");
    }

    public boolean isAddImage(){
        return function.equals("addImage");
    }

    public Float getX(){
        return Float.valueOf(data[1].split("=")[1]);
    }

    public int getId(){
        return id;
    }

    public String getText(){
        return data[0].split("=")[1];
    }

    public Float getY(){
        return Float.valueOf(data[2].split("=")[1]);
    }

    public String getImageUrl(){
        return data[0].split("=")[1];
    }

    public String getImageX(){
        return data[1].split("=")[1];
    }

    public String getImageY(){
        return data[2].split("=")[1];
    }

    public String getImageId(){
        return data[3].split("=")[1];
    }

    public Float getSize(){
        return Float.valueOf(data[3].split("=")[1]);
    }

    public int[] getColor(){
        String[] rgb = data[4].split("=")[1].split(",");
        int r = Integer.valueOf(rgb[0]);
        int g = Integer.valueOf(rgb[1]);
        int b = Integer.valueOf(rgb[2]);
        return new int[]{r, g, b};
    }
}
