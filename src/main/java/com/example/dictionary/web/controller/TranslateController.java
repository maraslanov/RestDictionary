package com.example.dictionary.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;

@Controller
@Api(description = "api list")
public class TranslateController {

    private static final Logger logger = LoggerFactory.getLogger(TranslateController.class);

    @Value("${yapath:}")
    String path;

    @Value("${apikey}")
    String key;

    @ApiOperation(value = "google translate")
    @RequestMapping(value = "/translate", method = RequestMethod.GET)
    public ResponseEntity<String> getTranslaltiaon(@RequestParam("text") String text, @RequestParam("from") String from, @RequestParam("to") String to) {
            ResponseEntity<String> response;
        if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(to) && StringUtils.isNotBlank(from)) {
            text = getFirstWord(text);
            //translate
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(path);

            response = new ResponseEntity<>(text, HttpStatus.OK);
        } else{
            response = new ResponseEntity<>(text, HttpStatus.OK);
        }
        return response;
    }

    private String getFirstWord(String text) {
        int index = text.indexOf(' ');
        if (index > -1) {
            return text.substring(0, index);
        } else {
            return text;
        }
    }
}
