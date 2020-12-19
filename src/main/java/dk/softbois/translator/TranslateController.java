package dk.softbois.translator;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.mime.Headers;
import org.json.JSONObject;
import org.json.XML;
import org.json.JSONException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
@ResponseBody
@RequestMapping("/translate")
public class TranslateController {

    private int PRETTY_PRINT_INDENT_FACTOR = 4;

    @PostMapping("/")
    public ResponseEntity<String> translate(@RequestBody String body, @RequestHeader("Accept") String returnType) {
        System.out.println("Started method");
        Logger logger = Logger.getLogger(TranslateController.class.getName());
        logger.warning("Entered translate");
        try {
            System.out.println("content type: " + returnType);
            System.out.println("body: " + body);
            String resString = null;

            if (returnType.contains("application/json")) {
                resString = xmlToJson(body);
            } else if (returnType.contains("application/xml") || returnType.contains("*/*")) {
                resString = jsonToXml(body);
            } else {
                return new ResponseEntity<>("Your Content-Type is not XML or JSON", HttpStatus.NOT_IMPLEMENTED);
            }
            resString = jsonToXml(body);

            return new ResponseEntity<>(resString, HttpStatus.OK);
        } catch (JSONException je) {
            return new ResponseEntity<>(je.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String xmlToJson(String xmlString) throws JSONException {
        JSONObject xmlJSONObj = XML.toJSONObject(xmlString);
        String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        System.out.println(jsonPrettyPrintString);

        return xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
    }

    private String jsonToXml(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        String xml = XML.toString(json);
        System.out.println(xml);

        return xml;
    }

}
