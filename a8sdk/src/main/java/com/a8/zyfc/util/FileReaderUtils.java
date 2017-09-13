package com.a8.zyfc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class FileReaderUtils {
    /**
     * 整个ini的引用
     */
    private Map<String, String> map = null;

    /**
     * 读取
     *
     * @param path
     */
    public FileReaderUtils(Context ctx, String fileName) {
        map = new HashMap<String, String>();
        try {
            InputStreamReader inputReader = new InputStreamReader(ctx
                    .getResources().getAssets().open(fileName), "utf-8");
            BufferedReader reader = new BufferedReader(inputReader);
            read(reader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO Exception:" + e);
        }

    }

    /**
     * 读取文件
     *
     * @param reader
     * @throws IOException
     */
    private void read(BufferedReader reader) throws IOException {
        String line = null;
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
    }

    /**
     * 转换
     *
     * @param line
     */
    private void parseLine(String line) {
        line = line.trim();
        // 此部分为注释
        if (line.matches("^\\#.*$")) {
            return;
        } else if (line.matches("^\\S+=.*$")) {
            // key ,value
            int i = line.indexOf("=");
            String key = line.substring(0, i).trim();
            String value = line.substring(i + 1).trim();
            addKeyValue(map, key, value);
        }
    }

    /**
     * 增加新的Key和Value
     *
     * @param map
     * @param key
     * @param value
     */
    private void addKeyValue(Map<String, String> map, String key, String value) {
        if (map.containsKey(key)) {
        } else {
            map.put(key, value);
        }
    }

    /**
     * 获取这个配置文件的节点和值
     *
     * @return
     */
    public Map<String, String> get() {
        return map;
    }

    /**
     * 获取单个字符
     *
     * @return
     */
    public String getStr() {
        String str = "";
        Map<String, String> map = get();
        Set<String> set = map.keySet();
        JSONObject json = new JSONObject();
        for (String key : set) {
            str = map.get(key);

        }
        return str;
    }

    /**
     * 获取读取后的json
     *
     * @return
     */
    public String getJsonStr() {
        Map<String, String> map = get();
        Set<String> set = map.keySet();
        String str = "";
        JSONObject json = new JSONObject();
        for (String key : set) {
            String value = map.get(key);
            try {
                json.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return json.toString();
    }

}
