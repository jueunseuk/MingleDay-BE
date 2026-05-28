package returns.mingleday.model.anniversary;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "response")
public class AnniversaryApiResponse {
    private AnniversaryBody body;
}
