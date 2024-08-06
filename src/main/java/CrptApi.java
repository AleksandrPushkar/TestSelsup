import lombok.Synchronized;
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
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(name = "CreateDocumentServlet", urlPatterns = "/api/v3/lk/documents/create")
public class CrptApi extends HttpServlet {
    private TimeUnit timeUnit;
    private int requestLimit;
    private AtomicInteger threadPool;
    private DocumentService documentService;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.threadPool = new AtomicInteger(requestLimit);
        documentService = new DocumentServiceImp();
    }

    private void getMilisecFromTimeUnit() {
        long NANO_SCALE = 1L;
        long MICRO_SCALE = 1000L * NANO_SCALE;
        long MILLI_SCALE = 1000L * MICRO_SCALE;
        long SECOND_SCALE = 1000L * MILLI_SCALE;
        long MINUTE_SCALE = 60L * SECOND_SCALE;
        long HOUR_SCALE = 60L * MINUTE_SCALE;
        long DAY_SCALE = 24L * HOUR_SCALE;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder postData = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                postData.append(line);
            }
        } catch (IOException ex) {
            System.out.println("Log: При Post запросе произошло исключение ввода-вывода! " + ex.getMessage());
        }
        if (!postData.isEmpty()) {
            DocumentService documentService = new DocumentServiceImp();
            String docId = documentService.createDocument(postData.toString());
            PrintWriter printWriter = resp.getWriter();
            printWriter.write(docId);
            printWriter.close();
        }
    }

    private void addThreadPool() {
        while (true) {
            synchronized (this) {
                if (threadPool.get() == 0) {

                }
            }
        }
    }

    class Timer extends Thread {
        @Override
        public void run() {
            
        }
    }

    /*private DocumentService getDocumentService() {
        timeUnit.
        while(!file.isDownloaded())
        {
            Thread.sleep(1000);
        }
        processFile(file);
        not
    }*/

    class JsonRequestParser {
        public Document parseRequestCreateDocument(String outputJsonStr) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject jsonData = (JSONObject) parser.parse(outputJsonStr);
                Description description = new Description();
                description.participantInn = (String) ((JSONObject) jsonData.get("description")).get("participantInn");
                Document document = new Document();
                document.description = description;
                document.docId = (String) jsonData.get("doc_id");
                document.docStatus = (String) jsonData.get("doc_status");
                document.docType = (String) jsonData.get("doc_type");
                document.importRequest = (boolean) jsonData.get("importRequest");
                document.ownerInn = (String) jsonData.get("owner_inn");
                document.participantInn = (String) jsonData.get("participant_inn");
                document.producerInn = (String) jsonData.get("producer_inn");
                document.productionDate = getDate((String) jsonData.get("production_date"));
                document.productionType = (String) jsonData.get("production_type");
                Product product = new Product();
                JSONArray productArray = (JSONArray) jsonData.get("products");
                JSONObject productObject = (JSONObject) productArray.get(0);
                product.certificateDocument = (String) productObject.get("certificate_document");
                product.certificateDocumentDate = getDate((String) productObject.get("certificate_document_date"));
                product.certificateDocumentNumber = (String) productObject.get("certificate_document_number");
                product.ownerInn = (String) productObject.get("owner_inn");
                product.producerInn = (String) productObject.get("producer_inn");
                product.productionDate = getDate((String) productObject.get("production_date"));
                product.tnvedCode = (String) productObject.get("tnved_code");
                product.uitCode = (String) productObject.get("uit_code");
                product.uituCode = (String) productObject.get("uitu_code");
                document.product = product;
                document.regDate = getDate((String) jsonData.get("reg_date"));
                document.regNumber = (String) jsonData.get("reg_number");
                return document;
            } catch (Exception ex) {
                System.out.println("Log: Пробелмы с парсингом документа при создании! " + ex.getMessage());
                return new Document();
            }
        }

        private Date getDate(String strDate) {
            Date date;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = formatter.parse(strDate);
            } catch (ParseException ex) {
                System.out.println("Log: Пробелмы с датой! " + ex.getMessage());
                date = new Date();
            }
            return date;
        }
    }

    class Document {
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
        private Product product;
        private Date regDate;
        private String regNumber;
    }

    class Description {
        private String participantInn;
    }

    class Product {
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
        public String createDocument(String jsonDocStr);
    }

    class DocumentServiceImp implements DocumentService {
        private final Map<String, Document> documentMap = new HashMap<>();

        public String createDocument(String jsonDocStr) {
            JsonRequestParser parser = new JsonRequestParser();
            Document document = parser.parseRequestCreateDocument(jsonDocStr);
            if (document.docId == null) {
                return "";
            }
            synchronized (documentMap) {
                documentMap.put(document.docId, document);
            }
            return document.docId;
        }
    }
}
