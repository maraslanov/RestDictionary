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

/**
 * Класс-spring controller для обработки запросов
 */
@Controller
@Api(description = "api list")//описание api для сваггера
public class TranslateController {

    private static final Logger logger = LoggerFactory.getLogger(TranslateController.class);

    //ключ-пароль(для яндекс api)
    @Value("${yakey}")
    String key;

    /**
     * Обработчик /translate валидирует входные параметры, отправляет запрас к апи и возвращает его ответ
     * если какой то из параметров пустой - обработчик вернет текст из входныхх данных
     * @param text текст для перевода
     * @param from язык, с которого необходимо перевести текст, в скоращенном виде из 2 символом (см. описание апи)
     * @param to язык, на который необходимо перевести текст, в скоращенном виде из 2 символом (см. описание апи)
     * @return http ответ после обработки, http header + http статус, в body переведенный текст/описание ошибки запроса
     */
    @ApiOperation(value = "yandex translate")
    @RequestMapping(value = "/translate", method = RequestMethod.GET)
    public ResponseEntity<String> getYandexTranslaltion(@RequestParam("text") String text, @RequestParam("from") String from, @RequestParam("to") String to) {
        logger.info("call YandexTranslaltion : text=" + text + " from=" + from + " to=" + to);
        ResponseEntity<String> response = null;
        StringBuilder text2 = new StringBuilder();
        //если не все необходмиые параметры заполнены - api не вызывается
        if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(to) && StringUtils.isNotBlank(from)) {
            //проверка входных данных
            from = from.trim();
            to = to.trim();
            boolean validation = isValid(from, to);

            if (validation) {
                //требование отправлять запросы к апи только по 1 слову
                List<String> words = Arrays.asList(text.trim().split(" "));
                YandexTranslateApi api = new YandexTranslateApi();
                for (String word : words) {
                    try {
                        //ответ от api помещается в объект из "key": "value"
                        JSONObject obj = new JSONObject(api.translate(key, word, from, to));
                        //перведенный текст апи помещает с ключем "text"
                        JSONArray translation = obj.getJSONArray("text");
                        //перевод складывается в 1 строку через пробел
                        if (translation != null) {
                            text2.append(translation.getString(0));
                            text2.append(" ");
                        }
                    }
                    //при ошибке возвращаем не переведенный текст и логируем причину
                    catch (IOException e) {
                        text2 = new StringBuilder(text);
                        logger.error("getYandexTranslaltiaon problem: " + e);
                    }
                }
            } else {
                //перевод на не поддерживаемые языки не осуществляется
                text2 = new StringBuilder(text);
            }
            //возвращается успешная обработка запроса
            response = new ResponseEntity<>(text2.toString().trim(), HttpStatus.OK);
        } else {
            //возвращается успешная обработка запроса без переведенного текста
            response = new ResponseEntity<>(text, HttpStatus.OK);
        }
        return response;
    }

    /**
     * Метод для проверки корректности языков для перевода (см. описание api)
     * @param from язык, с которого необходимо перевести текст, в скоращенном виде из 2 символом (см. описание апи)
     * @param to язык, на который необходимо перевести текст, в скоращенном виде из 2 символом (см. описание апи)
     * @return true/false в зависимости от корректности сокращений языка
     */
    private boolean isValid(@RequestParam("from") String from, @RequestParam("to") String to) {
        boolean validation = false;
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
        if (checkTo && checkFrom){
            validation = true;
        }
        return validation;
    }
}
