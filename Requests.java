package websocket.server;

import fi.iki.elonen.NanoHTTPD;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Requests {
    private String data;
    private String function;
    int id;

    public Requests(String message){
        this.data = message.split(":")[1];
        this.function = message.split(":")[0];
        this.id = Integer.parseInt(splitQuery(data).get("id"));
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

    public float getX(){
        return Float.valueOf(splitQuery(data).get("x"));
    }

    public int getId(){
        return id;
    }

    public String getText(){
        return splitQuery(data).get("text");
    }

    public Float getY(){
        return Float.valueOf(splitQuery(data).get("y"));
    }

    public String getImageUrl(){
        return splitQuery(data).get("url");
    }


    public Float getSize(){
        return Float.valueOf(splitQuery(data).get("size"));
    }

    public int[] getColor(){
        String[] rgb = splitQuery(data).get("color").split(",");
        int r = Integer.valueOf(rgb[0]);
        int g = Integer.valueOf(rgb[1]);
        int b = Integer.valueOf(rgb[2]);
        return new int[]{r, g, b};
    }

    public static Map<String, String> splitQuery(String query) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return query_pairs;
    }
}
