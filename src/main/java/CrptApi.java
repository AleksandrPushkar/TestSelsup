import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CreateDocumentServlet", urlPatterns = "/api/v3/lk/documents/create")
public class CrptApi extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder postData = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                postData.append(line);
            }
        } catch (IOException ex) {
            System.out.println("Произошло исключение ввода-вывода");
        }
        JsonRequestParser parser = new JsonRequestParser();
        InputDocumentRequest inputDocReq = parser.parseRequestCreateDocument(postData.toString());
    }

    class JsonRequestParser {
        public InputDocumentRequest parseRequestCreateDocument(String outputJsonStr) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject jsonData = (JSONObject) parser.parse(outputJsonStr);
                ((JSONObject) jsonData.get("description")).get("participantInn");
                jsonData.get("doc_id");
                jsonData.get("doc_status");
                jsonData.get("doc_type");
                jsonData.get("importRequest");
                jsonData.get("owner_inn");
                jsonData.get("participant_inn");
                jsonData.get("producer_inn");
                jsonData.get("production_date");
                jsonData.get("production_type");
                JSONArray innerArray = (JSONArray) jsonData.get("products");
                JSONObject innerObject = (JSONObject) innerArray.get(0);
                innerObject.get("certificate_document");
                innerObject.get("certificate_document_date");
                innerObject.get("certificate_document_number");
                innerObject.get("owner_inn");
                innerObject.get("producer_inn");
                innerObject.get("production_date");
                innerObject.get("tnved_code");
                innerObject.get("uit_code");
                innerObject.get("uitu_code");
                jsonData.get("reg_date");
                jsonData.get("reg_number");

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = formatter.parse("2020-01-23");
                    System.out.println(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Setter
    @Getter
    class RequestInputDocument {
        private Description description;
        private String docId;
        private String docStatus;
        private String docType;
        private boolean importRequest;
        private String ownerInn;
        private String participantInn;
        private String producerInn;
        private Date productionDate;
        private String productionType;
        private Products products;
        private Date regDate;
        private String regNumber;
    }

    @Setter
    @Getter
    class  Description {
        private String participantInn;
    }

    @Setter
    @Getter
    class Products {
       private String certificateDocument;
       private Date certificateDocumentDate;
       private String certificateDocumentNumber;
       private String ownerInn;
       private String producerInn;
       private Date productionDate;
       private String tnvedCode;
       private String uitCode;
       private String uituCode;
    }


    interface DocumentService {
        public void createDocument(InputDocumentRequest inputDocReq);
    }

    class DocumentServiceImp implements DocumentService {

        private Map<Long, Document> documentMap = new HashMap<>();
        //private
        public void createDocument(InputDocumentRequest inputDocReq) {
            countId++;
            doc_id = countId;
        }
    }
}
