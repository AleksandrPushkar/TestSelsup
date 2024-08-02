import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CrptApi {
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

    @Data
    class InputDocumentRequest {
        private String documentText;
        private String sign;
    }

    interface Document {
        public LocalDateTime getTransDate();
    }

    class DocumentImp implements Document {
        private String text;
        private String sign;
        private LocalDateTime transData;

        public DocumentImp(InputDocumentRequest inputDocReq) {
            text = inputDocReq.getDocumentText();
            sign = inputDocReq.getSign();
            transData = LocalDateTime.now();
        }

        @Override
        public LocalDateTime getTransDate() {
            return transData;
        }
    }

}
