package com.example.dictionary.web.YandexTranslate;

import com.example.dictionary.web.controller.TranslateController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class YandexTranslateApi {

    private static final Logger logger = LoggerFactory.getLogger(TranslateController.class);

    public String translate(String key, String text, String sourceLang, String targetLang) throws IOException {
        URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + key + "&text=" + text + "&lang=" + sourceLang + "-" + targetLang);
        logger.info(new Date() + " Yandex Translate Api call: " + url);
        URLConnection urlConn = url.openConnection();
        urlConn.setDoOutput(true);
        InputStream inStream = urlConn.getInputStream();
        String recieved = new BufferedReader(new InputStreamReader(inStream)).readLine();
        inStream.close();
        return recieved;
    }
}
