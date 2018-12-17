package com.example.dictionary.web.controller;

import com.example.dictionary.web.YandexTranslate.Languages;
import com.example.dictionary.web.YandexTranslate.YandexTranslateApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@Api(description = "api list")
public class TranslateController {

    private static final Logger logger = LoggerFactory.getLogger(TranslateController.class);

    @Value("${yakey}")
    String key;

    @ApiOperation(value = "yandex translate")
    @RequestMapping(value = "/translate", method = RequestMethod.GET)
    public ResponseEntity<String> getYandexTranslaltion(@RequestParam("text") String text, @RequestParam("from") String from, @RequestParam("to") String to) {
        logger.info("call YandexTranslaltion : text=" + text + " from=" + from + " to=" + to);
        ResponseEntity<String> response = null;
        StringBuilder text2 = new StringBuilder();
        if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(to) && StringUtils.isNotBlank(from)) {
            //check input params
            from = from.trim();
            to = to.trim();
            boolean checkFrom = false;
            boolean checkTo = false;
            for (Languages c : Languages.values()) {
                if (c.toString().equalsIgnoreCase(from)) {
                    checkFrom = true;
                }
                if (c.toString().equalsIgnoreCase(to)) {
                    checkTo = true;
                }
            }

            if (checkFrom && checkTo) {
                List<String> words = Arrays.asList(text.trim().split(" "));
                //translate
                YandexTranslateApi api = new YandexTranslateApi();
                for (String word : words) {
                    try {
                        JSONObject obj = new JSONObject(api.translate(key, word, from, to));
                        JSONArray translation = obj.getJSONArray("text");
                        if (translation != null) {
                            text2.append(translation.getString(0));
                            text2.append(" ");
                        }
                    } catch (IOException e) {
                        text2 = new StringBuilder(text);
                        logger.error("getYandexTranslaltiaon problem: " + e);
                    }
                }
            } else {
                text2 = new StringBuilder(text);
            }
            response = new ResponseEntity<>(text2.toString().trim(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(text, HttpStatus.OK);
        }
        return response;
    }
}
